package solutions.brilliant.proceduralGeneration.game.rules;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.game.Rule;

import java.util.logging.Level;

public class DefaultRules implements Rule {

    private final Plugin plugin;

    public DefaultRules(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enter() {

    }

    @Override
    public void event(Event event) {
        if (event.getClass() == PlayerMoveEvent.class)
            onPlayerMove((PlayerMoveEvent) event);
        if (event.getClass() == BlockBreakEvent.class)
            onBlockBreakByPlayer((BlockBreakEvent) event);
        if (event.getClass() == BlockPlaceEvent.class)
            onPlayerPlaceBlock((BlockPlaceEvent) event);
        if (event.getClass() == PlayerJoinEvent.class)
            onPlayerJoin((PlayerJoinEvent) event);
    }

    @Override
    public void tick() {

    }

    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getBlockY() <= 0) {
            if (player.hasPermission("bsg.lobby"))
                player.teleport(
                        new Location(player.getWorld(), 0, 51, 0)
                );
        }
    }

    private void onBlockBreakByPlayer(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp())
            event.setCancelled(true);
    }

    private void onPlayerPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp())
            event.setCancelled(true);
    }

    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = Bukkit.getWorld("field");

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                "lp user " + player.getName() + " parent set lobby");

        if (world == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Мир не создан");
            return;
        }

        Location location = new Location(world, 0, 51, 0);
        player.teleport(location);
        player.setGameMode(GameMode.ADVENTURE);
        player.setExp(0);
        player.setLevel(0);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setFlying(false);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(plugin, player);
        }
    }
}
