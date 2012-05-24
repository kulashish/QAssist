package com.aneedo.util;

import java.util.HashMap;
import java.util.Map;

import com.aneedo.indexing.IndexingConstants;

public class SemanticClassDatastore {
	private static final SemanticClassDatastore instance = new SemanticClassDatastore();
	Map<String, StringBuilder> interpretation = new HashMap<String, StringBuilder>();
	
	private SemanticClassDatastore() {
		interpretation.put(IndexingConstants.SEM_CLASS_HYPERNYM, getHypernym());
	}
	
	public SemanticClassDatastore getInstance() {
		return instance;
	}
	
	public StringBuilder getHypernym() {
		StringBuilder hypernyms = new StringBuilder();
		hypernyms.append("type, classification, list");
		return hypernyms;
	}
	
	public StringBuilder getMaker() {
		StringBuilder hypernyms = new StringBuilder();
		hypernyms.append("develop, make, product, gadget, device, designed, model");
		return hypernyms;
	}
	
	public StringBuilder getProduct() {
		StringBuilder hypernyms = new StringBuilder();
		hypernyms.append("shopping, vendor,repair,accessories, upgrade,support,hardware,organization,location,place,export, import,website " +
				"purchase, buy, sell, tool, cost, food,handset,equipment" +
				"news and reviews, techniques, recipe, ideas,references, approaches, Performance,comparison, promising suggestion, recommendation, special offer, interesting,surprising");
		return hypernyms;
	}
	
	public StringBuilder getReference() {
		StringBuilder hypernyms = new StringBuilder();
		hypernyms.append("news and reviews, techniques, recipe, ideas, references, approaches, performance, comparison, promising suggestion," +
				" recommendation, special offer, interesting, surprising");
		return hypernyms;
	} 
	
	public StringBuilder getCauseEffect() {
		StringBuilder hypernyms = new StringBuilder();
		hypernyms.append("issue, conflict, effect, error, exception, side effect, loss, lose, overhead, break down, " +
				"stress, impact, controversy, feed back, comment, unsuspect, unexpect, health concerns, hazard, threat, " +
				"environmental social aspect, environmental social concern, " +
				"security, hazard, safety, toxicity, contaminate, expensive, time consuming, slow, poor, damage, risk");
		return hypernyms;
	}
	
	public StringBuilder getBenefitProfit() {
		StringBuilder hypernyms = new StringBuilder();
		hypernyms.append("facilitates, facility, award, amount, cost, revenue, prize, nutritional, advantage, profit, benefit, cheap, " +
				"fast, efficient, achievement, powerful, pay, payment");
		return hypernyms;
	}
}
