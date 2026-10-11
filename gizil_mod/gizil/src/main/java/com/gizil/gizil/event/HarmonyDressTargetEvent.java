package com.gizil.gizil.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.registry.ModItems;

@Mod.EventBusSubscriber(modid = Gizil.MODID)
public class HarmonyDressTargetEvent {

    private static final double KEEP_RANGE = 6.0;
    private static final int PARTICLE_COOLDOWN_TICKS = 100;
    private static final Map<UUID, Long> lastParticleTick = new HashMap<>();

    @SubscribeEvent
    public static void onChangeTarget(LivingChangeTargetEvent event) {
        if (!(event.getNewTarget() instanceof Player player)) return;

        ItemStack chest = player.getInventory().getArmor(2);
        if (chest.getItem() != ModItems.HARMONY_DRESS.get()) return;

        if (event.getEntity().distanceTo(player) > KEEP_RANGE) {
            event.setCanceled(true);

            if (event.getEntity().level() instanceof ServerLevel serverLevel) {
                UUID monsterId = event.getEntity().getUUID();
                long now = serverLevel.getGameTime();
                long last = lastParticleTick.getOrDefault(monsterId, -9999L);

                if (now - last >= PARTICLE_COOLDOWN_TICKS) {
                    lastParticleTick.put(monsterId, now);
                    serverLevel.sendParticles(
                            ParticleTypes.ENCHANT,
                            event.getEntity().getX(),
                            event.getEntity().getY() + event.getEntity().getBbHeight() * 0.5,
                            event.getEntity().getZ(),
                            6, 0.3, 0.4, 0.3, 0.05);
                }
            }
        }
    }
}