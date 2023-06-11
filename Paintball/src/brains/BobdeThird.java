/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package brains;

import arena.*;
import static brains.SmartBaseCharger.shortestPath;
import java.awt.Color;

import java.util.*;
import java.io.*;
/**
 *
 * @author cadenli
 */
public class BobdeThird implements Brain {

    int baseRow = 16;
    int baseCol = 47;
    
    int bRow = 16;
    int bCol = 49;
    
    int[][] map = new int[33][50];
    int[][] realMap = new int[33][50];
    int[][] pureObsMap = new int[33][50];
    
    @Override
    public String getName() {
        return "BobdeThird";
    }

    @Override
    public String getCoder() {
        return "Caden Li";
    }

    @Override
    public Color getColor() {
        return Color.PINK;
    }

    @Override
    public Action getMove(Player p, Board b) {
        bCol = b.getBase(3 - p.getTeam()).getCol();
                
        updateMap(p, b);
        Action next = dodgeRoute(p, b);
        
        if(next == null) {
            int dir = Direction.getDirectionTowards(p.getRow(), p.getCol(), bRow, bCol);
            int[] coor = Direction.getLocInDirection(p.getRow(), p.getCol(), dir);
            
            if(map[coor[0]][coor[1]] == 1)
                return new Action("M", dir);
            // use real map to decide if all spots are helpless
            // TODO: Also check if timer for shooting ran out before crossing out a place 1 block away 
           return new Action("T", 0);
        }
        
        return next;
    }
        
