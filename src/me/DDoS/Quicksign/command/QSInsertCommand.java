package me.DDoS.Quicksign.command;

import java.util.List;
import me.DDoS.Quicksign.util.QSBlackList;
import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.util.QSUtil;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QSInsertCommand implements QSCommand {

    private List<Sign> signs;
    private int line;
    private int index;
    private String text;
    private boolean colors;
    private String[] backups;

    public QSInsertCommand(List<Sign> signs, int line, int index, String text, boolean colors) {

        this.signs = signs;
        this.line = line;
        this.index = index;
        this.text = text;
        this.colors = colors;
        backups = new String[signs.size()];

    }

    @Override
    public boolean run(Player player) {

        if (QSBlackList.verify(text, player)) {

            QSUtil.tell(player, "You are not allowed to place the provided text.");
            return false;

        }

        if (line < 0 || line > 3) {

            QSUtil.tell(player, "Invalid line.");
            return false;

        }

        if (index < 0 || index > 14) {

            QSUtil.tell(player, "Invalid index: must be between 1 and 15");
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

            backups[i] = sign.getLine(line);

            if (sign.getLine(0).length() > 0) {

                int ti = index;

                if (ti > sign.getLine(line).length() - 1) {

                    ti = sign.getLine(line).length() - 1;

                }

                sign.setLine(line, (new StringBuilder(sign.getLine(line)).insert(ti, text)).toString());

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

            if (sign.getLine(0).length() > 0) {

                int ti = index;

                if (ti > sign.getLine(line).length() - 1) {

                    ti = sign.getLine(line).length() - 1;

                }

                sign.setLine(line, (new StringBuilder(sign.getLine(line)).insert(ti, text)).toString());

            }

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