package com.multiminer;

import com.multiminer.config.MultiMinerConfig;
import com.multiminer.handler.TreeFellingHandler;
import com.multiminer.handler.VeinMiningHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiMiner implements ModInitializer {

    public static final String MOD_ID = "multiminer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static MultiMinerConfig CONFIG;

    @Override
    public void onInitialize() {
        CONFIG = MultiMinerConfig.load();
        LOGGER.info("MultiMiner loaded!");

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient()) return;
            if (player.isSpectator()) return;

            if (CONFIG.veinMiningEnabled) {
                VeinMiningHandler.handle(world, player, pos, state);
            }
            if (CONFIG.treeFellingEnabled) {
                TreeFellingHandler.handle(world, player, pos, state);
            }
        });
    }
}
