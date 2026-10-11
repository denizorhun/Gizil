package com.gizil.gizil.synthesis;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class PlantChemistry {

    private record Chemistry(PlantCategory category, PlantPotency potency) {}

    private static final Map<Item, Chemistry> DATA = new HashMap<>();

    static {
        register(Items.SUNFLOWER, PlantCategory.NEUTRALIZER, PlantPotency.LOW);
        register(Items.LILY_OF_THE_VALLEY, PlantCategory.TOXIN, PlantPotency.HIGH);
        register(Items.DANDELION, PlantCategory.COOLING, PlantPotency.LOW);
        register(Items.ALLIUM, PlantCategory.TOXIN, PlantPotency.LOW);
        register(Items.BLUE_ORCHID, PlantCategory.COOLING, PlantPotency.HIGH);
    }

    private static void register(Item item, PlantCategory category, PlantPotency potency) {
        DATA.put(item, new Chemistry(category, potency));
    }

    public static boolean isKnownPlant(Item item) {
        return DATA.containsKey(item);
    }

    public static PlantCategory getCategory(Item item) {
        Chemistry c = DATA.get(item);
        return c != null ? c.category() : null;
    }

    public static PlantPotency getPotency(Item item) {
        Chemistry c = DATA.get(item);
        return c != null ? c.potency() : null;
    }
}