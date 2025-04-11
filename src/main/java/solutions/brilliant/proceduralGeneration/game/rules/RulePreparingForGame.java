package solutions.brilliant.proceduralGeneration.game.rules;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.commands.GenerateField;
import solutions.brilliant.proceduralGeneration.config.CustomField;
import solutions.brilliant.proceduralGeneration.game.Role;
import solutions.brilliant.proceduralGeneration.game.Rule;
import solutions.brilliant.proceduralGeneration.game.RuleExecutor;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class RulePreparingForGame implements Rule {

    private final Plugin plugin;

    private int countdown;
    private final int delay = 30;
    private final int assignUsersToRolesTime = 20;

    private CustomField field;

    public RulePreparingForGame(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enter() {
        field = GenerateField.getInstance(plugin).generate(
                CustomField.getAllFieldsFilesNames(plugin).get(0)
        );
        countdown = delay * 20;
        notificationOfStart();
    }

    @Override
    public void event(Event event) {
        if (event.getClass() == PlayerMoveEvent.class)
            onPlayerMove((PlayerMoveEvent) event);
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
                    sendPlayersToField(field, player);
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
        int murderIndex = random.nextInt(Bukkit.getOnlinePlayers().size());
        Player murderer = List.copyOf(Bukkit.getOnlinePlayers()).get(murderIndex);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(murderer.getName())) {
                RuleExecutor.getInstance(plugin).setPlayerRole(player, Role.MURDERER);
                murderer.showTitle(murdererTitle);
            } else {
                RuleExecutor.getInstance(plugin).setPlayerRole(player, Role.CIVILIAN);
                player.showTitle(civilianTitle);
                sendRoleInfo(player);
            }
        }
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

    private void sendPlayersToField(CustomField field, Player player) {
        plugin.getLogger().log(Level.INFO, "Send Players To Field");
        Random random = new Random();

        World world = Bukkit.getWorld("field");
        Location location = new Location(
                world, 2, 57, 2
        );

        if (RuleExecutor.getInstance(plugin).getPlayerRole(player) == Role.CIVILIAN) {
            List<Integer> spawnPoint = field.getSpawnPoints().get(random.nextInt(field.getSpawnPoints().size()));
            location = new Location(
                    world, spawnPoint.get(0), 1, spawnPoint.get(1)
            );
        }

        player.teleport(location);
    }

    private void onPlayerMove(PlayerMoveEvent event) {
        Random random = new Random();
        World world = Bukkit.getWorld("field");

        Player player = event.getPlayer();
        if (player.getLocation().getBlockY() <= 55) {
            if (RuleExecutor.getInstance(plugin).getPlayerRole(player) == Role.MURDERER) {
                Location location = new Location(
                        world, 2, 57, 2
                );
                player.teleport(location);
                sendStopMessageForMurder(player);
            } else if (player.getLocation().getBlockY() <= 0) {
                List<Integer> spawnPoint = field.getSpawnPoints().get(random.nextInt(field.getSpawnPoints().size()));
                Location location = new Location(
                        world, spawnPoint.get(0), 1, spawnPoint.get(1)
                );
                player.teleport(location);
            }
        }


    }

    private void sendRoleInfo(Player player) {
        Component civilianInfo = Component.textOfChildren(
                Component.text("BS")
                        .color(TextColor.color(0xff6a00))
                        .decorate(TextDecoration.BOLD),
                Component.text(" >> ")
                        .color(TextColor.color(0xffffff)),
                Component.text("Маняк скоро выйдет на охоту! Живи как можно дольше!")
                        .color(TextColor.color(0xffffff))
        );
        Component civilianTip = Component.textOfChildren(
                Component.text("BS")
                        .color(TextColor.color(0xff6a00))
                        .decorate(TextDecoration.BOLD),
                Component.text(" >> ")
                        .color(TextColor.color(0xffffff)),
                Component.text("Используй предметы появляющиеся на карте. Они помогут тебе выжить")
                        .color(TextColor.color(0xffffff))
        );
        Component murdererInfo = Component.textOfChildren(
                Component.text("BS")
                        .color(TextColor.color(0xff6a00))
                        .decorate(TextDecoration.BOLD),
                Component.text(" >> ")
                        .color(TextColor.color(0xffffff)),
                Component.text("Убей всех мирных жителей.")
                        .color(TextColor.color(0xffffff))
        );
        Component murdererTip = Component.textOfChildren(
                Component.text("BS")
                        .color(TextColor.color(0xff6a00))
                        .decorate(TextDecoration.BOLD),
                Component.text(" >> ")
                        .color(TextColor.color(0xffffff)),
                Component.text("Подожди. Скоро ты сможешь начать")
                        .color(TextColor.color(0xffffff))
        );
        if (RuleExecutor.getInstance(plugin).getPlayerRole(player) == Role.MURDERER) {
            player.sendMessage(murdererInfo);
            player.sendMessage(murdererTip);
        } else if (RuleExecutor.getInstance(plugin).getPlayerRole(player) == Role.CIVILIAN) {
            player.sendMessage(civilianInfo);
            player.sendMessage(civilianTip);
        }
    }

    private void sendStopMessageForMurder(Player player) {
        player.sendMessage(Component.textOfChildren(
                Component.text("BS")
                        .color(TextColor.color(0xff6a00))
                        .decorate(TextDecoration.BOLD),
                Component.text(" >> ")
                        .color(TextColor.color(0xffffff)),
                Component.text("Вы сможете начать охоту после окончания таймера")
                        .color(TextColor.color(0xffffff))
        ));
    }

}
