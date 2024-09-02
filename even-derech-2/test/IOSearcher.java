package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class IOSearcher {
    public static boolean search(String word, String...fileNames){
        Scanner myScanner = null;
        String scannedWord;
        try {
            // iterating over the files and serching the word
            for(String fileName : fileNames){
                myScanner = new Scanner(new BufferedReader(new FileReader(fileName)));
                while(myScanner.hasNext()){
                    scannedWord = myScanner.next();
                    if(scannedWord.equals(word)){
                        return true;
                    }
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
        return false;
    }

}
