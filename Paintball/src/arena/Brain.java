/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package arena;

import java.awt.Color;

/**
 * AI for players of the Paintball game. All strategies to be used should
 * implement this interface and must be placed in the brains package.
 * @author Thomas Weisswange
 */
public interface Brain {
    /**
     * Returns the name of the Paintball strategy ("bot"). Note that this does
     * not need to match the name of the class.
     * @return the name of this strategy
     */
    public String getName();
    
    /**
     * Returns the name of the *person* who wrote the code. This should match
     * the name of the class. For example, if the Coder is named Ada Lovelace,
     * the class should be named AdaLovelace.java.
     * @return the name of the Brain's coder.
     */
    public String getCoder();
    
    /**
     * Returns the color of the Player that uses this strategy.
     * @return the Player's color
     */
    public Color getColor();
    
    /**
     * Returns the chosen action by the given Player
     * located on the given Board.
     * @return the passed Player's chosen Action.
     * @param p the Player to act. Note that the player is passed as an argument,
     * this way multiple Player objects could utilize the same Brain. However,
     * if the Brain stores local information about its particular Player, then
     * a separate instance of the class implementing Brain should be created
     * for each Player.
     * @param b the Board to which Player p belongs. This object is needed
     * by the Brain in order to determine all information about all other Players,
     * Shots, Blockers, and basically everything else about the game.
     */
    public Action getMove(Player p, Board b);
}
