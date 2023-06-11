package arena;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * This class is a subclass of JPanel and handles the actual play area of the game
 * (not the buttons or labels).
 * @author Thomas Weisswange
 */
public class FieldPanel extends javax.swing.JPanel {

    private Board myBoard;
    /**
     * Constructs a new FieldPanel.
     */
    public FieldPanel() {
        initComponents();
    }

    /**
     * Getter function for the Board object corresponding to this FieldPanel.
     * @return this FieldPanel's Board
     */
    public Board getMyBoard() {
        return myBoard;
    }

    /**
     * Setter function for the Board object corresponding to this FieldPanel.
     * @param myBoard the Board to be assigned to this FieldPanel
     */
    public void setMyBoard(Board myBoard) {
        this.myBoard = myBoard;
    }
    
    /**
     * Draws the current state of this FieldPanel's Board into its graphics
     * context.
     * @param g1 the Graphics context for this Board
     */
    @Override
    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        //Draw grid
        g.draw(new Rectangle2D.Double(0,0,1000,660));
        for (int x = 0; x <= 1000; x += 20)
            g.draw(new Line2D.Double(x,0,x,660));
        for (int y = 0; y <= 660; y += 20)
            g.draw(new Line2D.Double(0,y,1000,y));
        if (myBoard == null)
            return;
        //Draw elements
        for (int r = 0; r < 33; r++) {
            for (int c = 0; c < 50; c++) {
                Occupant occ = myBoard.get(r, c);
                if (occ == null)
                    continue;
                if (occ instanceof Blocker)
                    drawBlocker(g, r, c);
                if (occ instanceof Base)
                    drawBase(g, r, c, ((Base) occ).getTeam());
                if (occ instanceof Shot)
                    drawShot(g, r, c, ((Shot) occ).getTeam(),
                        ((Shot) occ).getDirection());
                if (occ instanceof Player) {
                    Color color;
                    try {
                        color = ((Player) occ).getController().getColor();
                    } catch (Exception e) {
                        color = Color.GRAY;
                    }
                    drawPlayer(g, r, c, 
                            ((Player) occ).getTeam(),
                            ((Player) occ).getDirection(), color);
                }
            }
        }
    }
    
    /**
     * Draws a Blocker (gray square) into the appropriate space on the Board
     * @param g this Board's Graphics context
     * @param r the row of the Blocker's location
     * @param c the column of the Blocker's location
     */
    private void drawBlocker(Graphics2D g, int r, int c) {
        //System.out.println("block: row " + r + ", col " + c);
        g.setPaint(Color.GRAY);
        g.fill(new Rectangle2D.Double(c*20 + 1, r*20 + 1,
                        19, 19));
    }
    
    /**
     * Draws a Base into the appropriate space on the Board, in that team's color
     * @param g this Board's Graphics context
     * @param r the row of the Base's location
     * @param c the column of the Base's location
     * @param team the team to whom this Base belongs
     */
    private void drawBase(Graphics2D g, int r, int c, int team) {
        if (team == 1)
            g.setPaint(Color.BLACK);
        if (team == 2)
            g.setPaint(Color.RED);
        int x0 = c * 20;
        int y0 = r * 20;
        g.fill(new Rectangle2D.Double(x0 + 2, y0 + 2, 5, 5));
        g.fill(new Rectangle2D.Double(x0 + 14, y0 + 2, 5, 5));
        g.fill(new Rectangle2D.Double(x0 + 2, y0 + 14, 5, 5));
        g.fill(new Rectangle2D.Double(x0 + 14, y0 + 14, 5, 5));
        g.fill(new Rectangle2D.Double(x0 + 4, y0 + 4, 13, 13));
    }
    
    /**
     * Draws a Shot into the appropriate space on the Board, in that team's
     * color, pointing in the appropriate direction.
     * @param g this Shot's Graphics context
     * @param r the row of the Shot's location
     * @param c the column of the Shot's location
     * @param team the team to whom this Shot belongs
     * @param direction the direction in which this Shot is pointing
     */
    private void drawShot(Graphics2D g, int r, int c, int team,
            int direction) {
        if (team == 1)
            g.setPaint(Color.BLACK);
        if (team == 2)
            g.setPaint(Color.RED);
        int x0 = c * 20 + 10;
        int y0 = r * 20 + 10;
        g.fill(new Ellipse2D.Double(x0 - 4, y0 - 4, 9, 9));
        int dir = Direction.roundTo8(direction) / 45;
        int[] deltaX = {0, 6, 7, 6, 0, -6, -7, -6};
        int[] deltaY = {-7, -6, 0, 6, 7, 6, 0, -6};
        g.draw(new Line2D.Double(x0, y0, 
                x0+deltaX[dir], y0+deltaY[dir]));
    }
    
    /**
     * Draws a Player into the appropriate space on the Board, pointing in the
     * specified direction. The team determines the border color for the Player,
     * while the color passed as an argument determines the fill color for the
     * Player.
     * @param g this Player's Graphics context
     * @param r the row of the Player's location
     * @param c the column of the Player's location
     * @param team the team to whom this Player belongs
     * @param direction the direction in which this Player is facing
     * @param color the color of this Player
     */
    private void drawPlayer(Graphics2D g, int r, int c,
            int team, int direction, Color color) {
        if (team == 1)
            g.setPaint(Color.BLACK);
        if (team == 2)
            g.setPaint(Color.RED);
        
        int x0 = c * 20;
        int y0 = r * 20;
        int dir = Direction.roundTo8(direction) / 45;
        int[][] deltaX = { {5, 10, 15}, {2, 8, 17},
            {2, 2, 18}, {2, 8, 17}, {5, 10, 15},
            {3, 12, 18}, {2, 18, 18}, {3, 12, 18}};
        int[][] deltaY = { {18, 2, 18}, {12, 18, 3},
            {5, 15, 10}, {8, 2, 17}, {2, 18, 2}, 
            {17, 2, 8}, {10, 5, 15}, {2, 18, 12}};
        int[] xCoords = new int[3];
        int[] yCoords = new int[3];
        for (int i = 0; i < 3; i++) {
            xCoords[i] = x0 + deltaX[dir][i];
            yCoords[i] = y0 + deltaY[dir][i];
        }
        
        Polygon p = new Polygon(xCoords, yCoords, 3);
        g.setPaint(color);
        g.fill(p);
        
        if (team == 1)
            g.setPaint(Color.BLACK);
        if (team == 2)
            g.setPaint(Color.RED);
        g.draw(p);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1001, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 621, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
