package com.Gbserver.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import com.Gbserver.commands.Ride;

public class RideListener implements Listener{
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent pie){
		if(Ride.list.contains(pie.getPlayer())){
			if(Ride.ridingOthers){
				pie.getRightClicked().setPassenger(pie.getPlayer());
			}else{
				pie.getPlayer().setPassenger(pie.getRightClicked());
				Ride.hasPassenger = true;
			}
			
			Ride.list.remove(pie.getPlayer());
		}
	}
}
