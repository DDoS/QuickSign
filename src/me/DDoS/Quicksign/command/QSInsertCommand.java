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
public class QSInsertCommand extends QSCommand {

    private final int line;
    private final int index;
    private String text;
    private final boolean colors;
    private final String[] backups;

    public QSInsertCommand(QuickSign plugin, List<Sign> signs, int line, int index, String text, boolean colors) {

        super(plugin, signs);
        this.line = line;
        this.index = index;
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
        boolean someSignsIgnored = false;

        for (Sign sign : signs) {

            backups[i] = sign.getLine(line);

            if (sign.getLine(line).length() > 0) {

                int ti = index;

                if (ti > sign.getLine(line).length() - 1) {

                    ti = sign.getLine(line).length() - 1;

                }

                String finalLine = new StringBuilder(sign.getLine(line)).insert(ti, text).toString();

                if (!plugin.getBlackList().allows(finalLine, player)) {

                    someSignsIgnored = true;
                    continue;

                }

                sign.setLine(line, finalLine);
                sign.update();
                logChange(player, sign);

            }

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

            if (sign.getLine(line).length() > 0) {

                int ti = index;

                if (ti > sign.getLine(line).length() - 1) {

                    ti = sign.getLine(line).length() - 1;

                }

                String finalLine = new StringBuilder(sign.getLine(line)).insert(ti, text).toString();

                if (!plugin.getBlackList().allows(finalLine, player)) {

                    continue;

                }

                sign.setLine(line, finalLine);
                sign.update();
                logChange(player, sign);

            }
        }

        QSUtil.tell(player, "Redo successful.");

    }
}