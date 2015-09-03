package com.Gbserver.variables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreDisplay {
	private Scoreboard sb;
	private Objective obj;
	private String[] displays = {"","","","","","","","","","","","","","","",""};
	
	public ScoreDisplay(String d) {
		sb = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = sb.registerNewObjective("display", "dummy");
		display();
		obj.setDisplayName(d);
	}
	
	public Scoreboard getScoreboard() {
		return sb;
	}
	
	public void setLine(String text, int row){
		displays[16-row] = text;
		reset();
		for(int i = 0; i < 16; i++){
			obj.getScore(displays[i]).setScore(i);
		}
		
		display();
	}
	
	void reset() {
		obj.unregister();
		obj = sb.registerNewObjective("display", "dummy");
	}
	
	void display() {
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		for(Player p : Bukkit.getOnlinePlayers()){
			p.setScoreboard(sb);
		}
	}
}
