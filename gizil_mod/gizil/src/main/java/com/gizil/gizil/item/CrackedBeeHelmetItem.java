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

public class CrackedBeeHelmetItem extends CrackedRelicItem {

    @Override
    public net.minecraft.resources.ResourceLocation getUnlockedRecipeId() {
        return new net.minecraft.resources.ResourceLocation("gizil", "bee_helmet");
    }

    public CrackedBeeHelmetItem() {
        super(Type.HELMET);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("Warm Wax")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xC9B27A)));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Hold it to your ear. Something still buzzes.")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xD8C89A)).withItalic(true)));
    }
}