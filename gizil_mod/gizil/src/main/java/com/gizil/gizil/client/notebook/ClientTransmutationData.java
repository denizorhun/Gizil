package com.gizil.gizil.client.notebook;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.notebook.TransmutationEntry;

import java.util.ArrayList;
import java.util.List;

public class ClientTransmutationData {

    private static List<TransmutationEntry> entries = new ArrayList<>();

    public static void set(List<TransmutationEntry> newEntries) {
        entries = newEntries;
        Gizil.LOGGER.info("Synced {} transmutations", newEntries.size());
    }

    public static List<TransmutationEntry> all() {
        return entries;
    }
}