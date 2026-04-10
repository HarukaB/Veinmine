package top.diaoyugan.veinmine.networking.keypacket;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

public class KeyPacketImplements {
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(KeyPressPacket.ID,
                (server, player, handler, buf, responseSender) -> {
                    server.execute(() -> KeyPacketLogic.handleKeyPress(
                            player,
                            state -> {
                                FriendlyByteBuf response = PacketByteBufs.create();
                                response.writeBoolean(state);
                                ServerPlayNetworking.send(player, KeyResponsePacket.ID, response);
                            }
                    ));
                });
    }
}
