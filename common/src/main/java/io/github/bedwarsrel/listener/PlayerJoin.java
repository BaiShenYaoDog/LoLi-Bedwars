package io.github.bedwarsrel.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static io.github.bedwarsrel.database.ulti.initializeData;
import static io.github.bedwarsrel.utils.Utils.ChatColor;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onEvent(PlayerJoinEvent event) {
        initializeData(event.getPlayer());

        Player player = event.getPlayer();
        event.setJoinMessage(ChatColor("&7" + player.getName() + "&e加入了游戏 (&b" + Bukkit.getOnlinePlayers().size() + "&e/&b" + Bukkit.getMaxPlayers() + "&e)!"));
    }
}
