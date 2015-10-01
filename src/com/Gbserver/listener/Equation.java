package com.Gbserver.listener;

import org.bukkit.Bukkit;

import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;

import net.md_5.bungee.api.ChatColor;

public class Equation {
	//STATIC CONTENT
	public static final int PLUS = 1;
	public static final int MULTIPLY = 2;
	public static final int MINUS = 3;
	public static final int DIVIDE = 4;
	public static final int POWER = 5;
	//END STATIC
	
	public int left;
	public int right;
	public int operator;
	
	public Equation(int first, int op, int second){
		left = first;
		operator = op;
		right = second;
		Reaction.equations.add(this);
	}
	
	public int calculate() {
		switch(operator){
		case PLUS:
			return left+right;
		case MULTIPLY:
			return left*right;
		case MINUS:
			return left-right;
		case DIVIDE:
			return left/right;
		case POWER:
			return Double.valueOf(Math.pow(left, right)).intValue();
		}
		return 0;
	}
	
	public void getChatMessage() {
		String s = ChatWriter.getMessage(ChatWriterType.CHAT,
				ChatColor.YELLOW + "REACTION: Type the answer of the following equation in chat:");
		String op = "";
		switch(operator){
		case PLUS:
			op = "+";
			break;
		case MULTIPLY:
			op = "x";
			break;
		case MINUS:
			op = "-";
			break;
		case DIVIDE:
			op = "÷";
			break;
		case POWER:
			op = "^";
			break;
		}
		String s1 = ChatWriter.getMessage(ChatWriterType.CHAT,
				ChatColor.YELLOW + "" + left + " " + op + " " + right);
		Bukkit.broadcastMessage(s);
		Bukkit.broadcastMessage(s1);
	}
	public void close() {
		Reaction.equations.remove(this);
		left = 0;
		right = 0;
		operator = 0;
	}
}
