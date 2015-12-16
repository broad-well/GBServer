package com.Gbserver.listener;

import com.Gbserver.Main;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.TaskStorage;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DrawColorListener implements Listener {

    private TaskStorage ts;
    private static List<Player> l = new LinkedList<>();

    @EventHandler
    public void onPlayerUse(final PlayerInteractEvent pie) {
        if (pie.getPlayer().getItemInHand().getType().equals(Material.WOOD_SWORD)
                && pie.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Thin Brush")
                && pie.getPlayer().getWorld().equals(Bukkit.getWorld("Drawing"))) {
            //Do select color.
            Block b;
            if ((b = pie.getPlayer().getTargetBlock((Set<Material>) null, 100)).getType().equals(Material.STAINED_CLAY)) {
                Main.paintColor = b.getData();
                pie.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.GAME, "You have chosen " + DyeColor.getByData(Main.paintColor).name() + "."));
            }


        }
        if (pie.getPlayer().getItemInHand().getType().equals(Material.IRON_AXE)
                && pie.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Leap Axe")) {
            if(!Main.onEvent) {
                if (!l.contains(pie.getPlayer())) {
                    pie.getPlayer().setVelocity(pie.getPlayer().getEyeLocation().getDirection().multiply(2));
                    pie.getPlayer().playEffect(pie.getPlayer().getLocation(), Effect.BLAZE_SHOOT, 10);
                    l.add(pie.getPlayer());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

                        @Override
                        public void run() {
                            l.remove(pie.getPlayer());
                        }

                    }, 30L);
                } else {
                    pie.getPlayer().sendMessage(ChatWriter.getMessage(ChatWriterType.CONDITION, "You cannot spam Leaping!"));
                }
            }else{
                ChatWriter.writeTo(pie.getPlayer(), ChatWriterType.EVENT, "The Leap Axe is disabled during " +
                        Main.eventName + ". Sorry!");
            }
        }
    }
}
