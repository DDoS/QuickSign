package me.DDoS.Quicksign.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.permissions.QSPermissions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QSBlackList {

    private final Set<String> blackList = new HashSet<String>();
    private QuickSign plugin;

    public QSBlackList(QuickSign plugin) {

        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/QuickSign/black_list.yml"));

        if (config.getKeys(true).isEmpty()) {

            config.set("BlackList", Arrays.asList("[buy]", "[sell]"));

            try {

                config.save("plugins/QuickSign/black_list.yml");

            } catch (IOException ex) {

                QuickSign.log.info("[QuickSign] Couldn't save black list: " + ex.getMessage());
                return;

            }
        }

        List<String> bl = config.getList("BlackList");

        for (String ble : bl) {

            if (!blackList.contains(ble)) {

                blackList.add(ble);

            }
        }

        this.plugin = plugin;

        QuickSign.log.info("[QuickSign] Black list loaded");

    }

    public boolean verify(String line, Player player) {

        if (!plugin.hasPermissions(player,
                QSPermissions.IGNORE_BLACK_LIST.getPermissionString())) {

            for (String s : blackList) {

                if (line.equalsIgnoreCase(s)) {

                    return true;

                }
            }
        }

        if (!plugin.hasPermissions(player,
                QSPermissions.ALLOW_ICS.getPermissionString())) {

            return checkForIC(line);

        }

        return false;

    }

    public boolean verify(String[] lines, Player player) {

        if (!plugin.hasPermissions(player,
                QSPermissions.IGNORE_BLACK_LIST.getPermissionString())) {

            for (String line : lines) {

                for (String s : blackList) {

                    if (line.equalsIgnoreCase(s)) {

                        return true;

                    }
                }
            }
        }

        if (!plugin.hasPermissions(player,
                QSPermissions.ALLOW_ICS.getPermissionString())) {

            for (String line : lines) {

                if (checkForIC(line)) {

                    return true;

                }
            }
        }

        return false;

    }

    private boolean checkForIC(String txt) {
        //[MCXXXX]
        if (txt.length() >= 8) {

            try {

                if (txt.substring(0, 3).equalsIgnoreCase("[MC")
                        && QSUtil.isParsableToInt(txt.substring(3, 7))
                        && txt.charAt(7) == ']') {

                    return true;

                }

                return false;

            } catch (StringIndexOutOfBoundsException e) {

                return true;

            }
        }

        return false;

    }
}