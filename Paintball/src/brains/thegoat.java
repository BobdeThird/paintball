/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brains;

import arena.Action;
import arena.Board;
import arena.Brain;
import arena.Direction;
import arena.Player;
import arena.Shot;
import java.awt.Color;
import java.util.List;

/**
 * @author Max Lee
 */
public class thegoat implements Brain {

    int baseRow = 16;
    int baseCol = -1;
    int moveTowardsBase = 0;
    int baseDirection = 0;
    int angledOffset = 45;
    String lastMove = "";
    int team = 0;

    /**
     * Returns the name of the strategy.
     * 
     * @return "thegoat"
     */
    @Override
    public String getName() {
        return "thegoat";
    }

    /**
     * Returns the name of the coder.
     * 
     * @return "Max"
     */
    @Override
    public String getCoder() {
        return "Max Lee!!";
    }

    /**
     * Returns the color of the strategy
     * 
     * @return black
     */
    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    @Override
    public Action getMove(Player p, Board b) {
        team = p.getTeam();
        moveTowardsBase = -1; // base is on left
        if (p.getTeam() == 1) {
            moveTowardsBase = 1; // base is on right
        }
        baseDirection = 90;
        if (baseCol == -1) {
            baseCol = b.getBase(3 - p.getTeam()).getCol();

        }
        if (p.getTeam() == 2) {
            baseDirection = 270;
        }

        // depending on the team, diagonals will be different
        angledOffset = 45;
        if (p.getTeam() == 1)
            angledOffset = -45;

        // determine location of opposing base, only done once

        // first priority: see if about to die (square about to get hit)
        // if about to die, find new place to move (check these places)

        // second priority: get to the other base

        // third priority: once at base, start shooting (can be people or base)

        // prioritize going forward

        if (playerNearby(p, p.getRow(), p.getCol(), b)) {
            return dodge(p, b);
        }

        int distToBase = Direction.moveDistance(p.getRow(), p.getCol(), baseRow, baseCol);
        int dirToBase = Direction.getDirectionTowards(p.getRow(), p.getCol(), baseRow, baseCol);
        if (distToBase >= 4) {
            int[] towardsBase = Direction.getLocInDirection(p.getRow(), p.getCol(), dirToBase);

            // if (playerNearby(p, towardsBase[0], towardsBase[1], b) != null) {
            // // shoot nearby player
            // Player closestEnemy = playerNearby(p, towardsBase[0], towardsBase[1], b);
            // int dirToEnemy = Direction.getDirectionTowards(p.getRow(), p.getCol(),
            // closestEnemy.getRow(),
            // closestEnemy.getCol());
            // if (dirToEnemy != p.getDirection()) {
            // return new Action("T", dirToEnemy);
            // }

            // return new Action("S");

            // }

            // shoot if can???? makes u walk slower tho shrug
            // if (isSafeSpace(p.getRow(), p.getCol(), b, "idcifempty") && p.canShoot())
            // return new Action("S");
            if (!isSafeSpace(p.getRow(), p.getCol(), b, "idcifempty")) {
                return dodge(p, b);
            }

            if (isSafeSpace(towardsBase[0], towardsBase[1], b)) {
                return new Action("M", dirToBase);

            }

            if (isSafeSpace(p.getRow(), p.getCol() + moveTowardsBase, b)) {
                return new Action("M", baseDirection);
            } else if (isSafeSpace(p.getRow() + 1, p.getCol() + moveTowardsBase, b)) {
                return new Action("M", baseDirection - angledOffset);
            }
            if (isSafeSpace(p.getRow() - 1, p.getCol() + moveTowardsBase, b)) {
                return new Action("M", baseDirection + angledOffset);
            }

            return dodge(p, b);

        }
        // we found the base
        if (!DirectLOS(baseRow, baseCol, p.getDirection(), p.getRow(), p.getCol(), distToBase, b)) {
            if (isSafeSpace(p.getRow(), p.getCol(), b, "idcifempty") && !lastMove.equals("T")) {
                lastMove = "T";
                return new Action("T", dirToBase);
            }
            int randDir = 45 * ((int) (Math.random() * 8));
            int[] movingLoc = Direction.getLocInDirection(p.getRow(), p.getCol(), randDir);
            int count = 0;
            while (!isSafeSpace(movingLoc[0], movingLoc[1], b)) {
                count++;
                randDir = 45 * ((int) (Math.random() * 8));
                movingLoc = Direction.getLocInDirection(p.getRow(), p.getCol(), randDir);
                if (count == 10)
                    break; // incase it gets stuck in a corner, dont crash the game
            }
            lastMove = "M";
            return new Action("M", randDir);

        }
        // properly shooting at base!
        int[] spaceInfront = Direction.getLocInDirection(p.getRow(), p.getCol(), p.getDirection());

        if (!isSafeSpace(p.getRow(), p.getCol(), b, "no")
                || b.get(spaceInfront[0], spaceInfront[1]) instanceof Player) {
            return dodge(p, b);

        }

        if (dirToBase != p.getDirection())
            return new Action("T", dirToBase);

        if (p.canShoot())
            return new Action("S");

        return new Action("P");

    }

