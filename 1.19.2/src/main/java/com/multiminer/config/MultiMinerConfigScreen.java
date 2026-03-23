package com.multiminer.config;

import com.multiminer.MultiMiner;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class MultiMinerConfigScreen extends Screen {

    private final Screen parent;
    private final MultiMinerConfig config;

    public MultiMinerConfigScreen(Screen parent) {
        super(Text.literal("MultiMiner Config"));
        this.parent = parent;
        this.config = MultiMiner.CONFIG;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = 40;
        int buttonWidth = 200;
        int spacing = 24;

        // Vein Mining Toggle
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.veinMiningEnabled)
                .build(centerX - buttonWidth / 2, y, buttonWidth, 20,
                        Text.literal("Vein Mining"),
                        (button, value) -> config.veinMiningEnabled = value));
        y += spacing;

        // Max Vein Size cycle: 16, 32, 64, 128, 256
        this.addDrawableChild(CyclingButtonWidget.<Integer>builder(v -> Text.literal("Max Vein Size: " + v))
                .values(16, 32, 64, 128, 256)
                .initially(config.maxVeinSize)
                .build(centerX - buttonWidth / 2, y, buttonWidth, 20,
                        Text.literal("Max Vein Size"),
                        (button, value) -> config.maxVeinSize = value));
        y += spacing;

        // Vein Requires Tool
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.veinRequiresTool)
                .build(centerX - buttonWidth / 2, y, buttonWidth, 20,
                        Text.literal("Vein Requires Tool"),
                        (button, value) -> config.veinRequiresTool = value));
        y += spacing;

        // Tree Felling Toggle
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.treeFellingEnabled)
                .build(centerX - buttonWidth / 2, y, buttonWidth, 20,
                        Text.literal("Tree Felling"),
                        (button, value) -> config.treeFellingEnabled = value));
        y += spacing;

        // Max Tree Size cycle: 32, 64, 128, 256, 512
        this.addDrawableChild(CyclingButtonWidget.<Integer>builder(v -> Text.literal("Max Tree Size: " + v))
                .values(32, 64, 128, 256, 512)
                .initially(config.maxTreeSize)
                .build(centerX - buttonWidth / 2, y, buttonWidth, 20,
                        Text.literal("Max Tree Size"),
                        (button, value) -> config.maxTreeSize = value));
        y += spacing;

        // Tree Requires Tool
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.treeRequiresTool)
                .build(centerX - buttonWidth / 2, y, buttonWidth, 20,
                        Text.literal("Tree Requires Axe"),
                        (button, value) -> config.treeRequiresTool = value));
        y += spacing;

        // Activation Mode
        this.addDrawableChild(CyclingButtonWidget.<MultiMinerConfig.ActivationMode>builder(
                        mode -> Text.literal("Activation: " + mode.name()))
                .values(MultiMinerConfig.ActivationMode.values())
                .initially(config.activationMode)
                .build(centerX - buttonWidth / 2, y, buttonWidth, 20,
                        Text.literal("Activation Mode"),
                        (button, value) -> config.activationMode = value));
        y += spacing + 10;

        // Done button
        this.addDrawableChild(new ButtonWidget(centerX - 100, y, 200, 20,
                ScreenTexts.DONE, button -> close()));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        config.save();
        this.client.setScreen(parent);
    }
}
