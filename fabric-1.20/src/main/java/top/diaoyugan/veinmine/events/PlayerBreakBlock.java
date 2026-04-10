package top.diaoyugan.veinmine.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class PlayerBreakBlock {
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register(BlockBreak::onBlockBreak);
    }
}
