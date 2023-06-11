/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package arena;

import java.util.Random;

/**
 * This class represents a Player in the game. Each Player has a Brain property,
 * which determines the behavior of the Player. Note that most of the actions
 * of a Player are handled using methods within this class.
 * @author Thomas Weisswange
 */
public class Player extends Occupant {
    private Brain controller;
    private int direction;
    private int turnsUntilShoot;
    private int kills;
    private int frags;
    private int deaths;
    private int enemyBaseHits;
    private int selfBaseHits;
    
    private final boolean DEBUG = false;
    
    /**
     * Constructor method for a Player, initializing properties.
     * @param team the team to which this Player belongs
     * @param brain the Brain object which controls this Player
     * @param dir the initial direction in which this Player will be facing
     */
    Player(int team, Brain brain, int dir) {
        super(team);
        controller = brain;
        direction = Direction.roundTo8(dir);
    }
    
    /**
     * Constructor method for a Player, initializing properties, assuming an
     * initial direction of 0 (north).
     * @param team the team to which this Player belongs
     * @param brain the Brain object which controls this Player
     */
    Player(int team, Brain brain) {
        this(team, brain, 0);
    }

    /**
     * Getter method for the direction of this Player
     * @return the current direction in which this Player is facing
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Provides the number of (player) turns that must pass until the Player
     * is able to "reload" and shoot again. A value of 0 indicates that the
     * Player may shoot; any positive value means that the Player must wait
     * to shoot, and any attempt to shoot will result in the action being
     * converted to a "pass".
     * @return the number of player turns remaining until the Player may shoot
     */
    public int getTurnsUntilShoot() {
        return turnsUntilShoot;
    }
    
    /**
     * Determines whether or not a Player may shoot this turn. Note that this
     * method takes into account not only whether or not the Player has to wait
     * to "reload", but also whether or not the space in front is empty. If the
     * space in front is off-the-board or occupied by a Player or Blocker, this
     * Player cannot shoot.
     * @return true if the Player can legally fire in its current state, or
     * false if it cannot.
     */
    public boolean canShoot() {
        if (turnsUntilShoot > 0)
            return false;
        int[] dest = Direction.getLocInDirection(row, col, 
                    direction);
        int destRow = dest[0];
        int destCol = dest[1];

        //destination is off of board
        if (!myBoard.isValid(destRow, destCol)) {
            return false;
        }

        Occupant target = myBoard.get(destRow, destCol);
        //destination is occupied by player or blocker, no shot
        if (target instanceof Player || target instanceof Blocker) {
            return false;
        }
        
        return true;
    }

    /**
     * Getter method for number of times this Player has shot a Player on the
     * opposing team.
     * @return total number of kills by this Player
     */
    public int getKills() {
        return kills;
    }

    /**
     * Getter method for number of times this Player has shot a Player on their
     * own team.
     * @return total number of frags by this Player
     */
    public int getFrags() {
        return frags;
    }

    /**
     * Getter method for number of times this Player has been shot.
     * @return total number of times this Player has been shot
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * Getter method for number of times this Player has shot the opposing Base.
     * @return total number of times this Player has shot the enemy Base
     */
    public int getEnemyBaseHits() {
        return enemyBaseHits;
    }

    /**
     * Getter method for number of times this Player has shot its own Base.
     * @return total number of times this Player has shot its own Base.
     */
    public int getSelfBaseHits() {
        return selfBaseHits;
    }
    
    /**
     * Determines the total score for this Player. Player scores work as follows:
     * <table border="1">
     * <tr><th>Action</th><th>Points</th></tr>
     * <tr><td>Shooting opposing player</td><td>1</td></tr>
     * <tr><td>Shooting enemy base</td><td>3</td></tr>
     * <tr><td>Being shot</td><td>-1</td></tr>
     * <tr><td>Shooting own team member</td><td>-1</td></tr>
     * <tr><td>Shooting own base</td><td>-3</td></tr>
     * </table>
     * @return the score for this Player
     */
    public int getScore() {
        return kills + 3*enemyBaseHits - deaths - frags
                - 3*selfBaseHits;
    }

    /**
     * <u>Package-private.</u> Getter method for the Brain for this Player.
     * @return the controller (Brain) for this Player
     */
    Brain getController() {
        return controller;
    }
    
    /**
     * <u>Package-private.</u> Called when this Player has been shot. Updates
     * the number of deaths for this Player as well as the team score, depending
     * on who was the owner for the Shot. Notifies the Owner of the shot that
     * they hit a Player, either a friend or enemy.
     * @param shot the Shot object that hit this Player
     */
    void hit(Shot shot) {
        deaths++;
        if(team != shot.getOwner().getTeam()) {
            shot.getOwner().hitEnemy();
            myBoard.scorePoints(3 - team, 1);
        } else {
            shot.getOwner().hitFriend();
            myBoard.losePoints(team, 1);
        }
        removeSelfFromBoard();
    }
    
