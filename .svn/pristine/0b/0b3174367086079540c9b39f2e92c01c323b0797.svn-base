package com.aneedo.search.ranking.affprpg;

import java.util.ArrayList;

public class AffinityPropagation {
	
	public static ArrayList<Node> nodes;
	public static ArrayList<Node> examplers;// cluster representatives
	
	public static ArrayList<ArrayList<Integer> > edges;
	public static Integer[] node_exampler;
	
	public static Double[][] responsibility;
	public static Double[][] availability;
	
	public static Double lambda = 0.5;//damping constant between 0 and 1
	
	public AffinityPropagation(){
		nodes = new ArrayList<Node>();
		examplers = new ArrayList<Node>();
		node_exampler = new Integer[nodes.size()];
	}
	
	public static Double computeSimilarity(Node n1, Node n2){
		return 0d;
	}
	
	public static void initializeSelfResponsibility(Node query_node){
		//similarity between node and query
		for(int i=0; i<nodes.size(); i++){
			responsibility[i][i] = computeSimilarity(query_node, nodes.get(i));
		}
	}
	
	public static void initializeResponsibility(){
		responsibility = new Double[nodes.size()][nodes.size()];
		for(int i=0; i<nodes.size(); i++){
			for(int j=0; j<nodes.size(); j++){
				responsibility[i][j] = 0d;
			}
		}
	}

	public static void initializeAvailability(){
		//initialise to 0
		availability = new Double[nodes.size()][nodes.size()];
		for(int i=0; i<nodes.size(); i++){
			for(int j=0; j<nodes.size(); j++){
				availability[i][j] = 0d;
			}
		}
	}
	
	public static void updateResponsibility(Node query_node){
		for(int i=0; i<nodes.size(); i++){
			Node curr_node = nodes.get(i);
			ArrayList<Integer> neighbours = curr_node.getNeigbours();//should contain the node itself
			for(int j=0; j<neighbours.size(); j++){
				Double max_resp = Double.MIN_VALUE;
				for(int k=0; k<neighbours.size(); k++){
					if(k==j) continue;
					Double resp = availability[i][neighbours.get(j)] + responsibility[i][neighbours.get(j)];
					if(resp>max_resp){
						max_resp = resp;
					}
				}
				if(i==j)
					responsibility[i][j] = lambda*responsibility[i][j] + (1-lambda) * computeSimilarity(curr_node, query_node) - max_resp;
				else
					responsibility[i][j] = lambda*responsibility[i][j] + (1-lambda) * computeSimilarity(curr_node, nodes.get(neighbours.get(j))) - max_resp;
			}
		}
	}

	public static void updateAvailability(){
		for(int k=0; k<nodes.size(); k++){
			//total responsibility available
			Node curr_node = nodes.get(k);
			ArrayList<Integer> neighbours = curr_node.getNeigbours();
			Double total_resp = 0d;
			for(int i =0; i<neighbours.size(); i++){
				if(neighbours.get(i) == k) continue;
				Double curr_resp = responsibility[neighbours.get(i)][k];
				if(curr_resp > 0) total_resp+=curr_resp;
			}
			for(int i=0; i<neighbours.size(); i++){
				Double curr_resp = responsibility[neighbours.get(i)][k];
				Double resp_available = total_resp;
				if(curr_resp > 0) resp_available = total_resp - curr_resp;
				Double avail = Math.max(0,responsibility[k][k] + resp_available);
				availability[i][k] = lambda*availability[i][k] + (1-lambda)*avail;
			}
			availability[k][k] = lambda*availability[k][k] + (1-lambda)*Math.max(0,total_resp);
		}
	}
	
	public static Integer getNodeExampler(Integer i){
		Node curr_node = nodes.get(i);
		ArrayList<Integer> neighbours = curr_node.getNeigbours();
		Double max_resp = Double.MIN_VALUE;
		Integer exampler = i;
		for(int j=0; j<neighbours.size(); j++){
			Double resp = availability[i][neighbours.get(j)] + responsibility[i][neighbours.get(j)];
			if(resp>max_resp){
				max_resp = resp;
				exampler = j;
			}
		}
		if(exampler != i) exampler = getNodeExampler(exampler);
		node_exampler[i] = exampler;
		
		return exampler;
	}
	
	public static void getExamplers() {
		for(int i=0; i<nodes.size(); i++) getNodeExampler(i);
	}
	
	public static Boolean isStable(){
		return false;
	}
	
	public static void clusterize(Node query_node){
		initializeResponsibility();
		initializeSelfResponsibility(query_node);
		initializeAvailability();
		while(!isStable()){
			updateResponsibility(query_node);
			updateAvailability();
			getExamplers();
		}
	}
	
	public static void main(String[] args){
		
		
	}
}
