package com.aneedo.search.ranking.features.edge;

import gnu.trove.TIntObjectHashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aneedo.indexing.IndexingConstants;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.ranking.util.FolksonomyMeasures;

public class Proximity extends EdgeFeature {

	public Proximity(List<SemInterpretation> interpretations, Set<SemEntityBean> store)
	{
		super(interpretations, store);
	}
	
	public void getHopDistance(Integer entityId1, Integer entityId2) {
		
	}
	
	private double getHierarchyProximity(String interL, String interR,FolksonomyMeasures folkMeasures) {
		SemEntityBean semEntityL = getSemEntity(interL);
		SemEntityBean semEntityR = getSemEntity(interR);
		return getHierarchyProximity(semEntityL, semEntityR,folkMeasures);
	}
	
	private double getHierarchyProximity(SemInterpretation semInterL, SemInterpretation semInterR,FolksonomyMeasures folkMeasures) {
		SemEntityBean semEntityL = getSemEntity(semInterL.getInterpretation());
		SemEntityBean semEntityR = getSemEntity(semInterR.getInterpretation());
		return getHierarchyProximity(semEntityL, semEntityR,folkMeasures);
	}
	
	private double getHierarchyProximity(SemEntityBean semEntityL, SemEntityBean semEntityR,FolksonomyMeasures folkMeasures) {
		if(semEntityL ==null || semEntityR == null) {
			return 0;
		}
		
		// Try even sub categories later
		TIntObjectHashMap<List<String>> wordSemMapL = semEntityL.getWordSemClassMap();
		TIntObjectHashMap<List<String>> wordSemMapR = semEntityR.getWordSemClassMap();

		final List<String> semClassMemberListL = wordSemMapL.get(IndexingConstants.INT_SEM_CLASS_HYPERNYM);
		final List<String> semClassMemberListR = wordSemMapR.get(IndexingConstants.INT_SEM_CLASS_HYPERNYM);
		double score = 0;
		if(semClassMemberListL != null && semClassMemberListR != null) {
			for(int j=0,size=semClassMemberListR.size();j<size;j++) {
				final String keyR = semClassMemberListR.get(j);
				Map<String,String[]> ancestorMapR = folkMeasures.getRootPath(keyR);
				
				if(ancestorMapR == null || ancestorMapR.size()==0) {
					continue;
				}
				// If hypernyms equal then more similar
				if (semClassMemberListL.contains(keyR)) {
					score = score + Math.log(ancestorMapR.size());
					continue;
				}
				
				final String[] ancestorsR = ancestorMapR.get(keyR);
				
				double tempScore = 0;
				int common = 0;
				for(int k=0,sizeL=semClassMemberListL.size();k<sizeL;k++) {
					final String keyL = semClassMemberListL.get(k);
					Map<String,String[]> ancestorMapL = folkMeasures.getRootPath(keyL);
					if(ancestorMapL == null || ancestorMapL.size() == 0) continue;
					final String[] ancesotrsL = ancestorMapL.get(keyL);
					
					if(ancestorsR == null || ancesotrsL == null) continue;
					for(int l=0;l<ancestorsR.length;l++) {
						for(int m=0;m<ancesotrsL.length;m++) {
							if(ancesotrsL[m].equalsIgnoreCase(ancestorsR[l])) {
								tempScore = tempScore + Math.log(ancesotrsL.length-m);
								common++;
								break;
							}
						}
					}
					
				}
				score = score + common * tempScore;
			}
		}
		
		
		return 0;
	}

	@Override
	public void buildEdgeScoreMatrix()
	{
		List<SemInterpretation> interpretationList = getAllInterpretations();
		SemInterpretation semInterL = null;
		SemInterpretation semInterR = null;
		FolksonomyMeasures folkMeasures = FolksonomyMeasures.getInstance();
		for(int i = 0, outSize=interpretationList.size(); i < outSize; i++)
		{
			semInterL = interpretationList.get(i);
			for(int j = i+1,inSize =interpretationList.size(); j <inSize; j++)
			{
				semInterR = interpretationList.get(j);
				final double weight = getHierarchyProximity(semInterL, semInterR,folkMeasures);
				edgeScore.set(i, j, weight);
				edgeScore.set(j, i, weight);
			}
			edgeScore.set(i, i, 1);
		}
	}
	
	private SemEntityBean getSemEntity(String interpretation) {
		Iterator<SemEntityBean> semEntityItr = store.iterator();
		interpretation = interpretation.toLowerCase();
		while(semEntityItr.hasNext()) {
			final SemEntityBean semEntity = semEntityItr.next();
			final String entTitle = semEntity.getTitle().toLowerCase();
			if(entTitle.equalsIgnoreCase(interpretation)
					|| entTitle.indexOf(interpretation) >= 0
					|| interpretation.indexOf(entTitle) >= 0) {
				
				return semEntity;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		Proximity finder= new Proximity(null,null);
		String catName = "Laptops";
		finder.getHopDistance(null,null);
	}
}


