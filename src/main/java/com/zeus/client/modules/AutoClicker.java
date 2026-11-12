package com.zeus.client.modules;

import com.zeus.client.utils.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.Random;

public class AutoClicker {

    private static final Random RANDOM = new Random();

    private static final double MIN_CPS = 9.0;
    private static final double MAX_CPS = 13.0;

    private static int delay = 0;

    public static void onTick(MinecraftClient mc) {
        if (!Modules.autoClicker) return;

        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;

        if (delay > 0) {
            delay--;
            return;
        }

        KeyBinding.onKeyPressed(mc.options.attackKey.getDefaultKey());

        delay = nextDelay();
    }

    private static int nextDelay() {
        double cps = MIN_CPS + RANDOM.nextDouble() * (MAX_CPS - MIN_CPS);
        int ticks = (int) Math.floor(20.0 / cps);

        return Math.max(1, ticks);
    }
}
