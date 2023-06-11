/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package arena;

/**
 * A Blocker is a simple Occupant whose purpose is to block movement and shots.
 * Blockers appear as gray blocks and do not belong to either team.
 * @author Thomas Weisswange
 */
public class Blocker extends Occupant {
    /**
     * Constructs a new Blocker, which belongs to team 0 (no team).
     */
    Blocker() {
        super(0);
    }
    
    /**
     * Blockers do not move, the method simply returns false.
     * @param row the destination row
     * @param col the destination column
     * @return false under all circumstances
     */
    @Override
    boolean moveTo(int row, int col) {
        return false;
    }
    
    /**
     * Returns "Blocker" plus this Blocker's location
     * @return a String representation of this Blocker object.
     */
    @Override
    public String toString() {
        return "Blocker: located at " + row + ", " + col;
    }
}
