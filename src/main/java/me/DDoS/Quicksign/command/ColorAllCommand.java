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
public class ColorAllCommand extends QSCommand {

    private final int index;
    private final ChatColor color;
    private final SignState[] backups;

    public ColorAllCommand(QuickSign plugin, List<Sign> signs, int index, String color) {

        super (plugin, signs);
        this.index = index;
        this.color = QSUtil.getColorFromName(color);
        backups = new SignState[signs.size()];

    }

    @Override
    public boolean run(Player player) {

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

            backups[i] = new SignState(sign);
            int ti = index;

            if (sign.getLine(0).length() > 0) {

                if (ti > sign.getLine(0).length() - 1) {

                    ti = sign.getLine(0).length() - 1;

                }

                sign.setLine(0, (new StringBuilder(sign.getLine(0)).insert(ti, color)).toString());

            }

            if (sign.getLine(1).length() > 0) {

                ti = index;

                if (ti > sign.getLine(1).length() - 1) {

                    ti = sign.getLine(1).length() - 1;

                }

                sign.setLine(1, (new StringBuilder(sign.getLine(1)).insert(ti, color)).toString());

            }

            if (sign.getLine(2).length() > 0) {

                ti = index;

                if (ti > sign.getLine(2).length() - 1) {

                    ti = sign.getLine(2).length() - 1;

                }

                sign.setLine(2, (new StringBuilder(sign.getLine(2)).insert(ti, color)).toString());

            }

            if (sign.getLine(3).length() > 0) {

                ti = index;

                if (ti > sign.getLine(3).length() - 1) {

                    ti = sign.getLine(3).length() - 1;

                }

                sign.setLine(3, (new StringBuilder(sign.getLine(3)).insert(ti, color)).toString());

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

            int ti = index;

            if (sign.getLine(0).length() > 0) {

                if (ti > sign.getLine(0).length() - 1) {

                    ti = sign.getLine(0).length() - 1;

                }

                sign.setLine(0, (new StringBuilder(sign.getLine(0)).insert(ti, color)).toString());

            }

            if (sign.getLine(1).length() > 0) {

                ti = index;

                if (ti > sign.getLine(1).length() - 1) {

                    ti = sign.getLine(1).length() - 1;

                }

                sign.setLine(1, (new StringBuilder(sign.getLine(1)).insert(ti, color)).toString());

            }

            if (sign.getLine(2).length() > 0) {

                ti = index;

                if (ti > sign.getLine(2).length() - 1) {

                    ti = sign.getLine(2).length() - 1;

                }

                sign.setLine(2, (new StringBuilder(sign.getLine(2)).insert(ti, color)).toString());

            }

            if (sign.getLine(3).length() > 0) {

                ti = index;

                if (ti > sign.getLine(3).length() - 1) {

                    ti = sign.getLine(3).length() - 1;

                }

                sign.setLine(3, (new StringBuilder(sign.getLine(3)).insert(ti, color)).toString());

            }
            
            sign.update();
            logChange(player, sign);

        }

        QSUtil.tell(player, "Redo successful.");

    }
}