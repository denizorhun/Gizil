package com.gizil.gizil.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.gizil.gizil.Gizil;

@Mod.EventBusSubscriber(modid = Gizil.MODID, value = Dist.CLIENT)
public class ClientTickHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        while (ModKeyMappings.OPEN_NOTEBOOK.consumeClick()) {
            if (mc.screen == null) {
                mc.setScreen(new com.gizil.gizil.client.notebook.NotebookScreen());
            }
        }
    }
}