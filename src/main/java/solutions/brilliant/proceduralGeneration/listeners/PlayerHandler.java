package solutions.brilliant.proceduralGeneration.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

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
        Player player = event.getPlayer();
        if (player.getLocation().getBlockY() <= 0) {
            player.teleport(
                    new Location(player.getWorld(), 0, 50, 0)
            );
        }
    }

}
