package com.zeus.client.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class InventoryUtils {

    private static final Block[] ALLOWED_BLOCKS = {
            Blocks.WHITE_WOOL,
            Blocks.ORANGE_WOOL,
            Blocks.MAGENTA_WOOL,
            Blocks.LIGHT_BLUE_WOOL,
            Blocks.YELLOW_WOOL,
            Blocks.LIME_WOOL,
            Blocks.PINK_WOOL,
            Blocks.GRAY_WOOL,
            Blocks.LIGHT_GRAY_WOOL,
            Blocks.CYAN_WOOL,
            Blocks.PURPLE_WOOL,
            Blocks.BLUE_WOOL,
            Blocks.BROWN_WOOL,
            Blocks.GREEN_WOOL,
            Blocks.RED_WOOL,
            Blocks.BLACK_WOOL,
            Blocks.STONE,
            Blocks.COBBLESTONE,
            Blocks.DIRT
    };

    public static int findBlockSlot() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return -1;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);

            if (stack.getItem() instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();

                for (Block allowed : ALLOWED_BLOCKS) {
                    if (block == allowed) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }
}
