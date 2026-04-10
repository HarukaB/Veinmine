package top.diaoyugan.veinmine.networking;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightResponse;
import top.diaoyugan.veinmine.utils.SmartVein;
import top.diaoyugan.veinmine.utils.Utils;

import java.util.*;

public final class NetPacketsRegistrar {
    private NetPacketsRegistrar() {}

    private static final Map<UUID, BlockPos> LAST_POS = new HashMap<>();
    private static final Map<UUID, Boolean> LAST_PROCESSED_STATE = new HashMap<>();

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(BlockHighlightRequest.ID,
                (server, player, handler, buf, responseSender) -> {
                    BlockPos pos = BlockPos.of(buf.readLong());
                    server.execute(() -> handleBlockHighlightRequest(player, pos));
                });
    }

    private static void handleBlockHighlightRequest(ServerPlayer player, BlockPos pos) {
        ServerLevel world = (ServerLevel) player.level();
        UUID playerId = player.getUUID();

        BlockPos lastPos = LAST_POS.get(playerId);
        Boolean lastState = LAST_PROCESSED_STATE.get(playerId);
        boolean currentState = Utils.getVeinMineSwitchState(player);

        if (lastState != null && pos.equals(lastPos) && currentState == lastState) {
            return;
        }

        LAST_POS.put(playerId, pos);
        LAST_PROCESSED_STATE.put(playerId, currentState);

        if (Utils.getVeinMineSwitchState(player)) {
            List<BlockPos> blocksToBreak = SmartVein.findBlocks(world, pos);
            if (blocksToBreak != null) {
                Set<BlockPos> newGlowingBlocks = new HashSet<>(blocksToBreak);
                ArrayList<BlockPos> positions = new ArrayList<>(newGlowingBlocks);

                FriendlyByteBuf responseBuf = PacketByteBufs.create();
                responseBuf.writeInt(positions.size());
                for (BlockPos p : positions) {
                    responseBuf.writeLong(p.asLong());
                }
                ServerPlayNetworking.send(player, BlockHighlightResponse.ID, responseBuf);
            }
        }
    }

    public static void clearLastStates(UUID playerId) {
        LAST_PROCESSED_STATE.remove(playerId);
        LAST_POS.remove(playerId);
    }
}
