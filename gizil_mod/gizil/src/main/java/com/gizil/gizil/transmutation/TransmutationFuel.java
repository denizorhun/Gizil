package com.gizil.gizil.transmutation;

import net.minecraft.world.item.Item;

import com.gizil.gizil.registry.ModItems;

public class TransmutationFuel {

    public static final int CHARGES_PER_INK = 5;

    public static boolean isFuel(Item item) {
        return item == ModItems.ALCHEMY_INK.get();
    }
}