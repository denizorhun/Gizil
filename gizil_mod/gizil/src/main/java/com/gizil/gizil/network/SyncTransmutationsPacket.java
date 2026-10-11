package com.gizil.gizil.network;

import com.gizil.gizil.notebook.TransmutationEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncTransmutationsPacket {

    private final List<TransmutationEntry> entries;

    public SyncTransmutationsPacket(List<TransmutationEntry> entries) {
        this.entries = entries;
    }

    public static void encode(SyncTransmutationsPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entries.size());
        for (TransmutationEntry e : packet.entries) {
            buf.writeUtf(e.circle());
            buf.writeUtf(e.inputItem());
            buf.writeInt(e.inputCount());
            buf.writeUtf(e.outputItem());
            buf.writeInt(e.outputCount());
        }
    }

    public static SyncTransmutationsPacket decode(FriendlyByteBuf buf) {
        int count = buf.readInt();
        List<TransmutationEntry> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new TransmutationEntry(
                    buf.readUtf(),
                    buf.readUtf(), buf.readInt(),
                    buf.readUtf(), buf.readInt()));
        }
        return new SyncTransmutationsPacket(list);
    }

    public static void handle(SyncTransmutationsPacket packet, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            com.gizil.gizil.client.notebook.ClientTransmutationData.set(packet.entries);
        });
        ctx.setPacketHandled(true);
    }
}