package com.gizil.gizil.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import com.gizil.gizil.registry.ModArmorMaterials;

import javax.annotation.Nullable;
import java.util.List;

public class HarmonyDressItem extends ArmorItem {

    public HarmonyDressItem() {
        super(ModArmorMaterials.GIZIL_SET, Type.CHESTPLATE, new Item.Properties());
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("✿ Kindred Bloom ✿")
                .withStyle(Style.EMPTY
                        .withColor(TextColor.fromRgb(0xE08FC0))
                        .withItalic(false)
                        .withBold(true));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("\"I couldn't protect you from the world, so I asked it to love you as I do.\"")
                .withStyle(Style.EMPTY
                        .withColor(TextColor.fromRgb(0xF0C8E0))
                        .withItalic(true)));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("✦ ")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFD700)))
                .append(Component.literal("Even what is fierce lets you pass")
                        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xF0D8E8)))));
        tooltip.add(Component.literal("✦ ")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFD700)))
                .append(Component.literal("Nature mends what it claims as its own")
                        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xF0D8E8)))));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}