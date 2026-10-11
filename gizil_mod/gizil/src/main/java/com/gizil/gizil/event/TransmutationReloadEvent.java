package com.gizil.gizil.event;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.transmutation.TransmutationManager;

@Mod.EventBusSubscriber(modid = Gizil.MODID)
public class TransmutationReloadEvent {

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new TransmutationManager());
    }
}