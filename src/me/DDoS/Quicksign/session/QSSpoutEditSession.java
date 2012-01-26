package me.DDoS.Quicksign.session;

import java.util.List;
import java.util.UUID;
import me.DDoS.Quicksign.QuickSign;
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

    @Override
    public boolean addSign(Sign sign) {

        this.sign = sign;
        return true;

    }

    @Override
    public void removeSign(Sign sign) {

        this.sign = null;

    }

    @Override
    public void removeSign(List<Sign> signs) {

        this.sign = null;

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

        QSUtil.tell(player, "Spout mode: select a sign to edit it.");
        return true;

    }
}