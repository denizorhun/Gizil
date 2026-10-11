package com.gizil.gizil.client.screen;

import com.gizil.gizil.menu.ResearchTableMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("gizil", "textures/gui/research_table.png");

    private static final int METER_X = 55;
    private static final int METER_Y = 59;
    private static final int METER_WIDTH = 69;
    private static final int METER_HEIGHT = 7;

    public ResearchTableScreen(ResearchTableMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 178;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        int progress = menu.getProgress();
        int maxProgress = menu.getMaxProgress();
        if (progress > 0) {
            int filledWidth = maxProgress > 0 ? (progress * METER_WIDTH) / maxProgress : 0;
            graphics.fill(
                    x + METER_X, y + METER_Y,
                    x + METER_X + filledWidth, y + METER_Y + METER_HEIGHT,
                    0xFFB296D6
            );
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 8, 6, 0xF1E4C4, false);
        graphics.drawString(font, playerInventoryTitle, 8, imageHeight - 94, 0xF1E4C4, false);
    }


}