package me.DDoS.Quicksign.handler;

import me.DDoS.Quicksign.session.SpoutEditSession;
import me.DDoS.Quicksign.util.QSUtil;
import java.util.UUID;

import me.DDoS.Quicksign.QuickSign;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 *
 * @author DDoS
 */
public class SpoutHandler {

    private final QuickSign plugin;

    public SpoutHandler(QuickSign plugin) {

        this.plugin = plugin;

    }

    public void handleSpoutEditing(Player player, Sign sign) {

        if (!plugin.getSelectionHandler().checkForSelectionRights(player, sign.getBlock())) {

            QSUtil.tell(player, "You don't own this sign.");
            return;

        }

        if (!plugin.getBlackList().allows(sign, player)) {

            QSUtil.tell(player, "You cannot edit this sign: its contents are blacklisted.");
            return;

        }

        SpoutPlayer spoutPlayer = SpoutManager.getPlayer(player);

        PopupScreen popup = new GenericPopup();

        int y = 30;
        UUID[] widgets = new UUID[5];

        for (int i = 0; i < 4; i++) {

            GenericTextField textField = new GenericTextField();

            String line0 = sign.getLine(i).replaceAll("\u00A7([0-9a-fA-Fk-oK-OrR])", "&$1");
            textField.setText(line0).setCursorPosition(line0.length() - 1).setFieldColor(new Color(0, 0, 0, 1.0F));
            textField.setBorderColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
            textField.setMaximumCharacters(15).setHeight(20).setWidth(100);
            textField.setX(170).setY(y);
            textField.setAnchor(WidgetAnchor.SCALE);
            popup.attachWidget(plugin, textField);
            y += 30;
            widgets[i] = textField.getId();

        }

        GenericButton button = new GenericButton("Done");

        button.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        button.setHoverColor(new Color(1.0F, 1.0F, 0, 1.0F));
        button.setX(170).setY(150);
        button.setWidth(100).setHeight(20);
        widgets[4] = button.getId();

        popup.attachWidget(plugin, button);

        attachColorLabels(popup);

        popup.setTransparent(true);

        spoutPlayer.getMainScreen().attachPopupScreen(popup);

        SpoutEditSession session = (SpoutEditSession) plugin.getSession(player);

        session.setPopup(popup);
        session.addSign(sign);
        session.setWidgets(widgets);

    }

    private void attachColorLabels(PopupScreen popup) {

        int y = 5;

        for (ChatColor color : ChatColor.values()) {

            GenericLabel label = new GenericLabel();
            label.setText((color.isFormat() ? "" : color)
                    + color.name().replace('_', ' ').toLowerCase()
                    + ChatColor.RESET + ": &"
                    + color.getChar());
            label.setHeight(25).setWidth(80).setX(300).setY(y);
            popup.attachWidget(plugin, label);
            y += 10;

        }
    }
}
