package top.diaoyugan.veinmine.client.configScreen.widget;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class TabButtonWidget extends Button {
    public TabButtonWidget(int x, int y, int width, int height,
                           Component text, OnPress onPress) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION);
    }
}
