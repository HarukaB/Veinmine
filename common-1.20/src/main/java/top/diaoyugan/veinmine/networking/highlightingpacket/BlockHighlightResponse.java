package top.diaoyugan.veinmine.networking.highlightingpacket;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import top.diaoyugan.veinmine.networking.Networking;

import java.util.ArrayList;

public record BlockHighlightResponse(ArrayList<BlockPos> positions) {
    public static final ResourceLocation ID = Networking.id("block_highlight_response");
}
