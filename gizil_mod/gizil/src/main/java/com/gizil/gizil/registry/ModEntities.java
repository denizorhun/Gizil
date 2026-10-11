package com.gizil.gizil.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.entity.GizilBee;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Gizil.MODID);

    public static final RegistryObject<EntityType<GizilBee>> GIZIL_BEE =
            ENTITY_TYPES.register("gizil_bee",
                    () -> EntityType.Builder.of(GizilBee::new, MobCategory.CREATURE)
                            .sized(0.7F, 0.6F)
                            .clientTrackingRange(10)
                            .build("gizil_bee"));
}
