package me.DDoS.Quicksign.util;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QSUtil {

    public static boolean isParsableToInt(String line) {

        try {

            Integer.parseInt(line);
            return true;

        } catch (NumberFormatException nfe) {

            return false;

        }
    }

    public static String mergeToString(String[] cmdArgs, int mergeStart) {

        String cmdArgsMerge = "";
        String space = " ";

        for (int i = mergeStart; i < cmdArgs.length; i++) {

            if (i == (cmdArgs.length - 1)) {

                space = "";

            }

            cmdArgsMerge = cmdArgsMerge.concat(cmdArgs[i] + space);

        }

        return cmdArgsMerge;

    }

    public static void tell(Player player, String string) {

        player.sendMessage(ChatColor.BLUE + "[QuickSign]" + ChatColor.GRAY + " " + string);

    }

    public static boolean checkForSign(Block block) {

        if (block == null) {

            return false;

        }

        switch (block.getType()) {

            case WALL_SIGN:
                return true;

            case SIGN_POST:
                return true;

            default:
                return false;

        }
    }

    public static ChatColor getColorFromName(String color) {

        if (color.equalsIgnoreCase("white")) {

            return ChatColor.WHITE;

        }

        if (color.equalsIgnoreCase("aqua")) {

            return ChatColor.AQUA;

        }

        if (color.equalsIgnoreCase("black")) {

            return ChatColor.BLACK;

        }

        if (color.equalsIgnoreCase("blue")) {

            return ChatColor.BLUE;

        }

        if (color.equalsIgnoreCase("darkaqua")) {

            return ChatColor.DARK_AQUA;

        }

        if (color.equalsIgnoreCase("darkblue")) {

            return ChatColor.DARK_BLUE;

        }

        if (color.equalsIgnoreCase("darkgray")) {

            return ChatColor.DARK_GRAY;

        }

        if (color.equalsIgnoreCase("darkgreen")) {

            return ChatColor.DARK_GREEN;

        }

        if (color.equalsIgnoreCase("darkpurple")) {

            return ChatColor.DARK_PURPLE;

        }

        if (color.equalsIgnoreCase("darkred")) {

            return ChatColor.DARK_RED;

        }

        if (color.equalsIgnoreCase("gold")) {

            return ChatColor.GOLD;

        }

        if (color.equalsIgnoreCase("gray")) {

            return ChatColor.GRAY;

        }

        if (color.equalsIgnoreCase("green")) {

            return ChatColor.GREEN;

        }

        if (color.equalsIgnoreCase("lightpurple")) {

            return ChatColor.LIGHT_PURPLE;

        }

        if (color.equalsIgnoreCase("red")) {

            return ChatColor.RED;

        }

        if (color.equalsIgnoreCase("yellow")) {

            return ChatColor.YELLOW;

        }

        return null;

    }
}
