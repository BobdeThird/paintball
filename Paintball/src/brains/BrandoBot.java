package brains;

import arena.Action;
import arena.Board;
import arena.*;
import arena.Direction;
import arena.Player;
import java.awt.Color;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author bkim0902
 */
public class BrandoBot implements Brain {
    int clock = 0;
    int numHitsLast = 0;
    int numHitsCurrent = 0;
    int streak = 0;
    int filler1 = 0, filler2 = 0;

    @Override
    public String getName() {
        return "BrandoBot";
    }

    @Override
    public String getCoder() {
        return "Brandon Kim";
    }

    @Override
    public Color getColor() {
        Color purple = new Color (102,0,153);
        return purple;
    }

    @Override
   public Action getMove(Player p, Board b) {
        int myTeam = p.getTeam();
        Base myBase = b.getBase(myTeam);
        
        
        int[] dest = {15, (myTeam == 1) ? 0 : 49};
        
        int distToSpot = Direction.moveDistance(p.getRow(), p.getCol(),
                                   dest[0], dest[1]);
        
        
        update(myBase);
        if (clock > 75 || (streak > 5 && distToSpot < 6)) {
           Action moveRandom = new Action("M", 45* ((int) (Math.random() * 8)));
           Action turnRandom = new Action("T", 45* ((int) (Math.random() * 8)));
           Action shoot = new Action("S");
           
           double number = Math.random();
           
           if (number > .67)
              return moveRandom;
           else if (number > .33) 
              return turnRandom;
           else 
            return shoot; 
        } 
        
       
        if (distToSpot != 0) {
            clock++;
            int dirToSpot = Direction.getDirectionTowards(p.getRow(), p.getCol(),
                                   dest[0], dest[1]);
            int[] nextSpace = Direction.getLocInDirection(p.getRow(),
                    p.getCol(), dirToSpot);
            if (b.isEmpty(nextSpace[0], nextSpace[1])) {
                return new Action("M", dirToSpot);
            } 
            else return new Action("M", 45* ((int) (Math.random() * 8)));
        }
        else {
           clock = 0;
           return new Action("S");
        }        
    }
    
    public void update(Base b) {
      numHitsLast = filler1;
      filler1 = filler2;
      filler2 = numHitsCurrent; 
      numHitsCurrent = b.getNumHits();
      if (numHitsLast < numHitsCurrent) streak++;
      else streak = 0;
    }
}