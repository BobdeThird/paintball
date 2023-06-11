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
 * The smartest of the "dumb" strategies provided with the software. This rudimentary
 * strategy attempts to shoot the opponent's base as often as possible. Since it
 * makes absolutely no attempt to avoid being shot, it tends to get shot easily.
 * Its basic strategy looks like this:
 * <ol>
 * <li>If next to opponent's base and pointing at it, shoot.</li>
 * <li>2. If next to opponent's base and not pointing at it, turn towards it.</li>
 * <li>3. If not next to opponent's base, move towards it, but if the space between
 * the DumbBaseCharger and the opponent's base is occupied, move in a random
 * direction.</li>
 * </ol>
 * 
 * @author Thomas Weisswange
 */
public class DumbBaseCharger implements Brain {
    
    int baseRow = 16;
    int baseCol = -1;

    /**
     * Returns the name of the strategy.
     * @return "DumbBaseCharger"
     */
    @Override
    public String getName() {
        return "DumbBaseCharger";
    }

    /**
     * Returns the name of the coder.
     * @return "Mr. Weisswange"
     */
    @Override
    public String getCoder() {
        return "Mr. Weisswange";
    }

    /**
     * Returns the color of the strategy
     * @return pink
     */
    @Override
    public Color getColor() {
        return Color.PINK;
    }

    /**
     * Decides the action of the DumbBaseCharger, according to the strategy
     * presented above.
     * @param p the Player object containing this Brain
     * @param b the Board on which this Brain's Player is located
     * @return a new Action object indicating the DumbBaseCharger's choice
     * to move or shoot, as appropriate.
     */
    @Override
    public Action getMove(Player p, Board b) {
        //determine location of opposing base, only done once
        if (baseCol == -1) {
            baseCol = b.getBase(3 - p.getTeam()).getCol();
        }
        //determine distance and direction to opposing base
        int distToBase = Direction.moveDistance(p.getRow(), p.getCol(),
                                   baseRow, baseCol);
        int dirToBase = Direction.getDirectionTowards(p.getRow(), p.getCol(),
                                   baseRow, baseCol);
        //if next to base, aim and fire!
        if (distToBase == 1) {
            if (dirToBase != p.getDirection())
                return new Action("T", dirToBase);
            else
                return new Action("S");
        } else {
            //move towards opponent's base. If closest space is occupied,
            //move in a random direction
            int[] nextSpace = Direction.getLocInDirection(p.getRow(),
                    p.getCol(), dirToBase);
            if (b.isEmpty(nextSpace[0], nextSpace[1]))
                return new Action("M", dirToBase);
            else {
                int[] pref;
                
                if(baseCol == 0) {
                    pref = new int[]{270, 315, 225, 135, 0, 90, 45, 180};
                } else {
                    pref = new int[]{90, 45, 135, 225, 0, 270, 180, 315};
                }
                
                for(int i : pref) {
                    int[] nextSpace2 = Direction.getLocInDirection(p.getRow(), p.getCol(), i);
                    if (b.isEmpty(nextSpace[0], nextSpace[1]))
                        return new Action("M", dirToBase);
                }
                
                
                return new Action("M", 
                        45* ((int) (Math.random() * 8)) );
            }
        }
    }
    
}
