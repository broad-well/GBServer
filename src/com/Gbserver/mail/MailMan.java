package com.Gbserver.mail;

import com.Gbserver.variables.ConfigManager;
import org.bukkit.entity.Player;

import java.nio.file.Path;

public class MailMan {
    public static final String NAME = "Broadwell Server Mail Subsystem";
    private static final Path datfile = ConfigManager.getPathInsidePluginFolder("mails.dat");
    public static MailMan getPersonalAssistant(Player p){

    }
    //----------
    /* file format:
    #Player{entry{content:(...);},entry{content:(...);},entry{content:(...);}}
    :
    #Player{
        entry{
            new:false;
            content:(
                Hello!\%/ <-- newline

                Me
                # real would be: Hello!\%/\%/Me\%/
            );
        }
        entry{
            something
        }
     }
     */
    private MailMan(String name) {

    }
}
