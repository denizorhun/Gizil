package com.gizil.gizil.notebook;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public class PlayerNotebookData {

    private static final String ROOT_KEY = "GizilNotebook";
    private static final String PAGES_KEY = "KnownPages";

    public static Set<String> getKnownPages(ServerPlayer player) {
        CompoundTag root = player.getPersistentData().getCompound(ROOT_KEY);
        Set<String> result = new HashSet<>();
        ListTag list = root.getList(PAGES_KEY, 8); // 8 = string tag type
        for (int i = 0; i < list.size(); i++) {
            result.add(list.getString(i));
        }
        return result;
    }

    public static boolean knowsPage(ServerPlayer player, String pageId) {
        return getKnownPages(player).contains(pageId);
    }

    public static boolean addKnownPage(net.minecraft.server.level.ServerPlayer player, String pageId) {
        Set<String> known = getKnownPages(player);
        if (!known.add(pageId)) {
            return false;
        }
        saveKnownPages(player, known);
        com.gizil.gizil.event.PlayerLoginEvent.sendNotebookSync(player);
        return true;
    }

    private static void saveKnownPages(ServerPlayer player, Set<String> pages) {
        CompoundTag root = player.getPersistentData().getCompound(ROOT_KEY);
        ListTag list = new ListTag();
        for (String id : pages) {
            list.add(StringTag.valueOf(id));
        }
        root.put(PAGES_KEY, list);
        player.getPersistentData().put(ROOT_KEY, root);
    }
}