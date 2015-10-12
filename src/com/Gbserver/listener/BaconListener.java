package com.Gbserver.listener;

import com.Gbserver.commands.Bacon;
import com.Gbserver.commands.BaconPlayer;
import com.Gbserver.commands.DamageRecord;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class BaconListener implements Listener{
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent piee){
        if(piee.getRightClicked().equals(Bacon.entry)){
            if(!Bacon.hasPlayer(piee.getPlayer())){
                Bacon.addPlayer(piee.getPlayer());
                ChatWriter.writeTo(piee.getPlayer(), ChatWriterType.GAME, "Added you to the Bacon Brawl game.");
                Bacon.log.add(ChatColor.BOLD + "[JOIN] " + piee.getPlayer().getName());
            }else{
                ChatWriter.writeTo(piee.getPlayer(), ChatWriterType.GAME, "You are already in the game.");
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent ede){
        if(ede.getEntity().equals(Bacon.entry)){
            ede.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent edbee){
        if(edbee.getDamager() instanceof Player && edbee.getEntity() instanceof Player){
            Player suffer = (Player) edbee.getEntity();
            Player butcher = (Player) edbee.getDamager();
            if(Bacon.isRunning && Bacon.hasPlayer((Player) edbee.getDamager())
                    && Bacon.hasPlayer(suffer)){

                new DamageRecord(BaconPlayer.getByHandle((Player) edbee.getDamager()),
                                 BaconPlayer.getByHandle(suffer));
            }
        }

    }

    public static Location spec = new Location(Bacon.world, 0,200,0); // -------Subject to change

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde){
        if(Bacon.isRunning && Bacon.hasPlayer(pde.getEntity())){
            //spectate, log, setMessage
            pde.setDeathMessage(ChatWriter.getMessage(ChatWriterType.DEATH, ChatColor.GREEN + pde.getEntity().getName()
                    + ChatColor.GRAY + " has been killed by " +
                    ChatColor.GREEN + BaconPlayer.getByHandle(pde.getEntity()).records.get(1).by.getHandle().getName()));
            pde.getEntity().setHealth(20);
            pde.getEntity().setGameMode(GameMode.SPECTATOR);
            pde.getEntity().teleport(spec);
            pde.getEntity().sendMessage(ChatColor.RED + "You are out of the game. Damage details:");
            for(int i = 1; i < BaconPlayer.getByHandle(pde.getEntity()).records.size(); i++){
                pde.getEntity().sendMessage(BaconPlayer.getByHandle(pde.getEntity()).getLastDamages(i));
            }
            Bacon.logDeath(BaconPlayer.getByHandle(pde.getEntity()));
        }
    }
}
