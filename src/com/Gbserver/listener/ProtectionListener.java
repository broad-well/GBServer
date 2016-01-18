package com.Gbserver.listener;

import com.Gbserver.Utilities;
import com.Gbserver.commands.TF;
import com.Gbserver.listener.protections.PermissionProtect;
import com.Gbserver.listener.protections.TerritoryProtect;
import com.Gbserver.listener.protections.WorldProtect;
import com.Gbserver.variables.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//Much more robust one needed!
public class ProtectionListener implements Listener {
    public static List<ProtectionModule> modules = Arrays.asList(new PermissionProtect(), new TerritoryProtect(), new WorldProtect());
    public static boolean isDisabled = false;
    public static List<Material> prohibitedMaterials = new LinkedList<Material>(){{
        add(Material.TNT);
        add(Material.STATIONARY_LAVA);
        add(Material.LAVA);
        add(Material.LAVA_BUCKET);
    }};
    /*final int[][][] DATA = {
            {
                    {-156, 78, 228},
                    {-130, 68, 208},
            },
            {
                    {72, 65, 365},
                    {-26, 254, 277},
            },
            {
                    {165, 101, 433},
                    {124, 145, 392},
            },
            {
                    {-162, 71, 185},
                    {-163, 75, 175}
            }
    };*/

    @EventHandler
    public void onBlockBreak(BlockBreakEvent bbe) {
        //Protection module indexing.
        boolean doAllow = true;
        List<String> reasons = new LinkedList<>();
        for(ProtectionModule module : modules){
            if(!module.allow(bbe)){
                doAllow = false;
                reasons.add(module.responseName());
            }
        }
        bbe.setCancelled(!doAllow);
        if(!doAllow){
            bbe.getPlayer().sendMessage("Your block break was blocked for the following reason(s):");
            bbe.getPlayer().sendMessage(reasons.toArray(new String[1]));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent bbe) {
        //Protection module indexing.
        boolean doAllow = true;
        List<String> reasons = new LinkedList<>();
        for(ProtectionModule module : modules){
            if(!module.allow(bbe)){
                doAllow = false;
                reasons.add(module.responseName());
            }
        }
        bbe.setCancelled(!doAllow);
        if(!doAllow){
            bbe.getPlayer().sendMessage("Your block place was blocked for the following reason(s):");
            bbe.getPlayer().sendMessage(reasons.toArray(new String[1]));
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent pbe) {
        //Protection module indexing.
        boolean doAllow = true;
        List<String> reasons = new LinkedList<>();
        for(ProtectionModule module : modules){
            if(!module.allow(pbe)){
                doAllow = false;
                reasons.add(module.responseName());
            }
        }
        pbe.setCancelled(!doAllow);
        if(!doAllow){
            pbe.getPlayer().sendMessage("Your bucket manipulation was blocked for the following reason(s):");
            pbe.getPlayer().sendMessage(reasons.toArray(new String[1]));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent pie) {
        //Protection module indexing.
        boolean doAllow = true;
        List<String> reasons = new LinkedList<>();
        for(ProtectionModule module : modules){
            if(!module.allow(pie)){
                doAllow = false;
                reasons.add(module.responseName());
            }
        }
        pie.setCancelled(!doAllow);
        if(!doAllow){
            pie.getPlayer().sendMessage("Your interaction was blocked for the following reason(s):");
            pie.getPlayer().sendMessage(reasons.toArray(new String[1]));
        }
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent pbe){
        //Protection module indexing.
        boolean doAllow = true;
        List<String> reasons = new LinkedList<>();
        for(ProtectionModule module : modules){
            if(!module.allow(pbe)){
                doAllow = false;
                reasons.add(module.responseName());
            }
        }
        pbe.setCancelled(!doAllow);
        if(!doAllow){
            pbe.getPlayer().sendMessage("Your bucket manipulation was blocked for the following reason(s):");
            pbe.getPlayer().sendMessage(reasons.toArray(new String[1]));
        }
    }
}
