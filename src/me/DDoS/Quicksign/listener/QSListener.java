package me.DDoS.Quicksign.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import me.DDoS.Quicksign.util.QSConfig;
import me.DDoS.Quicksign.session.QSEditSession;
import me.DDoS.Quicksign.permissions.QSPermissions;
import me.DDoS.Quicksign.util.QSUtil;
import me.DDoS.Quicksign.QuickSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author DDoS
 */
@SuppressWarnings("unchecked")
public class QSListener implements Listener {

    private final QuickSign plugin;
    
    public QSListener(QuickSign instance) {

        plugin = instance;
        
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        
        if (event.isCancelled()) {
            
            return;
            
        }
        
        if (!plugin.isInUse()) {

            return;

        }
        
        List<Sign> signs = getSigns(event.getBlock());

        if (signs.isEmpty()) {
            
            return;

        }

        Player player = event.getPlayer();

        for (Entry<Player, QSEditSession> entry : plugin.getSessions()) {

            QSEditSession session = entry.getValue();

            if (session.checkIfSelected(signs)) {

                if (entry.getKey().equals(player)) {

                    session.removeSign(signs);
                    QSUtil.tell(player, "Sign removed from selection, " + session.getNumberOfSigns() + " total.");
                    return;

                } else {

                    QSUtil.tell(player, "Someone is editing this sign.");
                    event.setCancelled(true);
                    return;

                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event) {

        if (QSConfig.colorSignChange) {

            if (QSUtil.checkForSign(event.getBlock())
                    && plugin.hasPermissions(event.getPlayer(), QSPermissions.COLOR_SIGN_CHANGE.getPermissionString())) {

                String[] lines = event.getLines();
                Sign sign = (Sign) event.getBlock().getState();

                event.setLine(0, lines[0].replaceAll("&([0-9[a-fA-F]])", "\u00A7$1"));
                event.setLine(1, lines[1].replaceAll("&([0-9[a-fA-F]])", "\u00A7$1"));
                event.setLine(2, lines[2].replaceAll("&([0-9[a-fA-F]])", "\u00A7$1"));
                event.setLine(3, lines[3].replaceAll("&([0-9[a-fA-F]])", "\u00A7$1"));

                sign.update();

            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!QSUtil.checkForSign(event.getClickedBlock())) {

            if (!plugin.isInUse() || !plugin.isUsing(event.getPlayer())) {

                return;

            }

            noReachSelection(event);
            return;

        }

        Player player = event.getPlayer();
        Sign sign = (Sign) event.getClickedBlock().getState();

        if (event.getAction() == QSConfig.dyeMethod) {

            if (!chatSigns(event, player, sign)) {

                colorDyes(event, player, sign);

            }

            return;

        }

        if (!plugin.isInUse() || !plugin.isUsing(event.getPlayer())) {

            return;

        }

        if (event.getAction() == QSConfig.selectionMethod) {

            event.setCancelled(true);

            if (!plugin.getSession(player).isSpoutSession()) {

                plugin.getSelectionHandler().handleSignSelection(event, sign, player);

            } else {

                plugin.getSpoutHandler().handleSpoutEditing(player, sign);

            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (plugin.isUsing(player)) {

            plugin.removeSession(player);

        }
    }

    private void colorDyes(PlayerInteractEvent event, Player player, Sign sign) {

        if (!QSConfig.colorDyes) {

            return;

        }

        if (!plugin.hasPermissions(player, QSPermissions.COLOR_DYE.getPermissionString())) {

            return;

        }

        if (!(plugin.getSelectionHandler().checkForSelectionRights(player, sign.getBlock().getLocation())
                && (!sign.getLine(0).equalsIgnoreCase("[QSCHAT]") && !sign.getLine(0).equalsIgnoreCase("[QSCMD]"))
                && player.getItemInHand().getTypeId() == 351)) {

            return;

        }

        ItemStack item = player.getItemInHand();
        ChatColor color = getColor(item.getData().getData());
        player.getInventory().removeItem(new ItemStack(351, 1, item.getDurability()));

        if (color != null) {

            sign.setLine(0, color + sign.getLine(0));
            sign.setLine(1, color + sign.getLine(1));
            sign.setLine(2, color + sign.getLine(2));
            sign.setLine(3, color + sign.getLine(3));

        } else {

            sign.setLine(0, ChatColor.stripColor(sign.getLine(0)));
            sign.setLine(1, ChatColor.stripColor(sign.getLine(1)));
            sign.setLine(2, ChatColor.stripColor(sign.getLine(2)));
            sign.setLine(3, ChatColor.stripColor(sign.getLine(3)));

        }

        sign.update();
        event.setCancelled(true);

        if (plugin.getConsumer() != null) {

            plugin.getConsumer().queueBlockPlace(player.getName(), sign);

        }
    }

    private boolean chatSigns(PlayerInteractEvent event, Player player, Sign sign) {

        if (!QSConfig.chatSigns) {

            return false;

        }
        
        String line = sign.getLine(0);

        if (line.equalsIgnoreCase(ChatColor.stripColor("[QSCHAT]"))
                && plugin.hasPermissions(player, QSPermissions.CHAT_SIGNS.getPermissionString())) {

            String chatLine = (sign.getLine(1) + sign.getLine(2) + sign.getLine(3)).replaceAll("/", "");
            chatLine = chatLine.replaceAll("\\Q{USER}\\E", player.getName());
            chatLine = chatLine.replaceAll("\\Q{USERF}\\E", player.getDisplayName());
            player.chat(chatLine);
            event.setCancelled(true);
            return true;

        } else if (line.equalsIgnoreCase(ChatColor.stripColor("[QSCMD]"))
                && plugin.hasPermissions(player, QSPermissions.COMMAND_SIGNS.getPermissionString())) {

            String chatLine = "/" + (sign.getLine(1) + sign.getLine(2) + sign.getLine(3)).replaceAll("/", "");
            chatLine = chatLine.replaceAll("\\Q{USER}\\E", player.getName());
            player.chat(chatLine);
            event.setCancelled(true);
            return true;

        } else if (line.equalsIgnoreCase(ChatColor.stripColor("[QSCCMD]"))) {
            
            String command = (sign.getLine(1) + sign.getLine(2) + sign.getLine(3)).replaceAll("/", "");
            command = command.replaceAll("\\Q{USER}\\E", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            event.setCancelled(true);
            return true;
            
        }

        return false;

    }

    private void noReachSelection(PlayerInteractEvent event) {

        if (!QSConfig.noReachLimit || event.getAction() != QSConfig.selectionMethodNoReach) {

            return;

        }

        Player player = event.getPlayer();

        if (!plugin.hasPermissions(player, QSPermissions.NO_REACH_LIMIT.getPermissionString())) {

            return;

        }

        Block block = getTargetBlock(player);

        if (!QSUtil.checkForSign(block)) {

            return;

        }

        event.setCancelled(true);
        Sign sign = (Sign) block;

        if (!plugin.getSession(player).isSpoutSession()) {

            plugin.getSelectionHandler().handleSignSelection(null, sign, player);

        } else {

            plugin.getSpoutHandler().handleSpoutEditing(player, sign);

        }
    }

    private Block getTargetBlock(Player player) {

        return player.getTargetBlock(null, QSConfig.maxReach);

    }

    private ChatColor getColor(byte data) {

        switch (data) {

            case 0x0:
                return null;

            case 0x1:
                return ChatColor.RED;

            case 0x2:
                return ChatColor.DARK_GREEN;

            case 0x3:
                return ChatColor.DARK_RED;

            case 0x4:
                return ChatColor.BLUE;

            case 0x5:
                return ChatColor.DARK_PURPLE;

            case 0x6:
                return ChatColor.DARK_AQUA;

            case 0x7:
                return ChatColor.GRAY;

            case 0x8:
                return ChatColor.DARK_GRAY;

            case 0x9:
                return ChatColor.LIGHT_PURPLE;

            case 0xa:
                return ChatColor.GREEN;

            case 0xb:
                return ChatColor.YELLOW;

            case 0xc:
                return ChatColor.BLUE;

            case 0xd:
                return ChatColor.DARK_PURPLE;

            case 0xe:
                return ChatColor.GOLD;

            case 0xf:
                return ChatColor.WHITE;

            default:
                return ChatColor.WHITE;

        }
    }

    private List<Sign> getSigns(Block block) {

        final List<Sign> signs = new ArrayList<Sign>();
        
        if (QSUtil.checkForSign(block)) {
            
            signs.add((Sign) block.getState());
            
        }
        
        if (checkForSignPost(block.getRelative(BlockFace.UP))) {

            signs.add((Sign) block.getRelative(BlockFace.UP).getState());

        }

        if (checkForWallSign(block.getRelative(BlockFace.NORTH))) {

            signs.add((Sign) block.getRelative(BlockFace.NORTH).getState());

        }

        if (checkForWallSign(block.getRelative(BlockFace.EAST))) {

            signs.add((Sign) block.getRelative(BlockFace.EAST).getState());

        }

        if (checkForWallSign(block.getRelative(BlockFace.SOUTH))) {

            signs.add((Sign) block.getRelative(BlockFace.SOUTH).getState());

        }

        if (checkForWallSign(block.getRelative(BlockFace.WEST))) {

            signs.add((Sign) block.getRelative(BlockFace.WEST).getState());

        }


        return signs;

    }
    
    private boolean checkForSignPost(Block block) {
        
        return block.getType() == Material.SIGN_POST;
        
    }
    
    private boolean checkForWallSign(Block block) {
        
        return block.getType() == Material.WALL_SIGN;
        
    }
}