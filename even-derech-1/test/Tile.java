package test;
import java.util.Random;

public class Tile {
    public final char letter;
    private final int score;

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    public char getLetter() {
        return letter;
    }

    public int getScore() {
        return score;
    }
    
    @Override
    public String toString() {
        return Character.toString(letter);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + letter;
        result = prime * result + score;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tile other = (Tile) obj;
        if (letter != other.letter)
            return false;
        if (score != other.score)
            return false;
        return true;
    }

    public static class Bag { 
        private static Bag bag = null;
        private final int[] quantities = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
        private final int[] legalQuantities = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};

        private int sumOfTiles;
        private final Tile[] tiles;

        private Bag() {
            tiles = new Tile[26];
            initializeTiles();
            initializeSumOfTiles();
        }
        public static Bag getBag(){
            if(bag == null){
                bag = new Bag();
            }
            return bag;
        }

        private void initializeSumOfTiles(){
            sumOfTiles = 0;
            for(int i = 0; i < quantities.length; i++){
                sumOfTiles += quantities[i];
            }
        }

        private void initializeTiles() {
            String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            int[] scores = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};

            for (int i = 0; i < 26; i++) {
                tiles[i] = new Tile(letters.charAt(i), scores[i]);
            }
        }

        public Tile getRand()
        {
            if(sumOfTiles == 0){
                return null;
            }
            int currentSum = 0;
            Random random = new Random();
            int randomIndex = random.nextInt(sumOfTiles);
            for(int i = 0; i < quantities.length; i++){
                currentSum += quantities[i];
                if(randomIndex < currentSum){
                    quantities[i]--;
                    sumOfTiles--;
                    return tiles[i];
                }
            }
            return null;
        }
        public Tile getTile(char letter){
            int index = letter - 'A';
            if(index < 0 || index > 25){
                return null;
            }
            if(quantities[index] == 0){
                return null;
            }
            quantities[index]--;
            sumOfTiles--;
            return tiles[index];
        }

        public void put(Tile tile){
            int index = tile.letter - 'A';
            if(quantities[index] == legalQuantities[index]){
                return;
            }
            quantities[index]++;
            sumOfTiles++;
        }
        public int size(){
            return sumOfTiles;
        }
        public int[] getQuantities() {
            return quantities.clone();
        }
    }
	
}