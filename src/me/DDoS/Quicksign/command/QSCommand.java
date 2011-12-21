package me.DDoS.Quicksign.command;

import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public interface QSCommand {
    
    public boolean run(Player player);
    
    public void undo(Player player);
    
    public void redo(Player player);
    
}