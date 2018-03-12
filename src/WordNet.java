import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class WordNet {
    private Map<String, Integer> synToId = new TreeMap<>();
    private ArrayList<ArrayList<Integer>> links = new ArrayList<>();

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
            String[] synset = data[1].split("\\s+");
            for (String aSynset : synset) {
                synToId.put(aSynset, id);
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

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return null;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return null;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
    }
}
