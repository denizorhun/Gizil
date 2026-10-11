package com.gizil.gizil.client.notebook;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import com.gizil.gizil.Gizil;

public class NotebookPageManager extends SimplePreparableReloadListener<Map<ResourceLocation, NotebookPage>> {

    private static final Gson GSON = new Gson();
    private static Map<ResourceLocation, NotebookPage> PAGES = new HashMap<>();

    public static Optional<NotebookPage> get(ResourceLocation id) {
        return Optional.ofNullable(PAGES.get(id));
    }

    public static Map<ResourceLocation, NotebookPage> all() {
        return PAGES;
    }

    @Override
    protected Map<ResourceLocation, NotebookPage> prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<ResourceLocation, NotebookPage> result = new HashMap<>();

        manager.listResources("notebook_pages", loc -> loc.getPath().endsWith(".json"))
                .forEach((fileLocation, resource) -> {
                    try (InputStreamReader reader = new InputStreamReader(resource.open())) {
                        JsonObject json = GSON.fromJson(reader, JsonObject.class);

                        String path = fileLocation.getPath();
                        String pageName = path.substring(
                                "notebook_pages/".length(), path.length() - ".json".length());
                        ResourceLocation pageId = ResourceLocation.fromNamespaceAndPath(fileLocation.getNamespace(), pageName);

                        List<String> properties = new java.util.ArrayList<>();
                        if (json.has("properties")) {
                            json.getAsJsonArray("properties").forEach(el -> properties.add(el.getAsString()));
                        }
                        String image = (json.has("image") && !json.get("image").isJsonNull())
                                ? json.get("image").getAsString() : null;

                        NotebookPage page = new NotebookPage(
                                pageId,
                                json.get("title").getAsString(),
                                json.get("subtitle").getAsString(),
                                json.get("note").getAsString(),
                                properties,
                                json.get("hint").getAsString(),
                                image
                        );

                        result.put(pageId, page);
                    } catch (Exception e) {
                        Gizil.LOGGER.error("Failed to load notebook page {}: {}", fileLocation, e.getMessage());
                    }
                });

        return result;
    }

    @Override
    protected void apply(Map<ResourceLocation, NotebookPage> data, ResourceManager manager, ProfilerFiller profiler) {
        PAGES = data;
        Gizil.LOGGER.info("Loaded {} notebook pages", PAGES.size());
    }
}