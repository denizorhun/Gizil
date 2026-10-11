package com.gizil.gizil.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.menu.ResearchTableMenu;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Gizil.MODID);

    public static final RegistryObject<MenuType<ResearchTableMenu>> RESEARCH_TABLE_MENU =
            MENUS.register("research_table_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        var pos = data.readBlockPos();
                        var be = inv.player.level().getBlockEntity(pos);
                        return new ResearchTableMenu(windowId, inv,
                                (com.gizil.gizil.block.entity.ResearchTableBlockEntity) be,
                                ((com.gizil.gizil.block.entity.ResearchTableBlockEntity) be).dataAccess());
                    }));

    public static final RegistryObject<MenuType<com.gizil.gizil.menu.SynthesisTableMenu>> SYNTHESIS_TABLE_MENU =
            MENUS.register("synthesis_table_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        var pos = data.readBlockPos();
                        var be = inv.player.level().getBlockEntity(pos);
                        return new com.gizil.gizil.menu.SynthesisTableMenu(windowId, inv,
                                (com.gizil.gizil.block.entity.SynthesisTableBlockEntity) be,
                                ((com.gizil.gizil.block.entity.SynthesisTableBlockEntity) be).dataAccess());
                    }));

    public static final RegistryObject<MenuType<com.gizil.gizil.menu.AlchemyTableMenu>> ALCHEMY_TABLE_MENU =
            MENUS.register("alchemy_table_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        var pos = data.readBlockPos();
                        var be = inv.player.level().getBlockEntity(pos);
                        return new com.gizil.gizil.menu.AlchemyTableMenu(windowId, inv,
                                (com.gizil.gizil.block.entity.AlchemyTableBlockEntity) be,
                                ((com.gizil.gizil.block.entity.AlchemyTableBlockEntity) be).dataAccess());
                    }));
}