package dan.plugin.manhunt;

import dan.plugin.manhunt.utils.OptionConstants;
import dan.plugin.manhunt.utils.OptionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Manhunt extends JavaPlugin {

    private final OptionManager optionManager = new OptionManager();
    private final ManhuntGame manhuntGame = new ManhuntGame(this, optionManager);

    private final ManhuntCommandManager commandManager = new ManhuntCommandManager(manhuntGame, optionManager);

    @Override
    public void onEnable() {

        getLogger().info("Manhunt plugin is enabled");
        optionManager.setOption(OptionConstants.PREVENT_TEAM_OVERLAP, false);
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
