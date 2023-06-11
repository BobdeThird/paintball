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
 * One of the "dumb" strategies provided with the Paintball program. RandoBot
 * selects one of the four basic moves at random. If the move requires a direction,
 * that is selected at random as well.<p>
 * The method used for Action selection is a weighted-random approach. Each
 * individual choice has a weight, which is a nonnegative integer. The probability
 * that a given Action is chosen is given by (weight of that Action)/(total weight
 * of all actions).<p>
 * To give an analogy, say that the weightings are 3 for shoot, 5 for move,
 * and 3 for turn. Imagine that there are 3 slips of paper with "shoot" written
 * on them, 5 with "move", and 3 with "turn". One of these slips is chosen at
 * random and the action is taken. Note that if the RandoBot cannot shoot at this
 * time, the slips marked "shoot" are not used.
 * @author Thomas Weisswange
 */
public class RandoBot implements Brain {
    //behavior weightings
    private final int PASS = 0;
    private final int SHOOT = 3;
    private final int MOVE = 5;
    private final int TURN = 3;
    
    private Random randGen;

    /**
     * Constructs a new RandoBot (just creates a new Random object for this instance.)
     */
    public RandoBot() {
        randGen = new Random();
    }
    
    /**
     * Returns the name of the strategy.
     * @return "RandoBot"
     */
    @Override
    public String getName() {
        return "RandoBot";
    }

    /**
     * Returns the color of the strategy.
     * @return yellow
     */
    @Override
    public Color getColor() {
        return Color.YELLOW;
    }

    /**
     * Decides the action of the RandoBot
     * @param p the Player object containing this Brain
     * @param b the Board on which this Brain's Player is located
     * @return a randomly-chosen Action, according to predefined weightings,
     * as explained above
     */
    @Override
    public Action getMove(Player p, Board b) {
        String action;
        int choice;
        if (!p.canShoot()) {
            choice = randGen.nextInt(PASS + MOVE + TURN);
        } else {
            choice = randGen.nextInt(PASS + MOVE + TURN + SHOOT);
        }
        if (choice < PASS) {
            return new Action("pass");
        } else if (choice < PASS + MOVE) {
            return new Action("move", randGen.nextInt(8) * 45);
        } else if (choice < PASS + MOVE + TURN) {
            return new Action("turn", randGen.nextInt(8) * 45);
        } else {
            return new Action("shoot");
        }
            
    }

    /**
     * Returns the name of the coder
     * @return "Mr. Weisswange"
     */
    @Override
    public String getCoder() {
        return "Mr. Weisswange";
    }
}
