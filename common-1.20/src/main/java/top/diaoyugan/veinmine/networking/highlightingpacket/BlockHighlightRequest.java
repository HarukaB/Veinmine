package top.diaoyugan.veinmine.networking.highlightingpacket;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import top.diaoyugan.veinmine.networking.Networking;

public record BlockHighlightRequest(BlockPos blockPos) {
    public static final ResourceLocation ID = Networking.id("block_highlight");
}
