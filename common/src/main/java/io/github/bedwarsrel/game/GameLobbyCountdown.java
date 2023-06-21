package io.github.bedwarsrel.game;

import io.github.bedwarsrel.BedwarsRel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static io.github.bedwarsrel.utils.Utils.ChatColor;

public class GameLobbyCountdown extends BukkitRunnable {

    @Getter
    @Setter
    private int counter = 0;
    private Game game = null;
    @Getter
    private int lobbytime;
    @Getter
    private int lobbytimeWhenFull;

    public GameLobbyCountdown(Game game) {
        this.game = game;
        this.counter = BedwarsRel.getInstance().getConfig().getInt("lobbytime");
        this.lobbytime = this.counter;
    }

    @Override
    public void run() {
        ArrayList<Player> players = this.game.getPlayers();
        float xpPerLevel = 1.0F / this.lobbytime;

        if (this.game.getState() != GameState.WAITING) {
            this.game.setGameLobbyCountdown(null);
            this.cancel();
            return;
        }

        //20秒的时候
        if (this.counter == 20) {
            Bukkit.broadcastMessage(ChatColor("&e游戏将在20秒后开始!"));
        }

        if (this.counter == 10) {
            Bukkit.broadcastMessage(ChatColor("&e游戏将在&610&e秒后开始!"));
            Bukkit.getScheduler().runTaskAsynchronously(BedwarsRel.getInstance(), () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor("&a10"), "");
                    player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE,0,100);
                }
            });
        }

        if (this.counter <= 5 && this.counter != 0) {
            Bukkit.broadcastMessage(ChatColor("&e游戏将在&c" + this.counter + "&e秒后开始!"));
            Bukkit.getScheduler().runTaskAsynchronously(BedwarsRel.getInstance(), () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (counter <= 5 && counter > 3) {
                        player.sendTitle(ChatColor("&e" + counter), "");
                    } else {
                        player.sendTitle(ChatColor("&c" + counter), "");
                    }
                    player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE,100,0);
                }
            });
        }

        if (this.counter == this.lobbytimeWhenFull) {
            Bukkit.getScheduler().runTaskAsynchronously(BedwarsRel.getInstance(), () -> {
                for (Player p : players) {
                    if (p.getInventory().contains(Material.EMERALD)) {
                        p.getInventory().remove(Material.EMERALD);
                    }
                }
            });
        }

        Bukkit.getScheduler().runTaskAsynchronously(BedwarsRel.getInstance(), () -> {
            for (Player p : players) {
                p.setLevel(this.counter);
                if (this.counter == this.lobbytime) {
                    p.setExp(1.0F);
                } else {
                    p.setExp(1.0F - (xpPerLevel * (this.lobbytime - this.counter)));
                }
            }
        });

        if (!this.game.isStartable()) {
            this.counter = this.lobbytime;
            Bukkit.getScheduler().runTaskAsynchronously(BedwarsRel.getInstance(), () -> {
                for (Player p : players) {
                    p.setLevel(0);
                    p.setExp(0.0F);
                    if (p.getInventory().contains(Material.EMERALD)) {
                        p.getInventory().remove(Material.EMERALD);
                    }
                }
            });

            this.game.setGameLobbyCountdown(null);
            this.cancel();
        }

        //游戏开始
        if (this.counter == 0) {
            this.game.setGameLobbyCountdown(null);
            this.cancel();
            this.game.start(BedwarsRel.getInstance().getServer().getConsoleSender());
            return;
        }

        this.counter--;
        game.updateScoreboard();
    }
}
