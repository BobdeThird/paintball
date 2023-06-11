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
 * One of the "dumb" strategies provided with the Paintball program. A Sprinkler,
 * as its name implies, turns and shoots, much like a rotating lawn sprinkler.
 * @author Thomas Weisswange
 */
public class Sprinkler implements Brain {
    
    private int turnCount;

    /**
     * Returns the name of the strategy.
     * @return "Sprinkler"
     */
    @Override
    public String getName() {
        return "Sprinkler";
    }

    /**
     * Returns the color of the strategy.
     * @return cyan
     */
    @Override
    public Color getColor() {
        return Color.CYAN;
    }

    /**
     * Determines the action of the Sprinkler. Since the Sprinkler cannot fire
     * for the two turns immediately following a shot, the Sprinkler simply
     * cycles through shoot, turn 45 degrees counterclockwise, and pass, which
     * it repeats indefinitely.
     * @param p the Player object containing this Brain
     * @param b the Board on which this Brain's Player is located
     * @return a new Action object indicating shoot, turn, or pass, depending
     * on the point in the cycle
     */
    @Override
    public Action getMove(Player p, Board b) {
        turnCount++;
        if (turnCount % 3 == 1)
            return new Action("shoot");
        if (turnCount % 3 == 2) {
            int newDir = Direction.roundTo8(p.getDirection() + 45);
            return new Action("turn", newDir);
        }
        return new Action("pass");
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
