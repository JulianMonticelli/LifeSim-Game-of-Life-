/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package life;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Julian
 */
class TilePanel extends JPanel {
    
    private int leftMostTile = 0;
    private int upMostTile = 0;
    private int endTileRight;
    private int endTileDown;
    
    public static final int CELL_SIZE_X_SMALL = 3;
    public static final int CELL_SIZE_SMALL = 5;
    public static final int CELL_SIZE_MEDIUM = 7;
    public static final int CELL_SIZE_LARGE = 9;
    public static final int CELL_SIZE_X_LARGE = 11;
    
    public static Color BROWN = Color.decode("#8B4513");
    public static Color PURPLE = Color.decode("#9932CC");
    
    private int sizeOfCell = 7;
    private static final int numColumns = 1000; // X -- max on my res is about 250
    private static final int numRows = 1000; // Y   -- max on my res is like about 140
    private static final int sizeOfGameX = 1024;
    private static final int sizeOfGameY = 768;
    
    private int currentRuleset = Rulesets.RULESET_STANDARD; // Standard ruleset
    
    private Color liveColor;
    
    private int simulationStep;
    private boolean cell[][] = new boolean[numColumns][numRows];
    private int age[][] = new int[numColumns][numRows];
    private boolean paused;
    private boolean updateOutsideOfScreen;
    
    //private ActionListener al = null; // will be sent from main class
    
