package me.DDoS.Quicksign.sign;

import me.DDoS.Quicksign.QuickSign;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftSign;

/**
 *
 * @author DDoS
 */
public class QSSign extends CraftSign implements Sign {

    public QSSign(final Block block) {

        super(block);

    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {

            return true;

        }

        if (!(o instanceof CraftSign)) {

            return false;

        }

        CraftBlockState other = (CraftSign) o;

        return this.getX() == other.getX() && this.getY() == other.getY() && this.getZ() == other.getZ()
                && this.getData().equals(other.getData()) && this.getWorld().equals(other.getWorld());

    }

    @Override
    public int hashCode() {

        return this.getY() << 24 ^ this.getX() ^ this.getZ() ^ this.getData().hashCode() ^ this.getWorld().hashCode();

    }
}
