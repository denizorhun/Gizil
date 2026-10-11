package com.gizil.gizil.transmutation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.gizil.gizil.Gizil;

public class TransmutationManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private static Map<ResourceLocation, TransmutationRecipe> RECIPES = new HashMap<>();

    public TransmutationManager() {
        super(GSON, "transmutations"); // data/<modid>/transmutations/*.json
    }

    public static Collection<TransmutationRecipe> all() {
        return RECIPES.values();
    }

    /** Bu çemberde, bu girdiyle, eldeki adet yetiyorsa eşleşen tarifi döner. */
    public static Optional<TransmutationRecipe> find(String circle, Item input, int available) {
        return RECIPES.values().stream()
                .filter(r -> r.circle().equals(circle))
                .filter(r -> r.inputItem() == input)
                .filter(r -> available >= r.inputCount())
                .findFirst();
    }

    public static TransmutationRecipe byId(ResourceLocation id) {
        return RECIPES.get(id);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
        Map<ResourceLocation, TransmutationRecipe> loaded = new HashMap<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : files.entrySet()) {
            try {
                JsonObject json = GsonHelper.convertToJsonObject(entry.getValue(), "transmutation");
                String circle = GsonHelper.getAsString(json, "circle");
                JsonObject in = GsonHelper.getAsJsonObject(json, "input");
                JsonObject out = GsonHelper.getAsJsonObject(json, "output");

                loaded.put(entry.getKey(), new TransmutationRecipe(
                        entry.getKey(),
                        circle,
                        readItem(GsonHelper.getAsString(in, "item")),
                        GsonHelper.getAsInt(in, "count", 1),
                        readItem(GsonHelper.getAsString(out, "item")),
                        GsonHelper.getAsInt(out, "count", 1)
                ));
            } catch (Exception e) {
                Gizil.LOGGER.error("Failed to load transmutation {}: {}", entry.getKey(), e.getMessage());
            }
        }

        RECIPES = loaded;
        Gizil.LOGGER.info("Loaded {} transmutation recipes", RECIPES.size());
    }

    private static Item readItem(String id) {
        ResourceLocation loc = ResourceLocation.tryParse(id);
        Item item = loc == null ? Items.AIR : ForgeRegistries.ITEMS.getValue(loc);
        if (item == null || item == Items.AIR) {
            throw new IllegalArgumentException("Unknown item: " + id);
        }
        return item;
    }
}