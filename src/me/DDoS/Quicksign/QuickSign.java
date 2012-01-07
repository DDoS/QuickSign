package me.DDoS.Quicksign;

import com.griefcraft.lwc.LWCPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import couk.Adamki11s.Regios.API.RegiosAPI;
import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import me.DDoS.Quicksign.handler.*;
import me.DDoS.Quicksign.listener.*;
import me.DDoS.Quicksign.permissions.Permissions;
import me.DDoS.Quicksign.permissions.PermissionsHandler;
import me.DDoS.Quicksign.permissions.QSPermissions;
import me.DDoS.Quicksign.session.*;
import me.DDoS.Quicksign.sign.QSSign;
import me.DDoS.Quicksign.sign.QSSignGenerator;
import me.DDoS.Quicksign.util.*;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author DDoS 
 */
public class QuickSign extends JavaPlugin {

    public static final Logger log = Logger.getLogger("Minecraft");
    //
    private Permissions permissions;
    //
    private final QSPlayerListener playerListener = new QSPlayerListener(this);
    private final QSBlockListener blockListener = new QSBlockListener(this);
    private QSScreenListener screenListener;
    //
    private final QSSelectionHandler selectionHandler = new QSSelectionHandler(this);
    private final QSSignGenerator signGenerator = new QSSignGenerator(this);
    //
    private QSSpoutHandler spoutHandler;
    //
    private final Map<Player, QSEditSession> playerSessions = new HashMap<Player, QSEditSession>();
    //
    public static boolean spoutOn = false;
    //
    public static Consumer consumer = null;

    @Override
    public void onEnable() {

        permissions = new PermissionsHandler(this).getPermissions();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Event.Priority.Normal, this);

        checkForWorldGuard();
        checkForResidence();
        checkForRegios();
        checkForLogBlock();
        checkForLWC();

        if (checkForSpout()) {

            screenListener = new QSScreenListener(this);
            spoutHandler = new QSSpoutHandler(this);
            pm.registerEvent(Event.Type.CUSTOM_EVENT, screenListener, Event.Priority.Normal, this);

        }

        new QSConfig().setupConfig(this);

        QSBlackList.load(this);

