/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package brains;

import arena.*;
import java.awt.Color;

import java.util.*;
import java.io.*;
/**
 *
 * @author cadenli
 */
public class SmartBaseCharger implements Brain{

    int baseRow = 16;
    int baseColol = 0;
    int baseCol = 0;
    int[][] route = new int[33][50];
    int left = -1;
    
    int OGbRow = 16;
    int OGbCol = 0;
    
 
    @Override
    public String getName() {
        return "SmartBaseCharger";
    }

    @Override
    public String getCoder() {
        return "Caden Li";
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    @Override
    @SuppressWarnings("empty-statement")
    public Action getMove(Player p, Board b) {
        // find out which base / team we on
        
        if (-1 == -1) {
            baseCol = b.getBase(3 - p.getTeam()).getCol();
            baseColol = baseCol;
            OGbCol = baseCol;
            if(baseCol == 0) {
                baseCol += 1;
                left = -1;
            } else {
                baseCol -= 1;
                left = 1;
            }
            
        }
        
        baseCol = Math.max(0, baseCol);
        OGbCol = Math.max(0, OGbCol);

        baseCol = Math.min(49, baseCol);
        OGbCol = Math.min(49, OGbCol);
        
        // set up the map with obstacles included
        for(int i = 0; i < route.length; i++) {
            for(int j = 0; j < route[0].length; j++) {
                if(b.isEmpty(i, j) || (p.getRow() == i && p.getCol() == j))
                    route[i][j] = 1;
                else {
                    route[i][j] = 0;
                }
            }
        }
        int temp = baseCol;
        int distToBase = Direction.moveDistance(p.getRow(), p.getCol(),
                                   OGbRow, OGbCol);
        
        if(!b.isEmpty(baseRow, baseCol) && distToBase > 1) {
            baseRow = 17;
            if(!b.isEmpty(baseRow, baseCol)) {
                baseRow = 15;
                if(!b.isEmpty(baseRow, baseCol)){
                    baseCol += left;
                    baseCol = Math.max(0, baseCol);
                    if(!b.isEmpty(baseRow, baseCol)){
                        baseRow = 17;
                        if(!b.isEmpty(baseRow, baseCol)){
                            baseRow = 16;
                            baseCol = temp;
                        }
                    }
                }
            }
        }
        baseCol = Math.max(0, baseCol);
        OGbCol = Math.max(0, OGbCol);
        
        baseCol = Math.min(49, baseCol);
        OGbCol = Math.min(49, OGbCol);
          
        List<Shot> shotPos = b.getAllShots();

        // adding bullets and their path as obstacles for future path planning
        for(Shot i : shotPos) {
            int tempRow = i.getRow();
            int tempCol = i.getCol();
            int direction = i.getDirection();
            
            //System.out.println(tempRow + " " + tempCol + ":::" + direction);
            if(tempCol < 0)
                tempCol = 0;
            if(direction == 0 && tempRow > 0)
                route[tempRow - 1][tempCol] = 0;
            else if(direction == 45 && (tempRow > 0 && tempCol < 48))
                route[tempRow - 1][tempCol + 1] = 0;
            else if(direction == 90 && tempCol < 48)
                route[tempRow][tempCol + 1] = 0;
            else if(direction == 135 && (tempRow < 32 && tempCol < 48))
                route[tempRow + 1][tempCol + 1] = 0;
            else if(direction == 180 && tempRow < 32)
                route[tempRow + 1][tempCol] = 0;
            else if(direction == 225 && (tempRow < 32 && tempCol > 0))
                route[tempRow + 1][tempCol - 1] = 0;
            else if(direction == 270 && tempCol > 0)
                route[tempRow][tempCol - 1] = 0;
            else if(direction == 315 && (tempRow > 0 && tempCol > 0))
                route[tempRow - 1][tempCol - 1] = 0;
            
            if(direction == 0 && tempRow > 1)
                route[tempRow - 2][tempCol] = 0;
            else if(direction == 45 && (tempRow > 1 && tempCol < 47))
                route[tempRow - 2][tempCol + 2] = 0;
            else if(direction == 90 && tempCol < 47)
                route[tempRow][tempCol + 2] = 0;
            else if(direction == 135 && (tempRow < 31 && tempCol < 47))
                route[tempRow + 2][tempCol + 2] = 0;
            else if(direction == 180 && tempRow < 31)
                route[tempRow + 2][tempCol] = 0;
            else if(direction == 225 && (tempRow < 31 && tempCol > 1))
                route[tempRow + 2][tempCol - 2] = 0;
            else if(direction == 270 && tempCol > 1)
                route[tempRow][tempCol - 2] = 0;
            else if(direction == 315 && (tempRow > 1 && tempCol > 1))
                route[tempRow - 2][tempCol - 2] = 0;
            
        }

        // dodging at the base
        if(p.getRow() == baseRow && p.getCol() == baseCol) {
            for(Shot i : shotPos) {
                int rowDist = Math.abs(i.getRow() - p.getRow());
                int colDist = Math.abs(i.getCol() - p.getCol());
                int dist = Math.max(rowDist, colDist);
                                
                int[] pos = {0, 45, 90, 135, 180, 225, 270, 315};
                

//                
//                if(dist < 4 && (i.getRow() != p.getRow() + 1 && i.getCol() != p.getCol()) && i.getDirection() == Direction.getDirectionTowards(i.getRow(), i.getCol(), p.getRow() + 1, p.getCol()))
//                    pos[4] = -1;
//                if(dist < 4 && (i.getRow() != p.getRow() - 1 && i.getCol() != p.getCol()) && i.getDirection() == Direction.getDirectionTowards(i.getRow(), i.getCol(), p.getRow() - 1, p.getCol()))
//                    pos[5] = -1;
//                if(dist < 4 && (i.getRow() != p.getRow() + 1 && i.getCol() != p.getCol() + 1) && i.getDirection() == Direction.getDirectionTowards(i.getRow(), i.getCol(), p.getRow() + 1, p.getCol() + 1))
//                    pos[1] = -1;
//                if(dist < 4 && (i.getRow() != p.getRow() + 1 && i.getCol() != p.getCol() - 1) && i.getDirection() == Direction.getDirectionTowards(i.getRow(), i.getCol(), p.getRow() + 1, p.getCol() - 1))
//                    pos[0] = -1;
//                if(dist < 4 && (i.getRow() != p.getRow() - 1 && i.getCol() != p.getCol() + 1) && i.getDirection() == Direction.getDirectionTowards(i.getRow(), i.getCol(), p.getRow() - 1, p.getCol() + 1))
//                    pos[3] = -1;
//                if(dist < 4 && (i.getRow() != p.getRow() - 1 && i.getCol() != p.getCol() - 1) && i.getDirection() == Direction.getDirectionTowards(i.getRow(), i.getCol(), p.getRow() - 1, p.getCol() - 1))
//                    pos[2] = -1;
//                if(dist < 4 && (i.getRow() != p.getRow() && i.getCol() != p.getCol() + 1) && i.getDirection() == Direction.getDirectionTowards(i.getRow(), i.getCol(), p.getRow(), p.getCol() + 1))
//                    pos[6] = -1;
//                if(dist < 4 && (i.getRow() != p.getRow() && i.getCol() != p.getCol() - 1) && i.getDirection() == Direction.getDirectionTowards(i.getRow(), i.getCol(), p.getRow(), p.getCol() - 1))
//                    pos[7] = -1;                    
                

                
//                if(dist < 4 && i.getDirection() == Direction.getDirectionTowards(i.getRow(), i.getCol(), p.getRow(), p.getCol())) {
//                    System.out.println("EEEEE"+baseRow + " " + baseCol);
//                    for(int avail : pos) {
//                        System.out.println("FFFFF"+baseRow + " " + baseCol);
//                        if(avail != -1) 
//                            return new Action("M", avail);
//                    }
//                }
            }
                    

            
            int rowDist2 = Math.abs(baseRow - p.getRow());
            int colDist2 = Math.abs(baseCol - p.getCol());
            int dist2 = Math.max(rowDist2, colDist2);
            
            if(dist2 < 5 && p.getDirection() != Direction.getDirectionTowards(p.getRow(), p.getCol(), OGbRow, OGbCol)) {
                //System.out.println("TURNN?>>");
                return new Action("T", Direction.getDirectionTowards(p.getRow(), p.getCol(), OGbRow, OGbCol));
            }
            
            return new Action("S");
        }
        
        if(p.getDirection() != Direction.getDirectionTowards(p.getRow(), p.getCol(), OGbRow, OGbCol)) {
                //System.out.println("TURNN?>>");
            return new Action("T", Direction.getDirectionTowards(p.getRow(), p.getCol(), OGbRow, OGbCol));
        }
        
        
        // if we're not at base yet we google maps it
        Cell next = shortestPath(route, new int[]{p.getRow(), p.getCol()}, new int[]{baseRow, baseCol});
                
        if(next == null) {
            if(p.getRow() == baseRow && p.getCol() == baseCol)
                return new Action("S");
            
            int dirToBaseUn = Direction.getDirectionTowards(p.getRow(), p.getCol(),
                                   baseRow, baseCol);
            int[] nextSpace = Direction.getLocInDirection(p.getRow(),
                    p.getCol(), dirToBaseUn);
            if (b.isEmpty(nextSpace[0], nextSpace[1]))
                return new Action("M", dirToBaseUn);
            else
                return new Action("M", 
                        45* ((int) (Math.random() * 8)) );
        }
        
        if(p.getRow() == next.x && p.getCol() == next.y)
            return new Action("S");
        
        int dirToBase = Direction.getDirectionTowards(p.getRow(), p.getCol(),
                                   next.x, next.y);   
        return new Action("M", dirToBase);
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
//                System.out.println("There is no path.");
                
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
            //System.out.println("there is no path.");
            return null;
        } else {
            LinkedList<Cell> path = new LinkedList<>();
            p = dest;
            do {
                path.addFirst(p);
            } while ((p = p.prev) != null);
            //System.out.println(path + "SDFsdf");
            
            if(path.size() > 1)
                return path.get(1);
            //System.out.println(path.size());
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


