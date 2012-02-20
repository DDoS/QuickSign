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
public class ColorCommand extends QSCommand {

    private final int line;
    private final int index;
    private final ChatColor color;
    private final String[] backups;

    public ColorCommand(QuickSign plugin, List<Sign> signs, int line, int index, String color) {

        super (plugin, signs);
        this.line = line;
        this.index = index;
        this.color = QSUtil.getColorFromName(color);
        backups = new String[signs.size()];

    }

    @Override
    public boolean run(Player player) {

        if (line < 0 || line > 3) {

            QSUtil.tell(player, "Invalid line");
            return false;

        }

        if (index < 0 || index > 14) {

            QSUtil.tell(player, "Invalid index: must be between 1 and 15");
            return false;

        }

        if (color == null) {

            QSUtil.tell(player, "Invalid color name.");
            return false;

        }

        int i = 0;

        for (Sign sign : signs) {

            backups[i] = sign.getLine(line);

            if (sign.getLine(line).length() > 0) {

                int ti = index;

                if (ti > sign.getLine(line).length() - 1) {

                    ti = sign.getLine(line).length() - 1;

                }

                sign.setLine(line, (new StringBuilder(sign.getLine(line)).insert(ti, color)).toString());

            }

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

            if (sign.getLine(line).length() > 0) {

                int ti = index;

                if (ti > sign.getLine(line).length() - 1) {

                    ti = sign.getLine(line).length() - 1;

                }

                sign.setLine(line, (new StringBuilder(sign.getLine(line)).insert(ti, color)).toString());

            }

            sign.update();
            logChange(player, sign);

        }

        QSUtil.tell(player, "Redo successful.");

    }
}