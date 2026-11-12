package com.zeus.client.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Modules {

    public static boolean killAura = true;
    public static boolean scaffold = true;
    public static boolean autoClicker = false;

    public static void toggleKillAura(MinecraftClient mc) {
        killAura = !killAura;
        notify(mc, "KillAura", killAura);
    }

    public static void toggleScaffold(MinecraftClient mc) {
        scaffold = !scaffold;
        notify(mc, "Scaffold", scaffold);
    }

    public static void toggleAutoClicker(MinecraftClient mc) {
        autoClicker = !autoClicker;
        notify(mc, "AutoClicker", autoClicker);
    }

    private static void notify(MinecraftClient mc, String module, boolean enabled) {
        if (mc.player == null) return;

        mc.player.sendMessage(
                Text.literal("Zeus | " + module + ": " + (enabled ? "ON" : "OFF")),
                true
        );
    }
}
