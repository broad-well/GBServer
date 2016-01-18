package com.Gbserver.listener.protections;

import com.Gbserver.listener.ProtectionModule;
import com.Gbserver.variables.Territory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by michael on 1/17/16.
 */
public class TerritoryProtect implements ProtectionModule{
    @Override
    public String responseName() {
        return "Territory Protection Module";
    }

    @Override
    public boolean allow(BlockBreakEvent bbe) {
        for (Territory t : Territory.activeTerritories) {
            if (t.isInside(bbe.getBlock().getLocation())
                    && !bbe.getPlayer().getUniqueId().equals(t.getOwner())
                    && !t.hasCollaborator(bbe.getPlayer().getUniqueId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean allow(BlockPlaceEvent bpe) {
        for (Territory t : Territory.activeTerritories) {
            if (t.isInside(bpe.getBlock().getLocation())
                    && !bpe.getPlayer().getUniqueId().equals(t.getOwner())
                    && !t.hasCollaborator(bpe.getPlayer().getUniqueId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean allow(PlayerInteractEvent pie) {
        return true;
    }

    @Override
    public boolean allow(PlayerBucketEvent pbe) {
        for (Territory t : Territory.activeTerritories) {
            if (t.isInside(pbe.getBlockClicked().getLocation())
                    && !pbe.getPlayer().getUniqueId().equals(t.getOwner())
                    && !t.hasCollaborator(pbe.getPlayer().getUniqueId())) {
                return false;
            }
        }
        return true;
    }
}
