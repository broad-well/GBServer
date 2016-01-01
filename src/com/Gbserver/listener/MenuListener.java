package com.Gbserver.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent ice) {
        if(ice.getClickedInventory() == null) return;
        if (ice.getClickedInventory().getName().equals("Commands")) {
            Player p = (Player) ice.getWhoClicked();
            ice.setCancelled(true);
            p.closeInventory();
            switch (ice.getSlot()) {
                case 0:
                    //To spawn

                    p.performCommand("spawn");
                    break;
                case 1:
                    //Do afk.

                    p.chat("/afk");
                    break;
                case 2:
                    //Back.

                    p.performCommand("back");
                    break;
                case 3:
                    //Nick.

                    break;
                case 4:
                    //Ride.

                    p.performCommand("ride");
                    break;
                case 5:
                    //Rideme.

                    p.performCommand("rideme");
                    break;
                case 6:
                    //gm.


                    break;
                case 7:
                    //heal.

                    p.performCommand("heal");
                    break;
                case 8:
                    //tf.

                    p.performCommand("tf");
                    break;

            }
        }
    }
}
