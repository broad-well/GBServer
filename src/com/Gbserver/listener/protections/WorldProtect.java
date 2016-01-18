package com.Gbserver.listener.protections;

import com.Gbserver.listener.ProtectionModule;
import com.Gbserver.variables.ConfigManager;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by michael on 1/17/16.
 */
public class WorldProtect implements ProtectionModule{

    @Override
    public String responseName() {
        return "World Protection";
    }

    @Override
    public boolean allow(BlockBreakEvent bbe) {
        String work = ConfigManager.smartGet("WorldProtect").get(bbe.getBlock().getWorld().getUID().toString());
        return work == null || work.equalsIgnoreCase("false");
    }

    @Override
    public boolean allow(BlockPlaceEvent bpe) {
        String work = ConfigManager.smartGet("WorldProtect").get(bpe.getBlock().getWorld().getUID().toString());
        return work == null || work.equalsIgnoreCase("false");
    }

    @Override
    public boolean allow(PlayerInteractEvent pie) {
        return true;
    }

    @Override
    public boolean allow(PlayerBucketEvent pbe) {
        String work = ConfigManager.smartGet("WorldProtect").get(pbe.getBlockClicked().getWorld().getUID().toString());
        return work == null || work.equalsIgnoreCase("false");
    }
}
