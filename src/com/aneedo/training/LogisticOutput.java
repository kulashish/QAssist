package com.aneedo.training;

import java.util.Set;

import com.aneedo.search.bean.SemEntityBean;

import gnu.trove.TIntObjectHashMap;

public class LogisticOutput {
	TIntObjectHashMap<RankedInterpretation> rankedInterQueryMap;
	Set<SemEntityBean> semEntBeanSet;
	public TIntObjectHashMap<RankedInterpretation> getRankedInterQueryMap() {
		return rankedInterQueryMap;
	}
	public void setRankedInterQueryMap(
			TIntObjectHashMap<RankedInterpretation> rankedInterQueryMap) {
		this.rankedInterQueryMap = rankedInterQueryMap;
	}
	public Set<SemEntityBean> getSemEntBeanSet() {
		return semEntBeanSet;
	}
	public void setSemEntBeanSet(Set<SemEntityBean> semEntBeanSet) {
		this.semEntBeanSet = semEntBeanSet;
	}
}
