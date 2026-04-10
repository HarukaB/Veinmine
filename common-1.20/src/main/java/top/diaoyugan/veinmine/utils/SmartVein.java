package top.diaoyugan.veinmine.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import top.diaoyugan.veinmine.utils.logging.Logger;
import top.diaoyugan.veinmine.utils.logging.LoggerLevels;

import java.util.*;

public class SmartVein {
    private static final BlockPos[] OFFSETS = createOffsets();

    static {
        try {
            Utils.getConfig().ignoredBlocks.add("minecraft:air");
        } catch (Exception e) {
            if (!(e instanceof UnsupportedOperationException)) {
                Logger.throwLog(LoggerLevels.ERROR, String.valueOf(e), e.fillInStackTrace());
            }
        }
    }

    public static List<BlockPos> findBlocks(Level world, BlockPos startPos) {
        BlockState startState = world.getBlockState(startPos);
        ResourceLocation blockID = BuiltInRegistries.BLOCK.getKey(startState.getBlock());
        String startBlockID = blockID.toString();

        if (Utils.getConfig().ignoredBlocks.contains(startBlockID) || !Utils.getConfig().useBFS) {
            return findBlocksInCube(world, startPos, startState);
        } else {
            return findConnectedBlocks(world, startPos, startState);
        }
    }

    public static List<BlockPos> findBlocks(Level world, BlockPos startPos, ResourceLocation startBlockID) {
        if (Utils.getConfig().ignoredBlocks.contains(String.valueOf(startBlockID)) || !Utils.getConfig().useBFS) {
            return findBlocksInCube(world, startPos, startBlockID);
        } else {
            return findConnectedBlocks(world, startPos, startBlockID);
        }
    }

    private static List<BlockPos> findBlocksInCube(Level world, BlockPos pos, BlockState targetState) {
        if (!Utils.getConfig().useRadiusSearch) return null;
        List<BlockPos> foundBlocks = new ArrayList<>();
        int radius = Utils.getConfig().searchRadius;
        for (int x = -radius; x <= radius; x++)
            for (int y = -radius; y <= radius; y++)
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = pos.offset(x, y, z);
                    if (world.getBlockState(targetPos).getBlock() == targetState.getBlock())
                        foundBlocks.add(targetPos);
                }
        return foundBlocks;
    }

    private static List<BlockPos> findBlocksInCube(Level world, BlockPos pos, ResourceLocation startBlockID) {
        if (!Utils.getConfig().useRadiusSearch) return null;
        List<BlockPos> foundBlocks = new ArrayList<>();
        Block block = BuiltInRegistries.BLOCK.get(startBlockID);
        int radius = Utils.getConfig().searchRadius;
        for (int x = -radius; x <= radius; x++)
            for (int y = -radius; y <= radius; y++)
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = pos.offset(x, y, z);
                    if (world.getBlockState(targetPos).getBlock() == block)
                        foundBlocks.add(targetPos);
                }
        return foundBlocks;
    }

    private static List<BlockPos> findConnectedBlocks(Level world, BlockPos startPos, BlockState targetState) {
        List<BlockPos> foundBlocks = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(startPos);
        visited.add(startPos);
        int connectedCount = 0;
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            foundBlocks.add(current);
            connectedCount++;
            if (connectedCount > Utils.getConfig().BFSLimit) {
                if (Utils.getConfig().useRadiusSearchWhenReachBFSLimit)
                    return findBlocksInCube(world, startPos, targetState);
                else return null;
            }
            for (BlockPos offset : OFFSETS) {
                BlockPos neighborPos = current.offset(offset);
                if (!visited.contains(neighborPos) && isSameBlock(world, targetState, neighborPos)) {
                    queue.add(neighborPos);
                    visited.add(neighborPos);
                }
            }
        }
        return foundBlocks;
    }

    private static List<BlockPos> findConnectedBlocks(Level world, BlockPos startPos, ResourceLocation startBlockID) {
        List<BlockPos> foundBlocks = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        Block block = BuiltInRegistries.BLOCK.get(startBlockID);
        BlockState startState = block.defaultBlockState();
        queue.add(startPos);
        visited.add(startPos);
        int connectedCount = 0;
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            foundBlocks.add(current);
            connectedCount++;
            if (connectedCount > Utils.getConfig().BFSLimit) {
                if (Utils.getConfig().useRadiusSearchWhenReachBFSLimit)
                    return findBlocksInCube(world, startPos, startBlockID);
                else return null;
            }
            for (BlockPos offset : OFFSETS) {
                BlockPos neighborPos = current.offset(offset);
                if (!visited.contains(neighborPos) && isSameBlock(world, startState, neighborPos)) {
                    queue.add(neighborPos);
                    visited.add(neighborPos);
                }
            }
        }
        return foundBlocks;
    }

    private static boolean isSameBlock(Level world, BlockState targetState, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == targetState.getBlock();
    }

    private static BlockPos[] createOffsets() {
        List<BlockPos> list = new ArrayList<>();
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++)
                for (int z = -1; z <= 1; z++)
                    if (x != 0 || y != 0 || z != 0)
                        list.add(new BlockPos(x, y, z));
        return list.toArray(new BlockPos[0]);
    }
}
