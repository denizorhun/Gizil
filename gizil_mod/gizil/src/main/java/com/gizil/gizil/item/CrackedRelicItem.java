package com.gizil.gizil.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.gizil.gizil.registry.ModArmorMaterials;

public abstract class CrackedRelicItem extends ArmorItem {

    public abstract net.minecraft.resources.ResourceLocation getUnlockedRecipeId();

    protected CrackedRelicItem(Type type) {
        super(ModArmorMaterials.GIZIL_SET, type, new Item.Properties().stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}