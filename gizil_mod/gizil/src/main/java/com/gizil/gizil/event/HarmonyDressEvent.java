package com.gizil.gizil.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.registry.ModItems;

@Mod.EventBusSubscriber(modid = Gizil.MODID)
public class HarmonyDressEvent {

    private static final double FORGET_RADIUS = 24.0;
    private static final double KEEP_RANGE = 6.0;
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide()) return;

        Player player = event.player;
        ItemStack chest = player.getInventory().getArmor(2);
        boolean wearingDress = chest.getItem() == ModItems.HARMONY_DRESS.get();
        if (!wearingDress) return;

        if (player.tickCount % 40 == 0 && player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    net.minecraft.core.particles.ParticleTypes.CHERRY_LEAVES,
                    player.getX(), player.getY() + 1.8, player.getZ(),
                    1, 0.3, 0.15, 0.3, 0.01);
        }

        if (player.tickCount % 20 == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0, true, false));

            if (player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                if (RANDOM.nextInt(30) == 0) {
                    serverLevel.sendParticles(
                            net.minecraft.core.particles.ParticleTypes.HEART,
                            player.getX(), player.getY() + 1.8, player.getZ(),
                            1, 0.2, 0.1, 0.2, 0.0);
                }
            }
        }
    }
}