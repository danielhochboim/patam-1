package test;
import java.util.ArrayList;


public class Board {
    private static Board board = null;
    private Tile[][] tiles = new Tile[15][15];
    private ArrayList<Word> words = new ArrayList<Word>();

    private Board(){

    }
    public static Board getBoard(){
        if(board == null){
            board = new Board();
        }
        return board;
    }
    public Tile[][] getTiles(){
        return tiles.clone();
    }
    public String toString(){
        String str = "";
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(tiles[i][j] == null){
                    str += " .";
                }
                else{
                    str += " " + tiles[i][j].getLetter();
                }
            }
            str += "\n";
        }
        return str;
    }
    public boolean boardLegal(Word word){
        //check if all of the word is in the board
        int col = word.getCol();
        int row = word.getRow();
        int len = word.getTiles().length;
        if(word.isVertical() == true){
            if(col < 0 || col > 14){return false;}
            if(row < 0 || row > 14 || row+len-1 < 0 || row+len-1 > 14){return false;}
        }
        else{
            if(row < 0 || row > 14){return false;}
            if(col < 0 || col > 14 || col+len-1 < 0 || col+len-1 > 14){return false;}
        }
        // check if the first word is in the star
        boolean empty = true;
        for (Tile[] line : tiles) {
            for (Tile t : line){
                if (t != null) {
                    empty = false;
                    break;
                }
            }
        }
        if(empty == true){
            if(word.isVertical()){
                if(!(col == 7 && row <= 7 && row+len-1 >= 7)){return false;}
            }
            else{
                if(!(row == 7 && col <= 7 && col+len-1 >= 7)){return false;}
            }
        }
        //check if the word is near or in existing tile
        boolean isNear = false;
        if(word.isVertical()){
            if(row > 0){ if(tiles[row-1][col] != null){isNear = true;}}
            if(row+len-1 < 14){ if(tiles[row+len+1-1][col] != null){isNear = true;}}
            if(col > 0){
                for(int i = 0; i < len; i++)
                {
                    if(tiles[row + i][col - 1] != null){isNear = true;}
                }
            }
            if(col < 14){
                for(int i = 0; i < len; i++){
                    if(tiles[row + i][col + 1] != null){isNear = true;}
                }
            }
            for(int i = 0; i < len; i++){
                if(tiles[row + i][col] != null){
                    isNear = true;
                    if(tiles[row + i][col] != word.getTiles()[i]){return false;}
                }
            }
        }
        else{
            if(col > 0){ if(tiles[row][col-1] != null){isNear = true;}}
            if(col+len < 14){ if(tiles[row][col+len+1-1] != null){isNear = true;}}
            if(row > 0){
                for(int i = 0; i < len; i++)
                {
                    if(tiles[row -1][col + i] != null){isNear = true;}
                }
            }
            if(row < 14){
                for(int i = 0; i < len; i++){
                    if(tiles[row + 1][col + i] != null){isNear = true;}
                }
            }
            for(int i = 0; i < len; i++){
                if(tiles[row][col + i] != null){
                    isNear = true;
                    if(tiles[row][col + i] != word.getTiles()[i]){return false;}
                }
            }

        }

        return true;
    }
    public boolean dictionaryLegal(Word word){
        return true;
    }

    private Word findNearWord(int row, int col, boolean vertical){
        int i;
        int start;
        int end;
        Tile[] wordTiles;
        if(vertical){
            // find the end of the near word
            for(i = 0; tiles[row+i][col] != null; i++){
                if(row+i+1 == 14){
                    break;
                }
            }
            end = row + i - 1;
            //find the start of the near word
            for(i = 0; tiles[row-i][col] != null; i++){
                if(row-i-1 < 0){
                    break;
                }
            }
            start = row - i + 1;
            //copying the near word to a new Word var, then returning it
            wordTiles = new Tile[end - start + 1];
            for(i = 0; start + i <= end; i++){
                wordTiles[i] = tiles[start + i][col];
            }
            return new Word(wordTiles, start, col, true);
        }
        else{
            // find the end of the near word
            for(i = 0; tiles[row][col+i] != null; i++){
                if(col+i+1 == 14){
                    break;
                }
            }
            end = col + i - 1;
            //find the start of the near word
            for(i = 0; tiles[row][col-i] != null; i++){
                if(col-i-1 < 0){
                    break;
                }
            }
            start = col - i + 1;
            //copying the near word to a new Word var, then returning it
            wordTiles = new Tile[end - start + 1];
            for(i = 0; start + i <= end; i++){
                wordTiles[i] = tiles[row][start + i];
            }
            return new Word(wordTiles, row, start, false);
        }
    }

    private ArrayList<Word> removeDuplicates(ArrayList<Word> words) 
    { 
  
        // Create a new ArrayList 
        ArrayList<Word> newList = new ArrayList<Word>(); 
  
        // Traverse through the first list 
        for (Word word : words) { 
  
            // If this element is not present in newList 
            // then add it 
            if (!newList.contains(word)) { 
  
                newList.add(word); 
            } 
        } 
  
        // return the new list 
        return newList; 
    } 
    public ArrayList<Word> getWords(Word word){
        int row = word.getRow();
        int col = word.getCol();
        int len = word.getTiles().length;
        Tile[] wordTiles = word.getTiles();
        ArrayList<Boolean> placedTest = new ArrayList<Boolean>();
        //creating a list of near words
        ArrayList<Word> nearWords = new ArrayList<Word>();
        //adding the word itself
        nearWords.add(word);

        if(word.isVertical()){
            //placing the word in the board temperarly to check for new words it creates
            //the list placedTest is a list of boolean in the length of the word
            //and it containe were I placed a letter on the board, so i can remove it at the end
            for(int i = 0; i < wordTiles.length; i++){
                if(tiles[row+i][col] == null){
                    tiles[row+i][col] = wordTiles[i];
                    placedTest.add(true);
                }
                else{
                    placedTest.add(false);
                }
            }
            //search new words above the word
            if(row > 0){
                if(tiles[row-1][col] != null){
                nearWords.add(findNearWord(row-1, col, true));
                nearWords.add(findNearWord(row-1, col, false));
                }
            }
            //search new words at the bottom of the word
            if(row+len-1 < 14){ 
                if(tiles[row+len+1-1][col] != null){
                    nearWords.add(findNearWord(row+len+1-1, col, true));
                    nearWords.add(findNearWord(row+len+1-1, col, false));

                }
            }
            //search new words to the left of the word
            if(col > 0){
                for(int i = 0; i < len; i++)
                {
                    if(tiles[row + i][col - 1] != null){nearWords.add(findNearWord(row + i, col - 1, false));}
                }
            }
            //search new words to the right of the word
            if(col < 14){
                for(int i = 0; i < len; i++){
                    if(tiles[row + i][col + 1] != null){nearWords.add(findNearWord(row + i, col + 1, false));}
                }
            }
            // for(int i = 0; i < len; i++){
            //     if(tiles[row + i][col] != null){
            //         nearWords.add(findNearWord(row + i, col, true));
            //     }
            // }

            //removing the placed word
            for(int i = 0; i < wordTiles.length; i++){
                if(placedTest.get(i) == true){
                    tiles[row+i][col] = null;
                }
            }
        }
        else{
            //placing the word in the board temperarly to check for new words it creates
            //the list placedTest is a list of boolean in the length of the word
            //and it containe were I placed a letter on the board, so i can remove it at the end
            for(int i = 0; i < wordTiles.length; i++){
                if(tiles[row][col+i] == null){
                    tiles[row][col+i] = wordTiles[i];
                    placedTest.add(true);
                }
                else{
                    placedTest.add(false);
                }
            }
            //search new words to the left of the word
            if(col > 0){ 
                if(tiles[row][col-1] != null){
                    nearWords.add(findNearWord(row, col-1, true));
                    nearWords.add(findNearWord(row, col-1, false));
                }
            }
            //search new words to the right of the word
            if(col+len < 14){ 
                if(tiles[row][col+len+1-1] != null){
                    nearWords.add(findNearWord(row, col+len+1-1, true));
                    nearWords.add(findNearWord(row, col+len+1-1, false));
                }
            }
            //search new words at the bottom of the word
            if(row > 0){
                for(int i = 0; i < len; i++)
                {
                    if(tiles[row - 1][col + i] != null){nearWords.add(findNearWord(row - 1, col + i, true));}
                }
            }
            //search new words above the word
            if(row < 14){
                for(int i = 0; i < len; i++){
                    if(tiles[row + 1][col + i] != null){nearWords.add(findNearWord(row + 1, col + i, true));}
                }
            }
            // for(int i = 0; i < len; i++){
            //     if(tiles[row][col + i] != null){
            //         isNear = true;
            //         if(tiles[row][col + i] != word.getTiles()[i]){return false;}
            //     }
            // }
            //removing the placed word
            for(int i = 0; i < wordTiles.length; i++){
                if(placedTest.get(i) == true){
                    tiles[row][col+i] = null;
                }
            }
        }
        //remove duplicates if it found the same word at the right and the left of the word
        nearWords = removeDuplicates(nearWords);
        //remove words that already exist in the board, and are not new
        nearWords.removeAll(words);
        return nearWords;
    }

    //checks if a letter is in the double letter score cell
    private boolean isDoubleLetter(int row, int col){
        int[][] doubleLetterPos = new int[][]{{0,3},{0,11},{2,6},{2,8},{3,0},{3,7},{3,14},{6,2},{6,6},{6,8},{6,12},{7,3},{7,11},{8,2},{8,6},{8,8},{11,12},{11,0},{11,7},{11,14},{12,6},{12,8},{14,3},{14,11}};
        for(int[] pos : doubleLetterPos){
            if(pos[0] == row && pos[1] == col){
                return true;
            }
        }
        return false;
    }
    //checks if a letter is in the triple letter score cell
    private boolean isTripleLetter(int row, int col){
        int[][] tripleLetterPos = new int[][]{
            {1, 5}, {1, 9},
            {5, 1}, {5, 5}, {5, 9}, {5, 13},
            {9, 1}, {9, 5}, {9, 9}, {9, 13},
            {13, 5}, {13, 9}
        };
        for(int[] pos : tripleLetterPos){
            if(pos[0] == row && pos[1] == col){
                return true;
            }
        }
        return false;
    }

    //checks if a letter is in the double word score cell
    private boolean isDoubleWord(int row, int col){
        int[][] doubleWordPos = new int[][]{
            {1, 1}, {2, 2}, {3, 3}, {4, 4}, {7, 7},
            {10, 10}, {11, 11}, {12, 12}, {13, 13}, {14, 14},
            {1, 13}, {2, 12}, {3, 11}, {4, 10},
            {10, 4}, {11, 3}, {12, 2}, {13, 1}
        };
        for(int[] pos : doubleWordPos){
            if(pos[0] == row && pos[1] == col){
                return true;
            }
        }
        return false;
    }
    
    //checks if a letter is in the triple word score cell
    private boolean isTripleWord(int row, int col){
        int[][] tripleWordPos = new int[][]{
            {0, 0}, {0, 7}, {0, 14},
            {7, 0}, {7, 14},
            {14, 0}, {14, 7}, {14, 14}
        };        for(int[] pos : tripleWordPos){
            if(pos[0] == row && pos[1] == col){
                return true;
            }
        }
        return false;
    }
    
    public int getScore(Word word){
        int score = 0;
        int row = word.getRow();
        int col = word.getCol();
        Tile[] wordTiles = word.getTiles();
        int multiply = 1;

        if(word.isVertical()){
            for(int i = 0; i < wordTiles.length; i++){
                if(isDoubleLetter(row+i,col)){
                    score += wordTiles[i].getScore() * 2;
                }
                else if(isTripleLetter(row+i, col)){
                    score += wordTiles[i].getScore() * 3;
                }
                else{
                    score += wordTiles[i].getScore();
                }
                //if the word already exist in the board or there is a letter already placed in the same location
                //then dont multiply it
                if(!words.contains(word) && tiles[row+i][col] == null){
                    if(isDoubleWord(row+i, col)){
                        multiply *= 2;
                    }
                    else if(isTripleWord(row+i, col)){
                        multiply *= 3;
                    }
                }
                
            }
        }
        else{
            for(int i = 0; i < wordTiles.length; i++){
                if(isDoubleLetter(row,col+i)){
                    score += wordTiles[i].getScore() * 2;
                }
                else if(isTripleLetter(row, col+i)){
                    score += wordTiles[i].getScore() * 3;
                }
                else{
                    score += wordTiles[i].getScore();
                }
                //if the word already exist in the board or there is a letter already placed in the same location
                //then dont multiply it
                if(!words.contains(word) && tiles[row][col+i] == null){
                    if(isDoubleWord(row, col+i)){
                        multiply *= 2;
                    }
                    else if(isTripleWord(row, col+i)){
                        multiply *= 3;
                    }
                }
            }
        }
        score *= multiply;
        return score;
    }

    //if I get a word like X_XX then the func replaces the _ with what is already in the board
    //and returns it
    private Word removeNull(Word word){
        int row = word.getRow();
        int col = word.getCol();
        Tile[] wordTiles = word.getTiles();
        Tile[] copyTiles = new Tile[wordTiles.length];
        if(word.isVertical()){
            //going through the word and if there is null, it replaces it with the letter on the board
            for(int i = 0; i < wordTiles.length; i++){
                if(wordTiles[i] == null){
                    copyTiles[i] = tiles[row+i][col];
                }
                else{
                    copyTiles[i] = wordTiles[i];
                }
            }
        }
        else{
            //going through the word and if there is null, it replaces it with the letter on the board
            for(int i = 0; i < wordTiles.length; i++){
                if(wordTiles[i] == null){
                    copyTiles[i] = tiles[row][col+i];
                }
                else{
                    copyTiles[i] = wordTiles[i];
                }
            }
        }
        return new Word(copyTiles, row, col, word.isVertical());
    }
    //placing the word in the board assuming it passed all the tests
    private void placeWord(Word word){
        int row = word.getRow();
        int col = word.getCol();
        Tile[] wordTiles = word.getTiles();
        
        if(word.isVertical()){
            for(int i = 0; i < wordTiles.length; i++){
                if(wordTiles[i] != null){
                    tiles[row+i][col] = wordTiles[i];
                }
            }
        }
        else{
            for(int i = 0; i < wordTiles.length; i++){
                if(wordTiles[i] != null){
                    tiles[row][col+i] = wordTiles[i];
                }
            }
        }
    }
    public int tryPlaceWord(Word word){
        ArrayList<Word> nearWords;
        int score = 0;
        Word copyWord = removeNull(word);
        if(!boardLegal(copyWord)){
            return 0;
        }
        // System.out.println(copyWord);
        nearWords = getWords(copyWord);
        // System.out.println(nearWords);
        for(Word nearWord : nearWords){
            if(!dictionaryLegal(nearWord)){
                return 0;
            }
        }
        //calculate score
        for(Word nearWord : nearWords){
            score += getScore(nearWord);
        }
        //place on the board
        placeWord(word);
        words.addAll(nearWords);
        
        return score;
    }

    
}
