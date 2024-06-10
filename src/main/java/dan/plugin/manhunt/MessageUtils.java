package dan.plugin.manhunt;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public abstract class MessageUtils {

    public static void sendConfirmation(String simpleMessage, Audience recipient) {
        sendMessage(simpleMessage, recipient, NamedTextColor.GREEN);
    }

    public static void sendWarning(String simpleMessage, Audience recipient) {
        sendMessage(simpleMessage, recipient, NamedTextColor.YELLOW);
    }

    public static void sendError(String simpleMessage, Audience recipient) {
        sendMessage(simpleMessage, recipient, NamedTextColor.RED);
    }

    public static void sendMessage(String simpleMessage, Audience recipient, NamedTextColor color) {
        recipient.sendMessage(Component.text(simpleMessage).color(color));
    }

}
