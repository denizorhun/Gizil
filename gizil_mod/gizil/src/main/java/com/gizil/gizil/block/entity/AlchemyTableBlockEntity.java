package com.gizil.gizil.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import com.gizil.gizil.menu.AlchemyTableMenu;
import com.gizil.gizil.registry.ModBlockEntities;
import com.gizil.gizil.transmutation.TransmutationCircle;
import com.gizil.gizil.transmutation.TransmutationFuel;
import com.gizil.gizil.transmutation.TransmutationManager;
import com.gizil.gizil.transmutation.TransmutationRecipe;

public class AlchemyTableBlockEntity extends BlockEntity implements MenuProvider {

    public static final int MAX_PROGRESS = 60; // 3 saniye
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FUEL = 1;
    public static final int SLOT_OUTPUT = 2;

    private java.util.UUID lastUser;

    private final SimpleContainer inventory = new SimpleContainer(3) {
        @Override
        public void setChanged() {
            super.setChanged();
            AlchemyTableBlockEntity.this.setChanged();
        }
    };

    private int progress = 0;
    private int charges = 0;
    private int circleIndex = 0;

    public AlchemyTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHEMY_TABLE.get(), pos, state);
    }

    public Container getInventory() {
        return inventory;
    }

    public void selectCircle(int index) {
        this.circleIndex = Math.floorMod(index, TransmutationCircle.values().length);
        this.progress = 0;
        setChanged();
    }

    private Optional<TransmutationRecipe> findRecipe() {
        ItemStack in = inventory.getItem(SLOT_INPUT);
        if (in.isEmpty()) return Optional.empty();
        return TransmutationManager.find(
                TransmutationCircle.byIndex(circleIndex).id(), in.getItem(), in.getCount());
    }

    private boolean canOutput(TransmutationRecipe recipe) {
        ItemStack out = inventory.getItem(SLOT_OUTPUT);
        if (out.isEmpty()) return true;
        return out.getItem() == recipe.outputItem()
                && out.getCount() + recipe.outputCount() <= out.getMaxStackSize();
    }

    public void serverTick() {
        Optional<TransmutationRecipe> recipe = findRecipe();
        if (recipe.isEmpty() || !canOutput(recipe.get())) {
            if (progress != 0) {
                progress = 0;
                setChanged();
            }
            return;
        }

        if (charges <= 0) {
            ItemStack fuel = inventory.getItem(SLOT_FUEL);
            if (!fuel.isEmpty() && TransmutationFuel.isFuel(fuel.getItem())) {
                fuel.shrink(1);
                charges = TransmutationFuel.CHARGES_PER_INK;
                setChanged();
            } else {
                if (progress != 0) {
                    progress = 0;
                    setChanged();
                }
                return;
            }
        }

        progress++;
        setChanged();
        if (progress >= MAX_PROGRESS) {
            complete(recipe.get());
            progress = 0;
        }
    }

    private void complete(TransmutationRecipe recipe) {
        inventory.getItem(SLOT_INPUT).shrink(recipe.inputCount());

        ItemStack out = inventory.getItem(SLOT_OUTPUT);
        if (out.isEmpty()) {
            inventory.setItem(SLOT_OUTPUT, new ItemStack(recipe.outputItem(), recipe.outputCount()));
        } else {
            out.grow(recipe.outputCount());
        }

        charges--;

        if (level != null && !level.isClientSide && lastUser != null) {
            net.minecraft.server.level.ServerPlayer sp =
                    level.getServer().getPlayerList().getPlayer(lastUser);
            if (sp != null && com.gizil.gizil.notebook.PlayerTransmutationData.add(sp, recipe.id().toString())) {
                sp.displayClientMessage(
                        Component.literal("A new transmutation is recorded in your notes."), true);
            }
        }

        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Alchemy Table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
        this.lastUser = player.getUUID();
        setChanged();
        return new AlchemyTableMenu(containerId, playerInv, this, this.dataAccess());
    }

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> charges;
                case 2 -> circleIndex;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> charges = value;
                case 2 -> circleIndex = value;
                default -> { }
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public ContainerData dataAccess() {
        return dataAccess;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Progress", progress);
        tag.putInt("Charges", charges);
        tag.putInt("Circle", circleIndex);
        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
        for (int i = 0; i < 3; i++) items.set(i, inventory.getItem(i));
        ContainerHelper.saveAllItems(tag, items);
        if (lastUser != null) tag.putUUID("LastUser", lastUser);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("Progress");
        charges = tag.getInt("Charges");
        circleIndex = tag.getInt("Circle");
        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
        for (int i = 0; i < 3; i++) inventory.setItem(i, items.get(i));
        if (tag.hasUUID("LastUser")) lastUser = tag.getUUID("LastUser");
    }
}