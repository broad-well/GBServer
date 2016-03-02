package com.Gbserver.listener;

import com.Gbserver.listener.protections.PermissionProtect;
import com.Gbserver.listener.protections.TerritoryProtect;
import com.Gbserver.listener.protections.WorldProtect;
import com.Gbserver.variables.ConfigLoader;
import com.Gbserver.variables.ConfigManager;
import com.Gbserver.variables.DebugLevel;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

//Much more robust one needed!
public class ProtectionListener implements Listener, ConfigLoader.ConfigUser {
    public static List<ProtectionModule> modules = Arrays.asList(
            new PermissionProtect(), new TerritoryProtect(), new WorldProtect());
    public static boolean isDisabled = false;
    private static DebugLevel dl = new DebugLevel(2, "Protection");
    public static List<Material> prohibitedMaterials = new LinkedList<>();

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

    @Override
    public void load() {
        prohibitedMaterials.clear();
        for (String entry : ConfigManager.smartGet("ProhibitedMaterials").keySet()) {
            if (prohibitedMaterials.contains(Material.valueOf(entry))) {
                dl.debugWrite("ERROR: Duplicate PROHIBITED materials found! " + entry);
            } else {
                prohibitedMaterials.add(Material.valueOf(entry));
                dl.debugWrite(4, "ProtectionConfMan: Adding entry " + entry + " into variable");
            }
        }
    }

    @Override
    public void unload() {
        for (Material mat : prohibitedMaterials) {
            dl.debugWrite(4, "ProtectionConfMan: Dumping entry " + mat.name() + " into ConfigManager");
            ConfigManager.smartGet("ProhibitedMaterials").put(mat.name(), "");
        }
    }
}
