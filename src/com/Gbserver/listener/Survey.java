package com.Gbserver.listener;

import com.Gbserver.Utilities;
import com.Gbserver.variables.*;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class Survey implements Listener, CommandExecutor {
    private static DebugLevel dl = new DebugLevel(2, "SurveyListener");
    private static final CPrefix prefix = new CPrefix("Survey", ChatColor.GREEN, ChatColor.GOLD);
    public static final Yaml helper = SwiftDumpOptions.BLOCK_STYLE();
    private static File file = ConfigManager.getPathInsidePluginFolder("surveys.yml").toFile();
    public static List<Question> loadedQuestions = new LinkedList<>();

    public static class Question implements Mappable {
        private static final Yaml helper = new Yaml();

        private final PUID puid;
        private final String toAsk;
        private final List<String> options;
        private final HashMap<OfflinePlayer, String> answereds;

        public Question(PUID puid, String toAsk, List<String> options, HashMap<OfflinePlayer, String> answereds) {
            this.puid = puid;
            this.toAsk = toAsk;
            this.options = options;
            this.answereds = answereds;
        }

        public PUID getPuid() {
            return puid;
        }

        public String getToAsk() {
            return toAsk;
        }

        public List<String> getOptions() {
            return options;
        }

        public HashMap<OfflinePlayer, String> getAnsweredPlayers() {
            return answereds;
        }

        @Override
        public HashMap<String, String> toMap() {
            final HashMap<String, String> answered = new HashMap<>();
            for (Map.Entry<OfflinePlayer, String> op : answereds.entrySet())
                answered.put(Identity.serializeIdentity(op.getKey()), op.getValue());
            return new HashMap<String, String>() {{
                put("puid", puid.toString());
                put("to_ask", toAsk);
                put("options", helper.dump(options));
                put("answered", helper.dump(answered));
            }};
        }

        public static Question fromMap(HashMap<String, String> map) {
            for (String item : Arrays.asList("puid", "to_ask", "options", "answered")) {
                assert map.containsKey(item);
            }
            Object list = helper.load(map.get("options"));
            assert list instanceof List;
            Object list0 = helper.load(map.get("answered"));
            assert list0 instanceof HashMap;
            HashMap<OfflinePlayer, String> ops = new HashMap<>();
            for (Map.Entry<String, String> entry : ((HashMap<String, String>) list0).entrySet())
                ops.put(Identity.deserializeIdentity(entry.getKey()), entry.getValue());
            return new Question(new PUID(map.get("puid")), map.get("to_ask"), (List<String>) list, ops);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje) {
        if (Utilities.genPossibility(20)) {
            List<Question> unanswereds = new LinkedList<>();
            for (Question q : loadedQuestions)
                if (!q.getAnsweredPlayers().containsKey(pje.getPlayer()))
                    unanswereds.add(q);
            if (unanswereds.isEmpty()) return; // Answered all surveys
            Question lucky = (Question) Utilities.randomElement(unanswereds);
            pje.getPlayer().sendMessage(prefix + lucky.getToAsk());
            pje.getPlayer().sendMessage(prefix + "Here are the options:");
            for (String option : lucky.getOptions()) {
                pje.getPlayer().sendMessage(ChatColor.GOLD + option);
            }
            pje.getPlayer().sendMessage(prefix + Sh.pc("italic") + "Use " + Sh.pc("yellow") + "/answer <your choice>" +
                    Sh.pc("gold") + " to answer.");
            Vaults.getVault(pje.getPlayer().getUniqueId()).properties.put("survey-pending", lucky);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Utilities.validateSender(sender)) {
            Player p = (Player) sender;
            Vault v = Vaults.getVault(p.getUniqueId());
            if (v.properties.containsKey("survey-pending")) {
                assert v.properties.get("survey-pending") instanceof Question;
                Question q = (Question) v.properties.get("survey-pending");
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.ITALIC + "You need to answer the question, though!");
                    sender.sendMessage(ChatColor.AQUA + "Here are the options:");
                    for (String choice : q.getOptions()) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "- " + choice);
                    }
                } else {
                    for (String option : q.getOptions()) {
                        if (option.equalsIgnoreCase(args[0])) {
                            q.getAnsweredPlayers().put(p, option);
                            sender.sendMessage(prefix + "Your choice has been recorded.");
                            v.properties.remove("survey-pending");
                            return true;
                        }
                    }
                    sender.sendMessage(ChatColor.ITALIC + "This option does not exist.");
                    sender.sendMessage(ChatColor.AQUA + "Here are the options:");
                    for (String choice : q.getOptions()) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "- " + choice);
                    }
                }
            } else {
                sender.sendMessage(CPrefix.Prf.COMMAND + "A survey was not recently assigned to you.");
            }
        }
        return true;
    }

    public static final ConfigLoader.ConfigUser configUser = new ConfigLoader.ConfigUser() {
        @Override
        public void load() throws Exception {
            dl.debugWrite("Dumping question list before loading");
            for (Question q : loadedQuestions) {
                dl.debugWrite(q.toMap().toString());
            }
            loadedQuestions.clear();
            FileReader fr = new FileReader(file);
            Object read = helper.load(fr);
            assert read instanceof List;
            for (HashMap<String, String> map : (List<HashMap<String, String>>) read) {
                Question q = Question.fromMap(map);
                if (loadedQuestions.contains(q)) {
                    dl.debugWrite(0, "Warning: Duplicate question found! \"" + q.getToAsk() + "\"");
                } else {
                    dl.debugWrite(5, "Discovered question in config: " + q.getToAsk());
                    loadedQuestions.add(q);
                }
            }
            fr.close();
        }

        @Override
        public void unload() throws Exception {
            FileWriter fw = new FileWriter(file);
            List<HashMap<String, String>> build = new LinkedList<>();
            for (Question q : loadedQuestions) {
                dl.debugWrite(5, "Discovered question in variable: " + q.toMap());
                build.add(q.toMap());
            }
            helper.dump(build, fw);
            fw.flush();
            fw.close();
        }
    };

}
