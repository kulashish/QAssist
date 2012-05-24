package com.aneedo.search.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aneedo.search.SemClassStore;
import com.aneedo.search.bean.CompatibleEntitySet;
import com.aneedo.search.bean.SemEntityBean;

public class CompatibleSetFinder {
	public void findCompatibleSets(SemClassStore repos) {
		List<CompatibleEntitySet> comEntitySet = new ArrayList<CompatibleEntitySet>();
		List<Integer> pageIdList = repos.getPageIds();
		List<boolean[]> hardMatch = repos.getHardMatch();
		int size = pageIdList.size();
		
		List<Integer> residual = new ArrayList<Integer>();
		//residual.addAll(pageIdList);
		List<boolean[]> hardMatchResidual = new ArrayList<boolean[]>();
		//hardMatchResidual.addAll(hardMatch);

		double idcg = repos.getQueryDetailBean().getQueryIDCG();

		boolean[] matchQuery = repos.getQueryMatch();
		boolean[] testNoMatch = new boolean[matchQuery.length];
		Arrays.fill(testNoMatch, false);
		boolean[] testAllMatch = new boolean[matchQuery.length];
		Arrays.fill(testAllMatch, true);
		
		boolean anyExactMatch = false;
		
		for(int i=0;i<size;i++) {
			boolean[] matches = hardMatch.get(i);
			if(Arrays.equals(matches, testAllMatch)) {
				final CompatibleEntitySet set = new CompatibleEntitySet(pageIdList.get(i));
				//set.setEntitySetType(SemClassConstants.ENT_SET_TYPE_EXACT_MATCH);
				comEntitySet.add(set);
				
				//hardMatchResidual.add(i);
				SemEntityBean semEntityBean = repos.getSemEntity(pageIdList.get(i));
				
				set.setTitleMatchScore(SemanticSearchUtil.getInstance().
						getNDCGSimilarity(semEntityBean.getSoftTitleQueryMatch(), 
								semEntityBean.getMatchedPositions(), matches, idcg));
						
			} else if(Arrays.equals(matches, testNoMatch)) {
				repos.getNoMatchEntityList().add(pageIdList.get(i));
				//set.setEntitySetType(SemClassConstants.ENT_SET_TYPE_NO_MATCH);
				//residual.add(i);
				//hardMatchResidual.remove(i);
				
			} else if(!anyExactMatch) {
				residual.add(pageIdList.get(i));
				hardMatchResidual.add(matches);
				// keep track what keywords in the query matched
				for(int j=0;j<matches.length;j++) {
					
					if(matches[j]) matchQuery[j] = true;
					
					if(Arrays.equals(matchQuery, testAllMatch)) {
						anyExactMatch = true;
						break;
					}
				}
			} else {
				residual.add(pageIdList.get(i));
				hardMatchResidual.add(matches);
			}
		}
		repos.setQueryMatch(matchQuery);
		
		
		size = residual.size();
		boolean[][] compMatrix = new boolean[size][size];
		int sizeOfLargerSet = 1;
		if(size > 1) {
		
		//System.out.println("Residual size : " + size);
		
		for(int i=0;i<size;i++) {
			compMatrix[i][i] = false;
			for(int j=i+1;j<size;j++) {
				compMatrix[i][j] = compMatrix[j][i] = 
					isCompatible(hardMatchResidual.get(i), hardMatchResidual.get(j));
				
			}
		}
		
		BronKerboschCliqueFinder cliqueFinder = new BronKerboschCliqueFinder(compMatrix);
		List<Set<Integer>> cliques = cliqueFinder.getAllMaximalCliques();
		double sum = 0.0;
		for(int i=0,noOfCliques=cliques.size();i<noOfCliques;i++) {
			final Set<Integer> nodes = cliques.get(i);
			if(nodes.size() > sizeOfLargerSet) {
				sizeOfLargerSet = nodes.size();
			}
			final Iterator<Integer> nodeItr = nodes.iterator();
			final Set<Integer> pageIdSet = new HashSet<Integer>();
			sum = 0.0;
			while(nodeItr.hasNext()) {
				int index = nodeItr.next();
				//System.out.println("Clique node : " + index);
				final Integer pageId = residual.get(index);
				//System.out.print(pageId+",");
				final SemEntityBean semEntityBean = repos.getSemEntity(pageId);
				sum = sum + SemanticSearchUtil.getInstance().
				getNDCGSimilarity(semEntityBean.getSoftTitleQueryMatch(), 
						semEntityBean.getMatchedPositions(), matchQuery, idcg);
				pageIdSet.add(pageId);
			}
			//System.out.println();
			final CompatibleEntitySet set = new CompatibleEntitySet(pageIdSet);
			//set.setEntitySetType(SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_NO_EVIDENCE);
			set.setTitleMatchScore(sum / nodes.size());
			comEntitySet.add(set);
		}
	} else if(size != 0) {
		
		final CompatibleEntitySet set = new CompatibleEntitySet(residual.get(0));
		//set.setEntitySetType(SemClassConstants.ENT_SET_TYPE_PAIRWISE_MATCH_NO_EVIDENCE);
		comEntitySet.add(set);
		
		//hardMatchResidual.add(i);
		SemEntityBean semEntityBean = repos.getSemEntity(residual.get(0));
		
		set.setTitleMatchScore(SemanticSearchUtil.getInstance().
				getNDCGSimilarity(semEntityBean.getSoftTitleQueryMatch(), 
						semEntityBean.getMatchedPositions(), matchQuery, idcg));

		
	}
		
		residual = null;
		hardMatchResidual = null;
		repos.setCompatibleList(comEntitySet);
		repos.setSizeOfLargerSet(sizeOfLargerSet);
	}
	
	private boolean isCompatible(boolean[] xi, boolean[] xj) {
		//print(xi);
		//print(xj);
		for (int i = 0; i < xi.length; i++) {
			if(xi[i] & xj[i]) {
				return false;
			}
		}
		return true;
	}
	
	private void print(boolean[] objs) {
		System.out.print("[");
		for(int i=0;i<objs.length;i++) {
			System.out.print(objs[i] +",");
		}
		System.out.println("]");
	}
}
