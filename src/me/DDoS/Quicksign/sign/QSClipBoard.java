package me.DDoS.Quicksign.sign;

/**
 *
 * @author DDoS
 */
public class QSClipBoard {
    
    private String line1;
    
    private String line2;
    
    private String line3;
    
    private String line4;
    
    public QSClipBoard(String line1, String line2, String line3, String line4) {
        
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line4 = line4;
        
    }
    
    public String getLine(int lineNum) {
        
        switch (lineNum) {
            
            case 1:
                return line1;
                
            case 2:
                return line2;
                
            case 3:
                return line3;
                
            case 4:
                return line4;
                
            default:
                return "";
        }   
    }
    
    public String[] getAllLines() {
        
        String[] allLines = new String[4];
        
        allLines[0] = line1;
        allLines[1] = line2;
        allLines[2] = line3;
        allLines[3] = line4;
        
        return allLines;
        
    }
}