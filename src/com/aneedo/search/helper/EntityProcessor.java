package com.aneedo.search.helper;

import java.util.List;

import com.aneedo.indexing.IndexingConstants;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;
import com.aneedo.search.util.SemanticSearchUtil;

public class EntityProcessor {
	
	private static final EntityProcessor entityProcessor = new EntityProcessor();
	
	private EntityProcessor() {
		
	}
	
	public static EntityProcessor getInstance() {
		return entityProcessor;
	}
	public void processEntity(SemEntityBean semEntityBean, 
			List<SemInterpretation> semInterList, 
			boolean isEntityMatch, double entSetScore) {

		String[] semClassMembers = semEntityBean.getAllSemClassMembers();
		List<double[]>[] semClassQueryMatch = semEntityBean.getSemClassQueryMatch();
		
		for(int i=0;i<semClassMembers.length;i++) {
			List<double[]> semSoftMatch = null;
			if(semClassMembers[i] != null) {
			switch(i) {
				case IndexingConstants.INT_SEM_CLASS_ASSOCIATION_HIERARCHY :
					semSoftMatch = semClassQueryMatch[IndexingConstants.INT_SEM_CLASS_ASSOCIATION_HIERARCHY];
					addSemMember(semInterList, semClassMembers[i], entSetScore, semEntityBean.getTitle() + "--> Association", semSoftMatch);
					break;
				case IndexingConstants.INT_SEM_CLASS_ASSOCIATION_RELATED :
					semSoftMatch = semClassQueryMatch[IndexingConstants.INT_SEM_CLASS_ASSOCIATION_RELATED];
					addSemMember(semInterList, semClassMembers[i], entSetScore, semEntityBean.getTitle() + "--> Related", semSoftMatch);
					break;
				case IndexingConstants.INT_SEM_CLASS_SIBLING :
					semSoftMatch = semClassQueryMatch[IndexingConstants.INT_SEM_CLASS_SIBLING];
					addSemMember(semInterList, semClassMembers[i], entSetScore, semEntityBean.getTitle() + "--> Sibling", semSoftMatch);
					break;
				case IndexingConstants.INT_SEM_CLASS_ASSOCIATION_INLINK :
					semSoftMatch = semClassQueryMatch[IndexingConstants.INT_SEM_CLASS_ASSOCIATION_INLINK];
					addSemMember(semInterList, semClassMembers[i], entSetScore, semEntityBean.getTitle() + "--> Association", semSoftMatch);
					break;
				case IndexingConstants.INT_SEM_CLASS_FREQUENT :
					semSoftMatch = semClassQueryMatch[IndexingConstants.INT_SEM_CLASS_FREQUENT];
					addSemMember(semInterList, semClassMembers[i], entSetScore, semEntityBean.getTitle() + "--> Association", semSoftMatch);
					break;
				default :
					break;
			}
			}
			
		}
		
	}
	
	private void addSemMember(List<SemInterpretation> semInterList, 
			String semClassMembers, double entSetScore, String actFlow, 
			List<double[]> semSoftMatch) {

		String[] splits = semClassMembers.split(" \\| ");

		for(int i=0;i<splits.length;i++) {
			final SemInterpretation semInter = new SemInterpretation();
			semInter.setAggScore(entSetScore + 
					SemanticSearchUtil.getInstance().getSoftMatchSum(semSoftMatch.get(i)));
			semInter.setInterpretation(splits[i]);
			semInter.setActivationPath(actFlow);
			semInterList.add(semInter);
		}
		
	}

}
