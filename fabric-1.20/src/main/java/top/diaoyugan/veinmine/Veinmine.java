package top.diaoyugan.veinmine;

import net.fabricmc.api.ModInitializer;
import top.diaoyugan.veinmine.events.PlayerBreakBlock;
import top.diaoyugan.veinmine.events.PlayerDisconnect;
import top.diaoyugan.veinmine.networking.NetPacketsRegistrar;
import top.diaoyugan.veinmine.networking.keypacket.KeyPacketImplements;

public class Veinmine implements ModInitializer {
    @Override
    public void onInitialize() {
        PlayerBreakBlock.register();
        KeyPacketImplements.init();
        NetPacketsRegistrar.init();
        PlayerDisconnect.register();
        FabricCommandRegister.register();
    }
}
