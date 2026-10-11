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
import com.gizil.gizil.item.CrackedRelicItem;
import com.gizil.gizil.notebook.ResearchableFlowers;
import com.gizil.gizil.notebook.PlayerNotebookData;

public class ResearchTableBlockEntity extends BlockEntity implements MenuProvider {

    public static final int MAX_PROGRESS = 160;

    private final SimpleContainer inventory = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            ResearchTableBlockEntity.this.setChanged();
        }
    };

    private int progress = 0;
    private boolean completed = false;
    private Player currentUser;
    private Boolean cachedKnownStatus = null;

    public ResearchTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESEARCH_TABLE.get(), pos, state);
    }

    public Container getInventory() {
        return inventory;
    }

    public int getProgress() {
        return progress;
    }

    public void setCurrentUser(Player player) {
        this.currentUser = player;
    }

    public boolean isResearchable() {
        ItemStack stack = inventory.getItem(0);
        if (stack.isEmpty()) return false;
        return stack.getItem() instanceof CrackedRelicItem
                || ResearchableFlowers.isResearchableFlower(stack.getItem());
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean consumeCompletionFlag() {
        if (completed) {
            completed = false;
            return true;
        }
        return false;
    }

    public void serverTick() {
        if (!isResearchable()) {
            if (progress != 0) { progress = 0; setChanged(); }
            cachedKnownStatus = null;
            return;
        }

        if (cachedKnownStatus == null) {
            cachedKnownStatus = isAlreadyKnown();
        }

        if (!cachedKnownStatus && progress < MAX_PROGRESS) {
            progress++;
            setChanged();
            if (progress >= MAX_PROGRESS) {
                completed = true;
                unlockForCurrentUser();
            }
        } else if (cachedKnownStatus && progress != 0) {
            progress = 0;
            setChanged();
        }
    }

    private boolean isAlreadyKnown() {
        if (!(currentUser instanceof net.minecraft.server.level.ServerPlayer serverPlayer)) return false;
        ItemStack stack = inventory.getItem(0);

        if (stack.getItem() instanceof CrackedRelicItem relic) {
            return serverPlayer.getRecipeBook().contains(relic.getUnlockedRecipeId());
        }
        if (ResearchableFlowers.isResearchableFlower(stack.getItem())) {
            var pageId = ResearchableFlowers.getPageFor(stack.getItem());
            return PlayerNotebookData.knowsPage(serverPlayer, pageId.toString());
        }
        return false;
    }

    private void unlockForCurrentUser() {
        ItemStack stack = inventory.getItem(0);
        if (!(currentUser instanceof net.minecraft.server.level.ServerPlayer serverPlayer)) return;

        if (stack.getItem() instanceof CrackedRelicItem relic) {
            serverPlayer.awardRecipesByKey(new net.minecraft.resources.ResourceLocation[]{relic.getUnlockedRecipeId()});
            serverPlayer.sendSystemMessage(
                    Component.literal("A memory flickers before your eyes, not yours, but somehow familiar.")
                            .withStyle(net.minecraft.network.chat.Style.EMPTY
                                    .withColor(net.minecraft.network.chat.TextColor.fromRgb(0xE8C9A0))
                                    .withItalic(true)));
        } else if (ResearchableFlowers.isResearchableFlower(stack.getItem())) {
            var pageId = ResearchableFlowers.getPageFor(stack.getItem());
            PlayerNotebookData.addKnownPage(serverPlayer, pageId.toString());
            serverPlayer.sendSystemMessage(
                    Component.literal("You add what you've learned to your notes.")
                            .withStyle(net.minecraft.network.chat.Style.EMPTY
                                    .withColor(net.minecraft.network.chat.TextColor.fromRgb(0xB8D97A))
                                    .withItalic(true)));
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Research Table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, net.minecraft.world.entity.player.Inventory playerInv, Player player) {
        return new com.gizil.gizil.menu.ResearchTableMenu(containerId, playerInv, this, this.dataAccess());
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
        NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
        items.set(0, inventory.getItem(0));
        net.minecraft.world.ContainerHelper.saveAllItems(tag, items);
    }

    @Override
    public void load(net.minecraft.nbt.CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("Progress");
        NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
        net.minecraft.world.ContainerHelper.loadAllItems(tag, items);
        inventory.setItem(0, items.get(0));
    }
}