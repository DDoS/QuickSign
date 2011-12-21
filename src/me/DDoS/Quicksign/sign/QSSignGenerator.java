package me.DDoS.Quicksign.sign;

import me.DDoS.Quicksign.util.QSConfig;
import me.DDoS.Quicksign.permissions.QSPermissions;
import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.util.QSUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author DDoS
 */
public class QSSignGenerator {

    private QuickSign plugin;

    public QSSignGenerator(QuickSign instance) {

        plugin = instance;

    }

    public void createSign(Player player, String positionValue, String text) {

        if (!player.getInventory().contains(Material.SIGN) && !plugin.hasPermissions(player, QSPermissions.FS_NO_INV.getPermissionString())) {

            QSUtil.tell(player, "Couldn't find any signs in your inventory.");
            return;

        }

        if (!plugin.hasPermissions(player, QSPermissions.FS_NO_INV.getPermissionString())) {

            player.getInventory().removeItem(new ItemStack(323, 1));

        }

        Block block = player.getTargetBlock(null, QSConfig.maxReach);

        if (!plugin.getSelectionHandler().checkForSelectionRights(player, block.getLocation())) {

            QSUtil.tell(player, "You do not own this block.");
            return;

        }

        if (!acceptsSigns(block.getTypeId())) {

            QSUtil.tell(player, "You cannot place a sign on this block.");
            return;

        }

        if (positionValue.equalsIgnoreCase("d")) {

            positionValue = getDefaultPos(player, block);

        }

        if (positionValue.equalsIgnoreCase("u")) {

            generateSign(block.getRelative(BlockFace.UP), Material.SIGN_POST, getFacingData(player), text, player);
            return;

        }

        if (positionValue.equalsIgnoreCase("n")) {

            generateSign(block.getRelative(BlockFace.NORTH), Material.WALL_SIGN, (byte) 0x4, text, player);
            return;

        }

        if (positionValue.equalsIgnoreCase("e")) {

            generateSign(block.getRelative(BlockFace.EAST), Material.WALL_SIGN, (byte) 0x2, text, player);
            return;

        }

        if (positionValue.equalsIgnoreCase("s")) {

            generateSign(block.getRelative(BlockFace.SOUTH), Material.WALL_SIGN, (byte) 0x5, text, player);
            return;

        }

        if (positionValue.equalsIgnoreCase("w")) {

            generateSign(block.getRelative(BlockFace.WEST), Material.WALL_SIGN, (byte) 0x3, text, player);
            return;

        }
    }

    private void generateSign(Block signLocBlock, Material material, byte data, String lines, Player player) {

        signLocBlock.setType(material);
        signLocBlock.setData(data);
        BlockState state = signLocBlock.getState();
        state.update();
        Sign sign = (Sign) state;

        if (QuickSign.consumer != null) {

            QuickSign.consumer.queueSignPlace(player.getName(), sign);

        }

        String[] splitTxt = lines.split("&/");
        int i2 = 4;

        if (splitTxt.length <= 4) {

            i2 = splitTxt.length;

        }

        boolean colors = plugin.hasPermissions(player, QSPermissions.COLOR_CMD.getPermissionString());

        if (!colors) {

            QSUtil.tell(player, "You don't have permission for colors. They will not be applied.");

        }

        for (int i = 0; i < i2; i++) {

            if (splitTxt[i].length() > 15) {

                splitTxt[i] = splitTxt[i].substring(0, 16);

            }

            if (!colors) {

                splitTxt[i] = splitTxt[i].replaceAll("&([0-9[a-fA-F]])", "");

            } else {

                splitTxt[i] = splitTxt[i].replaceAll("&([0-9[a-fA-F]])", "\u00A7$1");

            }

            sign.setLine(i, splitTxt[i]);

        }

        sign.update();
        QSUtil.tell(player, "Sign created.");

    }

