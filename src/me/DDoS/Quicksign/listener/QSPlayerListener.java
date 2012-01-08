package me.DDoS.Quicksign.listener;

import me.DDoS.Quicksign.util.QSConfig;
import me.DDoS.Quicksign.permissions.QSPermissions;
import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.sign.QSSign;
import me.DDoS.Quicksign.util.QSUtil;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author DDoS
 */
public class QSPlayerListener extends PlayerListener {

    private QuickSign plugin;

    public QSPlayerListener(QuickSign instance) {

        plugin = instance;

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!QSUtil.checkForSign(event.getClickedBlock())) {

            if (!plugin.isInUse() || !plugin.isUsing(event.getPlayer())) {

                return;

            }

            noReachSelection(event);
            return;

        }

        Player player = event.getPlayer();
        Sign sign = new QSSign(event.getClickedBlock());

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

            if (!plugin.getSession(player).isSpoutSession() || !plugin.isSpoutOn()) {

                plugin.getSelectionHandler().handleSignSelection(event, sign, player);

            } else {

                plugin.getSpoutHandler().handleSpoutEditing(player, sign);

            }
        }
    }

    @Override
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

        return;

    }

    private boolean chatSigns(PlayerInteractEvent event, Player player, Sign sign) {

        if (!QSConfig.chatSigns) {

            return false;

        }

        if (sign.getLine(0).equalsIgnoreCase(ChatColor.stripColor("[QSCHAT]"))
                && plugin.hasPermissions(player, QSPermissions.CHAT_SIGNS.getPermissionString())) {

            String chatLine = (sign.getLine(1) + sign.getLine(2) + sign.getLine(3)).replaceAll("/", "");
            player.chat(chatLine);
            event.setCancelled(true);
            return true;

        } else if (sign.getLine(0).equalsIgnoreCase(ChatColor.stripColor("[QSCMD]"))
                && plugin.hasPermissions(player, QSPermissions.COMMAND_SIGNS.getPermissionString())) {

            String chatLine = ("/" + (sign.getLine(1) + sign.getLine(2) + sign.getLine(3)).replaceAll("/", ""));
            player.chat(chatLine);
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

        Sign sign = new QSSign(block);

        if (!plugin.getSession(player).isSpoutSession() || !plugin.isSpoutOn()) {

            plugin.getSelectionHandler().handleSignSelection(null, sign, player);
            return;


        } else {

            plugin.getSpoutHandler().handleSpoutEditing(player, sign);
            event.setCancelled(true);

        }

        return;

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
}
