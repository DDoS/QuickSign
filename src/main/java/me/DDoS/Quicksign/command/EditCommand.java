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
public class EditCommand extends QSCommand {

    private final int line;
    private String text;
    private final boolean colors;
    private final String[] backups;

    public EditCommand(QuickSign plugin, List<Sign> signs, int line, String text, boolean colors) {

        super (plugin, signs);
        this.line = line;
        this.text = text;
        this.colors = colors;
        backups = new String[signs.size()];

    }

    @Override
    public boolean run(Player player) {

        if (!plugin.getBlackList().allows(text, player)) {
            
            QSUtil.tell(player, "You are not allowed to place the provided text.");
            return false;
            
        }
        
        if (line < 0 || line > 3) {

            QSUtil.tell(player, "Invalid line.");
            return false;

        }

        if (text.length() > 15) {

            QSUtil.tell(player, "The provided text is longer than 15 characters. It will be truncated.");
            text = text.substring(0, 16);

        }

        if (!colors) {

            QSUtil.tell(player, "You don't have permission for colors. They will not be applied.");
            text = QSUtil.stripColors(text);

        } else {
        
            text = ChatColor.translateAlternateColorCodes('&', text);

        }
        
        int i = 0;

        for (Sign sign : signs) {

            backups[i] = sign.getLine(line);
            sign.setLine(line, text);
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

            sign.setLine(line, text);
            sign.update();
            logChange(player, sign);

        }

        QSUtil.tell(player, "Redo successful.");

    }
}