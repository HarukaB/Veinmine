package top.diaoyugan.veinmine.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import top.diaoyugan.veinmine.utils.logging.Logger;
import top.diaoyugan.veinmine.utils.logging.LoggerLevels;

public class Messages {
    public static void clientMessage(Component message, Boolean isOnActionbar) {
        Minecraft client = Minecraft.getInstance();
        if (isOnActionbar)
            client.gui.setOverlayMessage(message, false);
        else
            client.gui.getChat().addMessage(message);
    }

    public static void sendMessage(ServerPlayer player, Component message, Boolean isOnActionbar) {
        try {
            if (player == null) throw new NullPointerException("player is null");
            if (message == null) throw new NullPointerException("message is null");
            if (isOnActionbar == null) throw new NullPointerException("isOnActionbar is null");
            var server = player.level().getServer();
            server.execute(() -> player.sendSystemMessage(message, isOnActionbar));
        } catch (NullPointerException e) {
            Logger.throwLog(LoggerLevels.ERROR, "Failed to send message due to null parameter!", e);
        }
    }

    public static void sendTitleMessage(ServerPlayer player, Component title) {
        player.connection.send(new ClientboundSetTitleTextPacket(title));
    }

    public static void sendTitleMessage(ServerPlayer player, Component title, Component subtitle) {
        player.connection.send(new ClientboundSetSubtitleTextPacket(subtitle));
        player.connection.send(new ClientboundSetTitleTextPacket(title));
    }
}
