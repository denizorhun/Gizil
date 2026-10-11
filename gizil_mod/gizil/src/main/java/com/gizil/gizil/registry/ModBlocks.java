package com.gizil.gizil.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.block.ResearchTableBlock;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Gizil.MODID);

    public static final RegistryObject<Block> RESEARCH_TABLE = BLOCKS.register("research_table",
            ResearchTableBlock::new);

    public static final RegistryObject<Item> RESEARCH_TABLE_ITEM = ModItems.ITEMS.register("research_table",
            () -> new BlockItem(RESEARCH_TABLE.get(), new Item.Properties()));

    public static final RegistryObject<Block> FILLER = BLOCKS.register("gizil_filler",
            com.gizil.gizil.block.FillerBlock::new);

    public static final RegistryObject<Block> SYNTHESIS_TABLE = BLOCKS.register("synthesis_table",
            com.gizil.gizil.block.SynthesisTableBlock::new);

    public static final RegistryObject<Block> ALCHEMY_TABLE = BLOCKS.register("alchemy_table",
            com.gizil.gizil.block.AlchemyTableBlock::new);

    public static final RegistryObject<Item> ALCHEMY_TABLE_ITEM = ModItems.ITEMS.register("alchemy_table",
            () -> new BlockItem(ALCHEMY_TABLE.get(), new Item.Properties()));

    public static final RegistryObject<Item> SYNTHESIS_TABLE_ITEM = ModItems.ITEMS.register("synthesis_table",
            () -> new BlockItem(SYNTHESIS_TABLE.get(), new Item.Properties()));
}