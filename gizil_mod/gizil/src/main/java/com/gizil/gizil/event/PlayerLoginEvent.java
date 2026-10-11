package com.gizil.gizil.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.notebook.PlayerNotebookData;
import com.gizil.gizil.network.ModNetworking;
import com.gizil.gizil.network.SyncNotebookPagesPacket;
import com.gizil.gizil.network.SyncTransmutationsPacket;

@Mod.EventBusSubscriber(modid = Gizil.MODID)
public class PlayerLoginEvent {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            sendNotebookSync(serverPlayer);
            sendTransmutationSync(serverPlayer);
        }
    }

    public static void sendNotebookSync(ServerPlayer player) {
        ModNetworking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new SyncNotebookPagesPacket(PlayerNotebookData.getKnownPages(player))
        );
    }

    public static void sendTransmutationSync(ServerPlayer player) {
        java.util.List<com.gizil.gizil.notebook.TransmutationEntry> entries = new java.util.ArrayList<>();
        for (String id : com.gizil.gizil.notebook.PlayerTransmutationData.getKnown(player)) {
            net.minecraft.resources.ResourceLocation loc = net.minecraft.resources.ResourceLocation.tryParse(id);
            com.gizil.gizil.transmutation.TransmutationRecipe r =
                    loc == null ? null : com.gizil.gizil.transmutation.TransmutationManager.byId(loc);
            if (r == null) continue;
            entries.add(new com.gizil.gizil.notebook.TransmutationEntry(
                    r.circle(),
                    net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(r.inputItem()).toString(),
                    r.inputCount(),
                    net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(r.outputItem()).toString(),
                    r.outputCount()));
        }
        ModNetworking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new SyncTransmutationsPacket(entries));
    }
}