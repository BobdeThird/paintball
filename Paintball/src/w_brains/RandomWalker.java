/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package w_brains;

import arena.Action;
import arena.Board;
import arena.Brain;
import arena.Player;
import java.awt.Color;
import java.util.Random;

/**
 * One of the "dumb" strategies provided with the Paintball program. A RandomWalker
 * attempts, on every turn, to move in a random direction.
 * @author Thomas Weisswange
 */
public class RandomWalker implements Brain {

    private Random randGen;
    
    /**
     * Constructs a new RandomWalker object (just creates a new Random object
     * for this instance).
     */
    public RandomWalker() {
        randGen = new Random();
    }

    /**
     * Returns the name of the strategy.
     * @return "Random walker"
     */
    @Override
    public String getName() {
        return "Random walker";
    }

    /**
     * Returns the color of the strategy.
     * @return magenta
     */
    @Override
    public Color getColor() {
        return Color.MAGENTA;
    }

    /**
     * Decides the action of the shooter, which is to move randomly.
     * @param p the Player object containing this Brain
     * @param b the Board on which this Brain's Player is located
     * @return a new Action object indicating a move in a random direction
     */
    @Override
    public Action getMove(Player p, Board b) {
        return new Action("move", randGen.nextInt(8) * 45);
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
