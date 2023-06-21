package io.github.bedwarsrel.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static io.github.bedwarsrel.utils.Utils.ChatColor;

public class PlayerQuit implements Listener {
    @EventHandler
    public void OnEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor("&7" + player.getName() + "&a断开连接!"));
    }
}
