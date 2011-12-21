package me.DDoS.Quicksign.command;

import java.util.List;
import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.util.QSUtil;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QSColorClearCommand implements QSCommand {

    private List<Sign> signs;
    private int line;
    private String[] backups;

    public QSColorClearCommand(List<Sign> signs, int line) {

        this.signs = signs;
        this.line = line;
        backups = new String[signs.size()];

    }

    @Override
    public boolean run(Player player) {

        if (line < 0 || line > 3) {

            QSUtil.tell(player, "Invalid line.");
            return false;

        }

        int i = 0;
        
        for (Sign sign : signs) {

            backups[i] = sign.getLine(line);
            sign.setLine(line, ChatColor.stripColor(sign.getLine(line)));
            sign.update();
            logChange(player, sign);
            i++;

        }

        QSUtil.tell(player, "Edit successful.");
        return true;

    }

    @Override
    public void undo(Player player) {

        int i = 0;

        for (Sign sign : signs) {

            sign.setLine(line, backups[i]);
            sign.update();
            logChange(player, sign);
            i++;

        }
        
        QSUtil.tell(player, "Undo successful.");
        
    }

    @Override
    public void redo(Player player) {

        for (Sign sign : signs) {

            sign.setLine(line, ChatColor.stripColor(sign.getLine(line)));
            sign.update();
            logChange(player, sign);

        }

        QSUtil.tell(player, "Redo successful.");

    }
    
    private void logChange(Player player, Sign sign) {

        if (QuickSign.consumer == null) {
            
            return;
        
        }

        QuickSign.consumer.queueSignPlace(player.getName(), sign);

    }
}