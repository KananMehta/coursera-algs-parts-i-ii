/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class WordNet {

    private SAP shAnP;
    private ArrayList<String> syns; // get syn string from id
    private HashMap<String, HashSet<Integer>> nounToSyns; // get syn ids from noun

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        In in = new In(synsets);
        nounToSyns = new HashMap<>();
        syns = new ArrayList<>();
        while (!in.isEmpty()) {
            String[] line = in.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            syns.add(id, line[1]);
            String[] words = line[1].split(" ");
            for (String noun : words) {
                HashSet<Integer> idSet = new HashSet<>();
                if (nounToSyns.containsKey(noun)) {
                    idSet = nounToSyns.get(noun);
                    idSet.add(id);
                }
                else idSet.add(id);
                nounToSyns.put(noun, idSet);
            }
        }
        In in2 = new In(hypernyms);
        Digraph g = new Digraph(syns.size());
        while (!in2.isEmpty()) {
            String[] line = in2.readLine().split(",");
            for (int i = 1; i < line.length; i++)
                g.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
        }
        rootedDAG(g);
        shAnP = new SAP(g);
    }

    private void rootedDAG(Digraph g) {
        DirectedCycle c = new DirectedCycle(g);
        if (c.hasCycle()) throw new IllegalArgumentException();
        int roots = 0;
        for (int i = 0; i < g.V(); i++) {
            if (!g.adj(i).iterator().hasNext()) roots++;
            if (roots > 1) break;
        }
        if (roots != 1) throw new IllegalArgumentException();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToSyns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounToSyns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        return shAnP.length(nounToSyns.get(nounA), nounToSyns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        int an = shAnP.ancestor(nounToSyns.get(nounA), nounToSyns.get(nounB));
        return syns.get(an);
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
