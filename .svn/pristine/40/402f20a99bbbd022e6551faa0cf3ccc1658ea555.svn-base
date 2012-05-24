package com.aneedo.search.ranking.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aneedo.search.SemClassStore;
import com.aneedo.search.SemanticIndexSearcher;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;

public class InterpretationRankingUtil {
	
	private static InterpretationRankingUtil util = new InterpretationRankingUtil();
	
	public static InterpretationRankingUtil getInstance() {
		return util;
	}
	
	public List<SemInterpretation> getEntitiesInterpretationsFromSearch(String key, Set<SemEntityBean> semEntBeanSet) {
		
		SemClassStore semClassStore = SemanticIndexSearcher.getInstance()
				.getResults(key, null);

		List<SemInterpretation> semInterpretList = semClassStore
				.getSemInterpretationList();
		int[] entIds = semClassStore.getSemEntityBeanMap().keys();
		for(int j=0;j<entIds.length;j++) {
			semEntBeanSet.add(semClassStore.getSemEntity(entIds[j]));
		}
		
		return semInterpretList;
	}

}
