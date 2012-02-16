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
import me.DDoS.Quicksign.sign.QSSignGenerator;
import me.DDoS.Quicksign.util.*;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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
    private final QSSelectionHandler selectionHandler = new QSSelectionHandler(this);
    //
    private final QSSignGenerator signGenerator = new QSSignGenerator(this);
    //
    private QSSpoutHandler spoutHandler;
    private boolean spoutOn = false;
    //
    private final Map<Player, QSEditSession> sessions = new HashMap<Player, QSEditSession>();
    //
    private Consumer consumer;
    //
    private final QSBlackList blackList = new QSBlackList(this);

    @Override
    public void onEnable() {

        permissions = new PermissionsHandler(this).getPermissions();

        getServer().getPluginManager().registerEvents(new QSListener(this), this);

        checkForWorldGuard();
        checkForResidence();
        checkForRegios();
        checkForLogBlock();
        checkForLWC();

        if (checkForSpout()) {

            spoutHandler = new QSSpoutHandler(this);
            getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, new QSScreenListener(this), Event.Priority.Normal, this);

        }

        new QSConfig().setupConfig(this);

        log.info("[QuickSign] Plugin enabled. v" + getDescription().getVersion() + ", by DDoS");

    }

    @Override
    public void onDisable() {

        sessions.clear();
        log.info("[QuickSign] Plugin disabled. v" + getDescription().getVersion() + ", by DDoS");

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

                sessions.put(player, new QSStandardEditSession(player, this));
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

                    if (!spoutOn) {
                        
                        QSUtil.tell(player, "Spout is not installed on the server.");
                        return true;
                        
                    }
                    
                    if (isUsing(player)) {

                        QSUtil.tell(player, "Please disable QuickSign, and renable with '/qs spout'.");
                        return true;

                    } else {

                        sessions.put(player, new QSSpoutEditSession(player, this));
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

                    selectionHandler.handleSignSelection(null, (Sign) block.getState(), player);
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

        return !sessions.isEmpty();

    }

    public boolean isUsing(Player player) {

        return sessions.containsKey(player);

    }

    public QSEditSession getSession(Player player) {

        return sessions.get(player);

    }

    public void removeSession(Player player) {

        sessions.remove(player);

    }

    public Set<Entry<Player, QSEditSession>> getSessions() {

        return sessions.entrySet();

    }
    
    public QSBlackList getBlackList() {
        
        return blackList;
        
    }
    
    public boolean isSpoutOn() {
        
        return spoutOn;
        
    }
    
    public void setSpoutOn(boolean spoutOn) {
        
        this.spoutOn = spoutOn;
        
    }

    public Consumer getConsumer() {

        return consumer;

    }

    public void setConsumer(Consumer consumer) {

        this.consumer = consumer;

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