package me.DDoS.Quicksign.command;

import java.util.List;
import me.DDoS.Quicksign.QuickSign;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public abstract class QSCommand {
    
    protected QuickSign plugin;
    protected List<Sign> signs;

    public QSCommand(QuickSign plugin, List<Sign> signs) {
        
        this.plugin = plugin;
        this.signs = signs;
    
    }
    
    public abstract boolean run(Player player);
    
    public abstract void undo(Player player);
    
    public abstract void redo(Player player);
    
    protected void logChange(Player player, Sign sign) {

        if (plugin.getConsumer() == null) {
            
            return;
        
        }

        plugin.getConsumer().queueSignPlace(player.getName(), sign);

    }
    
}