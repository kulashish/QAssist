package com.aneedo.search.ranking.util;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectDoubleHashMap;
import gnu.trove.TObjectIntHashMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ejml.simple.SimpleMatrix;

import com.aneedo.jwplext.dao.tablecreation.DBConnectionFactory;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.ranking.features.node.NodeFeatures;
import com.aneedo.training.FeatureBean;
import com.aneedo.training.LogisticOutput;
import com.aneedo.training.RankedInterpretation;
import com.aneedo.training.RelevanceFeedbackDao;

public class LRTrainer {

	//private LRTrainer LRTrainer = new LRTrainer();
	TIntObjectHashMap<Map<String, FeatureBean>> queryFeatureBeanMap = new TIntObjectHashMap<Map<String, FeatureBean>>();
	double[] prevWvlues = new double[LRConstants.NO_OF_LR_FEATURE];
	double[] currentWvlues = new double[LRConstants.NO_OF_LR_FEATURE];

//	public LRTrainer getInstance() {
//		return LRTrainer;
//	}
	
	public LRTrainer() {
		
	}
	
	public double[] getPrevWvlues() {
		return prevWvlues;
	}

	public void setPrevWvlues(double[] prevWvlues) {
		this.prevWvlues = prevWvlues;
	}

	public double[] getCurrentWvlues() {
		return currentWvlues;
	}

	public void setCurrentWvlues(double[] currentWvlues) {
		this.currentWvlues = currentWvlues;
	}

	public LogisticOutput trainFromGroundTruth() {
		RelevanceFeedbackDao dao = new RelevanceFeedbackDao();
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DBConnectionFactory.getInstance(dao.password)
					.getConnection();
			stmt = conn
					.prepareStatement(RelevanceFeedbackDao.SELECT_QUERY_FEEDBACK);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// Get all queries from DB
		TIntObjectHashMap<String> queryMap = dao.getQueries(conn);
		int[] keys = queryMap.keys();
		
		// Semantic entities of search
		Set<SemEntityBean> semEntBeanSet = new HashSet<SemEntityBean>();

		// Selected interpretations to ranking
		Map<String, SemInterpretation> semInterpretMap = new HashMap<String, SemInterpretation>();
		double[] lrFeatures = null;

		for (int i = 0; i < keys.length; i++) {

			// re run candidate interpretation generation
			List<SemInterpretation> semInterpretList = InterpretationRankingUtil.getInstance()
					.getEntitiesInterpretationsFromSearch(queryMap.get(keys[i]),semEntBeanSet);

			// Get all DB stored interpretations for this query
			TObjectIntHashMap<String> storedInterpretations = dao
					.getRelevanceFeedback(conn, stmt,keys[i]);
			
			for (int j = 0, intSize = semInterpretList.size(); j < intSize; j++) {
				final SemInterpretation semInterpret = semInterpretList.get(j);
				final String interpretation = semInterpret.getInterpretation();
				
				// Consider only those which are marked +ve and -ve
				if (storedInterpretations.containsKey(interpretation)) {
					semInterpretMap.put(interpretation, semInterpret);
					lrFeatures = new double[LRConstants.NO_OF_LR_FEATURE-1];

					final FeatureBean featureBean = new FeatureBean();

					// no of Logistic class = 2 
					int label = storedInterpretations.get(interpretation);
					if(label <=1 ) {
						featureBean.setLabel(1);
						featureBean.setPrevLabel(1);
					} else {
						featureBean.setLabel(0);
						featureBean.setPrevLabel(0);
					}

					System.out.println(featureBean.getLabel() +" : "+ featureBean.getPrevLabel());
					// Set all feature value scores
					//lrFeatures[0] = semInterpret.getQueryMatch();
					NodeFeatures nodeFeatures = semInterpret.getNodeFeatures();
					lrFeatures[0] = nodeFeatures.getFeature(NodeFeatures.GRANULARITY_EVIDENCE_SCORE);
					lrFeatures[1]= nodeFeatures.getFeature(NodeFeatures.DF);
					lrFeatures[2] = nodeFeatures.getFeature(NodeFeatures.TFIDF);
					lrFeatures[3] = nodeFeatures.getFeature(NodeFeatures.NO_OF_SEM_CLASS_MATCH);
					lrFeatures[4] = nodeFeatures.getFeature(NodeFeatures.INTER_SET_SCORE);
					lrFeatures[5] = nodeFeatures.getFeature(NodeFeatures.INTRA_SET_SCORE);
					featureBean.setFeatures(lrFeatures);
					
					if(this.queryFeatureBeanMap.get(keys[i]) != null) {
					this.queryFeatureBeanMap.get(keys[i]).put(interpretation,
							featureBean); 
					} else {
						Map<String,FeatureBean> featureBeanMap = new HashMap<String,FeatureBean>();
						featureBeanMap.put(interpretation, featureBean);
						this.queryFeatureBeanMap.put(keys[i], featureBeanMap);
					}
				}
			}

		}
		
		//train LR and return ranked list
		LogisticOutput lgOutput = new LogisticOutput();
		lgOutput.setRankedInterQueryMap(trainLR(true,semInterpretMap));
		lgOutput.setSemEntBeanSet(semEntBeanSet);
		return lgOutput;
	}
	
