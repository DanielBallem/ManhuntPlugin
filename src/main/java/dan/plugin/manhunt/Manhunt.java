package dan.plugin.manhunt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Manhunt extends JavaPlugin {

    private final ManhuntGame manhuntGame = new ManhuntGame(this);
    private final ManhuntCommandManager commandManager = new ManhuntCommandManager(manhuntGame);

    @Override
    public void onEnable() {
        getLogger().info("Manhunt plugin is enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Manhunt plugin is enabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commandManager.onCommand(sender, command, label, args);
    }
}
