package brains;

import arena.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

/*
 * @author cadenli
 */
public class Technoblade implements Brain {

    @Override
    public String getName() {
        return "Technoblade";
    }

    @Override
    public String getCoder() {
        return "Caden Li";
    }

    @Override
    public Color getColor() {
        return Color.PINK;
    }

    ////////////////////////////////////////////////////////////////////////////////////

    static int[][] map = new int[33][50];
    int baseRow = 16;
    int baseCol = 0;

    @Override
    public Action getMove(Player p, Board b) {
        // set baseCol
        baseCol =  baseCol = b.getBase(3 - p.getTeam()).getCol();

        // update map
        updateMap(p, b);

        // for(int i = 0; i < map.length; i++) {
        //     for(int j = 0; j < map[0].length; j++) {
        //         System.out.print(map[i][j] + " ");
        //     }
        //     System.out.println();
        // }

        System.out.println();

        // dodge
        int dir = dodge(p, b);

        if(dir >= 0) 
            return new Action("M", dir);

        // if we can shoot players or base right now

        // if we are in the base area and stuck up against meat shields

        // find optimal shooting location & go there
        int[] optimalLocation = optimalShootingLocation(p, b);
        int locRow = optimalLocation[0];
        int locCol = optimalLocation[1];

        Cell next = shortestPath(map, new int[]{p.getRow(), p.getCol()}, new int[]{locRow, locCol});
        
        if(next != null) {
            int dirToBase = Direction.getDirectionTowards(p.getRow(), p.getCol(),
                                   next.x, next.y);   
            return new Action("M", dirToBase);   
        }


        return new Action("S");
    }

