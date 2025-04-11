package solutions.brilliant.proceduralGeneration.game.rules;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.game.Rule;

import java.time.Duration;

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
                "pex user " + player.getName() + " group set spectator");
        player.teleport(
                new Location(player.getWorld(), player.getLocation().getX(), 10, player.getLocation().getZ())
        );
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("bsg.civilian") || p.hasPermission("bsg.murderer"))
                p.hidePlayer(plugin, player);
        }
        player.setAllowFlight(true);
        player.setFlying(true);

        Title looseTitle = Title.title(
                Component.text("Вы проиграли")
                        .color(TextColor.color(0xff0000)),

                Component.text("Дождитесь начала новой игры")
                        .color(TextColor.color(0xffffff)),
                Title.Times.times(
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(5),
                        Duration.ofSeconds(1)
                )
        );
        player.showTitle(looseTitle);
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT, 1, 1);
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
