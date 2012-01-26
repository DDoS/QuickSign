package me.DDoS.Quicksign.session;

import java.util.List;
import me.DDoS.Quicksign.QuickSign;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public abstract class QSEditSession {
    
    protected final Player player;
    protected final QuickSign plugin;
    
    protected QSEditSession(Player player, QuickSign plugin) {
        
        this.player = player;
        this.plugin = plugin;
        
    }
    
    public Player getPlayer() {
        
        return player;
        
    }
    
    public abstract boolean handleCommand(String[] args);
    
    public abstract boolean isSpoutSession();
    
    public abstract boolean addSign(Sign sign);
    
    public abstract void removeSign(Sign sign);
    
    public abstract void removeSign(List<Sign> signs);
    
    public abstract boolean checkIfSelected(List<Sign> signs);
    
    public abstract boolean checkIfSelected(Sign sign);
    
    public abstract int getNumberOfSigns();
    
}