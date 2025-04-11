package solutions.brilliant.proceduralGeneration.game.rules;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.commands.GenerateField;
import solutions.brilliant.proceduralGeneration.config.CustomField;
import solutions.brilliant.proceduralGeneration.game.Rule;
import solutions.brilliant.proceduralGeneration.game.RuleExecutor;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class RulePreparingForGame implements Rule {

    private final Plugin plugin;

    private int countdown;
    private final int delay = 30;
    private final int assignUsersToRolesTime = 20;

    public RulePreparingForGame(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enter() {
        GenerateField.getInstance(plugin).generate(
                CustomField.getAllFieldsFilesNames(plugin).get(0)
        );
        countdown = delay * 20;
        notificationOfStart();
    }

    @Override
    public void event(Event event) {

    }

    @Override
    public void tick() {
        countdown--;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(countdown / 20);
            player.setExp((float) countdown / (delay * 20));
            if (countdown % 20 == 0) {
                if (countdown / 20 <= 5) {
                    if (countdown == 0) {
                        player.playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1, 1);
                }
                if (countdown / 20 == assignUsersToRolesTime) {
                    assignUsersToRoles();
                }
            }
        }
        if (countdown == 0) {
            RuleExecutor.getInstance(plugin).changeState(RuleExecutor.State.GAME);
        }
    }

    private void assignUsersToRoles() {
        Random random = new Random();
        Title civilianTitle = Title.title(
                Component.textOfChildren(
                        Component.text("Ваша роль ")
                                .color(TextColor.color(0xffffff)),
                        Component.text("МИРНЫЙ")
                                .color(TextColor.color(0x00AAAA))
                                .decorate(TextDecoration.BOLD)
                ),
                Component.text(""),
                Title.Times.times(
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(assignUsersToRolesTime - 2),
                        Duration.ofSeconds(1)
                )
        );
        Title murdererTitle = Title.title(
                Component.textOfChildren(
                        Component.text("Ваша роль ")
                                .color(TextColor.color(0xffffff)),
                        Component.text("МАНЬЯК")
                                .color(TextColor.color(0xAA0000))
                                .decorate(TextDecoration.BOLD)
                ),
                Component.text(""),
                Title.Times.times(
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(assignUsersToRolesTime - 2),
                        Duration.ofSeconds(1)
                )
        );

        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                    "lp user " + player.getName() + " parent set civilian");
            player.showTitle(civilianTitle);
        }

        int murderIndex = random.nextInt(Bukkit.getOnlinePlayers().size());
        Player murderer = List.copyOf(Bukkit.getOnlinePlayers()).get(murderIndex);
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                "lp user " + murderer.getName() + " parent set murderer");
        murderer.showTitle(murdererTitle);
    }

    private void notificationOfStart() {
        Title title = Title.title(
                Component.text("Игра сейчас начнётся!")
                        .color(TextColor.color(0xff6a00)),

                Component.text("Приготовьтесь получить роль")
                        .color(TextColor.color(0xffffff)),
                Title.Times.times(
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(delay - assignUsersToRolesTime - 2),
                        Duration.ofSeconds(1)
                )
        );

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
        }
    }

}
