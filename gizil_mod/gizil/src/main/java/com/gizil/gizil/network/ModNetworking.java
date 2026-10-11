package com.gizil.gizil.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import com.gizil.gizil.Gizil;

public class ModNetworking {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Gizil.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        CHANNEL.registerMessage(packetId++,
                SyncNotebookPagesPacket.class,
                SyncNotebookPagesPacket::encode,
                SyncNotebookPagesPacket::decode,
                SyncNotebookPagesPacket::handle);

        CHANNEL.registerMessage(packetId++,
                SyncTransmutationsPacket.class,
                SyncTransmutationsPacket::encode,
                SyncTransmutationsPacket::decode,
                SyncTransmutationsPacket::handle);
    }


}