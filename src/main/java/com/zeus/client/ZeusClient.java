package com.zeus.client;

import com.zeus.client.modules.AutoClicker;
import com.zeus.client.modules.KillAura;
import com.zeus.client.modules.Scaffold;
import com.zeus.client.utils.BindManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ZeusClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BindManager.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            BindManager.update(client);

            if (client.player == null || client.world == null) return;

            KillAura.onTick(client);
            Scaffold.onTick(client);
            AutoClicker.onTick(client);
        });
    }
}
