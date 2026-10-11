package com.gizil.gizil.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.MenuProvider;

import org.jetbrains.annotations.Nullable;

import com.gizil.gizil.registry.ModBlockEntities;
import com.gizil.gizil.synthesis.PlantChemistry;
import com.gizil.gizil.synthesis.SynthesisRecipe;

public class SynthesisTableBlockEntity extends BlockEntity implements MenuProvider {

    public static final int MAX_PROGRESS = 100; // 5 saniye

    private final SimpleContainer inventory = new SimpleContainer(3) {
        @Override
        public void setChanged() {
            super.setChanged();
            SynthesisTableBlockEntity.this.setChanged();
        }
    };

    private int progress = 0;

    public SynthesisTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SYNTHESIS_TABLE.get(), pos, state);
    }

    public Container getInventory() {
        return inventory;
    }

    public int getProgress() {
        return progress;
    }

    private boolean isSynthesizable() {
        ItemStack in1 = inventory.getItem(0);
        ItemStack in2 = inventory.getItem(1);
        if (in1.isEmpty() || in2.isEmpty()) return false;
        if (!PlantChemistry.isKnownPlant(in1.getItem()) || !PlantChemistry.isKnownPlant(in2.getItem())) return false;

        SynthesisRecipe.Result result = SynthesisRecipe.getResult(in1.getItem(), in2.getItem());
        if (result == null) return false;

        var resultItem = SynthesisRecipe.getResultItem(result);
        ItemStack output = inventory.getItem(2);
        if (output.isEmpty()) return true;
        return output.getItem() == resultItem && output.getCount() < output.getMaxStackSize();
    }

    public void serverTick() {
        if (isSynthesizable()) {
            progress++;
            setChanged();
            if (progress >= MAX_PROGRESS) {
                completeSynthesis();
                progress = 0;
            }
        } else if (progress != 0) {
            progress = 0;
            setChanged();
        }
    }

    private void completeSynthesis() {
        ItemStack in1 = inventory.getItem(0);
        ItemStack in2 = inventory.getItem(1);
        SynthesisRecipe.Result result = SynthesisRecipe.getResult(in1.getItem(), in2.getItem());
        if (result == null) return;

        var resultItem = SynthesisRecipe.getResultItem(result);
        ItemStack output = inventory.getItem(2);

        if (output.isEmpty()) {
            inventory.setItem(2, new ItemStack(resultItem, 1));
        } else {
            output.grow(1);
        }

        in1.shrink(1);
        in2.shrink(1);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Synthesis Table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, net.minecraft.world.entity.player.Inventory playerInv, Player player) {
        return new com.gizil.gizil.menu.SynthesisTableMenu(containerId, playerInv, this, this.dataAccess());
    }

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? progress : 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) progress = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public ContainerData dataAccess() {
        return dataAccess;
    }

    @Override
    protected void saveAdditional(net.minecraft.nbt.CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Progress", progress);
        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
        for (int i = 0; i < 3; i++) items.set(i, inventory.getItem(i));
        net.minecraft.world.ContainerHelper.saveAllItems(tag, items);
    }

    @Override
    public void load(net.minecraft.nbt.CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("Progress");
        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
        net.minecraft.world.ContainerHelper.loadAllItems(tag, items);
        for (int i = 0; i < 3; i++) inventory.setItem(i, items.get(i));
    }
}