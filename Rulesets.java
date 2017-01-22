/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package life;

/**
 * @author Julian
 */
public class Rulesets {
    
    public static final int RULESET_STANDARD = 0;
    public static final int RULESET_CUSTOM_1 = 1;
    public static final int RULESET_CUSTOM_2 = 2;
    public static final int RULESET_AGE = 3;
    public static final int RULESET_AGE_2 = 4;
    public static final int RULESET_AGE_CRAZY = 5;
    
    // Conway's Game of Life, standard ruleset
    public static void standardRulesetTick(boolean[][] cell, int[][] age, int leftMost, int rightMost, int upMost, int downMost) {
        boolean change[][] = new boolean[cell.length][cell[0].length];
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                int neighborCount = getLiveNeighborCount(cell, col, row);
                
                // STARVATION RULE
                if(cell[col][row] && neighborCount < 2) {
                    change[col][row] = true;
                }
                // OVERPOPULATION RULE
                else if(cell[col][row] && neighborCount > 3) {
                    change[col][row] = true;
                }
                
                // STABILITY RULE SHOULD BE IMPLICIT IF TOP TWO CONDITIONS FAILED
                
                // REPOPULATION RULE
                if(!cell[col][row] && neighborCount == 3) {
                    change[col][row] = true;
                }
            }
        }
        
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                if(change[col][row]) {
                    if(cell[col][row]) {
                        cell[col][row] = false;
                    age[col][row] = 0;
                    } else {
                        cell[col][row] = true;
                    age[col][row] = 0;
                    }
                    change[col][row] = false;
                    age[col][row] = 0;
                }
            }
        }
    }
    
    
    // 2, 5, 3 --- This ruleset "flourishes" and has few oscillations
    public static void customRulesetTick1(boolean[][] cell, int[][] age, int leftMost, int rightMost, int upMost, int downMost) {
        boolean change[][] = new boolean[cell.length][cell[0].length];
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                int neighborCount = getLiveNeighborCount(cell, col, row);
                
                // STARVATION RULE
                if(cell[col][row] && neighborCount < 2) {
                    change[col][row] = true;
                }
                // OVERPOPULATION RULE
                else if(cell[col][row] && neighborCount > 5) {
                    change[col][row] = true;
                }
                
                // STABILITY RULE SHOULD BE IMPLICIT IF TOP TWO CONDITIONS FAILED
                
                // REPOPULATION RULE
                if(!cell[col][row] && neighborCount == 3) {
                    change[col][row] = true;
                }
            }
        }
        
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                if(change[col][row]) {
                    if(cell[col][row]) {
                        cell[col][row] = false;
                    age[col][row] = 0;
                    } else {
                        cell[col][row] = true;
                    age[col][row] = 0;
                    }
                    change[col][row] = false;
                    age[col][row] = 0;
                }
            }
        }
    }
    
    // Custom ruleset 2
    public static void customRulesetTick2(boolean[][] cell, int[][] age, int leftMost, int rightMost, int upMost, int downMost) {
        boolean change[][] = new boolean[cell.length][cell[0].length];
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                int neighborCount = getLiveNeighborCount(cell, col, row);
                
                // STARVATION RULE
                if(cell[col][row] && neighborCount < 1) {
                    change[col][row] = true;
                }
                // OVERPOPULATION RULE
                else if(cell[col][row] && neighborCount > 3) {
                    change[col][row] = true;
                }
                
                // STABILITY RULE SHOULD BE IMPLICIT IF TOP TWO CONDITIONS FAILED
                
                // REPOPULATION RULE
                if(!cell[col][row] && neighborCount == 3) {
                    change[col][row] = true;
                }
            }
        }
        
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                if(change[col][row]) {
                    if(cell[col][row]) {
                        cell[col][row] = false;
                    age[col][row] = 0;
                    } else {
                        cell[col][row] = true;
                    age[col][row] = 0;
                    }
                    change[col][row] = false;
                    age[col][row] = 0;
                }
            }
        }
    }
    
    
    // Age-based ruleset
    public static int[][] ageRuleset(boolean[][] cell, int[][] age, int leftMost, int rightMost, int upMost, int downMost) {
        int ageLimit = 25;
        boolean change[][] = new boolean[cell.length][cell[0].length];
       for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                int neighborAge = getLiveNeighborAgeCount(cell, col, row, age);
                
                // STARVATION RULE
                if(cell[col][row] && neighborAge < 4) {
                    change[col][row] = true;
                }
                // OVERPOPULATION RULE
                else if(cell[col][row] && neighborAge > 28) {
                    change[col][row] = true;
                }
                
                // STABILITY RULE SHOULD BE IMPLICIT IF TOP TWO CONDITIONS FAILED
                
                // REPOPULATION RULE
                if(!cell[col][row] && neighborAge == 6) {
                    change[col][row] = true;
                }
            }
        }
        
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                if(change[col][row]) {
                    if(cell[col][row]) {
                        cell[col][row] = false;
                        age[col][row] = 0;
                    } else {
                        cell[col][row] = true;
                    }
                    change[col][row] = false;
                    age[col][row] = 0;
                }
            }
        }
        
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                if(cell[col][row]) {
                    if(age[col][row] >= ageLimit) {
                        age[col][row] = 0;
                        cell[col][row] = false;
                    } else {
                        age[col][row]++;
                    }
                }
            }
        }
        
        return age;
    }
    
    
    // Age-based ruleset
    public static int[][] ageRuleset2(boolean[][] cell, int[][] age, int leftMost, int rightMost, int upMost, int downMost) {
        int ageLimit = 50;
        boolean change[][] = new boolean[cell.length][cell[0].length];
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                int neighborCount = getLiveNeighborCount(cell, col, row);
                int neighborAge = getLiveNeighborAgeCount(cell, col, row, age);
                
                // STARVATION RULE
                if(cell[col][row] && ((neighborCount < 1 && age[col][row] > 10) || (neighborCount < 2 && age[col][row] > 30) || (neighborCount == 0 && age[col][row] > 3))) {
                    change[col][row] = true;
                }
                // OVERPOPULATION RULE
                else if(cell[col][row] && neighborCount > 3) {
                    change[col][row] = true;
                }
                
                // STABILITY RULE SHOULD BE IMPLICIT IF TOP TWO CONDITIONS FAILED
                
                // REPOPULATION RULE
                if(!cell[col][row] && neighborCount == 2 && neighborAge >= 6 && neighborAge <= 24) {
                    change[col][row] = true;
                }
            }
        }
        
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                if(change[col][row]) {
                    if(cell[col][row]) {
                        cell[col][row] = false;
                        age[col][row] = 0;
                    } else {
                        cell[col][row] = true;
                    }
                    change[col][row] = false;
                    age[col][row] = 0;
                }
            }
        }
        
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                if(cell[col][row]) {
                    if(age[col][row] >= ageLimit) {
                        age[col][row] = 0;
                        cell[col][row] = false;
                    } else {
                        age[col][row]++;
                    }
                }
            }
        }
        
        return age;
    }
    
    
    // This was so ridiculous I had to save it :)
    public static int[][] ageRulesetCrazy(boolean[][] cell, int[][] age, int leftMost, int rightMost, int upMost, int downMost) {
        boolean change[][] = new boolean[cell.length][cell[0].length];
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                int neighborAge = getLiveNeighborAgeCount(cell, col, row, age);
                
                // STARVATION RULE
                if(cell[col][row] && neighborAge < 3) {
                    change[col][row] = true;
                }
                // OVERPOPULATION RULE
                else if(cell[col][row] && neighborAge > 18) {
                    change[col][row] = true;
                }
                
                // STABILITY RULE SHOULD BE IMPLICIT IF TOP TWO CONDITIONS FAILED
                
                // REPOPULATION RULE
                if(!cell[col][row] && neighborAge == 0) {
                    change[col][row] = true;
                }
            }
        }
        
        for(int col = leftMost; col < rightMost; col++) {
            for(int row = upMost; row < downMost; row++) {
                if(change[col][row]) {
                    if(cell[col][row]) {
                        cell[col][row] = false;
                        age[col][row] = 0;
                    } else {
                        cell[col][row] = true;
                    }
                    change[col][row] = false;
                    age[col][row] = 0;
                }
            }
        }
        
        for(int col = 0; col < cell.length; col++) {
            for(int row = 0; row < cell[col].length; row++) {
                age[col][row]++;
            }
        }
        
        return age;
    }
    
    
    private static int getLiveNeighborAgeCount(boolean[][] cell, int col, int row, int[][] age) {
        int neighborAge = 0;
        
        // Check for ArrayOutOfBoundsExceptions
        
        // Check left side
        if(col != 0) {
            if(cell[col-1][row]) neighborAge += age[col-1][row] + 1; // check LEFT (1)
            if(row != 0) {
                if(cell[col-1][row-1]) neighborAge += age[col-1][row-1] + 1; // check UPLEFT (2)
            }
            if(row != cell[col].length-1) {
                if(cell[col-1][row+1]) neighborAge += age[col-1][row+1] + 1; // check DOWNLEFT (3)
            }
        }
        
        // Check right side
        if(col != cell.length-1) { // if we can check the right side
            if(cell[col+1][row]) neighborAge += age[col+1][row]; // check RIGHT (4)
            if(row != 0) {
                if(cell[col+1][row-1]) neighborAge += age[col+1][row-1] + 1; // check UPRIGHT (5)
            }
            if(row != cell[col].length-1) {
                if(cell[col+1][row+1]) neighborAge += age[col+1][row+1] + 1; // check DOWNRIGHT (6)
            }
        }
        
        // Check top
        if(row != 0) { // if we can check the top
            if(cell[col][row-1]) neighborAge += age[col][row-1] + 1; // check UP (7)
        }
        
        // Check bottom
        if(row != cell[col].length-1) {
            if(cell[col][row+1]) neighborAge += age[col][row+1] + 1; // check DOWN (8)
        }
        
        return neighborAge;
    }
    
    private static int getLiveNeighborCount(boolean[][] cell, int col, int row) {
        int neighbors = 0;
        
        // Check for ArrayOutOfBoundsExceptions
        
        // Check left side
        if(col != 0) {
            if(cell[col-1][row]) neighbors++; // check LEFT (1)
            if(row != 0) {
                if(cell[col-1][row-1]) neighbors++; // check UPLEFT (2)
            }
            if(row != cell[col].length-1) {
                if(cell[col-1][row+1]) neighbors++; // check DOWNLEFT (3)
            }
        }
        
        // Check right side
        if(col != cell.length-1) { // if we can check the right side
            if(cell[col+1][row]) neighbors++; // check RIGHT (4)
            if(row != 0) {
                if(cell[col+1][row-1]) neighbors++; // check UPRIGHT (5)
            }
            if(row != cell[col].length-1) {
                if(cell[col+1][row+1]) neighbors++; // check DOWNRIGHT (6)
            }
        }
        
        // Check top
        if(row != 0) { // if we can check the top
            if(cell[col][row-1]) neighbors++; // check UP (7)
        }
        
        // Check bottom
        if(row != cell[col].length-1) {
            if(cell[col][row+1]) neighbors++; // check DOWN (8)
        }
        
        return neighbors;
    }
}
