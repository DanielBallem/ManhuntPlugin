package dan.plugin.manhunt.utils;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Set;

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

    public static void sendAllPlayersAMessage(String simpleMessage, Set<Audience> recipients, NamedTextColor color){
        for (Audience a : recipients) {
            sendMessage(simpleMessage, a, color);
        }
    }

}
