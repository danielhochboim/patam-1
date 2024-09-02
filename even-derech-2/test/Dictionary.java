package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Dictionary {
    private String[] fileNames;
    private CacheManager cacheExistingWords;
    private CacheManager cacheNonExistingWords;
    private BloomFilter bloomFilter;

    public Dictionary(String...fileNames){
        this.fileNames = fileNames;
        this.cacheExistingWords = new CacheManager(400, new LRU());
        this.cacheNonExistingWords = new CacheManager(100, new LFU());
        this.bloomFilter = new BloomFilter(256, "MD5", "SHA1");

        Scanner myScanner = null;
        try {
            //iterating over the files and puting it in the bloom filter
            for(String fileName : fileNames){
                myScanner = new Scanner(new BufferedReader(new FileReader(fileName)));
                while(myScanner.hasNext()){
                    bloomFilter.add(myScanner.next());
                }
                if(myScanner != null){
                    myScanner.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            if(myScanner != null){
                myScanner.close();
            }
        }
    }
    public boolean query(String word){
        if(cacheExistingWords.query(word)){
            return true;
        }
        else if(cacheNonExistingWords.query(word)){
            return false;
        }
        else if(bloomFilter.contains(word)){
            cacheExistingWords.add(word);
            return true;
        }
        else{
            cacheNonExistingWords.add(word);
            return false;
        }
    }
    public boolean challenge(String word){
        boolean isExist;
        try{
            isExist = IOSearcher.search(word, fileNames);
        }catch(Exception e){
            return false;
        }

        if(isExist){
            cacheExistingWords.add(word);
            return true;
        }
        else{
            cacheNonExistingWords.add(word);
            return false;
        }
    }

}
