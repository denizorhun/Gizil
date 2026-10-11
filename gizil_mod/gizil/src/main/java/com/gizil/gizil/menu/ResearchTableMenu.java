package com.gizil.gizil.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import com.gizil.gizil.block.entity.ResearchTableBlockEntity;
import com.gizil.gizil.registry.ModMenus;

public class ResearchTableMenu extends AbstractContainerMenu {

    private final Container tableInventory;
    private final ContainerData data;

    public ResearchTableMenu(int containerId, Inventory playerInv, ResearchTableBlockEntity be, ContainerData data) {
        super(ModMenus.RESEARCH_TABLE_MENU.get(), containerId);
        this.tableInventory = be.getInventory();
        this.data = data;

        this.addSlot(new Slot(tableInventory, 0, 81, 30));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }

        this.addDataSlots(data);
        be.setCurrentUser(playerInv.player);
    }

    public int getProgress() {
        return data.get(0);
    }

    public int getMaxProgress() {
        return ResearchTableBlockEntity.MAX_PROGRESS;
    }

    @Override
    public boolean stillValid(Player player) {
        return tableInventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack original = slot.getItem();
        ItemStack copy = original.copy();

        if (index == 0) {
            if (!this.moveItemStackTo(original, 1, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(original, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (original.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return copy;
    }
}