    public int dodge(Player p, Board b) {
        // we're safe
        if(map[p.getRow()][p.getCol()] == 1) 
            return -1;
        
        // we're being threatened

        // find the optimal location to be in
        int[] optimalLocation = optimalShootingLocation(p, b);
        int locRow = optimalLocation[0];
        int locCol = optimalLocation[1];

        // preferred positions to go to and sort them by distance
        List<int[]> prefPos = Arrays.asList(
            new int[]{Direction.moveDistance(p.getRow(), p.getCol() + 1, locRow, locCol), p.getRow(), p.getCol() + 1},
            new int[]{Direction.moveDistance(p.getRow(), p.getCol() - 1, locRow, locCol), p.getRow(), p.getCol() - 1},
            new int[]{Direction.moveDistance(p.getRow() + 1, p.getCol(), locRow, locCol), p.getRow() + 1, p.getCol()},
            new int[]{Direction.moveDistance(p.getRow() - 1, p.getCol(), locRow, locCol), p.getRow() - 1, p.getCol()},
            new int[]{Direction.moveDistance(p.getRow() + 1, p.getCol() + 1, locRow, locCol), p.getRow() + 1, p.getCol() + 1},
            new int[]{Direction.moveDistance(p.getRow() + 1, p.getCol() - 1, locRow, locCol), p.getRow() + 1, p.getCol() - 1},
            new int[]{Direction.moveDistance(p.getRow() - 1, p.getCol() + 1, locRow, locCol), p.getRow() - 1, p.getCol() + 1},
            new int[]{Direction.moveDistance(p.getRow() - 1, p.getCol() - 1, locRow, locCol), p.getRow() - 1, p.getCol() - 1}
        );
     
        // sort based on distance
        Collections.sort(prefPos, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[0], o2[0]);
            }
        });

        // 1st location that works is the best -> we return the direction
        for(int[] best : prefPos) {
            int bestRow = best[0];
            int bestCol = best[1];
            int dir = Direction.getDirectionTowards(p.getRow(), p.getCol(), bestRow, bestCol);
            if(b.isValid(bestRow, bestCol) && map[bestRow][bestCol] == 1 && !sameDirection(bestRow, bestCol, b)) {
                return dir;
            }
        }

        // we're dead... just shoot at this point
        return -2;
    }

    public boolean sameDirection(int aRow, int aCol, Board b) {
        // check if shots are going in same direction
        List<Shot> shotPos = b.getAllShots();
        for(Shot curr : shotPos) {
            int currRow = curr.getRow();
            int currCol = curr.getCol();
            int dist = Direction.moveDistance(currRow, currCol, aRow, aCol);
            int shotDir = curr.getDirection();

            if(dist <= 2 && shotDir == Direction.getDirectionTowards(currRow, currCol, aRow, aCol)) 
                return true;
        }

        // check if players are facing same direction
        List<Player> playerPos = b.getAllPlayers();
        for(Player curr : playerPos) {
            int currRow = curr.getRow();
            int currCol = curr.getCol();
            int dist = Direction.moveDistance(currRow, currCol, aRow, aCol);
            int playerDir = curr.getDirection();

            if(dist <= 2 && playerDir == Direction.getDirectionTowards(currRow, currCol, aRow, aCol)) 
                return true;
        }

        return false;
    }

    public int[] optimalShootingLocation(Player p, Board b) {
        int[][] prefShotLoc;
        
        // probably very inefficient but screw it LOL
        if(baseCol == 0) 
            prefShotLoc = new int[][]{{15, 1}, {17, 1}, {16, 2}, {16, 1}, {14, 0}, {18, 0}, {15, 0}, {17, 0}, {16, 3}, {14, 2}, {18, 2}, {19, 3}, {13, 3}, {13, 0}, {19, 0}};
        else 
            prefShotLoc = new int[][]{{15, 48}, {17, 48}, {16, 47}, {16, 48}, {14, 49}, {18, 49}, {15, 49}, {17, 49}, {16, 46}, {14, 47}, {18, 47}, {19, 46}, {13, 46}, {13, 49}, {19, 49}};
        
        for(int[] coords : prefShotLoc) {
            int currRow = coords[0];
            int currCol = coords[1];
            int dist = Direction.moveDistance(p.getRow(), p.getCol(), currRow, currCol);
            if(map[currRow][currCol] == 1 && !inBetween(p.getRow(), p.getCol(), currRow, currCol, b, dist)) {
                return coords;
            }
        }
        
        // no coords ;(
        return new int[] {-1, -1};
    }

    public boolean inBetween(int aRow, int aCol, int bRow, int bCol, Board b, int dist) {
        // getting the next location
        int dir = Direction.getDirectionTowards(aRow, aCol, bRow, bCol);
        int[] next = Direction.getLocInDirection(aRow, aCol, dir);

        // differences
        int rowDiff = Math.abs(aRow - bRow);
        int colDiff = Math.abs(aCol - bCol);

        // if they're not even lined up, there is nothing in between, return false
        if(rowDiff != 0 || colDiff != 0 || rowDiff != colDiff)
            return false;

        // if there is an obstacle, return true
        if(b.isValid(aRow, aCol) && !b.isEmpty(aRow, aCol)) 
            return true;

        // if we've looped enough, and the positions are all checked, return false
        if(rowDiff == 0 && colDiff == 0)
            return false;

        return inBetween(next[0], next[1], bRow, bCol, b, dist);
    }

    public void updateMap(Player p, Board b) {
        map = new int[33][50];

        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                if(b.isEmpty(i, j) || (p.getRow() == i && p.getCol() == j))
                    map[i][j] = 1;
            }
        }
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                // Check all players and 2 spots ahead
                if(b.get(i, j) instanceof Player) {
                    if(p.getRow() != i || p.getCol() != j) { // make sure it's not me
                        Player curr = (Player) b.get(i, j);
                        map[i][j] = 0;
                        int dir = curr.getDirection();
                        int[] next1 = Direction.getLocInDirection(i, j, dir);
                        int[] next2 = Direction.getLocInDirection(next1[0], next1[1], dir);

                        // set them to obstacles
                        if(b.isValid(next2[0], next2[1])) {
                            map[next2[0]][next2[1]] = 0;
                        }
                    }
                }
                // check all shots and 2 spots ahead
                if(b.get(i, j) instanceof Shot) {
                    map[i][j] = 0;
                    Shot curr = (Shot) b.get(i, j);
                    int dir = curr.getDirection();
                    int[] next1 = Direction.getLocInDirection(i, j, dir);
                    int[] next2 = Direction.getLocInDirection(next1[0], next1[1], dir);

                    // set them to obstacles
                    if(b.isValid(next1[0], next1[1])) {
                        map[next1[0]][next1[1]] = 0;
                    }
                    if(b.isValid(next2[0], next2[1])) {
                        map[next2[0]][next2[1]] = 0;
                    }
                }
                // check all blockers
                if(b.get(i, j) instanceof Blocker) {
                    map[i][j] = 0;
                }   
            }
        }
    }

    private static class Cell  {
        int x;
        int y;
        int dist;  	
        Cell prev;  //parent cell in the path
        Cell(int x, int y, int dist, Cell prev) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.prev = prev;
        }
        
        @Override
        public String toString(){
        	return "(" + x + "," + y + ")";
        }
    }

    public static Cell shortestPath(int[][] matrix, int[] start, int[] end) {        
        int sx = start[0], sy = start[1];
        int dx = end[0], dy = end[1];	
        //if start or end value is 0, return
        if (matrix[sx][sy] == 0 || matrix[dx][dy] == 0) {                
            return null;  
        }
        int m = matrix.length;
        int n = matrix[0].length;	    
        Cell[][] cells = new Cell[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != 0) {
                    cells[i][j] = new Cell(i, j, Integer.MAX_VALUE, null);
                }
            }
        }
        //breadth first search
        LinkedList<Cell> queue = new LinkedList<>();       
        Cell src = cells[sx][sy];
        src.dist = 0;
        queue.add(src);
        Cell dest = null;
        Cell p;
        while ((p = queue.poll()) != null) {
            //find destination 
            if (p.x == dx && p.y == dy) { 
                dest = p;
                break;
            }      
            // moving up
            visit(cells, queue, p.x - 1, p.y, p);        
            // moving down
            visit(cells, queue, p.x + 1, p.y, p);        
            // moving left
            visit(cells, queue, p.x, p.y - 1, p);        
            //moving right
            visit(cells, queue, p.x, p.y + 1, p);
            
            // moving up left
            visit(cells, queue, p.x - 1, p.y - 1, p);
            // moving up right
            visit(cells, queue, p.x - 1, p.y + 1, p); 
            // moving down left
            visit(cells, queue, p.x + 1, p.y - 1, p); 
            // moving down right
            visit(cells, queue, p.x + 1, p.y + 1, p); 
        }

        //compose the path if path exists
        if (dest == null) {
            return null;
        } else {
            LinkedList<Cell> path = new LinkedList<>();
            p = dest;
            do {
                path.addFirst(p);
            } while ((p = p.prev) != null);            
            if(path.size() > 1)
                return path.get(1);
            return path.get(0);
        }
    }

    private static void visit(Cell[][] cells, LinkedList<Cell> queue, int x, int y, Cell parent) { 
            //out of boundary
        if (x < 0 || x >= cells.length || y < 0 || y >= cells[0].length || cells[x][y] == null) {
            return;
        }    
        int dist = parent.dist + 1;
        Cell p = cells[x][y];
        if (dist < p.dist) {
            p.dist = dist;
            p.prev = parent;
            queue.add(p);
        }
    }

}