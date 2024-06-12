package dan.plugin.manhunt.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ManhuntTeamManager {

    public final Team hunterTeam = new Team("hunters");
    public final Team runnerTeam = new Team("runner", 1);

    public final OptionManager optionManager;

    public ManhuntTeamManager(OptionManager optionManager) {
        this.optionManager = optionManager;
    }


    public List<String> getHunterNames() {
        if (hunterTeam.size() == 0) return Collections.emptyList();
        return hunterTeam.stream().map(Player::getName).collect(Collectors.toList());
    }

    public String getRunnerName() {
        if (runnerTeam.isEmpty()) return "No runner selected.";
        return runnerTeam.get(0).getName();
    }

    public void addHunter(Player p) {
        if (optionManager.getBooleanOption(OptionConstants.PREVENT_TEAM_OVERLAP)) {
            if (!runnerTeam.isEmpty() && runnerTeam.contains(p)) {
                runnerTeam.clear();
                MessageUtils.sendWarning("You are no longer the runner.", p);
            }
        }

        // It errors if it's not synchronized, I'm not sure why
        synchronized (hunterTeam) {
            if (!hunterTeam.contains(p)) {
                hunterTeam.add(p);
                MessageUtils.sendConfirmation("You have been added to the hunter team", p);
            }
        }
    }

    public void removeRunner() {
        if (runnerTeam.isEmpty()) return;
        Player p = runnerTeam.get(0);
        MessageUtils.sendWarning("You are no longer the runner", p);
        runnerTeam.remove(p);
    }

    public void removeHunter(Player p) {
        if (hunterTeam.contains(p)) {
            hunterTeam.remove(p);
            MessageUtils.sendWarning("You've been removed from the hunter team", p);
        }
    }

    public void setRunnerTeam(Player p) {
        if (!runnerTeam.isEmpty() && !runnerTeam.get(0).equals(p)) {
            MessageUtils.sendWarning("You are being swapped for another runner.", runnerTeam.get(0));
        }
        if (optionManager.getBooleanOption(OptionConstants.PREVENT_TEAM_OVERLAP)) {
            if (hunterTeam.contains(p)) {
                removeHunter(p);
            }
        }
        runnerTeam.clear();
        runnerTeam.add(p);
        p.sendMessage(Component.text("You are now the runner").color(NamedTextColor.GREEN));
    }
}
