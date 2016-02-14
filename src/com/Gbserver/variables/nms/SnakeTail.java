package com.Gbserver.variables.nms;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;

/**
 * Created by michael on 2/2/16.
 */
public class SnakeTail extends EntitySheep {

    public SnakeTail(World world) {
        super(world);
        Bukkit.getLogger().finest("A snake tail has been spawned!");
        List goalB;
        List goalC;
        List targetB;
        List targetC;
        try {
            goalB = (List) ReflectUtils.getPrivateField(PathfinderGoalSelector.class, "b").get(goalSelector);
            goalC = (List) ReflectUtils.getPrivateField(PathfinderGoalSelector.class, "c").get(goalSelector);
            targetB = (List) ReflectUtils.getPrivateField(PathfinderGoalSelector.class, "b").get(targetSelector);
            targetC = (List) ReflectUtils.getPrivateField(PathfinderGoalSelector.class, "c").get(targetSelector);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
        this.goalSelector.a(5, new PathfinderGoalPassengerCarrotStick(this, 0.3F));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, false, true));

        this.setSize(1.4F, 2.0F);

    }

    public static EntitySheep spawn(Location loc){
        World w = ((CraftWorld) loc.getWorld()).getHandle();
        final SnakeTail tail = new SnakeTail(w);

        tail.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) tail.getBukkitEntity()).setRemoveWhenFarAway(false);
        w.addEntity(tail, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return tail;
    }
}
