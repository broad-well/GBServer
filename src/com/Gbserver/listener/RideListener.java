package com.Gbserver.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import com.Gbserver.commands.Ride;

public class RideListener implements Listener{
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent pie){
		if(Ride.pending){
			if(Ride.ridingOthers){
				pie.getRightClicked().setPassenger(Ride.p);
			}else{
				Ride.p.setPassenger(pie.getRightClicked());
				Ride.hasPassenger = true;
			}
			
			Ride.pending = false;
		}
	}
}
