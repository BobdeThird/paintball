/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package arena;

/**
 * A Base is a subclass of Occupant which represents a team's base. Bases
 * cannot move and score 3 points when hit by a member of the opposite team,
 * but -3 points when hit be a member of its own team.
 * @author Thomas Weisswange
 */
public class Base extends Occupant {
    private int numHits;

    /**
     * Constructor function. Initializes the team value, representing which
     * team the Base belongs to.
     * @param team the team to which this Base belongs
     */
    Base(int team) {
        super(team);
    }

    /**
     * Getter function for the number of hits.
     * @return the number of times this Base has been hit.
     */
    public int getNumHits() {
        return numHits;
    }
    
    /**
     * This method is called when the base it hit. It both updates the team scores
     * as well as sending a message to the Player that shot it, informing that
     * Player that it hit a Base (either its own or the enemy's).
     * @param shot the Shot object that hit this Base.
     */
    void hit(Shot shot) {
        numHits++;
        if(team != shot.getOwner().getTeam()) {
            shot.getOwner().hitEnemyBase();
            myBoard.scorePoints(3 - team, 3);
        } else {
            shot.getOwner().hitOwnBase();
            myBoard.losePoints(team, 3);
        }
    }

    /**
     * Since a Base cannot move, thie method simply returns false.
     * @param row the destination row
     * @param col the destination column
     * @return false under all circumstances
     */
    @Override
    boolean moveTo(int row, int col) {
        return false;
    }
    
    /**
     * Prints "Base", team and location.
     * @return a String representation of this Base object.
     */
    @Override
    public String toString() {
        return "Base: team " + team + " located at "
                + row + ", " + col;
    }
}
