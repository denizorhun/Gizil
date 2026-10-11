package com.gizil.gizil.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import com.gizil.gizil.block.entity.AlchemyTableBlockEntity;
import com.gizil.gizil.registry.ModMenus;
import com.gizil.gizil.transmutation.TransmutationFuel;

public class AlchemyTableMenu extends AbstractContainerMenu {

    private final Container tableInventory;
    private final ContainerData data;
    private final AlchemyTableBlockEntity blockEntity;

    public AlchemyTableMenu(int containerId, Inventory playerInv, AlchemyTableBlockEntity be, ContainerData data) {
        super(ModMenus.ALCHEMY_TABLE_MENU.get(), containerId);
        this.tableInventory = be.getInventory();
        this.data = data;
        this.blockEntity = be;

        this.addSlot(new Slot(tableInventory, AlchemyTableBlockEntity.SLOT_INPUT, 80, 18));
        this.addSlot(new Slot(tableInventory, AlchemyTableBlockEntity.SLOT_FUEL, 44, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return TransmutationFuel.isFuel(stack.getItem());
            }
        });
        this.addSlot(new Slot(tableInventory, AlchemyTableBlockEntity.SLOT_OUTPUT, 80, 54) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }

        this.addDataSlots(data);
    }

    public int getProgress() {
        return data.get(0);
    }

    public int getCharges() {
        return data.get(1);
    }

    public int getCircleIndex() {
        return data.get(2);
    }

    public int getMaxProgress() {
        return AlchemyTableBlockEntity.MAX_PROGRESS;
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

        if (index < 3) {
            if (!this.moveItemStackTo(original, 3, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (TransmutationFuel.isFuel(original.getItem())) {
            if (!this.moveItemStackTo(original, 1, 2, false)) {
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

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == 0) {
            blockEntity.selectCircle(blockEntity.dataAccess().get(2) + 1);
            return true;
        }
        return false;
    }
}