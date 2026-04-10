package top.diaoyugan.veinmine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightResponse;

import java.util.ArrayList;

public final class FabricHighlightNetworking {
    private FabricHighlightNetworking() {}

    public static void onInitialize() {
        ClientPlayNetworking.registerGlobalReceiver(BlockHighlightResponse.ID,
                (client, handler, buf, responseSender) -> {
                    int size = buf.readInt();
                    ArrayList<BlockPos> positions = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        positions.add(BlockPos.of(buf.readLong()));
                    }
                    client.execute(() -> ClientHighlightLogic.onHighlightResponse(positions));
                });
    }
}
