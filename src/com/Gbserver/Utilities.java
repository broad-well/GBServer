package com.Gbserver;

import com.Gbserver.commands.BL;
import com.Gbserver.commands.BaconPlayer;
import com.Gbserver.commands.CTF;
import com.Gbserver.commands.TF;
import com.Gbserver.variables.ChatWriter;
import com.Gbserver.variables.ChatWriterType;
import com.Gbserver.variables.GameType;
import com.Gbserver.variables.SwiftDumpOptions;
import com.Gbserver.variables.minigame.Games;
import com.Gbserver.variables.minigame.MGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Utilities {
    private static Yaml helper = SwiftDumpOptions.BLOCK_STYLE();
    public static final String OWNER = "_Broadwell";
    public static boolean validateSender(CommandSender cs) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(ChatWriter.getMessage(ChatWriterType.CHAT, "Only players are allowed."));
            return false;
        } else {
            return true;
        }
    }

    public static Main instance;

    public static boolean validateGamePlay(CommandSender p){
        if(!(p instanceof Player)) return false;
        if(isInGame((Player) p)){
            ChatWriter.writeTo(p, ChatWriterType.CONDITION, "This command is not available during a game. Sorry!");
            return false;
        }else{
            return true;
        }
    }
    public static boolean isInRangeOf(Location hi, Location lo, Location test) {
        int x1 = hi.getBlockX();
        int y1 = hi.getBlockY();
        int z1 = hi.getBlockZ();
        int x2 = lo.getBlockX();
        int y2 = lo.getBlockY();
        int z2 = lo.getBlockZ();
        return (isInRange(x1, x2, test.getBlockX()) && isInRange(y1, y2, test.getBlockY()) && isInRange(z1, z2, test.getBlockZ())) && test.getWorld() == hi.getWorld();

    }

    public static boolean isInRange(int x, int y, int test) {
        if (x > y) {
            return test < x && test > y;
        }
        if (y > x) {
            return test < y && test > x;
        }
        if (y == x) {
            return false;
        }
        return false;
    }

    public static Main getInstance() {
        assert instance != null;
        return instance;
    }

    public static String concat(String[] arg) {
        String output = "";
        for (int i = 0; i < arg.length; i++) {
            String s = arg[i];
            if (i == arg.length - 1) {
                output += s;
            } else {
                output += s + " ";
            }
        }
        return output;
    }

    public static int getRandom(int min, int max) {
        if(min == 0 && max == 0)
            return 0;

        Random rand = new Random();
        return rand.nextInt(max - min) + min;
    }

    public static void copy(Location originBottom, Location originTop, Location targetBottom) {
        int xrelation = originBottom.getBlockX() - targetBottom.getBlockX();
        int zrelation = originBottom.getBlockZ() - targetBottom.getBlockZ();
        for (int x = originBottom.getBlockX(); x - originTop.getBlockX() != 0;
             x += (originTop.getBlockX() - originBottom.getBlockX()) / Math.abs(originTop.getBlockX() - originBottom.getBlockX())) {
            for (int y = originBottom.getBlockY(); y < originTop.getBlockY(); y++) {
                for (int z = originBottom.getBlockZ(); z - originTop.getBlockZ() != 0;
                     z += (originTop.getBlockZ() - originBottom.getBlockZ()) / Math.abs(originTop.getBlockZ() - originBottom.getBlockZ())) {

                    Block origin = originBottom.getWorld().getBlockAt(x, y, z);
                    int x1 = x - xrelation;
                    int z1 = z - zrelation;
                    Block target = originBottom.getWorld().getBlockAt(x1, y, z1);
                    target.setType(origin.getType());
                    target.setData(origin.getData());
                }
            }
        }
    }

    public static void giveLeap(Player target) {
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        ItemMeta im = axe.getItemMeta();
        im.setDisplayName("Leap Axe");
        axe.setItemMeta(im);
        target.getInventory().addItem(axe);
    }

    public static int setFrozen(final Entity... t) {
        final Vector nul = new Vector(0, 0, 0);
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), new Runnable() {

            @Override
            public void run() {
                for (Entity a : t) {
                    a.setVelocity(nul);
                }
            }

        }, 0L, 1L);
    }

    public static int scheduleRepeat(Runnable toRun, long interval) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(Main.class), toRun, 0L, interval);
    }

    public static boolean isInGame(Player p){
        //List:
        /*
            BL
            Bacon
            CTF
            Runner
            Dragons
            TF
         */
        return BL.players.contains(p.getName()) ||
                BaconPlayer.getByHandle(p) != null ||
                CTF.allPlayers().contains(p) ||
                MGUtils.utilAccess.get(Games.RUNNER).mg.getPlayers().contains(p) ||
                GameType.DR.allPlayers().contains(p) ||
                TF.getAllPlayers().contains(p);
    }

    public static String read(Path p, boolean includeNewLines) throws IOException {
        String output = "";
        for(String str : Files.readAllLines(p, Charset.defaultCharset())){
            output += includeNewLines ? str + "\n" : str;
        }
        return output;
    }

    public static String serializeArray(Object[] array){
        String output = "[";
        for(Object obj : array){
            output += obj != array[array.length-1] ? obj.toString() + ",," : obj.toString();
        }
        output += "]";
        return output;
    }
    public static String[] deserializeArray(String serialized){
        if(!(serialized.startsWith("[") && serialized.endsWith("]")))
            return null; //check brackets
        String elements = serialized.substring(1, serialized.length()-1); //get rid of brackets
        //a, b, 3f, f3, f2, fa, fq
        return elements.split(",,");
    }

    public static String ListToLines(List<String> list){
        String output = "";
        for(String str : list){
            output += str + "\n";
        }
        return output;
    }

    public static String serializeLocation(Location p){
        return p.getWorld().getName() +
                        "," + p.getX() + "," + p.getY() + "," + p.getZ() + "," +
                        p.getPitch() + "," + p.getYaw();
    }

    public static Location deserializeLocation(String serialized){
        String[] axises = serialized.split(",");
        //aced1514-daec-worldUUID,0,100,0,-14,-13
        if(axises.length != 6) return null;
        return new Location(Bukkit.getWorld(axises[0]),
                Double.valueOf(axises[1]),
                Double.valueOf(axises[2]),
                Double.valueOf(axises[3]),
                Float.valueOf(axises[4]),
                Float.valueOf(axises[5]));
    }


    public static String getStackTrace(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static boolean isNumber(String str){
        try{
            Integer.parseInt(str);
            return true;
        }catch(Exception e){return false;}
    }


    public static HashMap<String, String> toMap(Object target) {
        if (target instanceof ItemStack) {
            ItemStack is = (ItemStack) target;
            HashMap<String, String> output = new HashMap<>();
            output.put("material", String.valueOf(is.getTypeId()));
            output.put("amount", String.valueOf(is.getAmount()));
            output.put("durability", String.valueOf(is.getDurability()));
            return output;
        } else if (target instanceof Location) {
            final Location l = (Location) target;
            return new HashMap<String, String>() {{
                put("world", l.getWorld().getUID().toString());
                put("x", String.valueOf(l.getX()));
                put("y", String.valueOf(l.getY()));
                put("z", String.valueOf(l.getZ()));
                put("yaw", String.valueOf(l.getYaw()));
                put("pitch", String.valueOf(l.getPitch()));
            }};
        } else throw new UnsupportedOperationException(target.getClass().getName() + " is not supported by toMap().");
    }

    public static Object toObject(HashMap<String, String> map, Class<?> targetClass) {
        if (targetClass.getSimpleName().equals("ItemStack")) {
            if (map == null) return null;
            if (map.keySet().contains("material") &&
                    map.keySet().contains("durability")) {
                ItemStack is = new ItemStack(Material.getMaterial(Integer.parseInt(map.get("material"))));
                is.setAmount(Integer.parseInt(map.get("amount")));
                is.setDurability(Short.parseShort(map.get("durability")));
                return is;
            } else return null;
        } else if (targetClass.getSimpleName().equals("Location")) {
            for (String requiredEntry : Arrays.asList("x", "y", "z", "yaw", "pitch"))
                assert map.containsKey(requiredEntry);
            return new Location(
                    Bukkit.getWorld(UUID.fromString(map.get("world"))),
                    Double.valueOf(map.get("x")),
                    Double.valueOf(map.get("y")),
                    Double.valueOf(map.get("z")),
                    Float.valueOf(map.get("yaw")),
                    Float.valueOf(map.get("pitch")));
        } else throw new UnsupportedOperationException(targetClass.getName() + " is not supported by toObject().");
    }

    public static Object randomElement(List list) {
        return list.get(Utilities.getRandom(0, list.size()));
    }
}
