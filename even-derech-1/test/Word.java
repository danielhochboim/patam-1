package test;

import java.util.Arrays;
import java.util.List;

public class Word {
    private Tile[] tiles;
    private int row;
    private int col;
    private boolean vertical;
    
    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = tiles;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }
    public Tile[] getTiles() {
        return tiles;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public boolean isVertical() {
        return vertical;
    }
    
    @Override
    public String toString() {
        return Arrays.toString(tiles);
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tiles == null) ? 0 : tiles.hashCode());
        result = prime * result + row;
        result = prime * result + col;
        result = prime * result + (vertical ? 1231 : 1237);
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
        Word other = (Word) obj;
        if (tiles == null) {
            if (other.tiles != null)
                return false;
        } 
        // else if (!tiles.equals(other.tiles))
        //     return false;
        else if(!Arrays.equals(tiles, other.tiles))
            return false;
        if (row != other.row)
            return false;
        if (col != other.col)
            return false;
        if (vertical != other.vertical)
            return false;
        return true;
    }

}
