package test;

import java.util.HashSet;

public class CacheManager {
    private int size;
    private HashSet<String> words;
    CacheReplacementPolicy crp;
	
    public CacheManager(int size, CacheReplacementPolicy crp){
        this.size = size;
        this.words = new HashSet<String>();
        this.crp = crp;
    }

    public boolean query(String word){
        crp.add(word);
        return words.contains(word);
    }
    public void add(String word){
        crp.add(word);
        words.add(word);
        if(words.size() > size){
            words.remove(crp.remove());
        }
    }
	

}
