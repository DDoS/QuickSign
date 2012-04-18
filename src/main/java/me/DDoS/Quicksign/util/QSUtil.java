package me.DDoS.Quicksign.util;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QSUtil {

    private static final Map<String, ChatColor> colorsByName = new HashMap<String, ChatColor>();

    static {

        for (ChatColor color : ChatColor.values()) {

            colorsByName.put(color.name().replace('_', '\0').toLowerCase(), color);

        }
    }
    
    public static String stripColors(String string) {
        
        return string.replaceAll("&[0-9a-fA-Fk-oK-OrR]", "");
        
    }

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

    public static ChatColor getColorFromName(String colorName) {

        return colorsByName.get(colorName);

    }
}
