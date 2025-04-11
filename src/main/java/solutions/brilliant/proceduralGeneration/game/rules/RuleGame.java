package solutions.brilliant.proceduralGeneration.game.rules;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.game.Rule;

import java.util.logging.Level;

public class RuleGame implements Rule {
    private final Plugin plugin;

    public RuleGame(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enter() {

    }

    @Override
    public void event(Event event) {
        if (event.getClass() == PlayerMoveEvent.class)
            onPlayerMove((PlayerMoveEvent) event);
    }

    @Override
    public void tick() {

    }

    private void sendPlayerToSpectator(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                "lp user " + player.getName() + " parent set spectator");
        player.teleport(
                new Location(player.getWorld(), player.getLocation().getX(), 3, player.getLocation().getZ())
        );
        player.setGameMode(GameMode.ADVENTURE);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("bsg.civilian") || p.hasPermission("bsg.murderer"))
                p.hidePlayer(plugin, player);
        }
        player.setFlying(true);
    }

    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getBlockY() <= 0) {
            if (player.hasPermission("bsg.civilian") || player.hasPermission("bsg.spectator")) {
                sendPlayerToSpectator(player);
            }
        }
    }
}
