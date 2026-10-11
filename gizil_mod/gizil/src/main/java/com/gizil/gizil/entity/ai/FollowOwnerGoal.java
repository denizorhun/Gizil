package com.gizil.gizil.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;

import java.util.EnumSet;

import com.gizil.gizil.entity.GizilBee;

public class FollowOwnerGoal extends Goal {

    private final GizilBee bee;
    private final double speedModifier;
    private final float followDistance;
    private Player owner;
    private int timeToRecalcPath;
    private double targetX, targetY, targetZ;
    private int orbitRecalcTimer;
    private double orbitAngle;
    private double lastOwnerX, lastOwnerZ;
    private boolean ownerPosInitialized;


    public FollowOwnerGoal(GizilBee bee, double speedModifier, float followDistance) {
        this.bee = bee;
        this.speedModifier = speedModifier;
        this.followDistance = followDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        Player player = bee.getOwnerAsPlayer();
        if (player == null || player.isSpectator() || !player.isAlive()) return false;
        this.owner = player;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return owner != null && owner.isAlive() && !owner.isSpectator();
    }

    @Override
    public void start() {
        this.orbitRecalcTimer = 0;
        this.ownerPosInitialized = false;
    }

    @Override
    public void stop() {
        this.owner = null;
    }

    @Override
    public void tick() {
        if (owner == null) return;

        double distSq = bee.distanceToSqr(owner);
        if (distSq > 400.0) {
            bee.teleportTo(owner.getX(), owner.getY() + 1.0D, owner.getZ());
            return;
        }

        boolean isFar = distSq > (double) (followDistance * followDistance);

        if (--orbitRecalcTimer <= 0) {
            orbitRecalcTimer = 4;

            if (!ownerPosInitialized) {
                lastOwnerX = owner.getX();
                lastOwnerZ = owner.getZ();
                orbitAngle = (bee.getId() % 360);
                ownerPosInitialized = true;
            }

            double movedDist = Math.hypot(owner.getX() - lastOwnerX, owner.getZ() - lastOwnerZ);
            boolean ownerMoving = movedDist > 0.3;
            lastOwnerX = owner.getX();
            lastOwnerZ = owner.getZ();

                if (ownerMoving || isFar) {
                orbitAngle += 12.0;
            }

            double orbitRadius = isFar ? 1.0 : 1.5;
            double rad = Math.toRadians(orbitAngle);
            targetX = owner.getX() + Math.cos(rad) * orbitRadius;
            targetY = owner.getY() + 1.3D;
            targetZ = owner.getZ() + Math.sin(rad) * orbitRadius;
        }

        double dx = targetX - bee.getX();
        double dy = targetY - bee.getY();
        double dz = targetZ - bee.getZ();
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (dist > 0.3) {
            double speed = isFar ? (0.22 + speedModifier * 0.08) : (0.10 + speedModifier * 0.03);
            double blend = isFar ? 0.4 : 0.2;

            bee.setDeltaMovement(
                    bee.getDeltaMovement().scale(1.0 - blend * 0.5).add(
                            (dx / dist) * speed * blend,
                            (dy / dist) * speed * blend,
                            (dz / dist) * speed * blend
                    )
            );

            float yaw = (float) (Mth.atan2(dz, dx) * (180D / Math.PI)) - 90.0F;
            float pitch = (float) (-(Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * (180D / Math.PI)));
            bee.setYRot(rotlerp(bee.getYRot(), yaw, 20.0F));
            bee.setXRot(rotlerp(bee.getXRot(), pitch, 20.0F));
            bee.yBodyRot = bee.getYRot();
            bee.yHeadRot = bee.getYRot();
        }
    }

    private static float rotlerp(float current, float target, float maxChange) {
        float diff = Mth.wrapDegrees(target - current);
        if (diff > maxChange) diff = maxChange;
        if (diff < -maxChange) diff = -maxChange;
        return current + diff;
    }
}