        log.info("[QuickSign] Configuration loaded.");
        log.info("[QuickSign] Plugin enabled! (v0.7), by DDoS");

    }

    @Override
    public void onDisable() {

        log.info("[QuickSign] Plugin disabled! (v0.7), by DDoS");
        
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (!(sender instanceof Player)) {

            sender.sendMessage("This command can only be used in-game.");
            return true;

        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("qs")
                && hasPermissions(player, QSPermissions.USE.getPermissionString())) {

            if (args.length == 0 && !isUsing(player)) {

                playerSessions.put(player, new QSStandardEditSession(player, this));
                QSUtil.tell(player, "enabled [Normal Mode].");
                return true;

            }

            if (args.length == 0 && isUsing(player)) {

                removeSession(player);
                QSUtil.tell(player, "disabled.");
                return true;

            }

            if (args.length == 1 && args[0].equalsIgnoreCase("spout")) {

                if (hasPermissions(player, QSPermissions.USE_SPOUT.getPermissionString())) {

                    if (isUsing(player)) {

                        QSUtil.tell(player, "Please disable QuickSign, and renable with '/qs spout'.");
                        return true;

                    } else {

                        playerSessions.put(player, new QSSpoutEditSession(player, this));
                        QSUtil.tell(player, "enabled [Spout Mode].");
                        return true;

                    }

                } else {

                    player.sendMessage(ChatColor.RED + "You don't have permission for this command.");
                    return true;

                }
            }

            if (args.length >= 3 && args[0].equalsIgnoreCase("fs")) {

                if (hasPermissions(player, QSPermissions.FS.getPermissionString())) {

                    signGenerator.createSign(player, args[1], QSUtil.mergeToString(args, 2));                    
                    return true;

                } else {

                    player.sendMessage(ChatColor.RED + "You don't have permission for this command.");
                    return true;

                }
            }

            if (!isUsing(player)) {

                QSUtil.tell(player, "You need to activate QuickSign to use this command.");
                return true;

            }

            if (args.length == 1 && args[0].equalsIgnoreCase("s")) {

                if (hasPermissions(player, QSPermissions.NO_REACH_LIMIT.getPermissionString())) {

                    Block block = player.getTargetBlock(null, QSConfig.maxReach);

                    if (!QSUtil.checkForSign(block)) {

                        QSUtil.tell(player, "This block is not a sign.");
                        return true;

                    }

                    selectionHandler.handleSignSelection(null, new QSSign(block), player);
                    return true;

                } else {

                    player.sendMessage(ChatColor.RED + "You don't have permission for this command.");
                    return true;

                }

            } else {

                if (!getSession(player).handleCommand(args)) {

                    QSUtil.tell(player, "Your command was not recognized or is invalid.");

                }

                return true;

            }
        }

        player.sendMessage(ChatColor.RED + "You don't have permission for this command.");
        return true;

    }

    public QSSpoutHandler getSpoutHandler() {

        return spoutHandler;

    }

    public QSSelectionHandler getSelectionHandler() {

        return selectionHandler;

    }

    public boolean isInUse() {

        return !playerSessions.isEmpty();

    }

    public boolean isUsing(Player player) {

        return playerSessions.containsKey(player);

    }

    public QSEditSession getSession(Player player) {

        return playerSessions.get(player);

    }

    public void removeSession(Player player) {

        playerSessions.remove(player);

    }

    public Set<Entry<Player, QSEditSession>> getSessions() {

        return playerSessions.entrySet();

    }

    private void checkForWorldGuard() {

        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin != null && plugin instanceof WorldGuardPlugin) {

            log.info("[QuickSign] WorldGuard detected. Features enabled.");
            selectionHandler.setWG((WorldGuardPlugin) plugin);

        } else {

            log.info("[QuickSign] No WorldGuard detected. Features disabled.");

        }
    }

    private void checkForResidence() {

        PluginManager pm = getServer().getPluginManager();
        Plugin plugin = pm.getPlugin("Residence");

        if (plugin != null) {

            log.info("[QuickSign] Residence detected. Features enabled.");
            selectionHandler.setResidence(true);

        } else {

            log.info("[QuickSign] No Residence detected. Features disabled.");

        }
    }

    private void checkForRegios() {

        PluginManager pm = getServer().getPluginManager();
        Plugin plugin = pm.getPlugin("Regios");

        if (plugin != null) {

            log.info("[QuickSign] Regios detected. Features enabled.");
            selectionHandler.setRegiosAPI(new RegiosAPI());

        } else {

            log.info("[QuickSign] No Regios detected. Features disabled.");

        }
    }

    private void checkForLWC() {

        PluginManager pm = getServer().getPluginManager();
        Plugin plugin = pm.getPlugin("LWC");

        if (plugin != null && plugin instanceof LWCPlugin) {

            log.info("[QuickSign] LWC detected. Features enabled.");
            selectionHandler.setLWC(((LWCPlugin) plugin).getLWC());

        } else {

            log.info("[QuickSign] No LWC detected. Features disabled.");

        }
    }

    private void checkForLogBlock() {

        PluginManager pm = getServer().getPluginManager();
        Plugin plugin = pm.getPlugin("LogBlock");

        if (plugin != null && plugin instanceof LogBlock) {

            log.info("[QuickSign] LogBlock detected. Features enabled.");
            consumer = ((LogBlock) plugin).getConsumer();
            return;

        } else {

            log.info("[QuickSign] No LogBlock detected. Features disabled.");

        }
    }

    private boolean checkForSpout() {

        PluginManager pm = getServer().getPluginManager();
        Plugin plugin = pm.getPlugin("Spout");

        if (plugin != null) {

            log.info("[QuickSign] Spout detected. Features enabled.");
            spoutOn = true;
            return true;

        } else {

            log.info("[QuickSign] No Spout detected. Features disabled.");
            spoutOn = false;
            return false;

        }
    }

    public boolean hasPermissions(Player player, String permissionNodeString) {

        return permissions.hasPermission(player, permissionNodeString);

    }
}