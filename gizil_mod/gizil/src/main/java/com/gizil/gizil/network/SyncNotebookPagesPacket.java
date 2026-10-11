package com.gizil.gizil.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class SyncNotebookPagesPacket {

    private final Set<String> knownPages;

    public SyncNotebookPagesPacket(Set<String> knownPages) {
        this.knownPages = knownPages;
    }

    public static void encode(SyncNotebookPagesPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.knownPages.size());
        for (String id : packet.knownPages) {
            buf.writeUtf(id);
        }
    }

    public static SyncNotebookPagesPacket decode(FriendlyByteBuf buf) {
        int count = buf.readInt();
        Set<String> pages = new HashSet<>();
        for (int i = 0; i < count; i++) {
            pages.add(buf.readUtf());
        }
        return new SyncNotebookPagesPacket(pages);
    }

    public static void handle(SyncNotebookPagesPacket packet, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            com.gizil.gizil.client.notebook.ClientNotebookData.setKnownPages(packet.knownPages);
        });
        ctx.setPacketHandled(true);
    }
}