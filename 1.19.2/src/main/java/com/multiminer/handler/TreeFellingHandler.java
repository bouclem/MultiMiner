package com.multiminer.handler;

import com.multiminer.MultiMiner;
import com.multiminer.config.MultiMinerConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class TreeFellingHandler {

    public static void handle(World world, PlayerEntity player, BlockPos pos, BlockState brokenState) {
        if (!brokenState.isIn(BlockTags.LOGS)) return;
        if (!isActivated(player)) return;

        MultiMinerConfig config = MultiMiner.CONFIG;

        // Check tool requirement (axe)
        ItemStack tool = player.getMainHandStack();
        if (config.treeRequiresTool && !(tool.getItem() instanceof AxeItem)) return;

        Block targetBlock = brokenState.getBlock();
        List<BlockPos> tree = findTree(world, pos, targetBlock, config.maxTreeSize);

        if (tree.isEmpty()) return;

        // Sort top-to-bottom so tree falls naturally
        tree.sort(Comparator.comparingInt(BlockPos::getY).reversed());

        ServerWorld serverWorld = (ServerWorld) world;
        for (BlockPos logPos : tree) {
            BlockState state = world.getBlockState(logPos);
            Block.dropStacks(state, world, logPos, world.getBlockEntity(logPos), player, tool);
            serverWorld.breakBlock(logPos, false, player);

            // Damage tool
            if (!tool.isEmpty()) {
                tool.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
                if (tool.isEmpty()) break;
            }
        }

        // Apply exhaustion
        player.addExhaustion(0.005f * tree.size() * config.treeExhaustionMultiplier);
    }

    private static List<BlockPos> findTree(World world, BlockPos origin, Block targetLog, int maxSize) {
        List<BlockPos> result = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        // Start from neighbors of the broken block
        for (BlockPos neighbor : getTreeNeighbors(origin)) {
            queue.add(neighbor);
        }
        visited.add(origin);

        while (!queue.isEmpty() && result.size() < maxSize) {
            BlockPos current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            BlockState state = world.getBlockState(current);
            // Accept same log type OR any log (for trees with mixed log types like mangrove)
            if (!state.isIn(BlockTags.LOGS)) continue;

            result.add(current);

            for (BlockPos neighbor : getTreeNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }
        return result;
    }

    /**
     * Gets neighbors biased upward for tree detection.
     * Checks all 26 neighbors but prioritizes upward blocks.
     */
    private static List<BlockPos> getTreeNeighbors(BlockPos pos) {
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
                return player.isSneaking();
            default:
                return false;
        }
    }
}
