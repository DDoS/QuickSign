package me.DDoS.Quicksign.util;

import java.util.ArrayList;
import java.util.List;
import me.DDoS.Quicksign.session.QSSpoutEditSession;
import me.DDoS.Quicksign.sign.QSSignState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QSSpoutHistory {

    private final QSSpoutEditSession session;
    private final List<QSSignState> history = new ArrayList<QSSignState>();
    private int pos = -1;

    public QSSpoutHistory(QSSpoutEditSession session) {

        this.session = session;

    }

    public void clear() {

        history.clear();
        pos = -1;

    }

    public void backup() {

        if (pos < history.size() - 1) {

            history.subList(pos + 1, history.size()).clear();

        }

        ++pos;
        history.add(new QSSignState(session.getSign()));

    }

    public void undo() {

        Player player = session.getPlayer();
        
        if (pos < 0) {

            QSUtil.tell(player, "Nothing to undo.");
            return;

        }

        String[] lines = history.get(pos--).getLines();
        Sign sign = session.getSign();
        sign.setLine(0, lines[0]);
        sign.setLine(1, lines[1]);
        sign.setLine(2, lines[2]);
        sign.setLine(3, lines[3]);
        sign.update();

        QSUtil.tell(player, "Undo successful.");
        
    }

    public void redo() {

        Player player = session.getPlayer();
        
        if (pos >= history.size() - 1) {

            QSUtil.tell(player, "Nothing to redo.");
            return;

        }

        String[] lines = history.get(++pos).getLines();
        Sign sign = session.getSign();
        sign.setLine(0, lines[0]);
        sign.setLine(1, lines[1]);
        sign.setLine(2, lines[2]);
        sign.setLine(3, lines[3]);
        sign.update();

        QSUtil.tell(player, "Undo successful.");
        
    }
}
