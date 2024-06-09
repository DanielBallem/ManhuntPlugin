package dan.plugin.manhunt.interfaces;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerAction {
    void execute(Player player);
}
