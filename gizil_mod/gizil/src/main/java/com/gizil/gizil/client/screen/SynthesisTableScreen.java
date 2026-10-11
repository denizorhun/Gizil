package com.gizil.gizil.client.screen;

import com.gizil.gizil.menu.SynthesisTableMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SynthesisTableScreen extends AbstractContainerScreen<SynthesisTableMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("gizil", "textures/gui/synthesis_table.png");

    private static final int BAR_X = 85;
    private static final int BAR_Y = 39;
    private static final int BAR_WIDTH = 6;
    private static final int BAR_HEIGHT = 11;

    public SynthesisTableScreen(SynthesisTableMenu menu, Inventory playerInv, Component title) {
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
            int filledHeight = maxProgress > 0 ? (progress * BAR_HEIGHT) / maxProgress : 0;
            int fillTop = y + BAR_Y + (BAR_HEIGHT - filledHeight);
            graphics.fill(
                    x + BAR_X, fillTop,
                    x + BAR_X + BAR_WIDTH, y + BAR_Y + BAR_HEIGHT,
                    0xFF96B270
            );
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 8, 6, 0xF1E4C4, false);
        graphics.drawString(font, playerInventoryTitle, 8, imageHeight - 94, 0xF1E4C4, false);
    }
}