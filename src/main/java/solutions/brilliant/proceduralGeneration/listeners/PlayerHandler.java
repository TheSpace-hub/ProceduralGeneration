package solutions.brilliant.proceduralGeneration.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.game.RuleExecutor;

public class PlayerHandler implements Listener {

    private Plugin plugin;

    public PlayerHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = Bukkit.getWorld("field");

        player.teleport(
                new Location(world, 0, 51, 0)
        );
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        RuleExecutor.getInstance().event(event);
    }

    @EventHandler
    public void onBlockBreakByPlayer(BlockBreakEvent event) {
        RuleExecutor.getInstance().event(event);
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        RuleExecutor.getInstance().event(event);
    }
}
