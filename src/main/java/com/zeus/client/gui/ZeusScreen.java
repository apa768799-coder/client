package com.zeus.client.gui;

import com.zeus.client.utils.BindManager;
import com.zeus.client.utils.Modules;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ZeusScreen extends Screen {

    private BindManager.BindTarget listening;

    private ButtonWidget killAuraToggle;
    private ButtonWidget scaffoldToggle;
    private ButtonWidget autoClickerToggle;
    private ButtonWidget openGuiLabel;

    private ButtonWidget killAuraBind;
    private ButtonWidget scaffoldBind;
    private ButtonWidget autoClickerBind;
    private ButtonWidget openGuiBind;

    public ZeusScreen() {
        super(Text.literal("Zeus Client"));
    }

    @Override
    protected void init() {
        int y = 45;

        int toggleWidth = 140;
        int bindWidth = 110;

        int toggleX = this.width / 2 - toggleWidth - 4;
        int bindX = this.width / 2 + 4;

        killAuraToggle = ButtonWidget.builder(
                Text.literal(toggleLabel("KillAura", Modules.killAura)),
                button -> {
                    if (this.client != null) {
                        Modules.toggleKillAura(this.client);
                        updateButtons();
                    }
                }
        ).dimensions(toggleX, y, toggleWidth, 20).build();

        killAuraBind = ButtonWidget.builder(
                Text.literal(bindLabel(BindManager.BindTarget.KILL_AURA)),
                button -> startListening(BindManager.BindTarget.KILL_AURA)
        ).dimensions(bindX, y, bindWidth, 20).build();

        addDrawableChild(killAuraToggle);
        addDrawableChild(killAuraBind);

        y += 24;

        scaffoldToggle = ButtonWidget.builder(
                Text.literal(toggleLabel("Scaffold", Modules.scaffold)),
                button -> {
                    if (this.client != null) {
                        Modules.toggleScaffold(this.client);
                        updateButtons();
                    }
                }
        ).dimensions(toggleX, y, toggleWidth, 20).build();

        scaffoldBind = ButtonWidget.builder(
                Text.literal(bindLabel(BindManager.BindTarget.SCAFFOLD)),
                button -> startListening(BindManager.BindTarget.SCAFFOLD)
        ).dimensions(bindX, y, bindWidth, 20).build();

        addDrawableChild(scaffoldToggle);
        addDrawableChild(scaffoldBind);

        y += 24;

        autoClickerToggle = ButtonWidget.builder(
                Text.literal(toggleLabel("AutoClicker", Modules.autoClicker)),
                button -> {
                    if (this.client != null) {
                        Modules.toggleAutoClicker(this.client);
                        updateButtons();
                    }
                }
        ).dimensions(toggleX, y, toggleWidth, 20).build();

        autoClickerBind = ButtonWidget.builder(
                Text.literal(bindLabel(BindManager.BindTarget.AUTO_CLICKER)),
                button -> startListening(BindManager.BindTarget.AUTO_CLICKER)
        ).dimensions(bindX, y, bindWidth, 20).build();

        addDrawableChild(autoClickerToggle);
        addDrawableChild(autoClickerBind);

        y += 24;

        openGuiLabel = ButtonWidget.builder(
                Text.literal("Open GUI"),
                button -> {
                }
        ).dimensions(toggleX, y, toggleWidth, 20).build();

        openGuiLabel.active = false;

        openGuiBind = ButtonWidget.builder(
                Text.literal(bindLabel(BindManager.BindTarget.OPEN_GUI)),
                button -> startListening(BindManager.BindTarget.OPEN_GUI)
        ).dimensions(bindX, y, bindWidth, 20).build();

        addDrawableChild(openGuiLabel);
        addDrawableChild(openGuiBind);
    }

    private void startListening(BindManager.BindTarget target) {
        this.listening = target;
        updateButtons();
    }

    private void updateButtons() {
        if (killAuraToggle != null) {
            killAuraToggle.setMessage(Text.literal(toggleLabel("KillAura", Modules.killAura)));
        }

        if (scaffoldToggle != null) {
            scaffoldToggle.setMessage(Text.literal(toggleLabel("Scaffold", Modules.scaffold)));
        }

        if (autoClickerToggle != null) {
            autoClickerToggle.setMessage(Text.literal(toggleLabel("AutoClicker", Modules.autoClicker)));
        }

        if (killAuraBind != null) {
            killAuraBind.setMessage(Text.literal(bindLabel(BindManager.BindTarget.KILL_AURA)));
        }

        if (scaffoldBind != null) {
            scaffoldBind.setMessage(Text.literal(bindLabel(BindManager.BindTarget.SCAFFOLD)));
        }

        if (autoClickerBind != null) {
            autoClickerBind.setMessage(Text.literal(bindLabel(BindManager.BindTarget.AUTO_CLICKER)));
        }

        if (openGuiBind != null) {
            openGuiBind.setMessage(Text.literal(bindLabel(BindManager.BindTarget.OPEN_GUI)));
        }
    }

    private String toggleLabel(String name, boolean enabled) {
        return name + ": " + (enabled ? "ON" : "OFF");
    }

    private String bindLabel(BindManager.BindTarget target) {
        if (listening == target) {
            return "Press key...";
        }

        return "Bind: " + BindManager.getKeyName(BindManager.getKey(target));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xB0000000);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Zeus Client"),
                this.width / 2,
                20,
                0xFFFFFF
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("BACKSPACE while binding = unbind"),
                this.width / 2,
                this.height - 16,
                0xAAAAAA
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listening != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                listening = null;
                updateButtons();
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                BindManager.setKey(listening, GLFW.GLFW_KEY_UNKNOWN);
                listening = null;
                updateButtons();
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_UNKNOWN) {
                return true;
            }

            BindManager.setKey(listening, keyCode);
            listening = null;
            updateButtons();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
