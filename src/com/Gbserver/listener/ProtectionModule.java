package com.Gbserver.listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by michael on 1/17/16.
 */
public interface ProtectionModule {
    String responseName();
    boolean allow(BlockBreakEvent bbe);
    boolean allow(BlockPlaceEvent bpe);
    boolean allow(PlayerInteractEvent pie);
    boolean allow(PlayerBucketEvent pbe);
}
