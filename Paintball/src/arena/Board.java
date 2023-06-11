/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package arena;

import java.util.ArrayList;
import java.util.List;

/**
 * The main game board class. A Board object maintains a 2-dimensional array of
 * references to every Occupant in the game. In addition, the Board keeps track
 * of the scores for both teams. It also contains a set of utility methods that
 * Brains can use to help determine their strategies. Note that a number of
 * methods here are package-private, which means that they cannot be accessed
 * by Brains outside of this package.
 * @author Thomas Weisswange
 */
public class Board {
    private int[] score = {0, 0, 0};
    private Occupant[][] grid;

    /**
     * <u>Package-private.</u> Constructor function. Specifies the dimensions of the game board.
     * @param numRows the number of rows for the new Board
     * @param numCols the number of columns for the new Board
     */
    Board(int numRows, int numCols) {
        grid = new Occupant[numRows][numCols];
    }
    
    /**
     * Determines the number of rows of the game board.
     * @return the number of rows in this Board
     */
    public int numRows() {
        return grid.length;
    }
    
    /**
     * Determines the number of columns of the game board.
     * @return the number of columns in this Board
     */
    public int numCols() {
        return grid[0].length;
    }
    
    /**
     * Determines whether or not a particular location (row and column) represents
     * a legal space within the confines of the board. Note that this method
     * only checks whether or not a space is on the board--not whether or not
     * the space is empty or anything else.
     * @param row the row of the space to be checked
     * @param col the column of the space to be checked
     * @return true if the (row, col) represents a valid space in this Board,
     * false otherwise
     */
    public boolean isValid(int row, int col) {
        return row >= 0 && row < numRows()
                && col >= 0 && col < numCols();
    }
    
    /**
     * Determines whether or not a particular location (row and column) represents
     * a legal <b>empty</b> space within the confines of the board.
     * @param row the row of the space to be checked
     * @param col the column of the space to be checked
     * @return true if the (row, col) represents a space which is both valid
     * (within the confines of the board) and empty, false otherwise
     */    
    public boolean isEmpty(int row, int col) {
        return isValid(row, col) && grid[row][col] == null;
    }
    
    /**
     * <u>Package-private.</u> Adds an Occupant to the board at the location
     * specified, if said space is empty. If the space is not empty, the method
     * leaves the Board unchanged and returns false.
     * @param occ the Occupant to be added to the board
     * @param row the row of the space to which occ is to be added
     * @param col the column of the space to which occ is to be added
     * @return true if the Occupant is successfully added to the board, and
     * false otherwise
     */
    boolean add(Occupant occ, int row, int col) {
        if (!isEmpty(row, col)) return false;
        grid[row][col] = occ;
        return true;
    }
    
    /**
     * Returns the occupant at location (row, col) on the Board
     * @param row the row of the requested location
     * @param col the column of the requested location
     * @return the Occupant at location (row, col), or null if the space is empty.
     */
    public Occupant get(int row, int col) {
        if (!isValid(row, col))
            throw new ArrayIndexOutOfBoundsException
                    ("Board row = " + row + ", col = " + col);
        return grid[row][col];
    }
    
    /**
     * <u>Package-private.</u> Moves an Occupant from one space on the Board
     * to another. Note that, unlike the add method, this method will remove
     * and return whichever Occupant, if any, is at the destination space.
     * If the source space is empty, the "null" will be moved from the soarce
     * space to the destination space, leaving the destination space empty.
     * @param fromRow the row of the source space
     * @param fromCol the column of the source space
     * @param toRow the row of the destination space
     * @param toCol the column of the destination space
     * @return the Occupant formerly in the destination apace, or null if
     * the destination space was formerly empty.
     */
    Occupant move(int fromRow, int fromCol,
            int toRow, int toCol) {
        if (!isValid(fromRow, fromCol))
            throw new IllegalArgumentException(
            "Illegal grid location: " + fromRow + ", " + fromCol);
        if (!isValid(toRow, toCol))
            throw new IllegalArgumentException(
            "Illegal grid location: " + toRow + ", " + toCol);
        Occupant temp = grid[toRow][toCol];
        grid[toRow][toCol] = grid[fromRow][fromCol];
        grid[fromRow][fromCol] = null;
        return temp;
    }
    
    /**
     * <u>Package-private.</u> Removes and returns the occupant at the specified location on the Board. 
     * If the location is currently empty, the method returns null.
     * @param row the row of the specified location
     * @param col the column of the specified location
     * @return the Occupant at (row, col), or null if empty
     */
    Occupant remove(int row, int col) {
        if (!isValid(row, col))
            throw new IllegalArgumentException(
            "Illegal grid location: " + row + ", " + col);
        Occupant occ = grid[row][col];
        grid[row][col] = null;
        return occ;
    }
    
    /**
     * <u>Package-private.</u> Awards a given number of points for a given team.
     * @param team the team to which points are to be awarded
     * @param points the number of points to be awarded
     */
    void scorePoints(int team, int points) {
        score[team] += points;
    }
    
    /**
     * <u>Package-private.</u> Deducts a given number of points from a given team's score
     * @param team the team to which points are to be deducted
     * @param points the number of points to be deducted
     */
    void losePoints(int team, int points) {
        score[team] -= points;
    }
    
    /**
     * <u>Package-private.</u> Resets both team's scores to zero.
     */
    void resetScores() {
        score[1] = 0;
        score[2] = 0;
    }
    
    /**
     * Getter function for a particular team's score.
     * @param team the team in question
     * @return the score for the specified team
     */
    int getScore(int team) {
        return score[team];
    }
    
    /**
     * This utility method is used to obtain a List of all Players on the board.
     * @return an ArrayList of all Players on this Board
     */
    public List<Player> getAllPlayers() {
        List<Player> result = new ArrayList<Player>(40);
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] instanceof Player)
                    result.add((Player) grid[r][c]);
            }
        }
        return result;
    }
    
    /**
     * This utility method is used to obtain a List of all Players on the board
     * from a specific team.
     * @param team the team in question
     * @return an ArrayList of all Players from the specified team on this Board
     */
    public List<Player> getAllPlayers(int team) {
        List<Player> result = new ArrayList<Player>(20);
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] instanceof Player &&
                        grid[r][c].getTeam() == team)
                    result.add((Player) grid[r][c]);
            }
        }
        return result;
    }
    
    /**
     * This utility method is used to obtain a List of all Shots on the board.
     * @return an ArrayList of all Shots on this Board
     */
    public List<Shot> getAllShots() {
        List<Shot> result = new ArrayList<Shot>(80);
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] instanceof Shot)
                    result.add((Shot) grid[r][c]);
            }
        }
        return result;
    }
    
    /**
     * This utility method is used to obtain a List of all Shots on the board
     * from a specific team.
     * @param team the team in question
     * @return an ArrayList of all Shots from the specified team on this Board
     */
    public List<Shot> getAllShots(int team) {
        List<Shot> result = new ArrayList<Shot>(40);
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] instanceof Shot &&
                        grid[r][c].getTeam() == team)
                    result.add((Shot) grid[r][c]);
            }
        }
        return result;
    }
    
    /**
     * This utility method is used to find the Base for a specific team
     * @param team the team in question
     * @return the Base belonging to the specified team
     */
    public Base getBase(int team) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] instanceof Base &&
                        grid[r][c].getTeam() == team)
                    return (Base) grid[r][c];
            }
        }
        return null;
    }
}
