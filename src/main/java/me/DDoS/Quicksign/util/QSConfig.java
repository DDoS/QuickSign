package me.DDoS.Quicksign.util;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import me.DDoS.Quicksign.QuickSign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.Action;

/**
 *
 * @author DDoS
 */
public class QSConfig {

    private final YamlConfiguration config = new YamlConfiguration();
    //
    public static boolean noReachLimit;
    public static boolean colorSignChange;
    public static boolean colorDyes;
    public static boolean chatSigns;
    //
    public static Action selectionMethod;
    public static Action selectionMethodNoReach;
    //
    public static Action dyeMethod;
    //
    public static int maxReach;
    //
    private boolean useWG;
    private boolean useRes;
    private boolean useRegios;
    private boolean useLogBlock;
    private boolean useSpout;
    private boolean useLWC;
    private boolean useChestShop;
    //
    private String selectionMethodString;

    public void setupConfig(QuickSign plugin) {

        File configDir = new File("plugins/QuickSign");

        if (!configDir.exists()) {

            configDir.mkdir();

        }

        File configFile = new File("plugins/QuickSign/config.yml");

        if (!configFile.exists()) {

            try {

                configFile.createNewFile();

            } catch (Exception e) {

                QuickSign.log.info("[QuickSign] Error when creating config file.");

            }
        }

        if (getConfig()) {

            checkConfig();
            loadData();
            convertProperties(plugin);
            return;

        } else {
            
            loadDefaults();
            convertProperties(plugin);
            
        }
        
        QuickSign.log.info("[QuickSign] Configuration loaded.");
        
    }

    private boolean getConfig() {

        try {

            config.load("plugins/QuickSign/config.yml");
            return true;

        } catch (Exception ex) {

            QuickSign.log.info("[QuickSign] Couldn't load config: " + ex.getMessage());
            return false;

        }
    }

    private void checkConfig() {

        Set<String> keys = config.getKeys(false);

        if (!keys.contains("selectionMethod")) {
            config.set("selectionMethod", "right_click");
        }

        if (!keys.contains("noReachLimit")) {
            config.set("noReachLimit", false);
        }

        if (!keys.contains("maxReach")) {
            config.set("maxReach", 100);
        }

        if (!keys.contains("useWorldGuard")) {
            config.set("useWorldGuard", true);
        }

        if (!keys.contains("useResidence")) {
            config.set("useResidence", false);
        }

        if (!keys.contains("useRegios")) {
            config.set("useRegios", false);
        }
        
        if (!keys.contains("useLWC")) {
            config.set("useLWC", false);
        }
        
        if (!keys.contains("useLogBock")) {
            config.set("useLogBock", true);
        }
        
        if (!keys.contains("useChestShop")) {
            config.set("useChestShop", true);
        }

        if (!keys.contains("useSpout")) {
            config.set("useSpout", true);
        }

        if (!keys.contains("colorOnPlacement")) {
            config.set("colorOnPlacement", true);
        }

        if (!keys.contains("colorWithDyes")) {
            config.set("colorWithDyes", false);
        }

        if (!keys.contains("chatSigns")) {
            config.set("chatSigns", true);
        }
        
        try {
        
            config.save("plugins/QuickSign/config.yml");
        
        } catch (IOException ex) {

            QuickSign.log.info("[QuickSign] Couldn't save config: " + ex.getMessage());

        }
    }

    private void loadData() {

        selectionMethodString = config.getString("selectionMethod");
        noReachLimit = config.getBoolean("noReachLimit", false);
        maxReach = config.getInt("maxReach", 100);
        useWG = config.getBoolean("useWorldGuard", true);
        useRes = config.getBoolean("useResidence", false);
        useRegios = config.getBoolean("useRegios", false);
        useLWC = config.getBoolean("useLWC", false);
        useLogBlock = config.getBoolean("useLogBock", true);
        useChestShop = config.getBoolean("useChestShop", true);
        useSpout = config.getBoolean("useSpout", true);
        colorSignChange = config.getBoolean("colorOnPlacement", true);
        colorDyes = config.getBoolean("colorWithDyes", false);
        chatSigns = config.getBoolean("chatSigns", true);

    }
    
    private void loadDefaults() {
        
        selectionMethodString = "right_click";
        noReachLimit = false;
        maxReach = 100;
        useWG = true;
        useRes = true;
        useRegios = true;
        useLWC = false;
        useLogBlock = true;
        useSpout = true;
        useChestShop = true;
        colorSignChange = true;
        colorDyes = false;
        chatSigns = true;
        
        QuickSign.log.info("[QuickSign] Loaded defaults.");
        
    }
    
    private void convertProperties(QuickSign plugin) {

        if (selectionMethodString.equalsIgnoreCase("left_click")) {

            selectionMethod = Action.LEFT_CLICK_BLOCK;
            dyeMethod = Action.RIGHT_CLICK_BLOCK;

        } else {

            selectionMethod = Action.RIGHT_CLICK_BLOCK;
            dyeMethod = Action.LEFT_CLICK_BLOCK;

        }

        if (noReachLimit) {

            if (selectionMethodString.equalsIgnoreCase("right_click")) {

                selectionMethodNoReach = Action.RIGHT_CLICK_AIR;

            } else if (selectionMethodString.equalsIgnoreCase("left_click")) {

                selectionMethodNoReach = Action.LEFT_CLICK_AIR;

            } else {

                selectionMethodNoReach = Action.RIGHT_CLICK_AIR;

            }
        }

        if (maxReach > 100) {

            maxReach = 100;

        }

        if (!useWG) {

            plugin.getSelectionHandler().setWG(null);
            QuickSign.log.info("[QuickSign] WorldGuard support disabled by config.");

        }

        if (!useRes) {

            plugin.getSelectionHandler().setResidence(false);
            QuickSign.log.info("[QuickSign] Residence support disabled by config.");

        }

        if (!useRegios) {

            plugin.getSelectionHandler().setRegiosAPI(null);
            QuickSign.log.info("[QuickSign] Regios support disabled by config.");

        }
        
        if (!useLWC) {

            plugin.getSelectionHandler().setLWC(null);
            QuickSign.log.info("[QuickSign] LWC support disabled by config.");

        }

        if (!useLogBlock) {

            plugin.setConsumer(null);
            QuickSign.log.info("[QuickSign] LogBlock support disabled by config.");

        }

        if (!useChestShop) {

            plugin.getSelectionHandler().setChestShop(false);
            QuickSign.log.info("[QuickSign] ChestShop support disabled by config.");

        }
        
        if (!useSpout) {

            plugin.setSpoutOn(false);
            QuickSign.log.info("[QuickSign] Spout support disabled by config.");

        }
    }
}