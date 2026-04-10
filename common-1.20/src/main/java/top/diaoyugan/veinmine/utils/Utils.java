package top.diaoyugan.veinmine.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import top.diaoyugan.veinmine.config.Config;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Utils {
    private static final Map<UUID, Boolean> playerVeinMineSwitchState = new HashMap<>();

    public static ConfigItems getConfig() {
        return Config.getInstance().getConfigItems();
    }

    public static boolean toggleVeinMineSwitchState(Player player) {
        UUID playerId = player.getUUID();
        boolean newState = !playerVeinMineSwitchState.getOrDefault(playerId, false);
        playerVeinMineSwitchState.put(playerId, newState);
        return newState;
    }

    public static boolean getVeinMineSwitchState(Player player) {
        return playerVeinMineSwitchState.getOrDefault(player.getUUID(), false);
    }

    public static void clearVeinMineState(UUID playerId) {
        playerVeinMineSwitchState.remove(playerId);
    }

    public static boolean isToolSuitable(BlockState blockState, Player player) {
        ItemStack tool = player.getMainHandItem();
        if (blockState.requiresCorrectToolForDrops()) {
            return tool.isCorrectToolForDrops(blockState);
        }
        return true;
    }

    public static void applyToolDurabilityDamage(Player player, int blockCount) {
        ItemStack tool = player.getMainHandItem();
        if (tool == null) return;
        if (tool.isDamageableItem()) {
            InteractionHand usedHand = InteractionHand.MAIN_HAND;
            if (player.getItemInHand(InteractionHand.OFF_HAND) == tool) {
                usedHand = InteractionHand.OFF_HAND;
            }
            EquipmentSlot slot = (usedHand == InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            tool.hurtAndBreak(blockCount, player, p -> p.broadcastBreakEvent(slot));
            if (tool.isEmpty()) {
                player.setItemInHand(usedHand, ItemStack.EMPTY);
            }
        }
    }

    public static boolean shouldNotDropItem(BlockState state, Level world, BlockPos pos, Player player) {
        if (!(world instanceof ServerLevel serverWorld)) return true;
        BlockEntity be = serverWorld.getBlockEntity(pos);
        ItemStack tool = player == null ? ItemStack.EMPTY : player.getMainHandItem();
        return Block.getDrops(state, serverWorld, pos, be, player, tool).isEmpty();
    }

    public static int calculateTotalDurabilityCost(List<BlockPos> blocksToBreak, Player player, BlockState state) {
        int cost = 0;
        for (BlockPos targetPos : blocksToBreak) {
            BlockState targetState = player.level().getBlockState(targetPos);
            if (Utils.isToolSuitable(targetState, player)) {
                cost++;
            }
        }
        return cost;
    }

    public static boolean hasEnoughDurability(Player player, int cost) {
        ItemStack tool = player.getMainHandItem();
        if (isToolProtected(tool)) {
            if (tool.getItem() != null) {
                return tool.getMaxDamage() - tool.getDamageValue() - getConfig().durabilityThreshold >= cost;
            }
        }
        return true;
    }

    public static boolean isToolProtected(ItemStack tool) {
        String toolName = tool.getItem().toString();
        if (getConfig().protectAllDefaultValuableTools) {
            return getConfig().defaultProtectedTools.contains(toolName) || getConfig().protectedTools.contains(toolName);
        }
        return getConfig().protectedTools.contains(toolName);
    }
}
