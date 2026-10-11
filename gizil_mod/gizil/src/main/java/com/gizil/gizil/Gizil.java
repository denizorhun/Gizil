package com.gizil.gizil;


import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;


import com.gizil.gizil.registry.ModItems;
import com.gizil.gizil.registry.ModEntities;
import com.gizil.gizil.registry.ModBlocks;
import com.gizil.gizil.registry.ModBlockEntities;
import com.gizil.gizil.registry.ModMenus;


@Mod(Gizil.MODID)
public class Gizil {
    public static final String MODID = "gizil";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Gizil(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModItems.CREATIVE_TABS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener((FMLCommonSetupEvent e) -> {
            LOGGER.info("Tab key: {}", ModItems.GIZIL_TAB.getKey().location());
        });

        modEventBus.addListener(this::registerAttributes);
        com.gizil.gizil.network.ModNetworking.register();
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GIZIL_BEE.get(), Bee.createAttributes().build());
    }

}
