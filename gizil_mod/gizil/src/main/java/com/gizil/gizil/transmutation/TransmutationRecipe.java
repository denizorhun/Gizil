package com.gizil.gizil.transmutation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public record TransmutationRecipe(
        ResourceLocation id,
        String circle,
        Item inputItem,
        int inputCount,
        Item outputItem,
        int outputCount
) {
}