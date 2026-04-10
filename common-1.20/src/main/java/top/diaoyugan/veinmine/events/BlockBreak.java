package top.diaoyugan.veinmine.events;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import top.diaoyugan.veinmine.config.ConfigItems;
import top.diaoyugan.veinmine.utils.Messages;
import top.diaoyugan.veinmine.utils.SmartVein;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.List;

import static top.diaoyugan.veinmine.utils.Utils.*;

public class BlockBreak {

    public static void onBlockBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity entity) {
        if (!getVeinMineSwitchState(player)) return;

        List<BlockPos> blocks = SmartVein.findBlocks(world, pos, BuiltInRegistries.BLOCK.getKey(state.getBlock()));
        if (blocks == null || blocks.isEmpty()) return;

        if (!checkDurabilityAndWarn(player, state, blocks)) return;

        int destroyed = breakBlocks(world, player, pos, state, blocks);

        if (world instanceof ServerLevel serverWorld) {
            AABB area = computeAABB(blocks);
            moveDropsToCenter(serverWorld, pos, area);
        }

        Utils.applyToolDurabilityDamage(player, destroyed);
    }

    private static boolean checkDurabilityAndWarn(Player player, BlockState state, List<BlockPos> blocks) {
        ConfigItems config = Utils.getConfig();
        if (!config.protectTools || player.isCreative()) return true;

        int totalCost = Utils.calculateTotalDurabilityCost(blocks, player, state);
        if (Utils.hasEnoughDurability(player, totalCost)) return true;

        if (player instanceof ServerPlayer serverPlayer) {
            Component msg = Component.translatable("vm.warn.breakthroughs").withStyle(s -> s.applyFormat(ChatFormatting.RED));
            Messages.sendMessage(serverPlayer, msg, true);
        }
        return false;
    }

    private static int breakBlocks(Level world, Player player, BlockPos centerPos, BlockState originalState, List<BlockPos> blocks) {
        int count = 0;
        for (BlockPos pos : blocks) {
            if (!pos.equals(centerPos)) {
                count += breakSingleBlock(world, player, centerPos, pos, originalState);
            }
        }
        return count;
    }

    private static int breakSingleBlock(Level world, Player player, BlockPos centerPos, BlockPos targetPos, BlockState originalState) {
        BlockState targetState = world.getBlockState(targetPos);
        if (targetState.getBlock() != originalState.getBlock()) return 0;
        if (shouldDropItems(player, targetState, world, targetPos)) {
            Block.dropResources(targetState, world, targetPos, world.getBlockEntity(targetPos), player, player.getMainHandItem());
        }
        world.destroyBlock(targetPos, false);
        return 1;
    }

    private static boolean shouldDropItems(Player player, BlockState state, Level world, BlockPos pos) {
        if (player.isCreative()) return false;
        if (!Utils.isToolSuitable(state, player)) return false;
        return !Utils.shouldNotDropItem(state, world, pos, player);
    }

    private static void moveDropsToCenter(ServerLevel world, BlockPos centerPos, AABB area) {
        Vec3 center = Vec3.atCenterOf(centerPos);
        List<Entity> drops = world.getEntitiesOfClass(
                Entity.class,
                area.inflate(1),
                e -> e instanceof ItemEntity || e instanceof ExperienceOrb
        );
        for (Entity drop : drops) {
            drop.setPos(center);
        }
    }

    public static AABB computeAABB(List<BlockPos> blocks) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (BlockPos pos : blocks) {
            int x = pos.getX(), y = pos.getY(), z = pos.getZ();
            if (x < minX) minX = x;
            if (y < minY) minY = y;
            if (z < minZ) minZ = z;
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
            if (z > maxZ) maxZ = z;
        }
        return new AABB(minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1);
    }
}
