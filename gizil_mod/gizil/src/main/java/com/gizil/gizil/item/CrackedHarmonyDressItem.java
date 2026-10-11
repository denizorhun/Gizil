package com.gizil.gizil.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import com.gizil.gizil.registry.ModArmorMaterials;

import javax.annotation.Nullable;
import java.util.List;

public class CrackedHarmonyDressItem extends CrackedRelicItem {

    @Override
    public net.minecraft.resources.ResourceLocation getUnlockedRecipeId() {
        return new net.minecraft.resources.ResourceLocation("gizil", "harmony_dress");
    }

    public CrackedHarmonyDressItem() {
        super(Type.CHESTPLATE);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("Worn Petals")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xC9B27A)));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Still warm, somehow.")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xD8C89A)).withItalic(true)));
    }
}