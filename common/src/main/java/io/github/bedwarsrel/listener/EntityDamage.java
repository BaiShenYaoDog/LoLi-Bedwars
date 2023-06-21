package io.github.bedwarsrel.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {
    @EventHandler
    public void OnEvent(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setDamage(9999);
        }
    }
}
