package com.Gbserver.listener.protections;

import com.Gbserver.commands.TF;
import com.Gbserver.listener.ProtectionListener;
import com.Gbserver.listener.ProtectionModule;
import com.Gbserver.variables.EnhancedPlayer;
import com.Gbserver.variables.PermissionManager;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by michael on 1/17/16.
 */
public class PermissionProtect implements ProtectionModule{
    public String responseName() {
        return "Permission/Safety/Preservation Check";
    }

    @Override
    public boolean allow(BlockBreakEvent bbe) {
        //Above guest or is snow and is in main world
        //Also exception for Turf Wars building
        return (EnhancedPlayer.getEnhanced(bbe.getPlayer()).getPermission().isAbove(PermissionManager.Permissions.GUEST) ||
                (bbe.getBlock().getType() == Material.SNOW_BLOCK && bbe.getBlock().getWorld().getName().equals("world"))) ||
                (bbe.getBlock().getType() == Material.STAINED_CLAY && bbe.getBlock().getWorld().getName().equals("Turf_Wars1") &&
                        TF.isRunning);
    }

    @Override
    public boolean allow(BlockPlaceEvent bpe) {
        //Same as BlockBreak, without snow because you don't place snow
        return EnhancedPlayer.getEnhanced(bpe.getPlayer()).getPermission().isAbove(PermissionManager.Permissions.GUEST) ||
                (bpe.getBlock().getType() == Material.STAINED_CLAY && bpe.getBlock().getWorld().getName().equals("Turf_Wars1") &&
                        TF.isRunning);
    }

    @Override
    public boolean allow(PlayerInteractEvent pie) {
        //Prohibited materials no. mob spawners cannot be placed without permission
        //1st layer: prohibited materials, 2nd layers: guest permission, 3nd layers: mob spawners
        //Bools: IsProhibited, AboveGuest, IsMobSpawner
        //!IsProhibited && (AboveGuest || !IsMobSpawner)
        return !ProtectionListener.prohibitedMaterials.contains(pie.getPlayer().getItemInHand().getType()) && (
                        EnhancedPlayer.getEnhanced(pie.getPlayer()).getPermission().isAbove(PermissionManager.Permissions.GUEST) ||
                                (pie.getPlayer().getItemInHand().getTypeId() != 383 && pie.getPlayer().getItemInHand().getTypeId() != 416));
    }

    @Override
    public boolean allow(PlayerBucketEvent pbe) {
        //Not in the main world.
        return EnhancedPlayer.getEnhanced(pbe.getPlayer()).getPermission().isAbove(PermissionManager.Permissions.GUEST);
    }
}