    public void updateMap(Player p, Board b) {
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                if(b.isEmpty(i, j) || (p.getRow() == i && p.getCol() == j)) {
                    map[i][j] = 1;
                } else {
                    map[i][j] = 0;
                }
            }
        }
        
        for(int i = 0; i < map.length; i++) {
            pureObsMap[i] = map[i].clone();
        }
        
        List<Shot> shotPos = b.getAllShots();

        // adding bullets and their path as obstacles for future path planning
        for(Shot i : shotPos) {
            int tempRow = i.getRow();
            int tempCol = i.getCol();
            int direction = i.getDirection();
            pureObsMap[tempRow][tempCol] --;
            if(tempCol < 0)
                tempCol = 0;
            if(direction == 0 && b.isValid(tempRow - 1, tempCol))
                map[tempRow - 1][tempCol] = 0;
            else if(direction == 45 && b.isValid(tempRow - 1, tempCol + 1))
                map[tempRow - 1][tempCol + 1] = 0;
            else if(direction == 90 && b.isValid(tempRow, tempCol + 1))
                map[tempRow][tempCol + 1] = 0;
            else if(direction == 135 && b.isValid(tempRow + 1, tempCol + 1))
                map[tempRow + 1][tempCol + 1] = 0;
            else if(direction == 180 && b.isValid(tempRow + 1, tempCol))
                map[tempRow + 1][tempCol] = 0;
            else if(direction == 225 && b.isValid(tempRow + 1, tempCol - 1))
                map[tempRow + 1][tempCol - 1] = 0;
            else if(direction == 270 && b.isValid(tempRow, tempCol - 1))
                map[tempRow][tempCol - 1] = 0;
            else if(direction == 315 && b.isValid(tempRow - 1, tempCol - 1))
                map[tempRow - 1][tempCol - 1] = 0;
            
            if(direction == 0 && b.isValid(tempRow - 2, tempCol))
                map[tempRow - 2][tempCol] = 0;
            else if(direction == 45 && b.isValid(tempRow - 2, tempCol + 2))
                map[tempRow - 2][tempCol + 2] = 0;
            else if(direction == 90 && b.isValid(tempRow, tempCol + 2))
                map[tempRow][tempCol + 2] = 0;
            else if(direction == 135 && b.isValid(tempRow + 2, tempCol + 2))
                map[tempRow + 2][tempCol + 2] = 0;
            else if(direction == 180 && b.isValid(tempRow + 2, tempCol))
                map[tempRow + 2][tempCol] = 0;
            else if(direction == 225 && b.isValid(tempRow + 2, tempCol - 2))
                map[tempRow + 2][tempCol - 2] = 0;
            else if(direction == 270 && b.isValid(tempRow, tempCol - 2))
                map[tempRow][tempCol - 2] = 0;
            else if(direction == 315 && b.isValid(tempRow - 2, tempCol - 2))
                map[tempRow - 2][tempCol - 2] = 0;
            
        }
        
       
        for(int i = 0; i < map.length; i++) {
            realMap[i] = map[i].clone();
        }
        
        
        
        List<Player> playerPos = b.getAllPlayers();
        
        for(Player i : playerPos) {
            int tempRow = i.getRow();
            int tempCol = i.getCol();
            int direction = i.getDirection();
            
            if(tempRow == p.getRow() && tempCol == p.getCol())
                continue;
            
            // map of only obstacles (and bullets ig)
            pureObsMap[tempRow][tempCol] --;
            
//            if(direction == 0 && b.isValid(tempRow - 1, tempCol))
//                map[tempRow - 1][tempCol] = 0;
//            else if(direction == 45 && b.isValid(tempRow - 1, tempCol + 1))
//                map[tempRow - 1][tempCol + 1] = 0;
//            else if(direction == 90 && b.isValid(tempRow, tempCol + 1))
//                map[tempRow][tempCol + 1] = 0;
//            else if(direction == 135 && b.isValid(tempRow + 1, tempCol + 1))
//                map[tempRow + 1][tempCol + 1] = 0;
//            else if(direction == 180 && b.isValid(tempRow + 1, tempCol))
//                map[tempRow + 1][tempCol] = 0;
//            else if(direction == 225 && b.isValid(tempRow + 1, tempCol - 1))
//                map[tempRow + 1][tempCol - 1] = 0;
//            else if(direction == 270 && b.isValid(tempRow, tempCol - 1))
//                map[tempRow][tempCol - 1] = 0;
//            else if(direction == 315 && b.isValid(tempRow - 1, tempCol - 1))
//                map[tempRow - 1][tempCol - 1] = 0;
            
            if(direction == 0 && b.isValid(tempRow - 2, tempCol))
                map[tempRow - 2][tempCol] = 0;
            else if(direction == 45 && b.isValid(tempRow - 2, tempCol + 2))
                map[tempRow - 2][tempCol + 2] = 0;
            else if(direction == 90 && b.isValid(tempRow, tempCol + 2))
                map[tempRow][tempCol + 2] = 0;
            else if(direction == 135 && b.isValid(tempRow + 2, tempCol + 2))
                map[tempRow + 2][tempCol + 2] = 0;
            else if(direction == 180 && b.isValid(tempRow + 2, tempCol))
                map[tempRow + 2][tempCol] = 0;
            else if(direction == 225 && b.isValid(tempRow + 2, tempCol - 2))
                map[tempRow + 2][tempCol - 2] = 0;
            else if(direction == 270 && b.isValid(tempRow, tempCol - 2))
                map[tempRow][tempCol - 2] = 0;
            else if(direction == 315 && b.isValid(tempRow - 2, tempCol - 2))
                map[tempRow - 2][tempCol - 2] = 0;
        }
    }
    
    public Action route(Player p, Board b) {
        return new Action("S");
    }
        
    public boolean canShoot(Player p, Board b) {
        return directAim(p, p.getRow(), p.getCol(), bRow, bCol) && inBetween(p.getRow(), p.getCol(), bRow, bCol, b);
    }
    
    public boolean directShot(Shot curr, int sRow, int sCol, int pRow, int pCol) {
        if((curr.getDirection() == Direction.getDirectionTowards(sRow, sCol, pRow, pCol) 
                && (sRow - pRow == 0 || sCol - pCol == 0 || Math.abs(sRow - pRow) == Math.abs(sCol - pCol))))
                return true;
        return false;
    }
    
    public boolean directAim(Player curr, int sRow, int sCol, int pRow, int pCol) {
        if((curr.getDirection() == Direction.getDirectionTowards(sRow, sCol, pRow, pCol) 
                && (sRow - pRow == 0 || sCol - pCol == 0 || Math.abs(sRow - pRow) == Math.abs(sCol - pCol))))
                return true;
        return false;
    }
    
    // a = shots / people, b = player
    // check if there are any obstacles in between to protect
    public boolean inBetween(int aRow, int aCol, int bRow, int bCol, Board b) {
        
        int currX = Math.min(aRow, bRow);
        int currY = Math.min(aCol, bCol);
        
       
        
        for(int i = Math.min(aRow, bRow); i < Math.max(aRow, bRow); i++) {
            for(int j = Math.min(aCol, bCol); j < Math.max(aCol, bCol); j++) {
                if(pureObsMap[i][j] == 0) {
                    //System.out.println("PLEASEEEEE");
                }
                
                if(aRow - bRow == 0 && i == aRow)
                    //System.out.println("BROWAHT");
                
                if(aCol - bCol == 0 && j == aCol)
                    //System.out.println("SD:FLJKSDLKFSJD");
                
                if(Math.abs(aRow - bRow) == Math.abs(aCol - bCol) && (i - currX) == (j - currY))
                    //System.out.println("DISCRIMINATION");
                
                if(aRow - bRow == 0 && i == aRow && pureObsMap[i][j] == 0) { // horizontals
                    //System.out.println("TRUE");
                    return true;
                }
                else if(aCol - bCol == 0 && j == aCol && pureObsMap[i][j] == 0) { // verticals
                    //System.out.println("T");
                    return true;
                }
                else if(Math.abs(aRow - bRow) == Math.abs(aCol - bCol) && (i - currX) == (j - currY) && pureObsMap[i][j] == 0) { // diagonals
                    //System.out.println("TRR");
                    return true;
                }
            }
        }
        
        return false;
    }
    
    
    // dodging while going to the base
    public Action dodgeRoute(Player p, Board b) {
        
        // find preferred positions to go to and sort them by distance
        List<int[]> prefPos = Arrays.asList(
            new int[]{Direction.moveDistance(p.getRow(), p.getCol() + 1, baseRow, baseCol), p.getRow(), p.getCol() + 1},
            new int[]{Direction.moveDistance(p.getRow(), p.getCol() - 1, baseRow, baseCol), p.getRow(), p.getCol() - 1},
            new int[]{Direction.moveDistance(p.getRow() + 1, p.getCol(), baseRow, baseCol), p.getRow() + 1, p.getCol()},
            new int[]{Direction.moveDistance(p.getRow() - 1, p.getCol(), baseRow, baseCol), p.getRow() - 1, p.getCol()},
            new int[]{Direction.moveDistance(p.getRow() + 1, p.getCol() + 1, baseRow, baseCol), p.getRow() + 1, p.getCol() + 1},
            new int[]{Direction.moveDistance(p.getRow() + 1, p.getCol() - 1, baseRow, baseCol), p.getRow() + 1, p.getCol() - 1},
            new int[]{Direction.moveDistance(p.getRow() - 1, p.getCol() + 1, baseRow, baseCol), p.getRow() - 1, p.getCol() + 1},
            new int[]{Direction.moveDistance(p.getRow() - 1, p.getCol() - 1, baseRow, baseCol), p.getRow() - 1, p.getCol() - 1}
        );
     
        // sort based on distance
        Collections.sort(prefPos, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[0], o2[0]);
            }
        });
        
     
        // check for all shots and avoid them
        List<Shot> allShots = b.getAllShots();
        
        for(Shot curr : allShots) {
            int sRow = curr.getRow();
            int sCol = curr.getCol();
            int dist = Direction.moveDistance(p.getRow(), p.getCol(), sRow, sCol);
            
            // if it's far or not towards us then we don't worry about it
            if(dist > 3 || !directShot(curr, sRow, sCol, p.getRow(), p.getCol()))
                continue;
            
            for(int[] nextMove : prefPos) {
                // check that the moves aren't out of bounds
                if(!b.isValid(nextMove[1], nextMove[2]))
                    continue;

                // check if the space is free OR if you'll die
                if(map[nextMove[1]][nextMove[2]] == 1 && !(directShot(curr, sRow, sCol, nextMove[1], nextMove[2])) && !inBetween(sRow, sCol, nextMove[1], nextMove[2], b)) {
                    return new Action("M", Direction.getDirectionTowards(p.getRow(), p.getCol(), nextMove[1], nextMove[2]));
                }
            }   
        }
        
        // check for people 1 spacing away from me
        List<Player> playerPos = b.getAllPlayers();
        boolean shootPlayer = false;
        
        for(Player curr : playerPos) {
            int pRow = curr.getRow();
            int pCol = curr.getCol();
            int dist = Direction.moveDistance(p.getRow(), p.getCol(), pRow, pCol);
            
            if(dist == 2 && !inBetween(p.getRow(), p.getCol(), pRow, pCol, b) && p.getDirection() == Direction.getDirectionTowards(p.getRow(), p.getCol(), pRow, pCol) && curr.getTeam() != p.getTeam()) 
                shootPlayer = true;
            
            // if dist != 2 or not facing me, we ignore this player
            if(dist != 2 || !directAim(curr, pRow, pCol, p.getRow(), p.getCol()))
                continue;
            
            // legit threat
            for(int[] nextMove : prefPos) {
                // check that the moves aren't out of bounds
                if(!b.isValid(nextMove[0], nextMove[1]))
                    continue;

                // check if the space is free OR if you'll die
                if(map[nextMove[1]][nextMove[2]] == 1 && !(directAim(curr, pRow, pCol, nextMove[1], nextMove[2])) && !inBetween(pRow, pCol, nextMove[1], nextMove[2], b)) {
                    return new Action("M", Direction.getDirectionTowards(p.getRow(), p.getCol(), nextMove[1], nextMove[2]));
                }
                
                
            } 
            
        }
        
        if(shootPlayer) {
            //System.out.println("KILLED");
            return new Action("S");
        }
        
        int rowDiff = Math.abs(p.getRow() - bRow);
        int colDiff = Math.abs(p.getCol() - bCol);
        
        if((rowDiff == 0 || colDiff == 0 || rowDiff == colDiff) && Direction.moveDistance(p.getRow(), p.getCol(), bRow, bCol) <= 3) {
            if(p.getDirection() != Direction.getDirectionTowards(p.getRow(), p.getCol(),
                                   bRow, bCol)) 
            return new Action("T", Direction.getDirectionTowards(p.getRow(), p.getCol(),
                                   bRow, bCol));
            
            //System.out.println("SHOOTING" + inBetween(p.getRow(), p.getCol(), bRow, bCol, b));
            if(inBetween(p.getRow(), p.getCol(), bRow, bCol, b)) {
                //System.out.println("FIGHT");
                int[] oneAhead = Direction.getLocInDirection(p.getRow(), p.getCol(), p.getDirection());
                int[] clear = Direction.getLocInDirection(p.getRow(), p.getCol(), (p.getDirection() + 180) % 360);
                if(pureObsMap[oneAhead[0]][oneAhead[1]] == 0 && map[clear[0]][clear[1]] == 1) {
                    return new Action("M", (p.getDirection() + 180) % 360);
                }
            }
                
            
            return new Action("S");
        }
            

        int[] coords = bestShootLoc(b, p);
        
        if(coords[0] != -1) {
            baseRow = coords[0];
            baseCol = coords[1];
        } else {
            baseRow = 16;
            if(bCol == 0)
                baseCol = 2;
            else
                baseCol = 47;
        }
        
                
        // Shortest Route Moment
        Cell next = shortestPath(map, new int[]{p.getRow(), p.getCol()}, new int[]{baseRow, baseCol});
                
        if(next == null) {
            
            if(Direction.moveDistance(p.getRow(), p.getCol(), bRow, bCol) > 5) {
                
                int fakeRow = bRow;
                int fakeCol = bCol;
                
                if(bCol == 0) {
                    fakeCol += 5;
                } else {
                    fakeCol -= 5;
                }
                
                next = shortestPath(map, new int[]{p.getRow(), p.getCol()}, new int[]{fakeRow, fakeCol});
            }
            
            if(next == null)
                return new Action("S");
        }
        
        if(canShoot(p, b)) {
            return new Action("S");
        }
        
        if(p.getRow() == next.x && p.getCol() == next.y) {
            if(p.getDirection() != Direction.getDirectionTowards(p.getRow(), p.getCol(),
                                   16, 49)) 
            return new Action("T", Direction.getDirectionTowards(p.getRow(), p.getCol(),
                                   16, 49));
            
            return new Action("S");
        }
        
        int dirToBase = Direction.getDirectionTowards(p.getRow(), p.getCol(),
                                   next.x, next.y);   
        return new Action("M", dirToBase);           
    }
    
    // find best shooting position
    public int[] bestShootLoc(Board b, Player p) {
        
        int[][] prefShotLoc;
        
        // probably very inefficient but screw it LOL
        if(bCol == 0) 
            prefShotLoc = new int[][]{{15, 1}, {17, 1}, {16, 2}, {16, 1}, {14, 0}, {18, 0}, {15, 0}, {17, 0}, {16, 3}, {14, 2}, {18, 2}, {19, 3}, {13, 3}, {13, 0}, {19, 0}};
        else 
            prefShotLoc = new int[][]{{15, 48}, {17, 48}, {16, 47}, {16, 48}, {14, 49}, {18, 49}, {15, 49}, {17, 49}, {16, 46}, {14, 47}, {18, 47}, {19, 46}, {13, 46}, {13, 49}, {19, 49}};
        
        for(int[] coords : prefShotLoc) {
            if(map[coords[0]][coords[1]] == 1 && !(inBetween(coords[0], coords[1], bRow, bCol, b) && Direction.moveDistance(coords[0], coords[1], bRow, bCol) < 3)) 
                return new int[] {coords[0], coords[1]};
        }
        
        // no coords ;(
        return new int[] {-1, -1};
    }
    
    /*
    1. find most optimal path to base
    2. go to base while avoiding bullets
    3. shoot at people on my way
    4. get to a place where i can shoot
    5. shoot if base is available 
    6. avoid bullets going towards me
    7. if opponent 1 block away from me & look towards me, act like they're shooting
    8. heat map of where i should be to shoot
    
    */    
    
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
