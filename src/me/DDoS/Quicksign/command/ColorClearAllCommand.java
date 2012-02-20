package me.DDoS.Quicksign.command;

import java.util.List;
import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.sign.SignState;
import me.DDoS.Quicksign.util.QSUtil;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class ColorClearAllCommand extends QSCommand {

    private final SignState[] backups;

    public ColorClearAllCommand(QuickSign plugin, List<Sign> signs) {

        super (plugin, signs);
        backups = new SignState[signs.size()];

    }

    @Override
    public boolean run(Player player) {
        
        int i = 0;
        
        for (Sign sign : signs) {

            backups[i] = new SignState(sign);
            sign.setLine(0, ChatColor.stripColor(sign.getLine(0)));
            sign.setLine(1, ChatColor.stripColor(sign.getLine(1)));
            sign.setLine(2, ChatColor.stripColor(sign.getLine(2)));
            sign.setLine(3, ChatColor.stripColor(sign.getLine(3)));
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

            String[] lines = backups[i].getLines();
            sign.setLine(0, lines[0]);
            sign.setLine(1, lines[1]);
            sign.setLine(2, lines[2]);
            sign.setLine(3, lines[3]);
            sign.update();
            logChange(player, sign);
            i++;

        }
        
        QSUtil.tell(player, "Undo successful.");
        
    }

    @Override
    public void redo(Player player) {

        for (Sign sign : signs) {

            sign.setLine(0, ChatColor.stripColor(sign.getLine(0)));
            sign.setLine(1, ChatColor.stripColor(sign.getLine(1)));
            sign.setLine(2, ChatColor.stripColor(sign.getLine(2)));
            sign.setLine(3, ChatColor.stripColor(sign.getLine(3)));
            sign.update();
            logChange(player, sign);

        }

        QSUtil.tell(player, "Redo successful.");

    }
}
