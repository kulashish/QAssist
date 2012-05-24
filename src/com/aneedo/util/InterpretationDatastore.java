package com.aneedo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aneedo.indexing.IndexingConstants;

public class InterpretationDatastore {
	
	private static final InterpretationDatastore instance = new InterpretationDatastore();
	Map<String, List<String>> interpretation = new HashMap<String, List<String>>();
	
	private InterpretationDatastore() {
		interpretation.put(IndexingConstants.SEM_CLASS_HYPERNYM, getHypernym());
		interpretation.put(IndexingConstants.SEM_CLASS_HYPONYM, getHyponym());
		interpretation.put(IndexingConstants.SEM_CLASS_MERONYM, getMeronym());
		interpretation.put(IndexingConstants.SEM_CLASS_MAKEPRODUCE, getMaker());
		interpretation.put(IndexingConstants.SEM_CLASS_PRODUCT, getProduct());
	}
	
	public InterpretationDatastore getInstance() {
		return instance;
	}
	
	private List<String> getHypernym() {
		List<String> hypernyms = new ArrayList<String>();
		hypernyms.add("? vs ?");
		hypernyms.add("? and ?");
		return hypernyms;
	}
	
	private List<String> getHyponym() {
		List<String> hypernyms = new ArrayList<String>();
		hypernyms.add("Types of ?");
		return hypernyms;
	}

	private List<String> getMeronym() {
		List<String> hypernyms = new ArrayList<String>();
		hypernyms.add("Components of ?");
		return hypernyms;
	}

	private List<String> getMaker() {
		List<String> hypernyms = new ArrayList<String>();
		hypernyms.add("Products ?");
		return hypernyms;
	}

	private List<String> getProduct() {
		List<String> hypernyms = new ArrayList<String>();
		hypernyms.add("? repair");
		hypernyms.add("? shopping");
		return hypernyms;
	}

	private List<String> getRole() {
		List<String> hypernyms = new ArrayList<String>();
		hypernyms.add("Availability of ?");
		return hypernyms;
	}

}
