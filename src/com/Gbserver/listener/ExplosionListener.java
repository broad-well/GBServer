package com.Gbserver.listener;

import com.Gbserver.commands.BL;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class ExplosionListener implements Listener {
    public final int RADIUS_T = 6;
    public final int RADIUS_C = 5;

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent el) {
        Location origin = el.getEntity().getLocation();
        World world = origin.getWorld();
        if(el.getEntity().getLocation().getWorld().getName().equals("world")) {
            el.setCancelled(true);
            return;
        }
        Block NearbyBlock;
        if (el.getEntityType().equals(EntityType.PRIMED_TNT)) {
            if (el.getLocation().getBlock().getType() != Material.WATER
                    && el.getLocation().getBlock().getType() != Material.STATIONARY_WATER) {
                for (int x = 0 - (RADIUS_T / 2); x < RADIUS_T / 2; x++) {
                    for (int y = 0 - (RADIUS_T / 2); y < RADIUS_T / 2; y++) {
                        for (int z = 0 - (RADIUS_T / 2); z < RADIUS_T / 2; z++) {
                            NearbyBlock = origin.getBlock().getRelative(x, y, z);
                            Vector thisVector = NearbyBlock.getLocation().toVector().subtract(origin.toVector());
                            thisVector.setY(getRandom(1, 5));
                            if (NearbyBlock.getType() != Material.BEDROCK && NearbyBlock.getType() != Material.OBSIDIAN
                                    && getRandom(0, 3) > 0 && NearbyBlock.getType() != Material.WATER
                                    && NearbyBlock.getType() != Material.STATIONARY_WATER) {
                                if (NearbyBlock.getType() == Material.TNT) {
                                    Entity e = NearbyBlock.getWorld().spawnEntity(NearbyBlock.getLocation(),
                                            EntityType.PRIMED_TNT);
                                    e.setVelocity(thisVector.multiply(0.2));
                                    NearbyBlock.setType(Material.AIR);
                                } else {
                                    FallingBlock fb = world.spawnFallingBlock(NearbyBlock.getLocation(),
                                            NearbyBlock.getType(), NearbyBlock.getData());
                                    NearbyBlock.setType(Material.AIR);
                                    fb.setVelocity(thisVector.multiply(0.17));
                                }
                            }

                        }
                    }
                }

            }
            List<Entity> list = el.getEntity().getNearbyEntities(RADIUS_T, RADIUS_T, RADIUS_T);
            for (Object e : list.toArray()) {
                if (e instanceof LivingEntity) {
                    LivingEntity en = (LivingEntity) e;

                    Vector v = en.getLocation().toVector().subtract(origin.toVector()).multiply(0.1);
                    v.setY(getRandom(1, 5));
                    if (!(BL.isRunning) && en.getWorld().getName().equals("world")) {
                        en.setVelocity(v);
                    } else {
                        en.setVelocity(v.multiply(0.05));
                    }
                }

            }
        }
        if (el.getEntityType().equals(EntityType.ENDER_DRAGON)) {
            if (el.getLocation().getBlock().getType() != Material.WATER
                    && el.getLocation().getBlock().getType() != Material.STATIONARY_WATER) {
                for (int x = 0 - (RADIUS_C / 2); x < RADIUS_C / 2; x++) {
                    for (int y = 0 - (RADIUS_C / 2); y < RADIUS_C / 2; y++) {
                        for (int z = 0 - (RADIUS_C / 2); z < RADIUS_C / 2; z++) {
                            NearbyBlock = origin.getBlock().getRelative(x, y, z);
                            Vector thisVector = NearbyBlock.getLocation().toVector().subtract(origin.toVector());
                            thisVector.setY(getRandom(1, 5));
                            if (NearbyBlock.getType() != Material.BEDROCK && NearbyBlock.getType() != Material.OBSIDIAN
                                    && getRandom(0, 3) > 0 && NearbyBlock.getType() != Material.WATER
                                    && NearbyBlock.getType() != Material.STATIONARY_WATER) {
                                if (NearbyBlock.getType() == Material.TNT) {
                                    Entity e = NearbyBlock.getWorld().spawnEntity(NearbyBlock.getLocation(),
                                            EntityType.PRIMED_TNT);
                                    e.setVelocity(thisVector.multiply(0.2));
                                    NearbyBlock.setType(Material.AIR);
                                } else {
                                    FallingBlock fb = world.spawnFallingBlock(NearbyBlock.getLocation(),
                                            NearbyBlock.getType(), NearbyBlock.getData());
                                    NearbyBlock.setType(Material.AIR);
                                    fb.setVelocity(thisVector.multiply(0.17));
                                }
                            }

                        }
                    }
                }

            }
        }

        el.setCancelled(true);

    }

    @EventHandler
    public void onBlockChange(final EntityChangeBlockEvent ecbe) {
        //if (ecbe.getBlock().getWorld().equals(Bukkit.getWorld("Bomb_Lobbers1")) || BL.isRunning) {
        ecbe.getBlock().getWorld().playEffect(ecbe.getBlock().getLocation(), Effect.STEP_SOUND, 1);
        if (!(ecbe.getEntity().getWorld().getName().equals("world"))) {
            ecbe.setCancelled(true);
        }

        //}

    }

    private int getRandom(int min, int max) {
        Random rand = new Random();

        return rand.nextInt(max) + min;
        // 50 is the maximum and the 1 is our minimum
    }
}
