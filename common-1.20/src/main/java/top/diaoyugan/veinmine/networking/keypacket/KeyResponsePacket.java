package top.diaoyugan.veinmine.networking.keypacket;

import net.minecraft.resources.ResourceLocation;
import top.diaoyugan.veinmine.networking.Networking;

public record KeyResponsePacket(boolean state) {
    public static final ResourceLocation ID = Networking.id("keybinding_response");
}
