package solutions.brilliant.proceduralGeneration.game.rules;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import solutions.brilliant.proceduralGeneration.game.Role;
import solutions.brilliant.proceduralGeneration.game.Rule;
import solutions.brilliant.proceduralGeneration.game.RuleExecutor;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

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
        if (event.getClass() == PlayerDropItemEvent.class)
            onPlayerDropItem((PlayerDropItemEvent) event);
        if (event.getClass() == PlayerInteractEvent.class)
            onPlayerInteract((PlayerInteractEvent) event);
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
        axeMeta.displayName(Component.text("Топор")
                .color(TextColor.color(0xaa0000))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        );
        axeMeta.lore(List.of(
                Component.text("Топор убирает игрока с одного удара")
                        .color(TextColor.color(0xffffff))
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                Component.text("Подожди 1 сек. чтобы ударить снова")
                        .color(TextColor.color(0xffffff))
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
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

    private void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (RuleExecutor.getInstance(plugin).getPlayerRole(player) == Role.MURDERER)
            event.setCancelled(true);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() != null && event.getAction().isRightClick()) {
            ItemStack item = event.getItem();
            if (item.getType() == Material.IRON_AXE) {
                Block block = player.getTargetBlock(120);
                if (block == null) {
                    player.sendMessage(getTeleportByAxeTip());
                    return;
                }
                Location location = block.getLocation();
                location.add(new Vector(0, 1, 0));
                player.teleport(location);
            }
        }
    }

    private Component getTeleportByAxeTip() {
        return Component.textOfChildren(
                Component.text("BS")
                        .color(TextColor.color(0xff6a00))
                        .decorate(TextDecoration.BOLD),
                Component.text(" >> ")
                        .color(TextColor.color(0xffffff)),
                Component.text("Телепортируйся с помощью топора, посмотрев на блок")
                        .color(TextColor.color(0xffffff))
        );
    }
}
