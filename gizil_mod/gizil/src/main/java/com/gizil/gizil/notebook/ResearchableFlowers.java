package com.gizil.gizil.notebook;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ResearchableFlowers {

    private static final Map<Item, ResourceLocation> FLOWER_TO_PAGE = new HashMap<>();

    static {
        register(Items.SUNFLOWER, "gizil:sunflower");
        register(Items.LILY_OF_THE_VALLEY, "gizil:lily_of_the_valley");
        register(Items.DANDELION, "gizil:dandelion");
        register(Items.ALLIUM, "gizil:allium");
        register(Items.BLUE_ORCHID, "gizil:blue_orchid");
        register(Items.FERN, "gizil:tundra_mint");
    }

    private static void register(Item item, String pageId) {
        FLOWER_TO_PAGE.put(item, ResourceLocation.tryParse(pageId));
    }

    public static boolean isResearchableFlower(Item item) {
        return FLOWER_TO_PAGE.containsKey(item);
    }

    public static ResourceLocation getPageFor(Item item) {
        return FLOWER_TO_PAGE.get(item);
    }
}