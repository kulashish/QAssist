package com.aneedo.search.ranking.features.edge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.ejml.simple.SimpleMatrix;

import com.aneedo.indexing.IndexingConstants;
import com.aneedo.search.SemClassStore;
import com.aneedo.search.bean.SemEntityBean;
import com.aneedo.search.bean.SemInterpretation;

public class EdgeFeatures
{
	private InterpretationContentOverlap interpretationContentOverlap = null;
	private Proximity proximity = null;
	protected Set<SemEntityBean> store = null;
	private Collection<SemClassOverlap> semClassBased = null;
	
	public EdgeFeatures(List<SemInterpretation> interpretations, Set<SemEntityBean> store)
	{
		this.store  = store;
		interpretationContentOverlap = new InterpretationContentOverlap(interpretations, store);
		proximity = new Proximity(interpretations, store);
		semClassBased = new ArrayList<SemClassOverlap>(32);
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SECTIONS, interpretations, store));
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SEM_CLASS_SYNOPSIS, interpretations, store));
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SEM_CLASS_SYNONYM, interpretations, store));
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SEM_CLASS_HYPERNYM, interpretations, store));
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SEM_CLASS_HYPONYM, interpretations, store));
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SEM_CLASS_SIBLING, interpretations, store));
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SEM_CLASS_ASSOCIATION_INLINK, interpretations, store));
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SEM_CLASS_ASSOCIATION_RELATED, interpretations, store));
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SEM_CLASS_ASSOCIATION_HIERARCHY, interpretations, store));
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SEM_CLASS_REFERENCE, interpretations, store));
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SEM_CLASS_FREQUENT, interpretations, store));
		semClassBased.add(new SemClassOverlap(IndexingConstants.INT_SEM_CLASS_MERONYM, interpretations, store));
	}
	
	public InterpretationContentOverlap interpretationContentOverlap()
    {
		return interpretationContentOverlap;
	}
	
	public Proximity proximity()
    {
		return proximity;
	}

	public Collection<SemClassOverlap> semClassBased()
	{
		return semClassBased ;
	}
}
