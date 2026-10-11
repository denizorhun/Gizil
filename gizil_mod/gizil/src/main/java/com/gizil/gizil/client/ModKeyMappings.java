package com.gizil.gizil.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class ModKeyMappings {
    public static final String CATEGORY = "key.categories.gizil";

    public static final KeyMapping OPEN_NOTEBOOK = new KeyMapping(
            "key.gizil.open_notebook",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_K,
            CATEGORY
    );
}