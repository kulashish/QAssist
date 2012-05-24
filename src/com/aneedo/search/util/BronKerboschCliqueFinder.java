package com.aneedo.search.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BronKerboschCliqueFinder {
    //~ Instance fields --------------------------------------------------------

   // private final Graph<V, E> graph;

    private List<Set<Integer>> cliques;
    private boolean[][] graph;
    //private List<Integer> nodes = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new clique finder.
     *
     * @param graph the graph in which cliques are to be found; graph must be
     * simple
     */
    public BronKerboschCliqueFinder(boolean[][] graph)
    {
        this.graph = graph;
       // this.nodes = nodes;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Finds all maximal cliques of the graph. A clique is maximal if it is
     * impossible to enlarge it by adding another vertex from the graph. Note
     * that a maximal clique is not necessarily the biggest clique in the graph.
     *
     * @return Collection of cliques (each of which is represented as a Set of
     * vertices)
     */
    public List<Set<Integer>> getAllMaximalCliques()
    {
        // TODO jvs 26-July-2005:  assert that graph is simple

        cliques = new ArrayList<Set<Integer>>();
        List<Integer> potential_clique = new ArrayList<Integer>();
        List<Integer> candidates = new ArrayList<Integer>();
        List<Integer> already_found = new ArrayList<Integer>();
        //candidates = nodes;
        for(int i=0;i<graph.length;i++) {
        	candidates.add(i);
        }
        findCliques(potential_clique, candidates, already_found);
        return cliques;
    }

    /**
     * Finds the biggest maximal cliques of the graph.
     *
     * @return Collection of cliques (each of which is represented as a Set of
     * vertices)
     */
    public Collection<Set<Integer>> getBiggestMaximalCliques()
    {
        // first, find all cliques
        getAllMaximalCliques();

        int maximum = 0;
        Collection<Set<Integer>> biggest_cliques = new ArrayList<Set<Integer>>();
        for (Set<Integer> clique : cliques) {
            if (maximum < clique.size()) {
                maximum = clique.size();
            }
        }
        for (Set<Integer> clique : cliques) {
            if (maximum == clique.size()) {
                biggest_cliques.add(clique);
            }
        }
        return biggest_cliques;
    }

    private void findCliques(
        List<Integer> potential_clique,
        List<Integer> candidates,
        List<Integer> already_found)
    {
        List<Integer> candidates_array = new ArrayList<Integer>(candidates);
        if (!end(candidates, already_found)) {
            // for each candidate_node in candidates do
            for (Integer candidate : candidates_array) {
                List<Integer> new_candidates = new ArrayList<Integer>();
                List<Integer> new_already_found = new ArrayList<Integer>();

                // move candidate node to potential_clique
                potential_clique.add(candidate);
                candidates.remove(candidate);

                // create new_candidates by removing nodes in candidates not
                // connected to candidate node
                for (Integer new_candidate : candidates) {
                    if (graph[candidate][new_candidate]) {
                        new_candidates.add(new_candidate);
                    } // of if
                } // of for

                // create new_already_found by removing nodes in already_found
                // not connected to candidate node
                for (Integer new_found : already_found) {
                    if (graph[candidate][new_found]) {
                        new_already_found.add(new_found);
                    } // of if
                } // of for

                // if new_candidates and new_already_found are empty
                if (new_candidates.isEmpty() && new_already_found.isEmpty()) {
                    // potential_clique is maximal_clique
                    cliques.add(new HashSet<Integer>(potential_clique));
                    //System.out.println(potential_clique);
                } // of if
                else {
                    // recursive call
                    findCliques(
                        potential_clique,
                        new_candidates,
                        new_already_found);
                } // of else

                // move candidate_node from potential_clique to already_found;
                already_found.add(candidate);
                potential_clique.remove(candidate);
            } // of for
        } // of if
    }
    
    private void print(List<Integer> cliques) {
    	System.out.print("[");
		for(int i=0;i<cliques.size();i++) {
			System.out.print(cliques.get(i) +",");
		}
		System.out.println("]");
    }

    private boolean end(List<Integer> candidates, List<Integer> already_found)
    {
        // if a node in already_found is connected to all nodes in candidates
        boolean end = false;
        int edgecounter;
        for (Integer found : already_found) {
            edgecounter = 0;
            for (Integer candidate : candidates) {
                if (graph[found][candidate]) {
                    edgecounter++;
                } // of if
            } // of for
            if (edgecounter == candidates.size()) {
                end = true;
            }
        } // of for
        return end;
    }
    
    public static void main(String[] args) {
		boolean[][] graph = new boolean[4][4];
		graph[0][1] = graph[1][0] = true;
		graph[2][1] = graph[1][2] = true;
		//graph[2][0] = graph[0][2] = true;
		
		List<Integer> nodeList = new ArrayList<Integer>();
		nodeList.add(2324);
		nodeList.add(4894);
		nodeList.add(354);
		nodeList.add(133);
		
		
		BronKerboschCliqueFinder cliqueFinder = new BronKerboschCliqueFinder(graph);
		List<Set<Integer>> cliques = cliqueFinder.getAllMaximalCliques();
		
		for(int i=0,size=cliques.size();i<size;i++) {
			Set<Integer> nodes = cliques.get(i);
			final Iterator<Integer> itr = nodes.iterator();
			while(itr.hasNext()) {
				System.out.print(itr.next() +" ");
			}
			System.out.println();
		}
	}
}

