package solutions.brilliant.proceduralGeneration.game.rules;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.game.Role;
import solutions.brilliant.proceduralGeneration.game.Rule;
import solutions.brilliant.proceduralGeneration.game.RuleExecutor;

import java.time.Duration;
import java.util.List;

public class RuleGame implements Rule {
    private final Plugin plugin;

    public RuleGame(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enter() {
        giveItemsToMurderer();
    }

    @Override
    public void event(Event event) {
        if (event.getClass() == PlayerMoveEvent.class)
            onPlayerMove((PlayerMoveEvent) event);
    }

    @Override
    public void tick() {

    }

    private void giveItemsToMurderer() {
        Player murderer = null;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (RuleExecutor.getInstance(plugin).getPlayerRole(player) == Role.MURDERER) {
                murderer = player;
                break;
            }
        }
        assert murderer != null;

        ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
        ItemMeta axeMeta = axe.getItemMeta();
        axeMeta.setUnbreakable(true);
        axeMeta.displayName(Component.text("Топор").color(TextColor.color(0xaa0000)));
        axeMeta.lore(List.of(
                Component.text("Топор убирает игрока с одного удара"),
                Component.text("Подожди 1 сек. чтобы ударить снова")
        ));
        axe.setItemMeta(axeMeta);

        murderer.getInventory().setItem(0, axe);
    }

    private void sendPlayerToSpectator(Player player) {
        RuleExecutor.getInstance(plugin).setPlayerRole(player, Role.SPECTATOR);
        player.teleport(
                new Location(player.getWorld(), player.getLocation().getX(), 10, player.getLocation().getZ())
        );
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (RuleExecutor.getInstance(plugin).getPlayerRole(player) == Role.CIVILIAN ||
                    RuleExecutor.getInstance(plugin).getPlayerRole(player) == Role.MURDERER)
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
            if (RuleExecutor.getInstance(plugin).getPlayerRole(player) == Role.CIVILIAN ||
                    RuleExecutor.getInstance(plugin).getPlayerRole(player) == Role.SPECTATOR) {
                sendPlayerToSpectator(player);
            }
        }
    }
}
