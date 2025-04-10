package solutions.brilliant.proceduralGeneration.game.rules;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.game.Rule;

import java.util.logging.Level;

public class RulePause implements Rule {

    private final Plugin plugin;

    public RulePause(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enter() {
        World world = Bukkit.getWorld("field");

        if (world == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Мир не создан");
            return;
        }

        Location location = new Location(world, 0, 51, 0);
        world.setDifficulty(Difficulty.PEACEFUL);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(location);
            player.setGameMode(GameMode.ADVENTURE);
        }
    }

    @Override
    public void event(Event event) {

    }

    @Override
    public void tick() {

    }



}
