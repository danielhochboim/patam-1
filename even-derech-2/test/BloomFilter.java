package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
    private final int size;
    private final BitSet bits;
    private final String[] algs;

    public BloomFilter(int size, String... algs) {
        this.size = size;
        this.algs = algs;
        this.bits = new BitSet(size);
    }
    
    public void add(String word){
        MessageDigest md;
        BigInteger bigInt;
        // iterate over the hash algorithms and placing the result in the bit array
        for(String alg : algs){
            try{
                md = MessageDigest.getInstance(alg);
            }
            catch (NoSuchAlgorithmException e){
                return;
            }
            byte[] bts = md.digest(word.getBytes());
            bigInt = new BigInteger(bts);
            bits.set(Math.abs(bigInt.intValue()) % size);
        }
    }
    
    public boolean contains(String word){
        MessageDigest md;
        BigInteger bigInt;
        // checking if the word is in the bloom filter
        for(String alg : algs){
            try{
                md = MessageDigest.getInstance(alg);
            }
            catch (NoSuchAlgorithmException e){
                return false;
            }
            byte[] bts = md.digest(word.getBytes());
            bigInt = new BigInteger(bts);
            if(!bits.get(Math.abs(bigInt.intValue()) % size)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < bits.length(); i++){
            str += bits.get(i) ? "1" : "0";
        }
        return str;
    }   
}
