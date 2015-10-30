package com.Gbserver.unicorn;

import com.Gbserver.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by michael on 10/26/15.
 */
public class FontWriter {
    private int[][][] pixels;
    private int DIREKTION;
    private Location workPen;
    private Material penstroke;
    private List<Worker> workers = new LinkedList<>();

    public FontWriter(Location start, Material mat, int[][][] pixels, final int direction){
        this.pixels = pixels;
        DIREKTION = direction;
        workPen = start;
        penstroke = mat;
    }

    public void writeText() {
        Worker.resetCountDownLatch();
        /*
        |-----------|
        |   |   |   |
        |-----------|
         */
        for(int[][] character : pixels){
            Worker current = new Worker(workPen.clone(), character, penstroke, DIREKTION);
            workers.add(current);
            while(true){
                if(current.thisThread.isAlive()){
                    break;
                }
            }
            moveTowardsDirection(workPen, DIREKTION, character.length+1);
        }

    }

    public void close() {
        pixels = null;
        DIREKTION = 0;
        workPen = null;
        penstroke = null;
        workers = null;
    }

    private void moveTowardsDirection(Location origin, int direction, int steps){
        switch(direction){
            case TextRender.NORTH:
                //Z minus.
                origin.add(0,0,0-steps);
                break;
            case TextRender.SOUTH:
                //Z plus.
                origin.add(0,0,steps);
                break;
            case TextRender.EAST:
                //X plus.
                origin.add(steps,0,0);
                break;
            case TextRender.WEST:
                //X minus.
                origin.add(0-steps,0,0);
                break;
        }
    }

    private boolean areAllWorkersRetired() {
        for(Worker w : workers){
            if(!w.retired){
                return false;
            }
        }
        return true;
    }
}

class Worker {
    public static int activeWorkersCount = 0;
    public static List<Thread> threads = new LinkedList<>();
    public static CountDownLatch GLOBAL_LATCH = new CountDownLatch(1);
    public static void resetCountDownLatch() { GLOBAL_LATCH = new CountDownLatch(1);}

    private int addX;
    private int addZ;
    public Thread thisThread;
    public boolean retired = false;
    public Worker(final Location startingLocation, final int[][] toDraw, final Material stroke, final int direktion){
        activeWorkersCount++;
        thisThread = new Thread("WORKER-" + activeWorkersCount) {
            public void run() {
                System.out.println("Hello world from " + this.getName());
                /*try {
                    GLOBAL_LATCH.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }*/
                System.out.println("Latch opened");
                switch(direktion){
                    case TextRender.NORTH:
                        //Z minus.
                        addX = 0;
                        addZ = -1;
                        break;
                    case TextRender.SOUTH:
                        //Z plus.
                        addX = 0;
                        addZ = 1;
                        break;
                    case TextRender.EAST:
                        //X plus.
                        addX = 1;
                        addZ = 0;
                        break;
                    case TextRender.WEST:
                        //X minus.
                        addX = -1;
                        addZ = 0;
                        break;
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
                    public void run() {
                        for(int[] line : toDraw){
                            for(int dot : line){
                                if(dot == 1) {
                                    startingLocation.getBlock().setType(stroke);
                                }else{
                                    startingLocation.getBlock().setType(Material.AIR);
                                }
                                startingLocation.add(addX, 0, addZ);
                            }
                            startingLocation.add((0-addX)*line.length, 0, (0-addZ)*line.length);
                            startingLocation.add(0, -1, 0);
                        }
                        close();
                    }
                });

            }
        };
        if(GLOBAL_LATCH.getCount() != 1) return;
        thisThread.start();

    }

    public void close() {
        threads.remove(thisThread);
        activeWorkersCount--;
        retired = true;
    }
}
