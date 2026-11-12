package com.zeus.client.utils;

import com.google.gson.Gson;
import com.zeus.client.gui.ZeusScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Files;
import java.nio.file.Path;

public class BindManager {

    public static int killAuraKey = GLFW.GLFW_KEY_R;
    public static int scaffoldKey = GLFW.GLFW_KEY_G;
    public static int autoClickerKey = GLFW.GLFW_KEY_V;
    public static int openGuiKey = GLFW.GLFW_KEY_RIGHT_SHIFT;

    private static boolean prevKillAura = false;
    private static boolean prevScaffold = false;
    private static boolean prevAutoClicker = false;
    private static boolean prevOpenGui = false;

    private static final Gson GSON = new Gson();

    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("zeus_client")
            .resolve("binds.json");

    public enum BindTarget {
        KILL_AURA,
        SCAFFOLD,
        AUTO_CLICKER,
        OPEN_GUI
    }

    public static void update(MinecraftClient client) {
        if (client == null || client.getWindow() == null) return;

        long window = client.getWindow().getHandle();
        if (window == 0L) return;

        boolean killAuraPressed = isKeyPressed(window, killAuraKey);
        boolean scaffoldPressed = isKeyPressed(window, scaffoldKey);
        boolean autoClickerPressed = isKeyPressed(window, autoClickerKey);
        boolean openGuiPressed = isKeyPressed(window, openGuiKey);

        boolean noScreen = client.currentScreen == null;
        boolean zeusScreen = client.currentScreen instanceof ZeusScreen;

        boolean guiClicked = openGuiPressed && !prevOpenGui;

        if ((noScreen || zeusScreen) && guiClicked) {
            if (noScreen) {
                client.setScreen(new ZeusScreen());
            } else {
                client.setScreen(null);
            }
        }

        if (noScreen && !guiClicked) {
            if (killAuraPressed && !prevKillAura) {
                Modules.toggleKillAura(client);
            }

            if (scaffoldPressed && !prevScaffold) {
                Modules.toggleScaffold(client);
            }

            if (autoClickerPressed && !prevAutoClicker) {
                Modules.toggleAutoClicker(client);
            }
        }

        prevKillAura = killAuraPressed;
        prevScaffold = scaffoldPressed;
        prevAutoClicker = autoClickerPressed;
        prevOpenGui = openGuiPressed;
    }

    private static boolean isKeyPressed(long window, int key) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return false;
        return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
    }

    public static void setKey(BindTarget target, int key) {
        switch (target) {
            case KILL_AURA -> killAuraKey = key;
            case SCAFFOLD -> scaffoldKey = key;
            case AUTO_CLICKER -> autoClickerKey = key;
            case OPEN_GUI -> openGuiKey = key;
        }

        save();
    }

    public static int getKey(BindTarget target) {
        return switch (target) {
            case KILL_AURA -> killAuraKey;
            case SCAFFOLD -> scaffoldKey;
            case AUTO_CLICKER -> autoClickerKey;
            case OPEN_GUI -> openGuiKey;
        };
    }

    public static String getKeyName(int key) {
        switch (key) {
            case GLFW.GLFW_KEY_UNKNOWN:
                return "NONE";

            case GLFW.GLFW_KEY_ESCAPE:
                return "ESC";

            case GLFW.GLFW_KEY_SPACE:
                return "SPACE";

            case GLFW.GLFW_KEY_ENTER:
                return "ENTER";

            case GLFW.GLFW_KEY_TAB:
                return "TAB";

            case GLFW.GLFW_KEY_BACKSPACE:
                return "BACKSPACE";

            case GLFW.GLFW_KEY_DELETE:
                return "DELETE";

            case GLFW.GLFW_KEY_LEFT_SHIFT:
                return "L-SHIFT";

            case GLFW.GLFW_KEY_RIGHT_SHIFT:
                return "R-SHIFT";

            case GLFW.GLFW_KEY_LEFT_CONTROL:
                return "L-CTRL";

            case GLFW.GLFW_KEY_RIGHT_CONTROL:
                return "R-CTRL";

            case GLFW.GLFW_KEY_LEFT_ALT:
                return "L-ALT";

            case GLFW.GLFW_KEY_RIGHT_ALT:
                return "R-ALT";

            case GLFW.GLFW_KEY_UP:
                return "UP";

            case GLFW.GLFW_KEY_DOWN:
                return "DOWN";

            case GLFW.GLFW_KEY_LEFT:
                return "LEFT";

            case GLFW.GLFW_KEY_RIGHT:
                return "RIGHT";

            case GLFW.GLFW_KEY_CAPS_LOCK:
                return "CAPS";

            default:
                String name = GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));
                if (name != null && !name.isEmpty()) {
                    return name.toUpperCase();
                }

                return "KEY " + key;
        }
    }

    public static void load() {
        try {
            if (!Files.exists(CONFIG_PATH)) return;

            String json = Files.readString(CONFIG_PATH);
            BindsData data = GSON.fromJson(json, BindsData.class);

            if (data == null) return;

            killAuraKey = data.killAura;
            scaffoldKey = data.scaffold;
            autoClickerKey = data.autoClicker;
            openGuiKey = data.openGui;
        } catch (Throwable ignored) {
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            BindsData data = new BindsData(
                    killAuraKey,
                    scaffoldKey,
                    autoClickerKey,
                    openGuiKey
            );

            Files.writeString(CONFIG_PATH, GSON.toJson(data));
        } catch (Throwable ignored) {
        }
    }

    private static class BindsData {
        int killAura;
        int scaffold;
        int autoClicker;
        int openGui;

        BindsData() {
        }

        BindsData(int killAura, int scaffold, int autoClicker, int openGui) {
            this.killAura = killAura;
            this.scaffold = scaffold;
            this.autoClicker = autoClicker;
            this.openGui = openGui;
        }
    }
}
