package com.gizil.gizil.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import com.gizil.gizil.registry.ModArmorMaterials;

import javax.annotation.Nullable;
import java.util.List;

public class FlowerBootsItem extends ArmorItem {

    public FlowerBootsItem() {
        super(ModArmorMaterials.GIZIL_SET, Type.BOOTS, new Item.Properties());
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("✿ Guiding Blooms ✿")
                .withStyle(Style.EMPTY
                        .withColor(TextColor.fromRgb(0x88E57A))
                        .withItalic(false)
                        .withBold(true));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("\"You joked I could never lose you now. Truth is, it was my biggest fear.\"")
                .withStyle(Style.EMPTY
                        .withColor(TextColor.fromRgb(0xA8F0A0))
                        .withItalic(true)));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("✦ ")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFD700)))
                .append(Component.literal("Grows flowers on grass as you move")
                        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xC8F0C0)))));
        super.appendHoverText(stack, level, tooltip, flag);
    }


}
