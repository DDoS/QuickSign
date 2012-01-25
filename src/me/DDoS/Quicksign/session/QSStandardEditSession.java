package me.DDoS.Quicksign.session;

import me.DDoS.Quicksign.sign.QSClipBoard;
import me.DDoS.Quicksign.util.QSUtil;
import me.DDoS.Quicksign.permissions.QSPermissions;
import java.util.ArrayList;
import java.util.List;
import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.command.*;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QSStandardEditSession extends QSEditSession {

    private QSClipBoard clipBoard;
    private final List<Sign> signs = new ArrayList<Sign>();
    private final List<QSCommand> history = new ArrayList<QSCommand>();
    private int pos = -1;

    public QSStandardEditSession(Player player, QuickSign plugin) {

        super(player, plugin);

    }

    public void setClipBoard(QSClipBoard clipBoard) {

        this.clipBoard = clipBoard;

    }

    public QSClipBoard getClipBoard() {

        return clipBoard;

    }

    @Override
    public boolean isSpoutSession() {

        return false;

    }

    @Override
    public boolean addSign(Sign sign) {

        if (!checkIfSelected(sign)) {

            signs.add(sign);
            return true;

        }

        return false;

    }

    @Override
    public void removeSign(Sign sign) {

        signs.remove(sign);

    }
    
    @Override
    public void removeSign(List<Sign> signs) {

        this.signs.removeAll(signs);
        
    }

    @Override
    public boolean checkIfSelected(Sign sign) {

        return signs.contains(sign);

    }
    
    @Override
    public boolean checkIfSelected(List<Sign> signs) {
        
        for (Sign sign : signs) {
            
            if (this.signs.contains(sign)) {
                
                return true;
                
            }
            
        }
        
        return false;

    }

    @Override
    public int getNumberOfSigns() {

        return signs.size();

    }

    public void clearAllSigns() {

        signs.clear();

    }

    @Override
    public boolean handleCommand(String[] args) {
        //clear selection
        if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {

            clearAllSigns();
            QSUtil.tell(player, "Selection cleared.");
            return true;

        }
        //clear clipboard
        if (args.length == 2 && args[0].equalsIgnoreCase("cb") && args[1].equalsIgnoreCase("clear")) {

            setClipBoard(null);
            QSUtil.tell(player, "Clipboard cleared.");
            return true;

        }

        if (signs.isEmpty()) {

            QSUtil.tell(player, "Your selection is empty.");
            return true;

        }
        //edit
        if (args.length >= 2 && (QSUtil.isParsableToInt(args[0]))) {

            int line = Integer.parseInt(args[0]) - 1;
            String text = QSUtil.mergeToString(args, 1);
            boolean colorPerms = checkForColorPerms();

            QSCommand cmd = new QSEditCommand(plugin, signs, line, text, colorPerms);

            if (cmd.run(player)) {

                updateHistory(cmd);

            }

            return true;

        }
        //edit all
        if (args.length >= 2 && args[0].equalsIgnoreCase("all")) {

            String text = QSUtil.mergeToString(args, 1);
            boolean colorPerms = checkForColorPerms();

            QSCommand cmd = new QSEditAllCommand(plugin, signs, text, colorPerms);

            if (cmd.run(player)) {

                updateHistory(cmd);

            }

            return true;

        }
        //clear
        if (args.length == 2 && args[0].equalsIgnoreCase("clear") && QSUtil.isParsableToInt(args[1])) {

            int line = Integer.parseInt(args[1]) - 1;

            QSCommand cmd = new QSClearCommand(plugin, signs, line);

            if (cmd.run(player)) {

                updateHistory(cmd);

            }

            return true;

        }
        //clear all
        if (args.length == 2 && args[0].equalsIgnoreCase("clear") && args[1].equalsIgnoreCase("all")) {

            QSCommand cmd = new QSClearAllCommand(plugin, signs);

            if (cmd.run(player)) {

                updateHistory(cmd);

            }

            return true;

        }
        //color a line
        if (args.length == 4 && args[0].equalsIgnoreCase("color") && QSUtil.isParsableToInt(args[2]) && QSUtil.isParsableToInt(args[3])) {

            if (checkForColorPerms()) {

                String colorName = args[1];
                int line = Integer.parseInt(args[2]) - 1;
                int index = Integer.parseInt(args[3]) - 1;

                QSCommand cmd = new QSColorCommand(plugin, signs, line, index, colorName);

                if (cmd.run(player)) {

                    updateHistory(cmd);

                }

                return true;

            }

            player.sendMessage(ChatColor.RED + "You don't have permission for this command.");
            return true;

        }
        //color all lines
        if (args.length == 4 && args[0].equalsIgnoreCase("color") && args[2].equalsIgnoreCase("all") && QSUtil.isParsableToInt(args[3])) {

            if (checkForColorPerms()) {

                String colorName = args[1];
                int index = Integer.parseInt(args[3]) - 1;

                QSCommand cmd = new QSColorAllCommand(plugin, signs, index, colorName);

                if (cmd.run(player)) {

                    updateHistory(cmd);

                }

                return true;

            }

            player.sendMessage(ChatColor.RED + "You don't have permission for this command.");
            return true;

        }
        //clear color on line
        if (args.length == 3 && args[0].equalsIgnoreCase("color") && args[1].equalsIgnoreCase("clear") && QSUtil.isParsableToInt(args[2])) {

            int line = Integer.parseInt(args[2]) - 1;

            QSCommand cmd = new QSColorClearCommand(plugin, signs, line);

            if (cmd.run(player)) {

                updateHistory(cmd);

            }

            return true;

        }
        //clear all colors
        if (args.length == 3 && args[0].equalsIgnoreCase("color") && args[1].equalsIgnoreCase("clear")
                && args[2].equalsIgnoreCase("all")) {

            QSCommand cmd = new QSColorClearAllCommand(plugin, signs);

            if (cmd.run(player)) {

                updateHistory(cmd);

            }

            return true;

        }
        //copy
        if (args.length == 1 && args[0].equalsIgnoreCase("copy")) {

            Sign sign = signs.get(signs.size() - 1);
            clipBoard = new QSClipBoard(sign.getLine(0), sign.getLine(1), sign.getLine(2), sign.getLine(3));
            QSUtil.tell(player, "Sign copied.");
            return true;

        }
        //insert text at index for line
        if (args.length >= 4 && args[0].equalsIgnoreCase("insert") && (QSUtil.isParsableToInt(args[1])) && (QSUtil.isParsableToInt(args[2]))) {

            int line = Integer.parseInt(args[1]) - 1;
            int index = Integer.parseInt(args[2]) - 1;
            String text = QSUtil.mergeToString(args, 3);
            boolean colorPerms = checkForColorPerms();

            QSCommand cmd = new QSInsertCommand(plugin, signs, line, index, text, colorPerms);

            if (cmd.run(player)) {

                updateHistory(cmd);

            }

            return true;

        }
        //append for line
        if (args.length >= 3 && args[0].equalsIgnoreCase("append") && (QSUtil.isParsableToInt(args[1]))) {

            int line = Integer.parseInt(args[1]) - 1;
            String text = QSUtil.mergeToString(args, 2);
            boolean colorPerms = checkForColorPerms();

            QSCommand cmd = new QSAppendCommand(plugin, signs, line, text, colorPerms);

            if (cmd.run(player)) {

                updateHistory(cmd);

            }

            return true;

        }
        //replace word by word on line
        if (args.length == 4 && args[0].equalsIgnoreCase("replace") && (QSUtil.isParsableToInt(args[1]))) {

            int line = Integer.parseInt(args[1]) - 1;
            String text1 = args[2];
            String text2 = args[3];
            boolean colorPerms = checkForColorPerms();

            QSCommand cmd = new QSReplaceCommand(plugin, signs, line, text1, text2, colorPerms);

            if (cmd.run(player)) {

                updateHistory(cmd);

            }

            return true;

        }
        //undo
        if (args.length == 1 && args[0].equalsIgnoreCase("undo")) {

            if (pos < 0) {

                QSUtil.tell(player, "Nothing to undo.");
                return true;

            }

            history.get(pos--).undo(player);
            return true;

        }
        //redo
        if (args.length == 1 && args[0].equalsIgnoreCase("redo")) {

            if (pos >= history.size() - 1) {

                QSUtil.tell(player, "Nothing to redo.");
                return true;

            }

            history.get(++pos).redo(player);
            return true;

        }
        //paste line to line
        if (args.length == 3 && args[0].equalsIgnoreCase("paste") && QSUtil.isParsableToInt(args[1]) && QSUtil.isParsableToInt(args[2])) {

            if (clipBoard != null) {

                String text = clipBoard.getLine(Integer.parseInt(args[1]));
                int line = Integer.parseInt(args[2]) - 1;
                boolean colorPerms = checkForColorPerms();

                QSCommand cmd = new QSEditCommand(plugin, signs, line, text, colorPerms);

                if (cmd.run(player)) {

                    updateHistory(cmd);

                }

                return true;

            }

            QSUtil.tell(player, "Your clip board is empty.");
            return true;

        }
        //paste line to all
        if (args.length == 3 && args[0].equalsIgnoreCase("paste") && QSUtil.isParsableToInt(args[1]) && args[2].equalsIgnoreCase("all")) {

            if (clipBoard != null) {

                String text = clipBoard.getLine(Integer.parseInt(args[1]));
                boolean colorPerms = checkForColorPerms();

                QSCommand cmd = new QSEditAllCommand(plugin, signs, text, colorPerms);

                if (cmd.run(player)) {

                    updateHistory(cmd);

                }

                return true;

            }

            QSUtil.tell(player, "Your clip board is empty.");
            return true;

        }
        //paste all to all
        if (args.length == 2 && args[0].equalsIgnoreCase("paste") && args[1].equalsIgnoreCase("all")) {

            if (clipBoard != null) {

                String[] text = clipBoard.getAllLines();
                boolean colorPerms = checkForColorPerms();

                QSCommand cmd = new QSPasteCommand(plugin, signs, text, colorPerms);

                if (cmd.run(player)) {

                    updateHistory(cmd);

                }

                return true;

            }

            QSUtil.tell(player, "Your clip board is empty.");
            return true;

        }

        return false;

    }

    private boolean checkForColorPerms() {

        return plugin.hasPermissions(player, QSPermissions.COLOR_CMD.getPermissionString());

    }

    private void updateHistory(QSCommand cmd) {

        if (pos < history.size() - 1) {

            history.subList(pos + 1, history.size()).clear();

        }

        ++pos;
        history.add(cmd);

    }
}