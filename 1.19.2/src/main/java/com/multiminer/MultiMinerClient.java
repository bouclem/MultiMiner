package com.multiminer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MultiMinerClient implements ClientModInitializer {

    public static KeyBinding activationKey;

    @Override
    public void onInitializeClient() {
        activationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.multiminer.activate",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                "category.multiminer"
        ));
    }
}
