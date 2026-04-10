package top.diaoyugan.veinmine.networking.keypacket;

import net.minecraft.resources.ResourceLocation;
import top.diaoyugan.veinmine.networking.Networking;

public final class KeyPressPacket {
    public static final ResourceLocation ID = Networking.id("keybinding_press");
    private KeyPressPacket() {}
}
