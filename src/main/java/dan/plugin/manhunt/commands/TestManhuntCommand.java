package dan.plugin.manhunt.commands;

import dan.plugin.manhunt.ManhuntGame;
import dan.plugin.manhunt.utils.MessageUtils;
import dan.plugin.manhunt.utils.OptionConstants;
import dan.plugin.manhunt.utils.OptionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TestManhuntCommand extends BaseManhuntCommand{

    public TestManhuntCommand(ManhuntGame game, OptionManager optionManager) {
        super(game, optionManager);
    }

    //for my testing purposes because unit testing for this thing is WACK.
    //This will go through the flow of the commands.
    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        AddHunterCommand addHunter = new AddHunterCommand(manhuntGame, optionManager);
        SetRunnerCommand addRunner = new SetRunnerCommand(manhuntGame, optionManager);
        RemoveHunterCommand removeHunter = new RemoveHunterCommand(manhuntGame, optionManager);
        RemoveRunnerCommand removeRunner = new RemoveRunnerCommand(manhuntGame, optionManager);
        StartManhuntCommand startGame = new StartManhuntCommand(manhuntGame, optionManager);
        StopManhuntCommand stopGame = new StopManhuntCommand(manhuntGame, optionManager);

        optionManager.setOption(OptionConstants.PREVENT_TEAM_OVERLAP, false);
        String[] playerArg = new String[]{sender.getName()};
        addHunter.execute(sender, command, label, playerArg);
        addRunner.execute(sender, command, label, playerArg);

        //should be true
        boolean matching = this.manhuntGame.getHunterNames().get(0).equalsIgnoreCase(this.manhuntGame.getRunnerName());
        MessageUtils.sendConfirmation("Players Match: " + matching, sender);

        startGame.execute(sender, command, label, args);
        stopGame.execute(sender, command, label, args);

        //should not start
        optionManager.setOption(OptionConstants.PREVENT_TEAM_OVERLAP, true);
        MessageUtils.sendConfirmation("Should not start:", sender);
        startGame.execute(sender, command, label, args);
        stopGame.execute(sender, command, label, args);

        //adding a runner with enforcement on removes the hunter
        addRunner.execute(sender, command, label, playerArg);
        int hunterCount = this.manhuntGame.getHunterNames().size();

        MessageUtils.sendConfirmation("Should have 0 hunters: " + hunterCount, sender);
        MessageUtils.sendConfirmation("Should have 1 runner: " + this.manhuntGame.getRunnerName(), sender);

        //adding a hunter now. Should remove the runner.
        addHunter.execute(sender, command, label, playerArg);

        hunterCount = this.manhuntGame.getHunterNames().size();
        MessageUtils.sendConfirmation("Should have 1 hunter: " + hunterCount, sender);
        MessageUtils.sendConfirmation("Should have 0 runner: " + this.manhuntGame.getRunnerName(), sender);

        return new ManhuntInfoCommand(manhuntGame, optionManager).execute(sender, command, label, args);
    }
}
