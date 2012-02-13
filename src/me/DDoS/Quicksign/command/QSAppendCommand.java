package me.DDoS.Quicksign.command;

import java.util.List;
import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.util.QSUtil;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QSAppendCommand extends QSCommand {

    private final int line;
    private String text;
    private final boolean colors;
    private final String[] backups;

    public QSAppendCommand(QuickSign plugin, List<Sign> signs, int line, String text, boolean colors) {

        super (plugin, signs);
        this.line = line;
        this.text = text;
        this.colors = colors;
        backups = new String[signs.size()];

    }

    @Override
    public boolean run(Player player) {
        
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
            text = text.replaceAll("&([0-9[a-fA-F]])", "");

        } else {
        
            text = text.replaceAll("&([0-9[a-fA-F]])", "\u00A7$1");

        }
        
        int i = 0;
        boolean someSignsIgnored = false;
        
        for (Sign sign : signs) {
            
            backups[i] = sign.getLine(line);
            String finalLine = sign.getLine(line) + " " + text;
            
            if (!plugin.getBlackList().allows(finalLine, player)) {
                
                someSignsIgnored = true;
                continue;
                
            }
            
            sign.setLine(line, finalLine);
            sign.update();
            logChange(player, sign);
            i++;

        }
        
        if (someSignsIgnored) {
            
            QSUtil.tell(player, "Some signs we're not edited: the final text is blacklisted.");
            
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
            
            String finalLine = sign.getLine(line) + " " + text;
            
            if (!plugin.getBlackList().allows(finalLine, player)) {
                
                continue;
                
            }
            
            sign.setLine(line, finalLine);
            sign.update();
            logChange(player, sign);

        }

        QSUtil.tell(player, "Redo successful.");

    }
}
