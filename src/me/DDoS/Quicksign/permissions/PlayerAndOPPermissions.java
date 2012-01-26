package me.DDoS.Quicksign.permissions;

import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class PlayerAndOPPermissions implements Permissions {

    private final List<String> playerPerms;
    
    public PlayerAndOPPermissions(List<String> playerPerms) {
        
        this.playerPerms = playerPerms;
        
    }
    
    @Override
    public boolean hasPermission(Player player, String perm) {
     
        if (player.isOp()) {
            
            return true;
            
        } else {
            
            if (playerPerms != null && playerPerms.contains(perm)) {
                
                return true;
                
            }            
        }
        
        return false;
        
    }   
}
