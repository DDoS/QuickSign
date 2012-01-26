package me.DDoS.Quicksign.handler;

import me.DDoS.Quicksign.session.QSSpoutEditSession;
import me.DDoS.Quicksign.util.QSUtil;
import java.util.UUID;

import me.DDoS.Quicksign.QuickSign;
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
public class QSSpoutHandler {

    private final QuickSign plugin;

    public QSSpoutHandler(QuickSign plugin) {

        this.plugin = plugin;

    }

    public void handleSpoutEditing(Player player, Sign sign) {

        if (!plugin.getSelectionHandler().checkForSelectionRights(player, sign.getBlock().getLocation())) {

            QSUtil.tell(player, "You don't own this sign.");
            return;

        }

        SpoutPlayer spoutPlayer = SpoutManager.getPlayer(player);

        PopupScreen popup = new GenericPopup();

        GenericTextField textField0 = new GenericTextField();

        String line0 = sign.getLine(0).replaceAll("\\Q\u00A7\\E([0-9[a-fA-F]])", "&$1");
        textField0.setText(line0).setCursorPosition(line0.length() - 1).setFieldColor(new Color(0, 0, 0, 1.0F));
        textField0.setBorderColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        textField0.setMaximumCharacters(15).setHeight(20).setWidth(100);
        textField0.setX(170).setY(30);
        textField0.setAnchor(WidgetAnchor.SCALE);

        GenericTextField textField1 = new GenericTextField();

        String line1 = sign.getLine(1).replaceAll("\\Q\u00A7\\E([0-9[a-fA-F]])", "&$1");
        textField1.setText(line1).setCursorPosition(line1.length() - 1).setFieldColor(new Color(0, 0, 0, 1.0F));
        textField1.setBorderColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        textField1.setMaximumCharacters(15).setHeight(20).setWidth(100);
        textField1.setX(170).setY(60);
        textField1.setAnchor(WidgetAnchor.SCALE);


        GenericTextField textField2 = new GenericTextField();

        String line2 = sign.getLine(2).replaceAll("\\Q\u00A7\\E([0-9[a-fA-F]])", "&$1");
        textField2.setText(line2).setCursorPosition(line2.length() - 1).setFieldColor(new Color(0, 0, 0, 1.0F));
        textField2.setBorderColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        textField2.setMaximumCharacters(15).setHeight(20).setWidth(100);
        textField2.setX(170).setY(90);
        textField2.setAnchor(WidgetAnchor.SCALE);

        GenericTextField textField3 = new GenericTextField();

        String line3 = sign.getLine(3).replaceAll("\\Q\u00A7\\E([0-9[a-fA-F]])", "&$1");
        textField3.setText(line3).setCursorPosition(line3.length() - 1).setFieldColor(new Color(0, 0, 0, 1.0F));
        textField3.setBorderColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        textField3.setMaximumCharacters(15).setHeight(20).setWidth(100);
        textField3.setX(170).setY(120);
        textField3.setAnchor(WidgetAnchor.SCALE);

        GenericButton button = new GenericButton("Done");

        button.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        button.setHoverColor(new Color(1.0F, 1.0F, 0, 1.0F));
        button.setX(170).setY(150);
        button.setWidth(100).setHeight(20);

        popup.attachWidget(plugin, textField0);
        popup.attachWidget(plugin, textField1);
        popup.attachWidget(plugin, textField2);
        popup.attachWidget(plugin, textField3);
        popup.attachWidget(plugin, button);

        attachColorLabels(popup);

        popup.setTransparent(true);

        spoutPlayer.getMainScreen().attachPopupScreen(popup);

        QSSpoutEditSession session = (QSSpoutEditSession) plugin.getSession(player);

        session.setPopup(popup);
        session.addSign(sign);

        UUID[] widgets = {textField0.getId(), textField1.getId(), textField2.getId(), textField3.getId(), button.getId()};
        session.setWidgets(widgets);

    }