    public TilePanel() {
        simulationStep = 0;
        liveColor = Color.GREEN;
        paused = true; // start game paused
        updateOutsideOfScreen = false; // Turned off initially for performance
        this.setPreferredSize(new Dimension(sizeOfGameX, sizeOfGameY)); // set PREFERRED size so that pack() works
        this.setFocusable(true); // set focusable so our KeyAdapter (KeyListener) works
        //this.requestFocusInWindow();
        ///*
        // Add mouse motion listener (drag operations, 
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX() / sizeOfCell;
                int y = e.getY() / sizeOfCell;
                int button = e.getButton();
                
                // Determine what action is to be performed
                boolean action;
                if(SwingUtilities.isLeftMouseButton(e)) action = true;
                else action = false;
                
                // Check array bounds, because dragging offscreen will cause AOBE
                if (x >= 0 && x < cell.length && y >= 0 && y < cell[0].length) {
                    cell[x][y] = action;
                    age[x][y] = 0;
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // Do nothing
            }
        });
        //*/
        
        // Add MouseListener (via MouseAdapter)
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println("Mouse clicked x: " + e.getX() + " y: " + e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / sizeOfCell;
                int y = e.getY() / sizeOfCell;
                
                // Determine what action is to be performed
                boolean action;
                if(SwingUtilities.isLeftMouseButton(e)) action = true;
                else action = false;
                
                cell[x][y] = action;
                age[x][y] = 0;
                repaint();
            }
            // Not working / todo
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                System.out.println("Something happened here...\n");
                int notches = e.getWheelRotation();
                if(notches < 0) {
                    System.out.println("Mouse wheel moved up " + -notches + " notches \n");
                } else {
                    
                    System.out.println("Mouse wheel moved down " + notches + " notches \n");
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                //System.out.println("Mouse released x: " + e.getX() + " y: " + e.getY());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //System.out.println("Mouse entered x: " + e.getX() + " y: " + e.getY());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //System.out.println("Mouse exited x: " + e.getX() + " y: " + e.getY());
            }
        });
        
        // Add KeyListener (via KeyAdapter)
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //System.out.println("Key typed: " + e.getKeyChar() + "(" + e.getKeyCode() + ")");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("Key pressed: " + e.getKeyChar() + "(" + e.getKeyCode() + ")");
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    Life.kill();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (paused) paused = false;
                    else paused = true;
                } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_PLUS) {
                    System.out.println("Game speed set to " + ++Life.TICKS_PER_SECOND + " ticks per second.");
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_MINUS) {
                    if(Life.TICKS_PER_SECOND > 1)
                        System.out.println("Game speed set to " + --Life.TICKS_PER_SECOND + " ticks per second.");
                    else
                        System.out.println("Can't set game speed to 0 ticks per second... division by 0, duh!");
                }  
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("Key released: " + e.getKeyChar() + "(" + e.getKeyCode() + ")");
            }
        });
    }
    
    public void setCellSize(int cellSize) {
        sizeOfCell = cellSize;
        repaint();
    }
    
    public int getCellSize() {
        return sizeOfCell;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int liveColorMult = 0x400;
        if (liveColor == Color.GREEN) {
            liveColorMult = 0x400;
        } else if (liveColor == BROWN){
            liveColorMult = 0x02; // Why can't I do this correctly? Color doesn't update properly with the new brown
        } else if (liveColor == PURPLE) {
            liveColorMult = 0x20;
        }
        int width = this.getSize().width;
        int height = this.getSize().height;
        endTileRight = (width / sizeOfCell) + ((width % sizeOfCell > 0) ? 1 : 0) + leftMostTile; //
        endTileDown = (height / sizeOfCell) + ((height % sizeOfCell > 0) ? 1 : 0) + upMostTile; //
        for(int col = leftMostTile; col < endTileRight; col++) {
            for(int row = upMostTile; row < endTileDown; row++) {
                if(cell[col][row]) { // if true i.e. it's a LIVE cell
                    g.setColor(new Color(liveColor.getRGB() - (age[col][row]*liveColorMult) )); // This color thing isn't final...
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(col*sizeOfCell, row*sizeOfCell, sizeOfCell, sizeOfCell);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(col*sizeOfCell, row*sizeOfCell, sizeOfCell, sizeOfCell);
            }
        }
        
    }
    
    // GAME LOOP
    protected void run() {        
        while(Life.RUNNING) {
            while(paused) {
                try {
                    Thread.sleep(10); // just sleep for 10ms bc why wouldn't you
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            long startTime = System.currentTimeMillis();
            
            // Perform simulation logic
            tick();
            updateSimStep();
            
            // Repaint the screen to 
            repaint();
            
            long timeDiff = System.currentTimeMillis() - startTime;
            
            // Make sure we only process TICKS_PER_SECOND steps in a second
            if(timeDiff < (1000L/Life.TICKS_PER_SECOND)) {
                try {
                    Thread.sleep((1000L/Life.TICKS_PER_SECOND) - timeDiff);
                } catch (InterruptedException e) {
                    // do nothing because I really don't give a fudge
                }
            }
        }
    }

    private void tick() {
        int l, r, u, d;
        if(!updateOutsideOfScreen) {
            l = leftMostTile;
            r = endTileRight;
            u = upMostTile;
            d = endTileDown;
        } else {
            l = 0;
            r = cell[0].length;
            u = 0;
            d = cell.length;
        }
        switch(currentRuleset) {
            case Rulesets.RULESET_STANDARD:
                Rulesets.standardRulesetTick(cell, age, l, r, u, d);
                break;
            case Rulesets.RULESET_CUSTOM_1:
                Rulesets.customRulesetTick1(cell, age, l, r, u, d);
                break;
            case Rulesets.RULESET_CUSTOM_2:
                Rulesets.customRulesetTick2(cell, age, l, r, u, d);
                break;
            case Rulesets.RULESET_AGE:
                age = Rulesets.ageRuleset(cell, age, l, r, u, d);
                break;
            case Rulesets.RULESET_AGE_2:
                age = Rulesets.ageRuleset2(cell, age, l, r, u, d);
                break;
            case Rulesets.RULESET_AGE_CRAZY:
                age = Rulesets.ageRulesetCrazy(cell, age, l, r, u, d);
                break;
            default:
                Rulesets.standardRulesetTick(cell, age, l, r, u, d);
                break;
        }
    }
    
    public void setCurrentRuleset(int ruleset) {
        currentRuleset = ruleset;
    }

    protected void clear() { // Called by JMenuItem File>Clear
        paused = true; // Pause sim
        simulationStep = 0; // Set sim step to 0
        Life.jl.setText(" |   Current simulation step: " + simulationStep);
        simulationStep = 0;
        for(int col = 0; col < cell.length; col++) {
            for(int row = 0; row < cell[col].length; row++) {
                cell[col][row] = false;
                age[col][row] = 0;
            }
        }
        repaint();
    }

    protected void setColor(Color color) {
        liveColor = color;
        repaint();
    }
    
    protected void randomBoard(int percentChance) {
        clear(); // Call clear first for age
        assert percentChance <= 100; // 101+% chance of an event is not a real thing - needs -ea
        int colStart, colEnd, rowStart, rowEnd;
        if(!updateOutsideOfScreen) {
            colStart = leftMostTile;
            rowStart = upMostTile;
            colEnd = endTileRight;
            rowEnd = endTileDown;
        } else {
            colStart = rowStart = 0;
            colEnd = cell.length;
            rowEnd = cell[0].length;
        }
        Random rand = new Random();
        for(int col = colStart; col < colEnd; col++) {
            for(int row = rowStart; row < rowEnd; row++) {
                boolean alive = false;
                int roll = rand.nextInt(100);
                if(roll < percentChance) { // 0 through 24 == 25
                    alive = true;
                    age[col][row] = 1;
                }
                cell[col][row] = alive;
            }
        }
        repaint();
    }
    
    protected void setUpdateOutsideOfScreen(boolean bool) {
        updateOutsideOfScreen = bool;
    }
    protected boolean getUpdateOutsideOfScreen() {
        return updateOutsideOfScreen;
    }
    
    protected void save(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        int streamSize = (numRows * numColumns / 8);
        byte[] stream = new byte[streamSize];
        int byteCount = 0;
        byte currentByte = 0x00;
        int currentByteInt = 0;
        int numBits = 0;
        paused = true; // MAKE SURE we're paused
        for(int col = 0; col < cell.length; col++) {
            for(int row = 0; row < cell[col].length; row++) {
                if(cell[col][row]) {
                    currentByteInt += 1 << numBits; // If alive, set LSB to 1
                }
                if(++numBits == 8) { // If we have a full byte after incrementing the count
                    currentByte = (byte)currentByteInt;
                    currentByteInt = 0;
                    stream[byteCount++] = currentByte; // Add byte to stream
                    numBits = 0; // Reset numBits
                }
                
            }
        }
        if(numBits > 0) { // In the middle of writing the byte, but not done?
            while(numBits < 8) {
                currentByteInt *= 2; // Bitshift to left
                numBits++;
            }
            currentByte = (byte)currentByteInt;
            stream[byteCount] = currentByte;
        }
        fos.write(stream);
        fos.close();
    }
    
    protected void load(File file) throws FileNotFoundException, IOException {
        clear();
        FileInputStream fileIn = new FileInputStream(file);
        char currentByte = 0x00;
        int numBits = 0;
        boolean[] cells = new boolean[8];
        paused = true; // MAKE SURE we're paused
        for(int col = 0; col < cell.length; col++) {
            for(int row = 0; row < cell[col].length; row++) {
                if(numBits == 0) {
                    currentByte = (char)fileIn.read(); // Grab new byte
                    for(int i = 0; i < 8; i++) {
                        if(currentByte % 2 == 1) {
                            cells[7-i] = true;
                            currentByte-=1;
                        } else {
                            cells[7-i] = false;
                        }
                        currentByte /= 2;
                    }
                }
                cell[col][row] = cells[7-numBits];
                numBits++;
                if(numBits == 8) numBits = 0; // If at 8 bits, reset to 0
            }
        }
        repaint();
    }
    private void updateSimStep() {
        simulationStep++; // simulationStep
        Life.jl.setText(" |   Current simulation step: " + simulationStep);
    }
}
