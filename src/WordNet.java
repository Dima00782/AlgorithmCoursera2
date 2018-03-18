import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.Queue;
import java.util.LinkedList;

public class WordNet {
    private Map<String, ArrayList<Integer>> synToId = new TreeMap<>();
    private Map<Integer, String> IdToSyn = new TreeMap<>();
    private ArrayList<ArrayList<Integer>> links = new ArrayList<>();

    private int size() {
        return links.size();
    }

    private class DirectedBFS {
        private boolean[] marked;
        private int[] distanceTo;

        public DirectedBFS(ArrayList<Integer> starts) {
            marked = new boolean[size()];
            distanceTo = new int[size()];
            Arrays.fill(distanceTo, Integer.MAX_VALUE);

            bfs(starts);
        }

        private void bfs(ArrayList<Integer> starts) {
            Queue<Integer> queue = new LinkedList<>();
            for (Integer id : starts) {
                marked[id] = true;
                distanceTo[id] = 0;
                queue.add(id);
            }

            while (!queue.isEmpty()) {
                int current = queue.remove();
                for (Integer neighbor : links.get(current)) {
                    if (!marked[neighbor]) {
                        queue.add(neighbor);
                        distanceTo[neighbor] = Math.min(distanceTo[neighbor], distanceTo[current] + 1);
                        marked[neighbor] = true;
                    }
                }
            }
        }

        public int[] getDistanceTo() {
            return distanceTo;
        }
    }

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        In synsetIn = new In(synsets);
        int kSynsets = 0;
        while (synsetIn.hasNextLine()) {
            String line = synsetIn.readLine();
            String[] data = line.split(",");

            assert data.length == 3;

            int id = Integer.parseInt(data[0]);
            IdToSyn.put(id, data[1]);
            String[] synset = data[1].split("\\s+");
            for (String aSynset : synset) {
                if (!synToId.containsKey(aSynset)) {
                    synToId.put(aSynset, new ArrayList<>());
                }
                synToId.get(aSynset).add(id);
            }

            ++kSynsets;
        }

        for (int i = 0; i < kSynsets; ++i) {
            links.add(new ArrayList<>());
        }

        In hypernymsIn = new In(hypernyms);
        while (hypernymsIn.hasNextLine()) {
            String line = hypernymsIn.readLine();
            String[] data = line.split(",");

            int id = Integer.parseInt(data[0]);
            for (int i = 1; i < data.length; ++i) {
                links.get(id).add(Integer.parseInt(data[i]));
            }
        }
    }

    private int[] leastCommonAncestorWithLength(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        DirectedBFS bfsForA = new DirectedBFS(synToId.get(nounA));
        DirectedBFS bfsForB = new DirectedBFS(synToId.get(nounB));

        int[] distToA = bfsForA.getDistanceTo();
        int[] distToB = bfsForB.getDistanceTo();
        int min = Integer.MAX_VALUE;
        int leastCommonAncestor = -1;
        for (int i = 0; i < distToA.length; ++i) {
            if (distToA[i] != Integer.MAX_VALUE && distToB[i] != Integer.MAX_VALUE) {
                if (distToA[i] + distToB[i] < min) {
                    leastCommonAncestor = i;
                    min = distToA[i] + distToB[i];
                }
            }
        }
        return new int[] {leastCommonAncestor, min};
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new ArrayList<>(synToId.keySet());
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return synToId.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return leastCommonAncestorWithLength(nounA, nounB)[1];
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        int lca = leastCommonAncestorWithLength(nounA, nounB)[0];
        return IdToSyn.get(lca);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
    }
}
