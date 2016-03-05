package com.Gbserver.variables;

import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Vaults {
    public static List<Vault> vaults = new LinkedList<Vault>();

    public static Vault getVault(UUID uuid) {
        for (Vault vault : vaults)
            if (vault.uuid.equals(uuid))
                return vault;

        Vault va = new Vault(Bukkit.getOfflinePlayer(uuid));
        vaults.add(va);
        return va;
    }
}
