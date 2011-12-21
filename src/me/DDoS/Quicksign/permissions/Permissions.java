package me.DDoS.Quicksign.permissions;

import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public interface Permissions {
    
    public boolean hasPermission(Player player, String perm);
    
}