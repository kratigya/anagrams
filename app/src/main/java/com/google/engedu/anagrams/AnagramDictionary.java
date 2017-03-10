package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private HashSet<String> wordSet = new HashSet<>();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> letterToWord = new HashMap<>();
    private ArrayList<String> wordSelect = new ArrayList<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();

            wordList.add(word);
            wordSet.add(word);

            ArrayList<String> sortedList = new ArrayList<>();
            String sortedword = sortLetters(word);

            if (!(letterToWord.containsKey(sortedword))) {
                sortedList.add(word);
                letterToWord.put(sortedword, sortedList);
            } else {
                sortedList = letterToWord.get(sortedword);
                sortedList.add(word);
                letterToWord.put(sortedword, sortedList);
            }
        }
        for (Map.Entry<String, ArrayList<String>> entry : letterToWord.entrySet()) {
            String key = entry.getKey();
            if (key.length() >= DEFAULT_WORD_LENGTH && key.length() <= MAX_WORD_LENGTH) {
                if (entry.getValue().size() >= MIN_NUM_ANAGRAMS) {
                    wordSelect.add(key);
                }
            }
        }
    }

    private String sortLetters(String word) {
        char[] characters = word.toCharArray();
        Arrays.sort(characters);
        return new String(characters);

    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !base.contains(word);
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        String sortedTargetWord = sortLetters(targetWord);
        for (String item : wordList) {
            String sortedWordList = sortLetters(item);
            if (sortedTargetWord.length() == sortedWordList.length() && sortedTargetWord.equals(sortedWordList))
                result.add(item);
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> tempList;
        ArrayList<String> resultList = new ArrayList<>();

        //Add one character
        for (char alpa = 'a'; alpa <= 'z'; alpa++) {
            String anagram = word + alpa;
            //Sort the new word
            String sortedAnagaram = sortLetters(anagram);
            if (letterToWord.containsKey(sortedAnagaram)) {
                tempList = new ArrayList<>();
                tempList = letterToWord.get(sortedAnagaram);
                for (int i = 0; i < tempList.size(); i++)
                    if (!(tempList.get(i).contains(word))) {
                        resultList.add(String.valueOf(tempList.get(i)));
                    }
            }
        }
        return resultList;
    }

    public String pickGoodStarterWord() {
        int index = random.nextInt(wordSelect.size());
        return wordSelect.get(index);
    }
}