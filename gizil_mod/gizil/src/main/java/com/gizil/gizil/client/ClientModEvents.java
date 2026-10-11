package com.gizil.gizil.client;

import com.gizil.gizil.registry.ModMenus;
import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.registry.ModEntities;

@Mod.EventBusSubscriber(modid = Gizil.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.GIZIL_BEE.get(), BeeRenderer::new);
        net.minecraft.client.gui.screens.MenuScreens.register(
                ModMenus.RESEARCH_TABLE_MENU.get(), com.gizil.gizil.client.screen.ResearchTableScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(
                ModMenus.SYNTHESIS_TABLE_MENU.get(), com.gizil.gizil.client.screen.SynthesisTableScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(
                ModMenus.ALCHEMY_TABLE_MENU.get(), com.gizil.gizil.client.screen.AlchemyTableScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(net.minecraftforge.client.event.RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new com.gizil.gizil.client.notebook.NotebookPageManager());
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(net.minecraftforge.client.event.RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.OPEN_NOTEBOOK);
    }
}
