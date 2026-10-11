package com.gizil.gizil.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.block.entity.ResearchTableBlockEntity;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Gizil.MODID);

    public static final RegistryObject<BlockEntityType<ResearchTableBlockEntity>> RESEARCH_TABLE =
            BLOCK_ENTITIES.register("research_table",
                    () -> BlockEntityType.Builder.of(ResearchTableBlockEntity::new,
                            ModBlocks.RESEARCH_TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<com.gizil.gizil.block.entity.FillerBlockEntity>> FILLER =
            BLOCK_ENTITIES.register("filler",
                    () -> BlockEntityType.Builder.of(com.gizil.gizil.block.entity.FillerBlockEntity::new,
                            ModBlocks.FILLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<com.gizil.gizil.block.entity.AlchemyTableBlockEntity>> ALCHEMY_TABLE =
            BLOCK_ENTITIES.register("alchemy_table",
                    () -> BlockEntityType.Builder.of(com.gizil.gizil.block.entity.AlchemyTableBlockEntity::new,
                            ModBlocks.ALCHEMY_TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<com.gizil.gizil.block.entity.SynthesisTableBlockEntity>> SYNTHESIS_TABLE =
            BLOCK_ENTITIES.register("synthesis_table",
                    () -> BlockEntityType.Builder.of(com.gizil.gizil.block.entity.SynthesisTableBlockEntity::new,
                            ModBlocks.SYNTHESIS_TABLE.get()).build(null));
}