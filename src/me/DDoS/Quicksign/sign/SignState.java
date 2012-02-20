package me.DDoS.Quicksign.sign;

import org.bukkit.block.Sign;

/**
 *
 * @author DDoS
 */
public class SignState {
    
    private final String[] signState = new String[4];
    
    public SignState(Sign sign) {
        
        signState[0] = sign.getLine(0);
        signState[1] = sign.getLine(1);
        signState[2] = sign.getLine(2);
        signState[3] = sign.getLine(3);
        
    }
    
    public String[] getLines() {
        
        return signState;
        
    }
}
