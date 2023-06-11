/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package w_brains;

import arena.Action;
import arena.Board;
import arena.Brain;
import arena.Direction;
import arena.Player;
import java.awt.Color;

/**
 * One of the "dumb" strategies provided with the Paintball program. A spinner,
 * well, spins, turning on every move.
 * @author Thomas Weisswange
 */
public class Spinner implements Brain {

    /**
     * Returns the name of the strategy.
     * @return "Spinner"
     */
    @Override
    public String getName() {
        return "Spinner";
    }

    /**
     * Returns the color of the strategy.
     * @return orange
     */
    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    /**
     * Determines the action of the Spinner, which is to turn left 45 degrees.
     * @param p the Player object containing this Brain
     * @param b the Board on which this Brain's Player is located
     * @return a new Action object indicating a turn to a direction 45 degrees
     * counterclockwise from the Spinner's current direction
     */
    @Override
    public Action getMove(Player p, Board b) {
        int newDir = Direction.roundTo8(p.getDirection() + 45);
        return new Action("turn", newDir);
    }

    /**
     * Returns the name of the coder.
     * @return "Mr. Weisswange"
     */
    @Override
    public String getCoder() {
        return "Mr. Weisswange";
    }
    
}
