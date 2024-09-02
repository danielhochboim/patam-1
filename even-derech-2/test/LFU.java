package test;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LFU implements CacheReplacementPolicy{
    //I'm using two hash maps, the first hash map is to get the fequency of a given word
    // then i have LinkedHashSet for each frequency that contains the word in that frequency
    // the second hash map is to connect a fequency number to the corresponding LinkedHashSet
    // example of frequencyWords:
    // 1 -> ("c")
    // 2 -> ("b" <-> "a")
    private Map<String, Integer> wordFrequency;
    private Map<Integer, LinkedHashSet<String>> frequencyWords;
    private int minFrequency;

    public LFU() {
        wordFrequency = new HashMap<>();
        frequencyWords = new HashMap<>();
        minFrequency = 0;
    }

    @Override
    public void add(String word) {
        int frequency = wordFrequency.getOrDefault(word, 0);
        wordFrequency.put(word, frequency + 1);
        
        //remove the word from the LinkedHashSet of the previous frequency
        if (frequency > 0) {
            //remove from the LinkedHashSet
            frequencyWords.get(frequency).remove(word);
            // if the LinkedHashSet is empty, remove it
            if (frequencyWords.get(frequency).isEmpty()) {
                frequencyWords.remove(frequency);
                if (minFrequency == frequency) {
                    minFrequency++;
                }
            }
        } else {
            minFrequency = 1;
        }
        // add the word to the LinkedHashSet of the frequency + 1
        frequencyWords.computeIfAbsent(frequency + 1, k -> new LinkedHashSet<>()).add(word);
    }

    @Override
    public String remove() {
        if (minFrequency == 0 || frequencyWords.isEmpty()) {
            return null;
        }
        
        LinkedHashSet<String> leastFrequentWords = frequencyWords.get(minFrequency);
        String wordToRemove = leastFrequentWords.iterator().next();
        // removes the least frequent word
        leastFrequentWords.remove(wordToRemove);
        if (leastFrequentWords.isEmpty()) {
            frequencyWords.remove(minFrequency);
            minFrequency = frequencyWords.isEmpty() ? 0 : Collections.min(frequencyWords.keySet());
        }
        
        wordFrequency.remove(wordToRemove);
        
        return wordToRemove;
    }
}

