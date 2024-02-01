package spell;

//import javax.security.auth.kerberos.DelegationPermission;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;
import java.io.File;

public class SpellCorrector implements ISpellCorrector {
    private final Trie dictionary;
    private final TreeSet<String> possible;
    private final TreeSet<String> match;

    public SpellCorrector() {
        possible = new TreeSet<>();
        match = new TreeSet<>();
        dictionary = new Trie();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File file = new File(dictionaryFileName);
        ScanFile(file);
    }

    public void ScanFile(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("(#[^\\n]*\\n)|(\\s+)+");

        while (scanner.hasNext()) {
            String tempString = scanner.next();
            dictionary.add(tempString);
        }
    }

    @Override
    public String suggestSimilarWord(String wordInput) {
        if (wordInput.isEmpty()) {
            return null;
        }
        wordInput = wordInput.toLowerCase();

        if (dictionary.find(wordInput) != null) {
            return wordInput;
        }

        possible.clear();
        match.clear();
        calculateDistances(wordInput);
        String frequentWordCheck = CheckWord();

        if (match.isEmpty()) {
            TreeSet<String> clonePossibleWords = new TreeSet<>(possible);
            for (String temp : clonePossibleWords) {
                calculateDistances(temp);
            }

            frequentWordCheck = CheckWord();
            if (match.isEmpty()) {
                return null;
            }
        }
        if (match.size() == 1) {
            return match.first();
        }
        return frequentWordCheck;
    }

    private void calculateDistances(String word) {
        DeletionDistance(word);
        TranspositionDistance(word);
        AlterationDistance(word);
        InsertionDistance(word);
    }

    public void DeletionDistance(String word) {
        if (word.length() < 2) { return; }
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            temp.append(word);
            temp.deleteCharAt(i);
            possible.add(temp.toString());
            temp.setLength(0);
        }
    }

    public void TranspositionDistance(String word) {
        StringBuilder temp = new StringBuilder();
        StringBuilder subStr = new StringBuilder();

        if (word.length() < 2) {
            return;
        }

        for (int i = 0; i < word.length() - 1; ++i) {
            temp.append(word);
            subStr.append(temp.substring(i, i+2));
            subStr.reverse();
            temp.replace(i, i+2, subStr.toString());

            possible.add(temp.toString());
            temp.setLength(0);
            subStr.setLength(0);
        }
    }

    public void AlterationDistance(String word) {
        StringBuilder temp = new StringBuilder();
        char letter;

        for (int i = 0; i < word.length(); ++i) {
            for (int j = 0; j < 26; ++j) {
                letter = 'a';
                letter += (char) j;
                temp.append(word);
                temp.setCharAt(i, letter);
                possible.add(temp.toString());
                temp.setLength(0);
            }
        }
    }

    public void InsertionDistance(String word) {
        StringBuilder temp = new StringBuilder();
        char letter;

        for (int i = 0; i < word.length() + 1; ++i) {
            for (int j = 0; j < 26; ++j) {
                letter = 'a';
                letter += (char) j;
                temp.append(word);
                temp.insert(i, letter);
                possible.add(temp.toString());
                temp.setLength(0);
            }
        }
    }

    public String CheckWord() {
        INode tempNode;
        int max = 0;
        String wordFrequency = null;

        for (String str : possible) {
            tempNode = dictionary.find(str);
            if (tempNode != null) {
                match.add(str);
                if (tempNode.getValue() > max) {
                    max = tempNode.getValue();
                    wordFrequency = str;
                }
            }
        }
        return wordFrequency;
    }
}