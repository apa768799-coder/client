package com.zeus.client.modules;

import com.zeus.client.utils.Modules;
import com.zeus.client.utils.TeamUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.List;

public class KillAura {

    private static final double REACH = 4.5;

    public static void onTick(MinecraftClient mc) {
        if (!Modules.killAura) return;

        ClientPlayerEntity player = mc.player;
        if (player == null || mc.world == null || mc.interactionManager == null) return;
        if (mc.currentScreen != null) return;

        if (player.getAttackCooldownProgress(0.0f) < 1.0f) return;

        Box box = player.getBoundingBox().expand(REACH, 3.0, REACH);

        List<LivingEntity> targets = mc.world.getEntitiesByClass(
                LivingEntity.class,
                box,
                entity -> {
                    if (entity == player) return false;
                    if (entity.isDead()) return false;

                    if (!(entity instanceof PlayerEntity targetPlayer)) return false;

                    if (targetPlayer.isSpectator() || targetPlayer.isCreative()) return false;

                    if (TeamUtils.isAlly(mc, targetPlayer)) return false;

                    return player.distanceTo(targetPlayer) <= REACH;
                }
        );

        if (targets.isEmpty()) return;

        targets.sort(
                Comparator.comparingDouble(player::distanceTo)
                        .thenComparingDouble(LivingEntity::getHealth)
        );

        LivingEntity target = targets.get(0);

        mc.interactionManager.attackEntity(player, target);
        player.swingHand(Hand.MAIN_HAND);

        faceTarget(player, target);
    }

    private static void faceTarget(ClientPlayerEntity player, LivingEntity target) {
        double dx = target.getX() - player.getX();
        double dz = target.getZ() - player.getZ();
        double dy = (target.getY() + target.getStandingEyeHeight())
                - (player.getY() + player.getStandingEyeHeight());

        double distance = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) (-Math.atan2(dy, distance) * 180.0 / Math.PI);

        player.setYaw(yaw);
        player.setPitch(pitch);
    }
}
