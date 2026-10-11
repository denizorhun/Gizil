package com.gizil.gizil.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.registry.ModItems;

@Mod.EventBusSubscriber(modid = Gizil.MODID)
public class FlowerBootsEvent {

    // Single block plants
    private static Block getRandomPlant() {
        int roll = RANDOM.nextInt(10);
        if (roll < 4) return Blocks.GRASS;          // 40% short grass
        if (roll < 5) return Blocks.FERN;           // 10% fern
        // remaining 50% split between all flowers
        List<Block> flowers = List.of(
                Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID,
                Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP,
                Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP,
                Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY
        );
        return flowers.get(RANDOM.nextInt(flowers.size()));
    }

    // Double block flowers
    private static final List<DoublePlantBlock> DOUBLE_PLANTS = List.of(
            (DoublePlantBlock) Blocks.SUNFLOWER,
            (DoublePlantBlock) Blocks.LILAC,
            (DoublePlantBlock) Blocks.ROSE_BUSH,
            (DoublePlantBlock) Blocks.PEONY
    );

    private static final Random RANDOM = new Random();

    // Track last known position per player
    private static final Map<UUID, BlockPos> lastPos = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide()) return;
        if (event.player.tickCount % 5 != 0) return;

        Player player = event.player;
        UUID id = player.getUUID();
        BlockPos currentPos = player.blockPosition();
        BlockPos previous = lastPos.get(id);

        // Update last known position
        lastPos.put(id, currentPos);

        // Skip if player hasn't moved since last check
        if (previous == null || currentPos.equals(previous)) return;

        // Skip if player is falling
        if (player.getDeltaMovement().y < -0.15) return;

        // Check if the player is wearing flower boots
        ItemStack boots = player.getInventory().getArmor(0);
        if (boots.getItem() != ModItems.FLOWER_BOOTS.get()) return;

        BlockPos below = currentPos.below();
        BlockPos plantPos = currentPos;
        Level level = player.level();

        // Only trigger (particles + growth) on grass blocks
        if (!level.getBlockState(below).is(Blocks.GRASS_BLOCK)) return;

        if (level instanceof ServerLevel serverLevel) {
            int roll = RANDOM.nextInt(3);
            switch (roll) {
                case 0 -> serverLevel.sendParticles(
                        ParticleTypes.COMPOSTER,
                        currentPos.getX() + 0.5, currentPos.getY() + 0.15, currentPos.getZ() + 0.5,
                        2, 0.25, 0.05, 0.25, 0.01);
                case 1 -> serverLevel.sendParticles(
                        ParticleTypes.HAPPY_VILLAGER,
                        currentPos.getX() + 0.5, currentPos.getY() + 0.15, currentPos.getZ() + 0.5,
                        3, 0.25, 0.05, 0.25, 0.01);
                default -> serverLevel.sendParticles(
                        ParticleTypes.CHERRY_LEAVES,
                        currentPos.getX() + 0.5, currentPos.getY() + 0.3, currentPos.getZ() + 0.5,
                        2, 0.3, 0.15, 0.3, 0.0);
            }
        }

        // Only spawn if the target position is empty
        if (!level.getBlockState(plantPos).isAir()) return;

        if (RANDOM.nextInt(30) < 29) {
            Block plant = getRandomPlant();
            BlockState plantState = plant.defaultBlockState();
            if (plantState.canSurvive(level, plantPos)) {
                level.setBlock(plantPos, plantState, 3);
            }
        } else {
            if (!level.getBlockState(plantPos.above()).isAir()) return;
            DoublePlantBlock plant = DOUBLE_PLANTS.get(RANDOM.nextInt(DOUBLE_PLANTS.size()));
            DoublePlantBlock.placeAt(level, plant.defaultBlockState(), plantPos, 3);
        }
    }
}
