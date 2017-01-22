/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

/*****************************
 * John Conway's Game of Life
 * Implemented from scratch
 * @author Julian Monticelli
 *****************************/

package life;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Life extends JFrame {
    private static final int PROG_VER_MAJ = 1;
    private static final int PROG_VER_MIN = 4;
    private static final int PROG_VER_SUB = 2;
    private static final String PRIMARY_NEW_FEATURE = "Now with new Rulesets!";
    private static final String PROGRAM_TITLE = ".:. LifeSim v" + PROG_VER_MAJ + '.' + PROG_VER_MIN + '.' + PROG_VER_SUB + "  |  " + PRIMARY_NEW_FEATURE;
    
    // Program globals
    public static int TICKS_PER_SECOND = 20;
    public static boolean RUNNING = true;
    public static JLabel jl;
    TilePanel tp = new TilePanel();
    JMenuBar menuBar = new JMenuBar();
    
    public Life() {
        JMenu file = new JMenu("File");
        // Clear function
        JMenuItem clear = new JMenuItem("Clear");
        file.add(clear);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tp.clear();
            }
        });
        JMenuItem save = new JMenuItem("Save...");
        file.add(save);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Select file...");
                JFileChooser jfc = new JFileChooser();
                FileNameExtensionFilter fef = new FileNameExtensionFilter("Game of Life files (*.cgl)", "cgl");
                jfc.setFileFilter(fef);
                jfc.setApproveButtonText("Save");
                frame.add(jfc);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //frame.setLocationRelativeTo(null);
                //frame.pack();
                frame.setVisible(false);
                File file;
                if(jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    file = jfc.getSelectedFile();
                    if(!file.getPath().endsWith(".cgl")) {
                        file = new File(file.getPath() + ".cgl");
                    }
                    frame.setVisible(false);
                    frame.setEnabled(false);
                } else {
                    frame.setVisible(false);
                    frame.setEnabled(false);
                    return;
                }
                try {
                    tp.save(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem load = new JMenuItem("Load...");
        file.add(load);
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Select file...");
                JFileChooser jfc = new JFileChooser();
                FileNameExtensionFilter fef = new FileNameExtensionFilter("Game of Life files (*.cgl)", "cgl");
                jfc.setFileFilter(fef);
                frame.add(jfc);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //frame.pack();
                frame.setVisible(false);
                //frame.setLocationRelativeTo(null);
                File file;
                if(jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    file = jfc.getSelectedFile();
                    if(!file.getPath().endsWith(".cgl")) {
                        file = new File(file.getPath() + ".cgl");
                    }
                    frame.setVisible(false);
                    frame.setEnabled(false);
                } else {
                    frame.setVisible(false);
                    frame.setEnabled(false);
                    return;
                }
                try {
                    tp.load(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        menuBar.add(file);
        
        // -- EDIT MENU -- //
        JMenu edit = new JMenu("Edit");
        
        // Cell colors //
        JMenu cellColor = new JMenu("Live cell colors");
        edit.add(cellColor);
        JMenuItem purple = new JMenuItem("Purple");
        purple.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setColor(TilePanel.PURPLE);
           }
        });
        JMenuItem green = new JMenuItem("Green");
        green.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setColor(java.awt.Color.GREEN);
           }
        });
        JMenuItem blue = new JMenuItem("Blue");
        blue.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setColor(java.awt.Color.BLUE);
           }
        });
        JMenuItem yellow = new JMenuItem("Yellow");
        yellow.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setColor(java.awt.Color.YELLOW);
           }
        });
        JMenuItem orange = new JMenuItem("Orange");
        orange.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setColor(java.awt.Color.ORANGE);
           }
        });
        JMenuItem white = new JMenuItem("White");
        white.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setColor(java.awt.Color.WHITE);
           }
        });
        JMenuItem pink = new JMenuItem("Pink");
        pink.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setColor(java.awt.Color.PINK);
           }
        });
        JMenuItem brown = new JMenuItem("Brown");
        brown.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setColor(TilePanel.BROWN);
           }
        });
        cellColor.add(purple);
        cellColor.add(green);
        cellColor.add(blue);
        cellColor.add(yellow);
        cellColor.add(orange);
        cellColor.add(white);
        cellColor.add(pink);
        cellColor.add(brown);
        
        // Random Fill... //
        JMenuItem randFill = new JMenuItem("Random Fill...");
        randFill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Initialize variables
                JOptionPane optionPane = new JOptionPane();
                JSlider slider = getSlider(optionPane);
                JFrame frame = new JFrame();
                JDialog dialog = optionPane.createDialog(optionPane, "Choose fill percentage...");
                
                // Set up Option Pane
                optionPane.setMessage(new Object[] {"Select a percentage value: ", slider} );
                optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
                                
                
                // Set JDialog to visible
                dialog.pack();
                dialog.setVisible(true);
                int fill;
                if((int)optionPane.getValue() == JOptionPane.OK_OPTION) { // Make sure OK button was pressed
                    // Handle return value (make sure uninitializedValue doesn't get returned)
                    String returnVal = "" + optionPane.getInputValue();
                    if(returnVal.equals("uninitializedValue")) returnVal = "25";
                    fill = Integer.parseInt(returnVal);
                } else // Otherwise
                    return; // Don't do anything...
                // Actual logic
                tp.randomBoard(fill);
            }
        });
        edit.add(randFill);
        menuBar.add(edit);
        
        // -- END EDIT MENU -- //
        
        // -- OPTIONS MENU -- //
        
        JMenu options = new JMenu("Options");
        
        JMenu updateOutside = new JMenu("Update outside of view");
        
        JMenuItem setTrue = new JMenuItem("True (May be slow)");
        setTrue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tp.setUpdateOutsideOfScreen(true);
            }
        });
        JMenuItem setFalse = new JMenuItem("False (Faster - Default)");
        setFalse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tp.setUpdateOutsideOfScreen(false);
            }
        });
        
        JMenu rules = new JMenu("Rules");
        JMenuItem conway = new JMenuItem("Conway's Game of Life (Standard Ruleset)");
        rules.add(conway);
        conway.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setCurrentRuleset(Rulesets.RULESET_STANDARD);
           }
        });
        JMenuItem custom1 = new JMenuItem("\"Flourish\" (Standard-Custom Ruleset)");
        rules.add(custom1);
        custom1.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setCurrentRuleset(Rulesets.RULESET_CUSTOM_1);
           }
        });
        JMenuItem custom2 = new JMenuItem("Custom 2 (Standard-Custom Ruleset)");
        rules.add(custom2);
        custom2.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setCurrentRuleset(Rulesets.RULESET_CUSTOM_2);
           }
        });
        JMenuItem age = new JMenuItem("Age Ruleset (Custom Ruleset)");
        rules.add(age);
        age.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setCurrentRuleset(Rulesets.RULESET_AGE);
           }
        });
        JMenuItem age2 = new JMenuItem("Age Ruleset 2 (Custom Ruleset)");
        rules.add(age2);
        age2.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setCurrentRuleset(Rulesets.RULESET_AGE_2);
           }
        });
        JMenuItem ageCrazy = new JMenuItem("Inversion-Age Ruleset (Custom Ruleset)");
        rules.add(ageCrazy);
        ageCrazy.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               tp.setCurrentRuleset(Rulesets.RULESET_AGE_CRAZY);
           }
        });
        menuBar.add(options);
        options.add(rules);
        options.add(updateOutside);
        updateOutside.add(setTrue);
        updateOutside.add(setFalse);
        
        
        JMenu cellSize = new JMenu("Cell size");
        JMenuItem xSmall = new JMenuItem("Extra small");
        xSmall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tp.setCellSize(TilePanel.CELL_SIZE_X_SMALL);
            }
        });
        cellSize.add(xSmall);
        JMenuItem small = new JMenuItem("Small");
        small.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tp.setCellSize(TilePanel.CELL_SIZE_SMALL);
            }
        });
        cellSize.add(small);
        JMenuItem medium = new JMenuItem("Medium (Default)");
        medium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tp.setCellSize(TilePanel.CELL_SIZE_MEDIUM);
            }
        });
        cellSize.add(medium);
        JMenuItem large = new JMenuItem("Large");
        large.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tp.setCellSize(TilePanel.CELL_SIZE_LARGE);
            }
        });
        cellSize.add(large);
        JMenuItem xLarge = new JMenuItem("Extra large");
        xLarge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tp.setCellSize(TilePanel.CELL_SIZE_X_LARGE);
            }
        });
        cellSize.add(xLarge);
        
        options.add(cellSize);
        
        // -- END OPTIONS MENU -- //
        
        
        // -- HELP MENU -- //
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        help.add(about);
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame about = new JFrame("About");
                JTextArea text = new JTextArea();
                text.setPreferredSize(new Dimension(400, 400));
                text.setEditable(false);
                text.setText("  This program was created by Julian Monticelli (C) 2016-2017\n\nControls:\n\n" +
                             "  RETURN/SPACE:\tPause/Resume simulation\n" +
                             "  UP/DOWN ARROW:\tSpeed up/slow down simulation\n" +
                             "  ESCAPE KEY:\t\tExit simulation.\n" +
                             "  LEFT MOUSE:\t\tBring cell to life\n" +
                             "  RIGHT MOUSE:\tKill cell\n\n\n\n" //+
                             //"  Like the program? Check out the source at:\n" + 
                             //"  https://github.com/dddJewelsbbb/Conway-s-Game-of-Life-Java-/"
                        );
                about.add(text);
                about.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                about.pack();
                about.setVisible(true);
            }
        });
        menuBar.add(help);
        
        // -- END HELP MENU -- //
        
        // -- SIMULATION STEP COUNTER -- //
        jl = new JLabel();
        jl.setText(" |   Current simulation step: 0");
        jl.setVisible(true);
        menuBar.add(jl);
        
        
        this.setTitle(PROGRAM_TITLE);
        this.setResizable(true); // Frame will fit EXACTLY to JPanel
        this.setJMenuBar(menuBar);
        // New addition
        this.setLayout(new BorderLayout());
        // End new addition
        this.add(tp); 
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        for (Component comp : getComponents()) {
//            comp.requestFocusInWindow();
//        }
        this.pack();
        this.setVisible(true);
    }
    
    
    public void start() {
        tp.run();
    }
    
    
    public static void main(String[] args) {
        assert TICKS_PER_SECOND <= 60; // needs -ea flag for assert to work:\ will fix this later
        Life life = new Life();
        life.start();
    }
    
    public static void kill() {
        RUNNING = false; // probably unneccessary
        System.exit(0);
    }
    
    private static JSlider getSlider(JOptionPane optionPane) {
        // Set up Slider
        JSlider slider = new JSlider();
        
        slider.setMajorTickSpacing(25); // Show big ticks every 25 increments
        slider.setMinorTickSpacing(5); // Show minor tick marks every 5
        slider.setPaintTicks(true); // Paint ticks
        slider.setPaintLabels(true); // Paint value labels of major values
        
        // Create ChangeListener
        ChangeListener cl = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                JSlider scSlider = (JSlider)changeEvent.getSource();
                if(!scSlider.getValueIsAdjusting()) {
                    optionPane.setInputValue(new Integer(scSlider.getValue()));
                }
            }
        };
        slider.setValue(25);
        slider.addChangeListener(cl);
        return slider;
    }
}