    /**
     * <u>Package-private.</u> Called when one of this Player's Shots hits the
     * enemy Base. Updates counter for individual score purposes (note that the
     * Base already updated the team score).
     */
    void hitEnemyBase() {
        enemyBaseHits++;
    }
    
    /**
     * <u>Package-private.</u> Called when one of this Player's Shots hits its
     * own Base. Updates counter for individual score purposes (note that the
     * Base already updated the team score).
     */
    void hitOwnBase() {
        selfBaseHits++;
    }
    
    /**
     * <u>Package-private.</u> Called when one of this Player's Shots hits an
     * enemy Player. Updates counter for individual score purposes (note that
     * the hit method had already updated the shooter as well as the Board to
     * update their respective scores).
     */
    void hitEnemy() {
        kills++;
    }
    
    /**
     * <u>Package-private.</u> Called when one of this Player's Shots hits a
     * Player on its own team. Updates counter for individual score purposes
     * (note that the hit method had already updated the shooter as well as the
     * Board to update their respective scores).
     */
    void hitFriend() {
        frags++;
    }
    
    /**
     * <u>Package-private.</u> Respawns this player on the specified Board. All Players respawn on an
     * empty space somewhere within the first 10 columns of their side of the
     * Board. Choice of space is at random. Newly respawned Players begin
     * facing left or right, towards their enemy base.
     * @param board the Board onto which the Player is to respawn.
     */
    void respawn(Board board) {
        Random randGen = new Random();
        int row, col;
        do {
            row = randGen.nextInt(33);
            if (team == 1)
                col = randGen.nextInt(10);
            else
                col = randGen.nextInt(10) + 40;
        } while (!board.isEmpty(row, col));
        direction = (team == 1 ? 90 : 270);
        addSelfToBoard(board, row, col);
    }
    
    /**
     * <u>Package-private.</u> This is the primary method for handling a single
     * action by a Player. The method begins by calling the controller's
     * getMove method, asking the Brain for an Action object, indicating what
     * the Brain wishes the Player to do. It then handles the Action, updating
     * both the Player and the Board depending on the result of the Action.
     */
    void act() {
        debugPrint("Player acts");
        if (turnsUntilShoot > 0)
            turnsUntilShoot--;
        Action action;
        try {
            action = controller.getMove(this, myBoard);
        } catch (Exception e) {
            action = new Action("P");
            System.err.println(controller.getName() + ": " + e);
        }
        if (action == null)
            action = new Action("P");
        String actionType = action.getType();
        
        //pass
        if (actionType.equals("P"))
            return;
        
        //shoot
        if (actionType.equals("S")) {
            debugPrint("Player shoots");
            if (turnsUntilShoot > 0)
                return;
            
            int[] dest = Direction.getLocInDirection(row, col, 
                    direction);
            int destRow = dest[0];
            int destCol = dest[1];
            
            //destination is off of board
            if (!myBoard.isValid(destRow, destCol)) {
                debugPrint("Destination is off board");
                return;
            }
            
            Occupant target = myBoard.get(destRow, destCol);
            //destination is occupied by player or blocker, no shot
            if (target instanceof Player || target instanceof Blocker) {
                debugPrint("Destination is occupied by player or blocker");
                return;
            }
            
            //destination is occupied by shot
            if (target instanceof Shot) {
                debugPrint("Destination is occupied by shot");
                target.removeSelfFromBoard();
                turnsUntilShoot = 3;
                return;
            }
            
            //destination is occupied by base
            if (target instanceof Base) {
                debugPrint("Destination is occupied by base");
                ((Base) target).hit(new Shot(team, direction, this));
                turnsUntilShoot = 3;
                return;
            }
            
            //destination is unoccupied
            debugPrint("Destination is unoccupied");
            new Shot(team, direction, this).addSelfToBoard(myBoard, destRow, destCol);
            turnsUntilShoot = 3;
        }
        
        //turn
        if (actionType.equals("T")) {
            debugPrint("Player turns");
            direction = Direction.roundTo8(action.getDirection());
        }
        
        //move
        if (actionType.equals("M")) {
            debugPrint("Player moves");
            int[] dest = Direction.getLocInDirection(row, col, 
                    action.getDirection());
            int destRow = dest[0];
            int destCol = dest[1];
            
            //destination is off of board
            if (!myBoard.isValid(destRow, destCol)) {
                debugPrint("Destination is off board");
                return;
            }
            
            Occupant target = myBoard.get(destRow, destCol);
            //destination is occupied by blocker, player, or
            //base: no movement
            if (target instanceof Blocker ||
                    target instanceof Player ||
                    target instanceof Base) {
                return;
            }
            
            //destination is occupied by shot
            if (target instanceof Shot) {
                //removeSelfFromBoard();
                target.removeSelfFromBoard();
                hit((Shot) target);
            }
            
            //destination is empty
            moveTo(destRow, destCol);
        }
    }
    
    /**
     * Debugging code. Prints a message to screen only if DEBUG is set to true.
     * @param s the String to be printed, conditionally
     */
    private void debugPrint(String s) {
        if (DEBUG)
            System.out.println(s);
    }
}
