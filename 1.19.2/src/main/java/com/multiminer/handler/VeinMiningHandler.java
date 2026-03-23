package com.multiminer.handler;

import com.multiminer.MultiMiner;
import com.multiminer.config.MultiMinerConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class VeinMiningHandler {

    // All vanilla ore-like tags we want to support
    private static boolean isOre(BlockState state) {
        return state.isIn(BlockTags.COAL_ORES)
                || state.isIn(BlockTags.IRON_ORES)
                || state.isIn(BlockTags.GOLD_ORES)
                || state.isIn(BlockTags.DIAMOND_ORES)
                || state.isIn(BlockTags.EMERALD_ORES)
                || state.isIn(BlockTags.LAPIS_ORES)
                || state.isIn(BlockTags.REDSTONE_ORES)
                || state.isIn(BlockTags.COPPER_ORES);
    }

    public static void handle(World world, PlayerEntity player, BlockPos pos, BlockState brokenState) {
        if (!isOre(brokenState)) return;
        if (!isActivated(player)) return;

        MultiMinerConfig config = MultiMiner.CONFIG;

        // Check tool requirement
        ItemStack tool = player.getMainHandStack();
        if (config.veinRequiresTool && !tool.isSuitableFor(brokenState)) return;

        Block targetBlock = brokenState.getBlock();
        List<BlockPos> vein = findConnected(world, pos, targetBlock, config.maxVeinSize);

        if (vein.isEmpty()) return;

        ServerWorld serverWorld = (ServerWorld) world;
        for (BlockPos veinPos : vein) {
            BlockState state = world.getBlockState(veinPos);
            // Drop items and experience
            Block.dropStacks(state, world, veinPos, world.getBlockEntity(veinPos), player, tool);
            serverWorld.breakBlock(veinPos, false, player);

            // Damage tool
            if (!tool.isEmpty()) {
                tool.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
                if (tool.isEmpty()) break;
            }
        }

        // Apply exhaustion
        player.addExhaustion(0.005f * vein.size() * config.veinExhaustionMultiplier);
    }

    private static List<BlockPos> findConnected(World world, BlockPos origin, Block target, int maxSize) {
        List<BlockPos> result = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        // Add all neighbors of the broken block as starting points
        for (BlockPos neighbor : getNeighbors(origin)) {
            queue.add(neighbor);
        }
        visited.add(origin);

        while (!queue.isEmpty() && result.size() < maxSize) {
            BlockPos current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            BlockState state = world.getBlockState(current);
            if (state.getBlock() != target) continue;

            result.add(current);

            for (BlockPos neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }
        return result;
    }

    private static List<BlockPos> getNeighbors(BlockPos pos) {
        List<BlockPos> neighbors = new ArrayList<>(26);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    neighbors.add(pos.add(dx, dy, dz));
                }
            }
        }
        return neighbors;
    }

    private static boolean isActivated(PlayerEntity player) {
        MultiMinerConfig config = MultiMiner.CONFIG;
        switch (config.activationMode) {
            case ALWAYS_ON:
                return true;
            case SNEAK:
                return player.isSneaking();
            case KEYBIND:
                // Keybind check only works client-side; on server we check sneaking as fallback
                // A proper implementation would use networking packets
                return player.isSneaking();
            default:
                return false;
        }
    }
}