    private boolean acceptsSigns(int i) {

        switch (i) {

            case 1:
                return true;

            case 2:
                return true;

            case 3:
                return true;

            case 4:
                return true;

            case 5:
                return true;

            case 7:
                return true;

            case 12:
                return true;

            case 13:
                return true;

            case 14:
                return true;

            case 15:
                return true;

            case 16:
                return true;

            case 17:
                return true;

            case 18:
                return true;

            case 19:
                return true;

            case 20:
                return true;

            case 21:
                return true;

            case 22:
                return true;

            case 24:
                return true;

            case 35:
                return true;

            case 41:
                return true;

            case 42:
                return true;

            case 43:
                return true;

            case 44:
                return true;

            case 45:
                return true;

            case 46:
                return true;

            case 47:
                return true;

            case 48:
                return true;

            case 49:
                return true;

            case 52:
                return true;

            case 53:
                return true;

            case 56:
                return true;

            case 57:
                return true;

            case 60:
                return true;

            case 67:
                return true;

            case 73:
                return true;

            case 74:
                return true;

            case 79:
                return true;

            case 80:
                return true;

            case 82:
                return true;

            case 85:
                return true;

            case 86:
                return true;

            case 87:
                return true;

            case 88:
                return true;

            case 89:
                return true;

            case 91:
                return true;

            case 97:
                return true;

            case 98:
                return true;

            case 99:
                return true;

            case 100:
                return true;

            case 101:
                return true;

            case 102:
                return true;

            case 103:
                return true;

            case 108:
                return true;

            case 109:
                return true;

            case 110:
                return true;

            case 112:
                return true;

            case 113:
                return true;

            case 114:
                return true;

            case 120:
                return true;

            case 121:
                return true;

            default:
                return false;

        }
    }

    private static float correctYaw(float yaw) {

        float angle = (yaw - 90.0f) % 360.0f;

        if (angle < 0) {

            angle += 360.0f;

        }

        return angle;

    }

    private BlockFace getFacing(Player player) {

        int r = (int) correctYaw(player.getLocation().getYaw());

        if (r < 46) {

            return BlockFace.NORTH;

        } else if (r < 136) {

            return BlockFace.EAST;

        } else if (r < 226) {

            return BlockFace.SOUTH;

        } else if (r < 316) {

            return BlockFace.WEST;

        } else {

            return BlockFace.NORTH;

        }
    }

    private BlockFace getPreciseFacing(Player player) {

        int r = (int) correctYaw(player.getLocation().getYaw());

        if (r < 23) {

            return BlockFace.NORTH;

        } else if (r < 68) {

            return BlockFace.NORTH_EAST;

        } else if (r < 113) {

            return BlockFace.EAST;

        } else if (r < 158) {

            return BlockFace.SOUTH_EAST;

        } else if (r < 203) {

            return BlockFace.SOUTH;

        } else if (r < 248) {

            return BlockFace.SOUTH_WEST;

        } else if (r < 293) {

            return BlockFace.WEST;

        } else if (r < 338) {

            return BlockFace.NORTH_WEST;

        } else {

            return BlockFace.NORTH;

        }
    }

    private boolean checkBlockFace(Block block, BlockFace blockFace) {

        if (block.getRelative(blockFace).getTypeId() == 0) {

            return true;

        }

        return false;

    }

    private byte getFacingData(Player player) {

        switch (getPreciseFacing(player)) {

            case SOUTH:
                return 0x4;

            case SOUTH_WEST:
                return 0x6;

            case WEST:
                return 0x8;

            case NORTH_WEST:
                return 0xA;

            case NORTH:
                return 0xC;

            case NORTH_EAST:
                return 0xE;

            case EAST:
                return 0x0;

            case SOUTH_EAST:
                return 0x2;

            default:
                return 0x0;

        }
    }

    private String getDefaultPos(Player player, Block block) {

        switch (getFacing(player)) {

            case NORTH:

                if (checkBlockFace(block, BlockFace.SOUTH)) {

                    return "s";

                } else {

                    return "u";

                }

            case EAST:

                if (checkBlockFace(block, BlockFace.WEST)) {

                    return "w";

                } else {

                    return "u";

                }

            case SOUTH:

                if (checkBlockFace(block, BlockFace.NORTH)) {

                    return "n";

                } else {

                    return "u";

                }

            case WEST:

                if (checkBlockFace(block, BlockFace.EAST)) {

                    return "e";

                } else {

                    return "u";

                }

            default:
                return "u";

        }
    }
}