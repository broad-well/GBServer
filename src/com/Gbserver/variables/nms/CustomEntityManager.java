package com.Gbserver.variables.nms;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.entity.EntityType;

/**
 * Created by michael on 2/2/16.
 */
public enum CustomEntityManager {
    SNAKETAIL("SnakeTail", 91, EntityType.SHEEP, EntitySheep.class, SnakeTail.class);

    //Type data

    private String name;
    private int id;
    private EntityType et;
    private Class<? extends EntityInsentient> nmsClass;
    private Class<? extends EntityInsentient> customClass;

    private CustomEntityManager(String name, int id, EntityType et,
                                Class<? extends EntityInsentient> nmsClass,
                                Class<? extends EntityInsentient> customClass){
        this.name = name;
        this.id = id;
        this.et = et;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }

    public String getName() {return name;}
    public int getID() {return id;}
    public EntityType getEntityType() {return et;}
    public Class<? extends EntityInsentient> getNMSClass() {return nmsClass;}
    public Class<? extends EntityInsentient> getCustomClass() {return customClass;}

    //STATIC CONTENT AHEAD

    public static void registerEntities() {
        for(CustomEntityManager cem : values()){

        }
    }
}
