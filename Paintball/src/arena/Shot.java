/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package arena;

/**
 * This class is a subclass of Occupant that represents shots on the board.
 * In addition to the usual Occupant properties, Shots also need to keep track
 * of their direction of movement as well as which Player originally fired
 * the shot (for color and scoring purposes). In addition, since a Shot cannot
 * move in the first turn after it is created, there is a property which
 * keeps track of whether or not it is the Shot's first turn.
 * @author Thomas Weisswange
 */
public class Shot extends Occupant {
    private int direction;
    private Player owner;
    private boolean firstMove;
    
    /**
     * Constructor method. Initializes the team affiliation, the direction of
     * travel, and the owner of this Shot.
     * @param team the team to which the Shot's firer belongs
     * @param direction the direction of travel for the Shot (which never changes)
     * @param owner the Player who fired the shot
     */
    Shot(int team, int direction, Player owner) {
        super(team);
        this.direction = direction;
        this.owner = owner;
        firstMove = true;
    }

    /**
     * Getter method for the direction of the Shot's travel.
     * @return the direction in which the Shot is moving
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Getter method for the owner of this Shot.
     * @return the Player who originally fired the Shot.
     */
    public Player getOwner() {
        return owner;
    }
    
    /**
     * Handles movement for the Shot. This handles all of the details of the
     * Shot's movement, including going out-of-bounds as well as collision with
     * other Occupants.
     */
    void move() {
        //make sure shot is on the board
        if (myBoard == null)
            return;
        //determine intended destination location
        int destRow, destCol;
        if (firstMove) {
            firstMove = false;
            return;
        } else {
            int[] dest = Direction.getLocInDirection(row, col, 
                    direction);
            destRow = dest[0];
            destCol = dest[1];
        }
        
        //check for move-off-of-board
        if (!myBoard.isValid(destRow, destCol)) {
            removeSelfFromBoard();
            return;
        }
        
        //if space in front is empty, just move
        if (myBoard.isEmpty(destRow, destCol)) {
            moveTo(destRow,destCol);
            return;
        }
        
        //resolve collision with object in front
        Occupant target = myBoard.get(destRow, destCol);
        if (target instanceof Blocker) {
            removeSelfFromBoard();
            return;
        }
        if (target instanceof Shot) {
            removeSelfFromBoard();
            target.removeSelfFromBoard();
            return;
        }
        if (target instanceof Base) {
            removeSelfFromBoard();
            ((Base)target).hit(this);
            return;
        }
        if (target instanceof Player) {
            removeSelfFromBoard();
            ((Player)target).hit(this);
        }
    }
    
    /**
     * Provides a String representation of the Shot. Returns "Shot: " followed
     * by a listing of the team, location, and direction of the Shot.
     * @return an appropriate String representation of this Shot
     */
    @Override
    public String toString() {
        return "Shot: team " + team + " located at "
                + row + ", " + col + ", direction " +
                direction;
    }
}
