package me.DDoS.Quicksign.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import me.DDoS.Quicksign.util.QSConfig;
import me.DDoS.Quicksign.session.QSEditSession;
import me.DDoS.Quicksign.permissions.QSPermissions;
import me.DDoS.Quicksign.util.QSUtil;
import me.DDoS.Quicksign.QuickSign;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author DDoS
 */
@SuppressWarnings("unchecked")
public class QSBlockListener extends BlockListener {

    private QuickSign plugin;
    
    public QSBlockListener(QuickSign instance) {

        plugin = instance;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        
        if (event.isCancelled()) {
            
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

    @Override
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