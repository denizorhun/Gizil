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

public class CrackedFlowerBootsItem extends CrackedRelicItem {

    @Override
    public net.minecraft.resources.ResourceLocation getUnlockedRecipeId() {
        return new net.minecraft.resources.ResourceLocation("gizil", "flower_boots");
    }

    public CrackedFlowerBootsItem() {
        super(Type.BOOTS);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("Faded Steps")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xC9B27A)));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("A pulse that forgot it was supposed to stop.")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xD8C89A)).withItalic(true)));
    }
}