    public boolean isSafeSpace(int i, int j, Board b, String idcifempty) {
        List<Shot> allShots = b.getAllShots();
        List<Player> allEnemies = b.getAllPlayers(3 - team);
        for (Player e : allEnemies) {
            if (DirectLOS(i, j, e.getDirection(), e.getRow(), e.getCol(), 3, "idcifrightnexttome", b)) {
                return false;
            }
        }
        if (i > 32 || i < 0 || j > 49 || j < 0)

        {
            return false;
        }
        for (Shot shot : allShots) {
            int dir = shot.getDirection();
            int[] nextSpace = Direction.getLocInDirection(shot.getRow(), shot.getCol(), dir);
            if (nextSpace[0] == i && nextSpace[1] == j) {
                return false;
            }
            int[] nextNextSpace = Direction.getLocInDirection(nextSpace[0], nextSpace[1], dir);
            if (nextNextSpace[0] == i && nextNextSpace[1] == j) {
                return false;
            }
        }

        // dont move towards other ppl

        return true;
    }

    public Action dodge(Player p, Board b) {
        if (isSafeSpace(p.getRow() + 1, p.getCol() - moveTowardsBase, b)) { // back and down
            return new Action("M", 180 + baseDirection - angledOffset);
        } else if (isSafeSpace(p.getRow(), p.getCol() - moveTowardsBase, b)) { // move away from base
            return new Action("M", 180 + baseDirection);

        } else if (isSafeSpace(p.getRow() - 1, p.getCol() - moveTowardsBase, b)) { // back and up
            return new Action("M", 180 + baseDirection + angledOffset);
        } else if (isSafeSpace(p.getRow() - 1, p.getCol() + moveTowardsBase, b)) { // towards and up
            return new Action("M", 180 + baseDirection + angledOffset);
        } else if (isSafeSpace(p.getRow() + 1, p.getCol() + moveTowardsBase, b)) { // towards and down
            return new Action("M", 180 + baseDirection - angledOffset);
        } else if (isSafeSpace(p.getRow() - 1, p.getCol(), b)) { // up
            return new Action("M", 0);
        } else if (isSafeSpace(p.getRow() + 1, p.getCol(), b)) {
            return new Action("M", 180); // down
        }
        return new Action("M" + baseDirection);
    }

    public boolean isSafeAndDontCareIfNextTo(int i, int j, Board b) {
        List<Shot> allShots = b.getAllShots();
        List<Player> allEnemies = b.getAllPlayers(3 - team);
        if (!b.isEmpty(i, j)) {
            return false;
        }
        for (Player e : allEnemies) {
            if (DirectLOS(i, j, e.getDirection(), e.getRow(), e.getCol(), 3, "idcifrightnexttome", b)) {
                return false;
            }
        }
        if (i > 32 || i < 0 || j > 49 || j < 0)

        {
            return false;
        }
        for (Shot shot : allShots) {
            int dir = shot.getDirection();
            int[] nextSpace = Direction.getLocInDirection(shot.getRow(), shot.getCol(), dir);
            if (nextSpace[0] == i && nextSpace[1] == j) {
                return false;
            }
            int[] nextNextSpace = Direction.getLocInDirection(nextSpace[0], nextSpace[1], dir);
            if (nextNextSpace[0] == i && nextNextSpace[1] == j) {
                return false;
            }
        }

        // dont move towards other ppl

        return true;
    }

