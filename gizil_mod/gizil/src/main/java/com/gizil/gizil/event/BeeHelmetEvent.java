package com.gizil.gizil.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.core.particles.ParticleTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gizil.gizil.Gizil;
import com.gizil.gizil.registry.ModItems;
import com.gizil.gizil.registry.ModEntities;
import com.gizil.gizil.entity.GizilBee;

@Mod.EventBusSubscriber(modid = Gizil.MODID)
public class BeeHelmetEvent {

    private static final Map<UUID, Integer> ownedBees = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide()) return;
        if (event.player.tickCount % 20 != 0) return;
        Player player = event.player;
        if (!(player.level() instanceof ServerLevel level)) return;
        UUID playerId = player.getUUID();

        ItemStack helmet = player.getInventory().getArmor(3);
        boolean wearingHelmet = helmet.getItem() == ModItems.BEE_HELMET.get();

        GizilBee existingBee = findOwnedBee(level, player, playerId);

        if (!wearingHelmet) {
            if (existingBee != null) {
                existingBee.discard();
            }
            ownedBees.remove(playerId);
            return;
        }

        if (existingBee == null) {
            GizilBee bee = ModEntities.GIZIL_BEE.get().create(level);
            if (bee == null) return;
            bee.moveTo(player.getX() + 0.5, player.getY() + 1.2, player.getZ() + 0.5,
                    player.getYRot(), 0.0F);
            bee.setOwnerId(playerId);
            bee.setCustomName(Component.literal("Nektar")
                    .withStyle(Style.EMPTY
                            .withColor(TextColor.fromRgb(0xF7C94B))
                            .withItalic(false)
                            .withBold(true)));
            bee.setCustomNameVisible(true);
            level.addFreshEntity(bee);
            level.sendParticles(ParticleTypes.POOF,
                    bee.getX(), bee.getY() + 0.3, bee.getZ(),
                    12,
                    0.25, 0.25, 0.25,
                    0.02);     // hız
            ownedBees.put(playerId, bee.getId());
        }
    }

    private static GizilBee findOwnedBee(ServerLevel level, Player player, UUID playerId) {
        Integer beeId = ownedBees.get(playerId);
        if (beeId != null) {
            Entity entity = level.getEntity(beeId);
            if (entity instanceof GizilBee bee && bee.isAlive() && playerId.equals(bee.getOwnerId())) {
                return bee;
            }
            ownedBees.remove(playerId);
        }

        var nearby = level.getEntitiesOfClass(GizilBee.class,
                player.getBoundingBox().inflate(64.0),
                b -> b.isAlive() && playerId.equals(b.getOwnerId()));
        if (!nearby.isEmpty()) {
            GizilBee bee = nearby.get(0);
            ownedBees.put(playerId, bee.getId());
            return bee;
        }

        return null;
    }
}
