package io.github.bedwarsrel.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static io.github.bedwarsrel.database.ulti.AddData;

public class PlayerDeath implements Listener {
    @EventHandler
    public void OnEvent(PlayerDeathEvent event) {
        if (event.getDeathMessage().equals(event.getEntity().getName() + " fell out of the world")) {
            AddData(event.getEntity().getUniqueId(),"void",1);
        }
    }
}
