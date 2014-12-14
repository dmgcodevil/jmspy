package com.github.dmgcodevil.jmspy.ui

import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.stage.Popup
import javafx.stage.Stage
import javafx.stage.WindowEvent

/**
 * Created by dmgcodevil on 12/14/2014.
 */
final class UIUtils {
    private UIUtils() {

    }

    public static String POPUP_WARN = "popupWarn";
    public static String POPUP = "popup";

    public static Popup createSimplePopup(final String message, final String style) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(message);
        label.setOnMouseReleased({ MouseEvent e -> popup.hide() });
        label.getStylesheets().add("/css/styles.css");
        label.getStyleClass().add(style);
        popup.getContent().add(label);
        return popup;
    }

    public static Popup createSimplePopup(final String message) {
        createSimplePopup(message, POPUP)
    }

    public static void showPopupMessage(final String message, final Stage stage) {
        showPopupMessage(createSimplePopup(message), stage)
    }

    public static void showPopupMessage(final Popup popup, final Stage stage) {
        popup.setOnShown({ WindowEvent e ->
            popup.setX(stage.getX() + stage.getWidth() / 2 - popup.getWidth() / 2);
            popup.setY(stage.getY() + stage.getHeight() / 2 - popup.getHeight() / 2);
        });
        popup.show(stage);
    }
}
