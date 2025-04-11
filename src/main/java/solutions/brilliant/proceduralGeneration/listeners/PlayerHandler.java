package solutions.brilliant.proceduralGeneration.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.game.RuleExecutor;

public class PlayerHandler implements Listener {

    private final Plugin plugin;

    public PlayerHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        RuleExecutor.getInstance(plugin).event(event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        RuleExecutor.getInstance(plugin).event(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        RuleExecutor.getInstance(plugin).event(event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        RuleExecutor.getInstance(plugin).event(event);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        RuleExecutor.getInstance(plugin).event(event);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        RuleExecutor.getInstance(plugin).event(event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        RuleExecutor.getInstance(plugin).event(event);
    }

}
