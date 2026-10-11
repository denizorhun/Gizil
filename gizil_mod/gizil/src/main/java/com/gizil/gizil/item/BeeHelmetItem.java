package com.gizil.gizil.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import com.gizil.gizil.registry.ModArmorMaterials;

import javax.annotation.Nullable;
import java.util.List;

public class BeeHelmetItem extends ArmorItem {

    public BeeHelmetItem() {
        super(ModArmorMaterials.GIZIL_SET, Type.HELMET, new Item.Properties());
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("✿ Nektar's Embrace ✿")
                .withStyle(Style.EMPTY
                        .withColor(TextColor.fromRgb(0xF7C94B))
                        .withItalic(false)
                        .withBold(true));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("\"I know I cannot always be by your side, so I leave you in gentler hands while I am gone.\"")
                .withStyle(Style.EMPTY
                        .withColor(TextColor.fromRgb(0xFFE9A8))
                        .withItalic(true)));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("✦ ")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFD700)))
                .append(Component.literal("Summons a loyal bee companion")
                        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFF0C0)))));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
