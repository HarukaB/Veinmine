package top.diaoyugan.veinmine.client.configScreen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import top.diaoyugan.veinmine.client.configScreen.widgetSprites.EnhancedWidgetSprites;
import top.diaoyugan.veinmine.client.configScreen.widgetSprites.SpriteData;

public class IconButton extends Button {
    private final EnhancedWidgetSprites iconSprites;
    private final int iconSize;

    public IconButton(int x, int y, int width, int height,
                      EnhancedWidgetSprites iconSprites, int iconSize, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.iconSprites = iconSprites;
        this.iconSize = iconSize;
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float delta) {
        super.renderWidget(g, mouseX, mouseY, delta);
        SpriteData sprite = iconSprites.get(this.active, this.isHoveredOrFocused());
        int iconX = this.getX() + (this.width - iconSize) / 2;
        int iconY = this.getY() + (this.height - iconSize) / 2;
        g.blit(sprite.texture(), iconX, iconY, sprite.u(), sprite.v(),
               iconSize, iconSize, sprite.texW(), sprite.texH());
    }

    public static class Builder {
        private final int x, y, width, height;
        private ResourceLocation texture;
        private int texW, texH;
        private ResourceLocation hoverTexture;
        private int hoverTexW, hoverTexH;
        private float u, v;
        private float hoverVShift = 0;
        private int iconSize = 16;
        private OnPress onPress = b -> {};

        public Builder(int x, int y, int width, int height) {
            this.x = x; this.y = y; this.width = width; this.height = height;
        }

        public Builder icon(ResourceLocation texture, int texW, int texH) {
            this.texture = texture; this.texW = texW; this.texH = texH; return this;
        }

        public Builder hoverIcon(ResourceLocation texture, int texW, int texH) {
            this.hoverTexture = texture; this.hoverTexW = texW; this.hoverTexH = texH; return this;
        }

        public Builder uv(float u, float v) { this.u = u; this.v = v; return this; }
        public Builder hoverShift(float shift) { this.hoverVShift = shift; return this; }
        public Builder iconSize(int size) { this.iconSize = size; return this; }
        public Builder onPress(OnPress onPress) { this.onPress = onPress; return this; }

        public IconButton build() {
            if (texture == null) throw new IllegalStateException("Icon texture not set");
            SpriteData normal = new SpriteData(texture, u, v, texW, texH);
            SpriteData hovered = hoverTexture != null
                    ? new SpriteData(hoverTexture, u, v, hoverTexW, hoverTexH)
                    : new SpriteData(texture, u, v + hoverVShift, texW, texH);
            EnhancedWidgetSprites sprites = new EnhancedWidgetSprites(normal).with(WidgetState.HOVERED, hovered);
            return new IconButton(x, y, width, height, sprites, iconSize, onPress);
        }
    }
}
