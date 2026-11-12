package com.zeus.client.modules;

import com.zeus.client.utils.InventoryUtils;
import com.zeus.client.utils.Modules;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Scaffold {

    public static void onTick(MinecraftClient mc) {
        if (!Modules.scaffold) return;

        ClientPlayerEntity player = mc.player;
        if (player == null || mc.world == null || mc.interactionManager == null) return;
        if (mc.currentScreen != null) return;

        BlockPos below = player.getBlockPos().down();
        BlockState state = mc.world.getBlockState(below);

        if (state.isAir() && !player.isSneaking()) {
            int blockSlot = InventoryUtils.findBlockSlot();

            if (blockSlot != -1) {
                int oldSlot = player.getInventory().selectedSlot;
                player.getInventory().selectedSlot = blockSlot;

                Vec3d hitVec = Vec3d.ofCenter(below);
                BlockHitResult hitResult = new BlockHitResult(hitVec, Direction.UP, below, false);

                // Сигнатура этой версии: (player, hand, hitResult)
                mc.interactionManager.interactBlock(player, Hand.MAIN_HAND, hitResult);

                player.getInventory().selectedSlot = oldSlot;
            }
        }
    }
}
