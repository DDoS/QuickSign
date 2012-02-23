package me.DDoS.Quicksign.permission;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author DDoS
 */
public class PermissionsHandler {

    private Permissions permissions;

    public PermissionsHandler(Plugin plugin) {

        YamlConfiguration config = getConfig(plugin.getDescription().getName());

        if (config != null) {

            getPerms(config, plugin);
            return;

        }

        System.out.println("[" + plugin.getDescription().getName() + "] Defaulting to SuperPerms.");
        getSuperPermsPerms(plugin);

    }

    public Permissions getPermissions() {

        return permissions;

    }

    private YamlConfiguration getConfig(String pluginName) {

        YamlConfiguration config = new YamlConfiguration();

        if (!loadConfig(config, pluginName)) {

            return null;

        }

        if (config.getKeys(true).isEmpty()) {

            config.set("PermissionsSystem", "SuperPerms");
            config.set("PlayerPerms", Arrays.asList("example.ex"));

            if (!saveConfig(config, pluginName)) {

                return null;

            }
            if (!loadConfig(config, pluginName)) {

                return null;

            }

        }

        return config;

    }

    private boolean loadConfig(YamlConfiguration config, String pluginName) {

        File path = new File("plugins/" + pluginName + "/perms_config.yml");

        if (!path.exists()) {

            try {

                path.createNewFile();

            } catch (IOException ex) {

                System.out.println("[" + pluginName + "] Couldn't create permissions config: " + ex.getMessage());
                return false;

            }
        }

        try {

            config.load(path);

        } catch (IOException ex) {

            System.out.println("[" + pluginName + "] Couldn't load permissions config: " + ex.getMessage());
            return false;

        } catch (InvalidConfigurationException ex) {

            System.out.println("[" + pluginName + "] Couldn't load permissions config: " + ex.getMessage());
            return false;

        }

        return true;

    }

    private boolean saveConfig(YamlConfiguration config, String pluginName) {

        File path = new File("plugins/" + pluginName + "/perms_config.yml");

        if (!path.exists()) {

            try {

                path.createNewFile();

            } catch (IOException ex) {

                System.out.println("[" + pluginName + "] Couldn't create permissions config: " + ex.getMessage());
                return false;

            }
        }

        try {

            config.save(path);

        } catch (IOException ex) {

            System.out.println("[" + pluginName + "] Couldn't save permissions config: " + ex.getMessage());
            return false;

        }

        return true;

    }

    private void getPerms(YamlConfiguration config, Plugin plugin) {

        String type = config.getString("PermissionsSystem");

        if (type.equalsIgnoreCase("Legacy")) {

            System.out.println("[" + plugin.getDescription().getName() + "] Legacy permissions are not supported anymore.");
            getSuperPermsPerms(plugin);

        } else if (type.equalsIgnoreCase("PlayerAndOP")) {

            getPlayerAndOPPerms(config, plugin);

        } else {

            getSuperPermsPerms(plugin);

        }
    }

    private void getSuperPermsPerms(Plugin plugin) {

        System.out.println("[" + plugin.getDescription().getName() + "] Got SuperPerms permissions.");
        permissions = new SuperPermsPermissions();

    }

    private void getPlayerAndOPPerms(YamlConfiguration config, Plugin plugin) {

        System.out.println("[" + plugin.getDescription().getName() + "] Got player and OP permissions.");
        permissions = new PlayerAndOPPermissions(config.getStringList("PlayerPerms"));

    }
}