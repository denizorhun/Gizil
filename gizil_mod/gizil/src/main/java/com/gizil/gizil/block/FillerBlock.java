package com.gizil.gizil.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

import com.gizil.gizil.block.entity.FillerBlockEntity;
import com.gizil.gizil.block.entity.ResearchTableBlockEntity;

public class FillerBlock extends Block implements EntityBlock {

    public FillerBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.5F)
                .noOcclusion());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FillerBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof FillerBlockEntity filler) {
                BlockPos controllerPos = filler.getControllerPos();
                if (controllerPos != null && level.getBlockEntity(controllerPos) instanceof ResearchTableBlockEntity be
                        && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                    net.minecraftforge.network.NetworkHooks.openScreen(serverPlayer, be, controllerPos);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && !level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof FillerBlockEntity filler) {
                BlockPos controllerPos = filler.getControllerPos();
                if (controllerPos != null) {
                    ResearchTableBlock.breakStructure(level, controllerPos);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
