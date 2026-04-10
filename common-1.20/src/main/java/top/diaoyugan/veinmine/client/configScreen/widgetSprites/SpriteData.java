package top.diaoyugan.veinmine.client.configScreen.widgetSprites;

import net.minecraft.resources.ResourceLocation;

public record SpriteData(
        ResourceLocation texture,
        float u,
        float v,
        int texW,
        int texH
) {}