	public SimpleMatrix predict(List<SemInterpretation> semInterList) {
		double[] wValues = getLRFeatureWeights();
		return getRankedInterpretations(semInterList);
	}

	private void writeInLRFormat(int[] queryIds, boolean firstTime) {
		BufferedWriter writer = null;
		String interpretation = null;
		FeatureBean featureBean = null;
		double[] feaValues = null;
		//StringBuilder strFeaValue = new StringBuilder();
		try {

			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(LRConstants.TRAIN_FILE), "UTF8"));
			for (int i = 0; i < queryIds.length; i++) {
				final Map<String, FeatureBean> featureBeanMap = queryFeatureBeanMap
						.get(queryIds[i]);
				Iterator<String> interItr = featureBeanMap.keySet().iterator();
				while (interItr.hasNext()) {
					interpretation = interItr.next();
					featureBean = featureBeanMap.get(interpretation);
					feaValues = featureBean.getFeatures();
					final StringBuilder strFeaValue = new StringBuilder();
					for (int k = 0; k < feaValues.length; k++) {
						strFeaValue.append("\t" + feaValues[k]);
					}
					System.out.println(strFeaValue);
					if (firstTime)
						writer.write(featureBean.getPrevLabel()
								+ strFeaValue.toString() + "\n");
					else {
						writer.write(featureBean.getPrevLabel()
								+ strFeaValue.toString() + "\n");

						writer.write(featureBean.getLabel()
								+ strFeaValue.toString() + "\n");
					}
				}
			}

			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private double[] getLRFeatureWeights() {
		File folder = new File(LRConstants.MODEL_FILE);
		double[] wValues = new double[LRConstants.NO_OF_LR_FEATURE];
		try {
			Reader reader = new InputStreamReader(new FileInputStream(
					folder.getPath()), "UTF8");
			BufferedReader bufreader = new BufferedReader(reader);
			String line = null;
			String[] tempValues = null;
			while ((line = bufreader.readLine()) != null) {
				tempValues = line.split(" : ");
				wValues[Integer.parseInt(tempValues[0].trim())-1] = Double
						.parseDouble(tempValues[1].trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wValues;
	}
		
	private TIntObjectHashMap<RankedInterpretation> trainLR(boolean firstTime, 
			Map<String, SemInterpretation> semInterpretMap) {

		// Write to a file in LR format
		int[] queryIds = queryFeatureBeanMap.keys();
		writeInLRFormat(queryIds, firstTime);

		// Train LR
		LogisticRegressionUtil.getInstance().train();

		// Read W values
		double[] wValues = getLRFeatureWeights();

		// Save w values for next iteration
		prevWvlues = Arrays.copyOf(currentWvlues,currentWvlues.length);
		currentWvlues=Arrays.copyOf(wValues,wValues.length);
		return getRankedInterpretations(queryIds,semInterpretMap);
	}
	
	private TIntObjectHashMap<RankedInterpretation> getRankedInterpretations
		(int[] queryIds, Map<String, SemInterpretation> semInterpretMap) {
		// Rank based on score
		double score;
		RankedInterpretation rankedInter = null;
		TempInterpretation[] tempInterArr = null;
		TempInterpretation tempInter = null;
		int index = 0;
		String interpretation = null;
		FeatureBean featureBean = null;
		double[] feaValues = null;

		TIntObjectHashMap<RankedInterpretation> queryInterMap = new TIntObjectHashMap<RankedInterpretation>();

		for (int i = 0; i < queryIds.length; i++) {
			final Map<String, FeatureBean> featureBeanMap = queryFeatureBeanMap
					.get(queryIds[i]);
			rankedInter = new RankedInterpretation();
			final int interSize = featureBeanMap.size();
			tempInterArr = new TempInterpretation[interSize];
			
			final SimpleMatrix teleports = new SimpleMatrix(interSize,1);
			final SimpleMatrix scores = new SimpleMatrix(interSize,1);
			
			Iterator<String> interItr = featureBeanMap.keySet().iterator();
			index = 0;
			double maxScore = 0;
			while (interItr.hasNext()) {
				tempInter = new TempInterpretation();
				interpretation = interItr.next();
				tempInter.setInterpretation(interpretation);
				featureBean = featureBeanMap.get(interpretation);
				feaValues = featureBean.getFeatures();
				score = 0;
				for (int k = 0; k < feaValues.length; k++) {
					score = score + currentWvlues[k] * feaValues[k];
				}
				
				tempInter.setScore(1/(1+Math.exp(-score)));
				//if(tempInter.getScore() > maxScore)
						maxScore = maxScore + tempInter.getScore();
				tempInter.setTeleport(featureBean.getPrevLabel());
				
				tempInterArr[index++] = tempInter;
			}

			// normalize scores
			for(int t=0;t<tempInterArr.length;t++) {
				tempInterArr[t].setScore((double)tempInterArr[t].getScore()/maxScore);
			}

			//Arrays.sort(tempInters, new InterpretScoreComparator());
			
			// Set score, teleport for ranking 
			if(semInterpretMap != null) {
				List<SemInterpretation> interpretations = new ArrayList<SemInterpretation>(interSize);
			for(int j=0;j<tempInterArr.length;j++) {
				teleports.set(j,0,tempInterArr[j].getTeleport());
				scores.set(j,0,tempInterArr[j].getScore());
				interpretations.add(semInterpretMap.get(tempInterArr[j].getInterpretation()));
			}
			
			rankedInter.setInterpretations(interpretations);
			} else {
				for(int j=0;j<tempInterArr.length;j++) {
					teleports.set(j,0,tempInterArr[j].getTeleport());
					scores.set(j,0,tempInterArr[j].getScore());
				}
			}
			rankedInter.setScores(scores);
			rankedInter.setTeleports(teleports);
			
			queryInterMap.put(queryIds[i], rankedInter);
		}

		return queryInterMap;

	}
	
	private SimpleMatrix getRankedInterpretations(List<SemInterpretation> semInterList) {
	// Rank based on score
	double score;
//	TempInterpretation[] tempInterArr = null;
//	TempInterpretation tempInter = null;
//	String interpretation = null;
	SemInterpretation semInterpretation = null;
	double[] feaValues = null;
	
	int interSize = semInterList.size();  

//		tempInterArr = new TempInterpretation[interSize];
		
		final SimpleMatrix relevant = new SimpleMatrix(interSize,1);
		
//		double maxScore = 0;
		for(int i=0;i<interSize;i++) {
			semInterpretation = semInterList.get(i);
//			tempInter = new TempInterpretation();
//			interpretation = semInterpretation.getInterpretation();
//			tempInter.setInterpretation(interpretation);
			feaValues = new double[LRConstants.NO_OF_LR_FEATURE];
			score = 0;
			for (int k = 0; k < feaValues.length-1; k++) {
				score = score + currentWvlues[k] * feaValues[k];
			}
			// Add bias term
			score = score + currentWvlues[LRConstants.NO_OF_LR_FEATURE-1];
			
			relevant.set(i,0,1/(1+Math.exp(-score)));
//			tempInter.setScore(1/(1+Math.exp(-score)));
			//if(tempInter.getScore() > maxScore)
//					maxScore = maxScore + tempInter.getScore();
//			tempInterArr[i] = tempInter;
		}

		// normalize scores
//		for(int t=0;t<tempInterArr.length;t++) {
//			tempInterArr[t].setScore((double)tempInterArr[t].getScore()/maxScore);
//		}

		//Arrays.sort(tempInterArr, new InterpretScoreComparator());
		
		// Set score for ranking 
//		for(int j=0;j<tempInterArr.length;j++) {
//			relevant.set(j,0,tempInterArr[j].getScore());
//		}
	return relevant;

}

	private TIntObjectHashMap<RankedInterpretation> trainFromLeastSquare() {
		int[] queryIds = queryFeatureBeanMap.keys();
		// Take a backup of current W's
		prevWvlues = Arrays.copyOf(currentWvlues, currentWvlues.length);
		
		// call least square
		NodeWeightLeastSquare ndLeastSquare = new NodeWeightLeastSquare();
		currentWvlues = ndLeastSquare.findLeastSquare(queryIds,queryFeatureBeanMap);
		
		return getRankedInterpretations(queryIds, null);
	}
	
	public LogisticOutput trainInIteration(
			TIntObjectHashMap<TObjectDoubleHashMap<String>> queryMap) {
		LogisticOutput lgOutput = new LogisticOutput();
		
		int[] queryIds = queryMap.keys();
		String interpretation = null;
		FeatureBean featureBean = null;

		for (int i = 0; i < queryIds.length; i++) {
			TObjectDoubleHashMap<String> labeledInterMap = queryMap
					.get(queryIds[i]);
			final Map<String, FeatureBean> featureLabelMap = queryFeatureBeanMap
					.get(queryIds[i]);
			Iterator<String> interItr = featureLabelMap.keySet().iterator();
			while (interItr.hasNext()) {
				interpretation = interItr.next();
				System.out.println("R Value : " + labeledInterMap.get(interpretation));
				featureBean = featureLabelMap.get(interpretation);
				//featureBean.setPrevLabel(featureBean.getLabel());
				featureBean.setLabel(labeledInterMap.get(interpretation));
			}
		}
		lgOutput.setRankedInterQueryMap(trainFromLeastSquare());
		return lgOutput;
	}

	public static void main(String[] args) {
		LRTrainer lrTrainer = new LRTrainer();
		TIntObjectHashMap<RankedInterpretation> rankInterMap = lrTrainer.trainFromGroundTruth().getRankedInterQueryMap();
		int keys[] = rankInterMap.keys();
//		for(int i=0;i<keys.length;i++) {
//			RankedInterpretation rankInter = rankInterMap.get(keys[i]);
//			List<SemInterpretation> interpretations = rankInter.getInterpretations();
//			SimpleMatrix scores = rankInter.getScores();
//			
//			for(int j=0;j<interpretations.size();j++) {
////			System.out.print(interpretations.get(j).getInterpretation() + " : ");
////			System.out.print(scores.get(j, 0));
////			System.out.println();
//			}
//			
//			Proximity proximity = new Proximity(interpretations,LRTrainer.getInstance().trainFromGroundTruth().getSemEntBeanSet());
//			proximity.buildEdgeScoreMatrix();
//			SimpleMatrix edgeScores = proximity.getEdgeScoreMatrix();
//			
//			for(int r=0,numRows = edgeScores.numRows();r<numRows;r++) {
//				for(int c=0,numCols=edgeScores.numCols();c<numCols;c++) {
//					System.out.print(edgeScores.get(r,c) +",");
//				}
//			}
//			
//			
//
//		}
		
		
		
		//lrTrainer.trainInIteration(null);
		
		
	}

}

class TempInterpretation {
	double score;
	double teleport;
	String interpretation;
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getTeleport() {
		return teleport;
	}
	public void setTeleport(double teleport) {
		this.teleport = teleport;
	}
	public String getInterpretation() {
		return interpretation;
	}
	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}
	
	
}



class InterpretScoreComparator implements Comparator {

	InterpretScoreComparator() {
	}

	public int compare(Object obj1, Object obj2) {

		TempInterpretation rankInter1 = ((TempInterpretation) obj1);

		TempInterpretation rankInter2 = ((TempInterpretation) obj2);

		if (rankInter1.getScore() < rankInter2.getScore())

			return 1;

		else if (rankInter1.getScore() > rankInter2.getScore())

			return -1;

		else

			return 0;
	}

}
