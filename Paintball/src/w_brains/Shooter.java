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

/**
 * One of the "dumb" strategies provided with the Paintball program. The Shooter,
 * well, shoots. That's about it.
 * @author Thomas Weisswange
 */
public class Shooter implements Brain {

    /**
     * Returns the name of the strategy.
     * @return "Shooter"
     */
    @Override
    public String getName() {
        return "Shooter";
    }

    /**
     * Returns the color of the strategy.
     * @return green
     */
    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    /**
     * Decides the action of the Shooter
     * @param p the Player object containing this Brain
     * @param b the Board on which this Brain's Player is located
     * @return a new Action object indicating that the Shooter wishes to shoot.
     */
    @Override
    public Action getMove(Player p, Board b) {
        return new Action("shoot");
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
