package test;
import java.lang.String;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {
    private Map<String, Dictionary> dicts;
    private static DictionaryManager dictionaryManager = null;

    private DictionaryManager(){
        dicts = new HashMap<String, Dictionary>();
    }

    public boolean query(String...args){
        String word = args[args.length - 1];
        String[] books = Arrays.copyOfRange(args, 0, args.length - 1);
        boolean isContains = false;
        //iterating over books to check if the word is in one of the books
        for(String book : books){
            if(!dicts.containsKey(book)){
                dicts.put(book, new Dictionary(book));
            }
            if(dicts.get(book).query(word)){
                isContains = true;
            }
        }
        return isContains;
    }

    public boolean challenge(String...args){
        String word = args[args.length - 1];
        String[] books = Arrays.copyOfRange(args, 0, args.length - 1);
        boolean isContains = false;
        for(String book : books){
            //iterating over books to check if the word is in one of the books
            if(!dicts.containsKey(book)){
                dicts.put(book, new Dictionary(book));
            }
            if(dicts.get(book).challenge(word)){
                isContains = true;
            }
        }
        return isContains;
    }
    public int getSize(){
        return dicts.size();
    }
    public static DictionaryManager get(){
        //singelton
        if(dictionaryManager == null){
            dictionaryManager = new DictionaryManager();
            }
            return dictionaryManager;
    }

}