    private void attachColorLabels(PopupScreen popup) {

        GenericLabel labelBlack = new GenericLabel();
        labelBlack.setText("Black: &0").setHeight(25).setWidth(80).setX(300).setY(5);
        labelBlack.setTextColor(new Color(0, 0, 0, 0));
        popup.attachWidget(plugin, labelBlack);

        GenericLabel labelDarkBlue = new GenericLabel();
        labelDarkBlue.setText("Dark blue: &1").setHeight(25).setWidth(80).setX(300).setY(15);
        labelDarkBlue.setTextColor(new Color(0, 0, 170, 0));
        popup.attachWidget(plugin, labelDarkBlue);

        GenericLabel labelDarkGreen = new GenericLabel();
        labelDarkGreen.setText("Dark green: &2").setHeight(25).setWidth(80).setX(300).setY(25);
        labelDarkGreen.setTextColor(new Color(0, 170, 0, 0));
        popup.attachWidget(plugin, labelDarkGreen);

        GenericLabel labelDarkAqua = new GenericLabel();
        labelDarkAqua.setText("Dark aqua: &3").setHeight(25).setWidth(80).setX(300).setY(35);
        labelDarkAqua.setTextColor(new Color(0, 170, 170, 0));
        popup.attachWidget(plugin, labelDarkAqua);

        GenericLabel labelDarkRed = new GenericLabel();
        labelDarkRed.setText("Dark red: &4").setHeight(25).setWidth(80).setX(300).setY(45);
        labelDarkRed.setTextColor(new Color(170, 0, 0, 0));
        popup.attachWidget(plugin, labelDarkRed);

        GenericLabel labelDarkPurple = new GenericLabel();
        labelDarkPurple.setText("Dark purple: &5").setHeight(25).setWidth(80).setX(300).setY(55);
        labelDarkPurple.setTextColor(new Color(170, 0, 170, 0));
        popup.attachWidget(plugin, labelDarkPurple);

        GenericLabel labelGold = new GenericLabel();
        labelGold.setText("Gold: &6").setHeight(25).setWidth(80).setX(300).setY(65);
        labelGold.setTextColor(new Color(255, 187, 51, 0));
        popup.attachWidget(plugin, labelGold);

        GenericLabel labelGray = new GenericLabel();
        labelGray.setText("Gray: &7").setHeight(25).setWidth(80).setX(300).setY(75);
        labelGray.setTextColor(new Color(190, 190, 190, 0));
        popup.attachWidget(plugin, labelGray);

        GenericLabel labelDarkGray = new GenericLabel();
        labelDarkGray.setText("Dark gray: &8").setHeight(25).setWidth(80).setX(300).setY(85);
        labelDarkGray.setTextColor(new Color(125, 125, 125, 0));
        popup.attachWidget(plugin, labelDarkGray);

        GenericLabel labelBlue = new GenericLabel();
        labelBlue.setText("Blue: &9").setHeight(25).setWidth(80).setX(300).setY(95);
        labelBlue.setTextColor(new Color(0, 0, 255, 0));
        popup.attachWidget(plugin, labelBlue);

        GenericLabel labelGreen = new GenericLabel();
        labelGreen.setText("Green: &a").setHeight(25).setWidth(80).setX(300).setY(105);
        labelGreen.setTextColor(new Color(0, 255, 0, 0));
        popup.attachWidget(plugin, labelGreen);

        GenericLabel labelAqua = new GenericLabel();
        labelAqua.setText("Aqua: &b").setHeight(25).setWidth(80).setX(300).setY(115);
        labelAqua.setTextColor(new Color(0, 255, 255, 0));
        popup.attachWidget(plugin, labelAqua);

        GenericLabel labelRed = new GenericLabel();
        labelRed.setText("Red: &c").setHeight(25).setWidth(80).setX(300).setY(125);
        labelRed.setTextColor(new Color(255, 0, 0, 0));
        popup.attachWidget(plugin, labelRed);

        GenericLabel labelLightPurple = new GenericLabel();
        labelLightPurple.setText("Light purple: &d").setHeight(25).setWidth(80).setX(300).setY(135);
        labelLightPurple.setTextColor(new Color(255, 0, 255, 0));
        popup.attachWidget(plugin, labelLightPurple);

        GenericLabel labelYellow = new GenericLabel();
        labelYellow.setText("Yellow: &e").setHeight(25).setWidth(80).setX(300).setY(145);
        labelYellow.setTextColor(new Color(255, 255, 0, 0));
        popup.attachWidget(plugin, labelYellow);

        GenericLabel labelWhite = new GenericLabel();
        labelWhite.setText("White: &f").setHeight(25).setWidth(80).setX(300).setY(155);
        labelWhite.setTextColor(new Color(255, 255, 255, 0));
        popup.attachWidget(plugin, labelWhite);

    }
}
