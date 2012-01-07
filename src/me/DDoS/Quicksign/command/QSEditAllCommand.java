package me.DDoS.Quicksign.command;

import java.util.List;
import me.DDoS.Quicksign.util.QSBlackList;
import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.sign.QSSignState;
import me.DDoS.Quicksign.util.QSUtil;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QSEditAllCommand extends QSCommand {

    private String text;
    private boolean colors;
    private QSSignState[] backups;

    public QSEditAllCommand(QuickSign plugin, List<Sign> signs, String text, boolean colors) {

        super (plugin, signs);
        this.signs = signs;
        this.text = text;
        this.colors = colors;
        backups = new QSSignState[signs.size()];

    }

    @Override
    public boolean run(Player player) {

        if (plugin.getBlackList().verify(text, player)) {
            
            QSUtil.tell(player, "You are not allowed to place the provided text.");
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
        
        for (Sign sign : signs) {

            backups[i] = new QSSignState(sign);
            sign.setLine(0, text);
            sign.setLine(1, text);
            sign.setLine(2, text);
            sign.setLine(3, text);
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

        }
        
        QSUtil.tell(player, "Undo successful.");
        
    }

    @Override
    public void redo(Player player) {

        for (Sign sign : signs) {

            sign.setLine(0, text);
            sign.setLine(1, text);
            sign.setLine(2, text);
            sign.setLine(3, text);
            sign.update();
            logChange(player, sign);

        }

        QSUtil.tell(player, "Redo successful.");

    }
}