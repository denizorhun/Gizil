package com.gizil.gizil.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.server.level.ServerPlayer;

import org.jetbrains.annotations.Nullable;

import com.gizil.gizil.block.entity.FillerBlockEntity;
import com.gizil.gizil.block.entity.ResearchTableBlockEntity;
import com.gizil.gizil.registry.ModBlocks;

public class ResearchTableBlock extends BaseEntityBlock {


    private static final BlockPos FILLER_OFFSET = new BlockPos(0, 0, -1);

    public ResearchTableBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.5F)
                .noOcclusion());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof ResearchTableBlockEntity be
                    && player instanceof ServerPlayer serverPlayer) {
                net.minecraftforge.network.NetworkHooks.openScreen(serverPlayer, be, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide) return;

        BlockPos target = pos.offset(FILLER_OFFSET);
        BlockState targetState = level.getBlockState(target);
        if (!targetState.canBeReplaced() && !targetState.isAir()) {
            level.removeBlock(pos, false);
            if (placer instanceof Player player) {
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("Not enough space beside this spot for the research table."),
                        true);
            }
            return;
        }

        level.setBlock(target, ModBlocks.FILLER.get().defaultBlockState(), 3);
        if (level.getBlockEntity(target) instanceof FillerBlockEntity filler) {
            filler.setControllerPos(pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && !level.isClientSide) {
            breakStructure(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public static void breakStructure(Level level, BlockPos controllerPos) {
        if (level.getBlockState(controllerPos).is(ModBlocks.RESEARCH_TABLE.get())) {
            level.removeBlock(controllerPos, false);
        }
        BlockPos fillerPos = controllerPos.offset(FILLER_OFFSET);
        if (level.getBlockState(fillerPos).is(ModBlocks.FILLER.get())) {
            level.removeBlock(fillerPos, false);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ResearchTableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                    BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, be) -> {
            if (be instanceof ResearchTableBlockEntity rt) {
                rt.serverTick();
            }
        };
    }
}
