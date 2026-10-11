package com.gizil.gizil.client.notebook;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotebookScreen extends Screen {

    private static final int PANEL_WIDTH = 320;
    private static final int PANEL_HEIGHT = 220;
    private static final int SIDEBAR_WIDTH = 110;
    private static final int ROW_HEIGHT = 20;

    private List<NotebookPage> pages = new ArrayList<>();
    private NotebookPage selected;
    private int panelX, panelY;
    private int scrollOffset = 0;
    private int contentHeight = 0;

    public NotebookScreen() {
        super(Component.literal("Notebook"));
    }

    @Override
    protected void init() {
        super.init();
        panelX = (this.width - PANEL_WIDTH) / 2;
        panelY = (this.height - PANEL_HEIGHT) / 2;

        pages = new ArrayList<>(NotebookPageManager.all().values());
        pages.sort((a, b) -> {
            boolean aCover = a.id().getPath().equals("cover");
            boolean bCover = b.id().getPath().equals("cover");
            if (aCover != bCover) return aCover ? -1 : 1;
            return a.title().compareTo(b.title());
        });

        if (!pages.isEmpty() && selected == null) {
            selected = pages.get(0);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // Outer panel
        graphics.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xFFC4AC84);
        graphics.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + 1, 0xFF4A3826);
        graphics.fill(panelX, panelY + PANEL_HEIGHT - 1, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xFF4A3826);

        // Sidebar
        int sidebarX = panelX;
        graphics.fill(sidebarX, panelY, sidebarX + SIDEBAR_WIDTH, panelY + PANEL_HEIGHT, 0xFFA88E68);
        graphics.fill(sidebarX + SIDEBAR_WIDTH - 1, panelY, sidebarX + SIDEBAR_WIDTH, panelY + PANEL_HEIGHT, 0xFF4A3826);

        int rowY = panelY + 8;
        for (NotebookPage page : pages) {
            boolean isSelected = page.equals(selected);
            boolean isCover = page.id().getPath().equals("cover");
            boolean known = isKnown(page);

            if (isSelected) {
                graphics.fill(sidebarX + 4, rowY, sidebarX + SIDEBAR_WIDTH - 4, rowY + ROW_HEIGHT - 2, 0xFFE0B050);
            }

            String label;
            if (isCover) {
                label = "\u2726 " + page.title();
            } else if (!known) {
                label = "\u00A7k" + page.title();
            } else {
                label = page.title();
            }

            int color = isSelected ? 0x4A2F0A : (known ? 0x3D2E1C : 0x8A8072);
            graphics.drawString(font, trimToWidth(label, SIDEBAR_WIDTH - 16), sidebarX + 8, rowY + 6, color, false);
            rowY += ROW_HEIGHT;
            if (isCover) {
                graphics.fill(sidebarX + 8, rowY, sidebarX + SIDEBAR_WIDTH - 8, rowY + 1, 0xFF8A7355);
                rowY += 6;
            }
        }

        if (selected != null) {
            int detailX = panelX + SIDEBAR_WIDTH + 12;
            int detailY = panelY + 12;
            int detailWidth = PANEL_WIDTH - SIDEBAR_WIDTH - 24;
            int viewBottom = panelY + PANEL_HEIGHT - 8;

            boolean isCoverPage = selected.id().getPath().equals("cover");
            boolean known = isKnown(selected);

            if (isCoverPage) {
                renderCoverPage(graphics, detailX, detailY, detailWidth, viewBottom);
            } else if (!known) {
                renderLockedPage(graphics, detailX, detailY, detailWidth);
            } else {
                graphics.enableScissor(detailX - 4, panelY + 4, panelX + PANEL_WIDTH - 8, viewBottom);
                graphics.pose().pushPose();
                graphics.pose().translate(0, -scrollOffset, 0);

                int cursorY = detailY;

                graphics.drawString(font, selected.title(), detailX, cursorY, 0x3D2E1C, false);
                cursorY += 14;

                List<String> subtitleLines = wrapText(selected.subtitle(), detailWidth);
                for (String line : subtitleLines) {
                    graphics.drawString(font, line, detailX, cursorY, 0x7A6650, false);
                    cursorY += 10;
                }
                cursorY += 6;

                if (selected.image() != null) {
                    cursorY -= 8;
                    int imgSize = detailWidth;
                    int imgX = detailX + (detailWidth - imgSize) / 2;
                    ResourceLocation imgLoc = ResourceLocation.tryParse(selected.image());
                    if (imgLoc != null) {
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        graphics.blit(imgLoc, imgX, cursorY, 0, 0, imgSize, imgSize, imgSize, imgSize);
                        RenderSystem.disableBlend();
                    }
                    cursorY += imgSize;
                }

                for (String prop : selected.properties()) {
                    List<String> propLines = wrapText("\u00A78- " + prop, detailWidth);
                    for (String line : propLines) {
                        graphics.drawString(font, line, detailX, cursorY, 0x4A4A4A, false);
                        cursorY += 10;
                    }
                }
                cursorY += 10;

                List<String> noteLines = wrapText(selected.note(), detailWidth);
                for (String line : noteLines) {
                    graphics.drawString(font, line, detailX, cursorY, 0x3D2E1C, false);
                    cursorY += 10;
                }
                cursorY += 14;

                graphics.fill(detailX, cursorY - 6, panelX + PANEL_WIDTH - 12, cursorY - 5, 0xFFA88E68);
                List<String> hintLines = wrapText(selected.hint(), detailWidth);
                for (String line : hintLines) {
                    graphics.drawString(font, line, detailX, cursorY, 0x6B5636, false);
                    graphics.drawString(font, line, detailX, cursorY, 0x6B5636, false);
                    cursorY += 10;
                }

                contentHeight = cursorY - detailY;

                graphics.pose().popPose();
                graphics.disableScissor();
            }
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderCoverPage(GuiGraphics graphics, int detailX, int detailY, int detailWidth, int viewBottom) {
        int centerX = detailX + detailWidth / 2;

        graphics.pose().pushPose();
        graphics.pose().translate(centerX, detailY + 10, 0);
        graphics.pose().scale(1.8F, 1.8F, 1.8F);
        String title = selected.title();
        graphics.drawString(font, title, -font.width(title) / 2, 0, 0x3D2E1C, false);
        graphics.pose().popPose();

        int subY = detailY + 38;
        String subtitle = "\u00A7o" + selected.subtitle();
        graphics.drawString(font, subtitle, centerX - font.width(selected.subtitle()) / 2, subY, 0x7A6650, false);

        int lineY = subY + 16;
        graphics.fill(detailX + 20, lineY, detailX + detailWidth - 20, lineY + 1, 0xFF8A7355);
        graphics.drawString(font, "\u2726", centerX - 3, lineY - 4, 0x8A7355, false);

        List<String> noteLines = wrapText("\u00A7o" + selected.note(), detailWidth - 20);
        int noteY = lineY + 16;
        for (String line : noteLines) {
            int lineWidth = font.width(line.replace("\u00A7o", ""));
            graphics.drawString(font, line, centerX - lineWidth / 2, noteY, 0x3D2E1C, false);
            noteY += 10;
        }

        if (!selected.hint().isEmpty()) {
            int hintY = viewBottom - 40;
            List<String> hintLines = wrapText("\u00A7o" + selected.hint(), detailWidth - 20);
            for (String line : hintLines) {
                int lineWidth = font.width(line.replace("\u00A7o", ""));
                graphics.drawString(font, line, centerX - lineWidth / 2, hintY, 0x6B5636, false);
                hintY += 10;
            }
        }
    }

    private void renderLockedPage(GuiGraphics graphics, int detailX, int detailY, int detailWidth) {
        String obfTitle = "\u00A7k" + selected.title();
        graphics.drawString(font, obfTitle, detailX, detailY, 0x8A8072, false);

        List<String> lines = wrapText("\u00A7oThis page has yet to be written.", detailWidth);
        int y = detailY + 24;
        for (String line : lines) {
            graphics.drawString(font, line, detailX, y, 0x8A8072, false);
            y += 10;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int rowY = panelY + 8;
        for (NotebookPage page : pages) {
            if (mouseX >= panelX && mouseX <= panelX + SIDEBAR_WIDTH
                    && mouseY >= rowY && mouseY <= rowY + ROW_HEIGHT - 2) {
                selected = page;
                scrollOffset = 0;
                return true;
            }
            rowY += ROW_HEIGHT;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int visibleHeight = PANEL_HEIGHT - 20;
        int maxScroll = Math.max(0, contentHeight - visibleHeight);
        scrollOffset -= (int) (delta * 12);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private String trimToWidth(String text, int maxWidth) {
        if (font.width(text) <= maxWidth) return text;
        while (font.width(text + "..") > maxWidth && text.length() > 1) {
            text = text.substring(0, text.length() - 1);
        }
        return text + "..";
    }

    private List<String> wrapText(String text, int maxPixelWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder current = new StringBuilder();
        for (String word : words) {
            String candidate = current.length() == 0 ? word : current + " " + word;
            if (font.width(candidate) > maxPixelWidth && current.length() > 0) {
                lines.add(current.toString());
                current = new StringBuilder(word);
            } else {
                current = new StringBuilder(candidate);
            }
        }
        if (current.length() > 0) lines.add(current.toString());
        return lines;
    }

    private boolean isKnown(NotebookPage page) {
        if (page.id().getPath().equals("cover")) return true;
        return com.gizil.gizil.client.notebook.ClientNotebookData.knows(page.id().toString());
    }

}