package top.diaoyugan.veinmine.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightLogic;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.client.hotkey.HotKeyState;
import top.diaoyugan.veinmine.networking.highlightingpacket.BlockHighlightRequest;
import top.diaoyugan.veinmine.networking.keypacket.KeyPressPacket;
import top.diaoyugan.veinmine.utils.Utils;

public final class HotKeys {

    static void receiveKeybindingResponse(boolean state) {
        HotKeyState.updateFromServer(state);
    }

    public static void tickEvent(Minecraft client) {
        if (client.player == null || client.getConnection() == null) return;

        boolean isPressed = KeyBinding.BINDING.isDown();
        boolean click = KeyBinding.BINDING.consumeClick();
        boolean useHold = Utils.getConfig().useHoldInsteadOfToggle;

        if (useHold) {
            if (HotKeyState.consumeLastPressedChange(isPressed)) {
                ClientPlayNetworking.send(KeyPressPacket.ID, PacketByteBufs.empty());
            }
        } else if (click) {
            ClientPlayNetworking.send(KeyPressPacket.ID, PacketByteBufs.empty());
        }

        if (HotKeyState.isVeinMineEnabled()) {
            BlockPos pos = ClientHighlightLogic.getLookedBlock(client.player);
            if (pos != null) {
                FriendlyByteBuf buf = PacketByteBufs.create();
                buf.writeLong(pos.asLong());
                ClientPlayNetworking.send(BlockHighlightRequest.ID, buf);
                ClientHighlightState.SHOW_HIGHLIGHT = true;
            } else {
                ClientHighlightState.SHOW_HIGHLIGHT = false;
            }
        } else {
            ClientHighlightState.HIGHLIGHTED_BLOCKS.clear();
        }
    }
}
