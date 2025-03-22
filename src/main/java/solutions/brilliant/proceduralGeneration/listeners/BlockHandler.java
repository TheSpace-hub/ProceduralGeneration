package solutions.brilliant.proceduralGeneration.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

public class BlockHandler implements Listener {

    private final Plugin plugin;

    public BlockHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        World world = event.getBlock().getWorld();

        event.setCancelled(!(player.isOp() && player.getGameMode() == GameMode.CREATIVE));

        if (world.getName().split("_")[0].equals("image")) {
            Location location = event.getBlock().getLocation();
            if (location.getBlockX() == 0 && location.getBlockY() == 50 && location.getBlockZ() == 0) {
                event.setCancelled(true);
                Material newMaterial = event.getPlayer().getInventory().getItemInMainHand().getType();
                if (newMaterial.isBlock() && newMaterial != Material.AIR && player.isOp())
                    location.getBlock().setType(newMaterial);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(!(player.isOp() && player.getGameMode() == GameMode.CREATIVE));
    }

}
