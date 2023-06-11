/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package arena;

/**
 * A generic abstract class representing any piece on the Board.
 * @author tweis0306
 */
public abstract class Occupant {
    protected int row;
    protected int col;
    protected int team;
    protected Board myBoard;

    /**
     * Constructs a new Occupant, initializing the team to which the Occupant
     * belongs. Note that a value of 0 should be used for Occupants that do
     * not belong to either team (such as Blockers).
     * @param team the team to which this Occupant belongs
     */
    Occupant(int team) {
        this.team = team;
    }

    /**
     * Getter function for this Occupant's row
     * @return the row component of this Occupant's location
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter function for this Occupant's column
     * @return the column component of this Occupant's location
     */
    public int getCol() {
        return col;
    }

    /**
     * Getter function for this Occupant's team
     * @return the team to whom this Occupant belongs
     */
    public int getTeam() {
        return team;
    }

    /**
     * Getter function for this Occupant's board.
     * @return the Board in which this Occupant is located
     */
    public Board getMyBoard() {
        return myBoard;
    }
    
    
    
    /**
     * Attempts to add this Occupant to a Board at a specified
     * location. The space to which the Occupant is to be added
     * must be empty.
     * @param b the Board to which the Occupant is to be added
     * @param row the intended row location
     * @param col the intended column location
     * @return true on successful adding, false on failure
     */
    boolean addSelfToBoard(Board b, int row, int col) {
        if (b == null || !b.isEmpty(row, col)) return false;
        this.row = row;
        this.col = col;
        this.myBoard = b;
        b.add(this, row, col);
        return true;
    }
    
    /**
     * Attempts to remove itself from the Board to which it
     * currently belongs.
     * @return true on success, false on failure
     */
    boolean removeSelfFromBoard() {
        if (myBoard == null) return false;
        myBoard.remove(row, col);
        myBoard = null;
        return true;
    }
    
    /**
     * Attempts to move itself to location (row, col) in its
     * current board. The space to which the Occupant wishes
     * to move must be empty.
     * @param row the intended row location
     * @param col the intended column location
     * @return true on success, false on failure
     */
    boolean moveTo(int row, int col) {
        if (myBoard == null || !myBoard.isEmpty(row, col))
            return false;
        myBoard.move(this.row, this.col, row, col);
        this.row = row;
        this.col = col;
        return true;
    }
    
    /**
     * Provides a String representation for this Occupant. Prints "Occupant"
     * plus the Occupant's team and location.
     * @return a String representation for this Occupant
     */
    public String toString() {
        return "Occupant: team " + team + " located at "
                + row + ", " + col;
    }
}
