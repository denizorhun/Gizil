package com.gizil.gizil.client.notebook;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record NotebookPage(
        ResourceLocation id,
        String title,
        String subtitle,
        String note,
        List<String> properties,
        String hint,
        String image
) {
}