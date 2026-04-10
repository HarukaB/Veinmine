package top.diaoyugan.veinmine.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import top.diaoyugan.veinmine.client.highlight.ClientHighlightState;
import top.diaoyugan.veinmine.config.IntrusiveConfig;
import top.diaoyugan.veinmine.utils.Utils;

public final class FabricOutlineRenderHook {
    public static void init() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(ctx -> {
            if (!Utils.getConfig().enableHighlights) return;
            if (!ClientHighlightState.SHOW_HIGHLIGHT) return;

            Minecraft mc = Minecraft.getInstance();

            OutlineRenderer.render(
                    ctx.matrixStack(),
                    mc.renderBuffers().bufferSource(),
                    mc.gameRenderer.getMainCamera(),
                    ClientHighlightState.HIGHLIGHTED_BLOCKS,
                    IntrusiveConfig.isEnabled()
                            ? CustomRenderTypes.getLinesNoDepth()
                            : RenderType.lines(),
                    OutlineRenderer.LineStyle.RIBBON_THICK_LINES,
                    UtilsColorHelper.fromConfig()
            );
        });
    }
}
