package com.gizil.gizil.transmutation;

public enum TransmutationCircle {
    MINERAL("mineral");

    private final String id;

    TransmutationCircle(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static TransmutationCircle byIndex(int index) {
        TransmutationCircle[] all = values();
        return all[Math.floorMod(index, all.length)];
    }
}