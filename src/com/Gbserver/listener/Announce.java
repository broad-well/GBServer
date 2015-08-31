package com.Gbserver.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.Gbserver.variables.AnnounceTask;
import com.Gbserver.variables.AnnounceTask.Tasks;

public class Announce {
	public static String[] announcements = {"Contact me about bugs, features, and ranks here: http://mwms.mooo.com/mc/contact.html. (Click on it)",
			"Explore the GitHub page of this plugin: https://github.com/michaelpeng2002/GBServer. (Click on it)"};
	
	
	public static void registerEvents() {
			new AnnounceTask(0);
			new AnnounceTask(1);

	}
	
	public static long toTicks(int minutes){
		return minutes * 60 * 20;
	}
}