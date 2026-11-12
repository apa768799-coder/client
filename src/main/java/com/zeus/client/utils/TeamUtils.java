package com.zeus.client.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class TeamUtils {

    public static boolean isAlly(MinecraftClient mc, LivingEntity target) {
        ClientPlayerEntity player = mc.player;
        if (player == null || target == null) return false;
        if (target == player) return true;

        // Команды скорборда: серверные тимы Bedwars
        if (player.isTeammate(target)) return true;

        // Цвет отображаемого имени
        TextColor playerNameColor = getTextColor(player.getDisplayName());
        TextColor targetNameColor = getTextColor(target.getDisplayName());

        if (playerNameColor != null
                && targetNameColor != null
                && playerNameColor.equals(targetNameColor)) {
            return true;
        }

        // Старые цветовые коды §
        Formatting playerLegacyColor = getLegacyColor(player.getDisplayName());
        Formatting targetLegacyColor = getLegacyColor(target.getDisplayName());

        if (playerLegacyColor != null
                && targetLegacyColor != null
                && playerLegacyColor == targetLegacyColor) {
            return true;
        }

        // Крашеная броня через компоненты данных
        return sameArmorColor(player, target);
    }

    private static TextColor getTextColor(Text text) {
        if (text == null) return null;

        try {
            return text.getStyle().getColor();
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Formatting getLegacyColor(Text text) {
        if (text == null) return null;

        String raw = text.getString();

        for (int i = 0; i < raw.length() - 1; i++) {
            if (raw.charAt(i) == '§') {
                Formatting formatting = Formatting.byCode(raw.charAt(i + 1));
                if (formatting != null && formatting.isColor()) {
                    return formatting;
                }
            }
        }

        return null;
    }

    private static boolean sameArmorColor(LivingEntity first, LivingEntity second) {
        int firstColor = getArmorColor(first);
        int secondColor = getArmorColor(second);

        return firstColor != -1 && firstColor == secondColor;
    }

    private static int getArmorColor(LivingEntity entity) {
        EquipmentSlot[] slots = {
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        };

        for (EquipmentSlot slot : slots) {
            int color = getDyedColor(entity.getEquippedStack(slot));
            if (color != -1) return color;
        }

        return -1;
    }

    private static int getDyedColor(ItemStack stack) {
        try {
            if (stack == null || stack.isEmpty()) return -1;

            // Yarn-маппинги используют DataComponentTypes
            DyedColorComponent dyed = stack.get(DataComponentTypes.DYED_COLOR);
            if (dyed == null) return -1;

            return dyed.rgb();
        } catch (Throwable ignored) {
            return -1;
        }
    }
}