    public boolean isSafeSpace(int i, int j, Board b) {
        List<Player> allEnemies = b.getAllPlayers(3 - team);
        if (playerNearby(team, i, j, b)) {
            return false;
        }

        if (i > 32 || i < 0 || j > 49 || j < 0) {
            return false;
        }
        List<Shot> allShots = b.getAllShots();
        if (!b.isEmpty(i, j)) {
            return false;
        }
        for (Shot shot : allShots) {
            int dir = shot.getDirection();
            int[] nextSpace = Direction.getLocInDirection(shot.getRow(), shot.getCol(), dir);
            if (nextSpace[0] == i && nextSpace[1] == j) {
                return false;
            }
            int[] nextNextSpace = Direction.getLocInDirection(nextSpace[0], nextSpace[1], dir);
            if (nextNextSpace[0] == i && nextNextSpace[1] == j) {
                return false;
            }
            int[] nextNextNextSpace = Direction.getLocInDirection(nextNextSpace[0], nextNextSpace[1], dir);
            if (nextNextNextSpace[0] == i && nextNextNextSpace[1] == j) {
                return false;
            }

        }

        // dont move towards other ppl

        return true;
    }

    public Action moveBack(Player p, Board b) {
        if (isSafeSpace(p.getRow() - 1, p.getCol(), b)) {
            return new Action("M", 0);
        } else if (isSafeSpace(p.getRow() + 1, p.getCol(), b)) {
            return new Action("M", 180);
        } else if (isSafeSpace(p.getRow(), p.getCol() - moveTowardsBase, b)) {
            return new Action("M", 180 + baseDirection);
        } else if (isSafeSpace(p.getRow() + 1, p.getCol() - moveTowardsBase, b)) {
            return new Action("M", 180 + baseDirection - angledOffset);
        }
        return new Action("M", 180 + baseDirection + angledOffset);

    }

    public boolean playerNearby(Player p, int i, int j, Board b) {
        List<Player> allPlayers = b.getAllPlayers();
        for (Player player : allPlayers) {
            if (player.getTeam() != p.getTeam()) {
                int dist = Direction.moveDistance(i, j, player.getRow(), player.getCol());
                if (dist <= 3) {
                    if (DirectLOS(i, j, player.getDirection(), player.getRow(), player.getCol(), dist, "idc", b)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean playerNearby(int team, int i, int j, Board b) {
        List<Player> allPlayers = b.getAllPlayers();
        for (Player player : allPlayers) {
            if (player.getTeam() != team) {
                int dist = Direction.moveDistance(i, j, player.getRow(), player.getCol());
                if (dist <= 3) {
                    if (DirectLOS(i, j, player.getDirection(), player.getRow(), player.getCol(), dist, "idc", b)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean DirectLOS(int targetRow, int targetCol, int enemDirection, int enemRow, int enemCol, int dist,
            Board b) {
        int direction = enemDirection;
        int[] coords = Direction.getLocInDirection(enemRow, enemCol, direction);

        for (int i = 0; i < dist; i++) {

            if (coords[0] == targetRow && coords[1] == targetCol) {
                return true;
            }
            if (coords[0] > 32 || coords[0] < 0 || coords[1] > 49 || coords[1] < 0) {
                return false;
            }
            if (b.get(coords[0], coords[1]) != null) {
                return false;
            }

            coords = Direction.getLocInDirection(coords[0], coords[1], direction);
        }

        return false;
    }

    public boolean DirectLOS(int targetRow, int targetCol, int enemDirection, int enemRow, int enemCol, int dist,
            String idcifrightnexttome, Board b) {
        int direction = enemDirection;
        int[] coords = Direction.getLocInDirection(enemRow, enemCol, direction);
        coords = Direction.getLocInDirection(coords[0], coords[1], direction);
        for (int i = 1; i < dist; i++) {
            if (coords[0] == targetRow && coords[1] == targetCol) {
                return true;
            }
            if (coords[0] > 32 || coords[0] < 0 || coords[1] > 49 || coords[1] < 0) {
                return false;
            }
            if (b.get(coords[0], coords[1]) != null) {
                return false;
            }

            coords = Direction.getLocInDirection(coords[0], coords[1], direction);
        }

        return false;
    }

}
