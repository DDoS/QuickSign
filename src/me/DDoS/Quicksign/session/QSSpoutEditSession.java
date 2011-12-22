package me.DDoS.Quicksign.session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.sign.QSSignState;
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
    private final List<QSSignState> history = new ArrayList<QSSignState>();
    private int pos = -1;

    public QSSpoutEditSession(Player player, QuickSign plugin) {

        super(player, plugin);

    }

    @Override
    public boolean isSpoutSession() {

        return true;

    }

    public Sign getSpoutSign() {

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

        //undo
        if (args.length == 1 && args[0].equalsIgnoreCase("undo")) {
            
            if (sign == null) {

                QSUtil.tell(player, "No sign selected.");
                return true;

            }
            
            if (pos < 0) {

                QSUtil.tell(player, "Nothing to undo.");
                return true;

            }

            String[] lines = history.get(pos--).getLines();
            sign.setLine(0, lines[0]);
            sign.setLine(1, lines[1]);
            sign.setLine(2, lines[2]);
            sign.setLine(3, lines[3]);
            QSUtil.tell(player, "Undo successful.");
            return true;

        }
        //redo
        if (args.length == 1 && args[0].equalsIgnoreCase("redo")) {
            
            if (sign == null) {

                QSUtil.tell(player, "No sign selected.");
                return true;

            }
            
            if (pos >= history.size() - 1) {

                QSUtil.tell(player, "Nothing to redo.");
                return true;

            }

            String[] lines = history.get(++pos).getLines();
            sign.setLine(0, lines[0]);
            sign.setLine(1, lines[1]);
            sign.setLine(2, lines[2]);
            sign.setLine(3, lines[3]);
            QSUtil.tell(player, "Redo successful.");
            return true;
            
        }

        QSUtil.tell(player, "Spout mode: select a sign to edit it.");
        QSUtil.tell(player, "Use '/undo' or '/redo' to navigate through your edit history.");
        return true;

    }
}