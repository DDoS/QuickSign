package me.DDoS.Quicksign.session;

import java.util.List;
import java.util.UUID;
import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.util.QSSpoutHistory;
import me.DDoS.Quicksign.util.QSUtil;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.PopupScreen;

/**
 *
 * @author DDoS
 */
public class QSSpoutEditSession extends QSEditSession {

    private Sign sign;
    private PopupScreen popup;
    private UUID[] widgets;
    private final QSSpoutHistory history = new QSSpoutHistory(this);

    public QSSpoutEditSession(Player player, QuickSign plugin) {

        super(player, plugin);

    }

    @Override
    public boolean isSpoutSession() {

        return true;

    }

    public Sign getSign() {

        return sign;

    }

    public PopupScreen getPopup() {

        return popup;

    }

    public void setPopup(PopupScreen popup) {

        this.popup = popup;

    }

    public void setWidgets(UUID[] widgets) {

        this.widgets = widgets;

    }

    public UUID[] getWidgets() {

        return widgets;

    }

    public void backupSign() {

        history.backup();

    }

    @Override
    public boolean addSign(Sign sign) {

        this.sign = sign;
        history.backup();
        return true;

    }

    @Override
    public void removeSign(Sign sign) {

        this.sign = null;
        history.clear();

    }

    @Override
    public void removeSign(List<Sign> signs) {

        this.sign = null;
        history.clear();

    }

    @Override
    public boolean checkIfSelected(Sign sign) {

        if (sign == null) {

            return false;

        }

        return (sign.equals(this.sign));

    }

    @Override
    public boolean checkIfSelected(List<Sign> signs) {

        if (sign == null) {

            return false;

        }

        return signs.contains(sign);

    }

    @Override
    public int getNumberOfSigns() {

        if (sign == null) {

            return 0;

        } else {

            return 1;

        }
    }

    @Override
    public boolean handleCommand(String[] args) {
        //undo
        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("undo")) {

                history.undo();
                return true;

            }
            //redo
            if (args[0].equalsIgnoreCase("redo")) {

                history.redo();
                return true;

            }
        }

        QSUtil.tell(player, "Spout mode: select a sign to edit it.");
        QSUtil.tell(player, "Use '/qs undo' or '/qs redo' to navigate through your edit history.");
        return true;

    }
}