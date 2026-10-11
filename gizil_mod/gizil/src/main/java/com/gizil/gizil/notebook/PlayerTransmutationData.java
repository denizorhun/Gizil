package com.gizil.gizil.notebook;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public class PlayerTransmutationData {

    private static final String ROOT_KEY = "GizilNotebook";
    private static final String KEY = "KnownTransmutations";

    public static Set<String> getKnown(ServerPlayer player) {
        CompoundTag root = player.getPersistentData().getCompound(ROOT_KEY);
        Set<String> result = new HashSet<>();
        ListTag list = root.getList(KEY, 8);
        for (int i = 0; i < list.size(); i++) {
            result.add(list.getString(i));
        }
        return result;
    }

    public static boolean add(ServerPlayer player, String recipeId) {
        Set<String> known = getKnown(player);
        if (!known.add(recipeId)) {
            return false;
        }
        CompoundTag root = player.getPersistentData().getCompound(ROOT_KEY);
        ListTag list = new ListTag();
        for (String id : known) {
            list.add(StringTag.valueOf(id));
        }
        root.put(KEY, list);
        player.getPersistentData().put(ROOT_KEY, root);
        com.gizil.gizil.event.PlayerLoginEvent.sendTransmutationSync(player);
        return true;
    }
}