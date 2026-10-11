package com.gizil.gizil.client.screen;

import com.gizil.gizil.menu.AlchemyTableMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import com.gizil.gizil.client.notebook.ClientTransmutationData;
import com.gizil.gizil.notebook.TransmutationEntry;

import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class AlchemyTableScreen extends AbstractContainerScreen<AlchemyTableMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("gizil", "textures/gui/alchemy_table.png");

    private boolean isOverCircleSelector(double mouseX, double mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        return mouseX >= x + 115 && mouseX < x + 133
                && mouseY >= y + 35 && mouseY < y + 53;
    }

    private boolean isOverBook(double mouseX, double mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        return mouseX >= x + BOOK_X && mouseX < x + BOOK_X + 18
                && mouseY >= y + BOOK_Y && mouseY < y + BOOK_Y + 18;
    }

    private static Item itemById(String id) {
        ResourceLocation loc = ResourceLocation.tryParse(id);
        Item item = loc == null ? null : ForgeRegistries.ITEMS.getValue(loc);
        return (item == null || item == Items.AIR) ? Items.BARRIER : item;
    }

    private static String capitalize(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static final int BAR_X = 85;
    private static final int BAR_Y = 38;
    private static final int BAR_WIDTH = 6;
    private static final int BAR_HEIGHT = 12;

    private static final int CHARGE_X = 33;
    private static final int CHARGE_Y = 57;
    private static final int CHARGE_STEP = 8;
    private static final int CHARGE_MAX = 5;

    private static final int SPRITE_CHARGE_U = 180;
    private static final int SPRITE_CHARGE_V = 0;
    private static final int SPRITE_CIRCLE_U = 180;
    private static final int SPRITE_CIRCLE_V = 32;

    private static final int CIRCLE_X = 116;
    private static final int CIRCLE_Y = 36;

    private static final int BOOK_X = 7;
    private static final int BOOK_Y = 17;

    private static final int NOTES_W = 120;
    private static final int NOTES_ROW_H = 20;
    private static final int NOTES_ROWS = 7;

    private boolean showNotes = false;
    private int notesScroll = 0;

    public AlchemyTableScreen(AlchemyTableMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 178;
        this.imageHeight = 166;
    }

    private void renderNotes(GuiGraphics graphics, int mouseX, int mouseY) {
        int px = (width - imageWidth) / 2 - NOTES_W - 2;
        int py = (height - imageHeight) / 2;

        graphics.fill(px, py, px + NOTES_W, py + imageHeight, 0xFF201713);                     // outer border
        graphics.fill(px + 1, py + 1, px + NOTES_W - 1, py + imageHeight - 1, 0xFF76587C);     // plum ring
        graphics.fill(px + 2, py + 2, px + NOTES_W - 2, py + imageHeight - 2, 0xFFD9C49A);     // paper
        graphics.drawString(font, "Notes", px + 8, py + 7, 0x2B2118, false);

        List<TransmutationEntry> entries = ClientTransmutationData.all();
        if (entries.isEmpty()) {
            graphics.drawWordWrap(font,
                    Component.literal("Nothing recorded yet. Some things only reveal themselves once done."),
                    px + 8, py + 24, NOTES_W - 16, 0x4A3B30);
            return;
        }

        ItemStack hovered = ItemStack.EMPTY;
        for (int i = 0; i < NOTES_ROWS; i++) {
            int index = i + notesScroll;
            if (index >= entries.size()) break;

            TransmutationEntry e = entries.get(index);
            int ry = py + 22 + i * NOTES_ROW_H;
            int inX = px + 8;
            int outX = px + 46;

            ItemStack in = new ItemStack(itemById(e.inputItem()), e.inputCount());
            ItemStack out = new ItemStack(itemById(e.outputItem()), e.outputCount());

            graphics.renderItem(in, inX, ry);
            graphics.renderItemDecorations(font, in, inX, ry);
            graphics.drawString(font, "\u2192", px + 28, ry + 4, 0x2B2118, false);
            graphics.renderItem(out, outX, ry);
            graphics.renderItemDecorations(font, out, outX, ry);
            graphics.drawString(font, capitalize(e.circle()), px + 68, ry + 4, 0x5A3F6E, false);

            if (mouseX >= inX && mouseX < inX + 16 && mouseY >= ry && mouseY < ry + 16) hovered = in;
            if (mouseX >= outX && mouseX < outX + 16 && mouseY >= ry && mouseY < ry + 16) hovered = out;
        }

        if (!hovered.isEmpty()) {
            graphics.renderTooltip(font, hovered, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isOverBook(mouseX, mouseY)) {
            showNotes = !showNotes;
            notesScroll = 0;
            minecraft.getSoundManager().play(
                    net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(
                            net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }

        if (button == 0 && isOverCircleSelector(mouseX, mouseY)) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
            minecraft.getSoundManager().play(
                    net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(
                            net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (showNotes) {
            int max = Math.max(0, ClientTransmutationData.all().size() - NOTES_ROWS);
            notesScroll = Mth.clamp(notesScroll - (int) Math.signum(delta), 0, max);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        if (showNotes) renderNotes(graphics, mouseX, mouseY);
        this.renderTooltip(graphics, mouseX, mouseY);
        if (isOverCircleSelector(mouseX, mouseY)) {
            graphics.renderTooltip(font, Component.literal("Mineral Circle"), mouseX, mouseY);
        }
        if (isOverBook(mouseX, mouseY)) {
            graphics.renderTooltip(font, Component.literal("Transmutation Notes"), mouseX, mouseY);
        }
    }
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        int progress = menu.getProgress();
        int maxProgress = menu.getMaxProgress();
        if (progress > 0 && maxProgress > 0) {
            int filledHeight = (progress * BAR_HEIGHT) / maxProgress;
            int fillTop = y + BAR_Y + (BAR_HEIGHT - filledHeight);
            graphics.fill(
                    x + BAR_X, fillTop,
                    x + BAR_X + BAR_WIDTH, y + BAR_Y + BAR_HEIGHT,
                    0xFFE8C070
            );
        }

        int charges = Math.min(menu.getCharges(), CHARGE_MAX);
        for (int i = 0; i < charges; i++) {
            graphics.blit(TEXTURE,
                    x + CHARGE_X + 1 + i * CHARGE_STEP, y + CHARGE_Y + 1,
                    SPRITE_CHARGE_U, SPRITE_CHARGE_V, 4, 4);
        }

        graphics.blit(TEXTURE, x + CIRCLE_X, y + CIRCLE_Y, SPRITE_CIRCLE_U, SPRITE_CIRCLE_V, 16, 16);

        boolean hoverBook = isOverBook(mouseX, mouseY);
        graphics.blit(TEXTURE, x + BOOK_X, y + BOOK_Y, hoverBook ? 200 : 180, 10, 18, 18);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 8, 6, 0xF1E4C4, false);
        graphics.drawString(font, playerInventoryTitle, 8, imageHeight - 94, 0xF1E4C4, false);
    }


}
