package top.diaoyugan.veinmine.client.configScreen.pages;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.client.configScreen.layout.VerticalLayout;
import top.diaoyugan.veinmine.client.configScreen.widget.*;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class ConfigHighlightsPage {
    private final ConfigItems items;

    public ConfigHighlightsPage(ConfigItems items) { this.items = items; }

    public List<AbstractWidget> build(int centerX) {
        List<AbstractWidget> widgets = new ArrayList<>();
        final int contentWidth = 200;
        final int sliderWidth = 90;
        final int sliderHeight = 20;
        final int numSliders = 4;
        final int previewHeight = (sliderHeight * numSliders) + 24;
        final int leftX = centerX - contentWidth / 2;
        VerticalLayout layout = new VerticalLayout(leftX, 10, 4);

        widgets.add(new TitleWidget(layout.x(), layout.y(), Component.translatable("vm.config.screen.highlights")));
        layout.next(10);

        widgets.add(bool(layout, contentWidth, "vm.config.enableHighlights", () -> items.enableHighlights, v -> items.enableHighlights = v));
        layout.next(24);

        widgets.add(createColorSlider(layout, sliderWidth, "vm.config.renderRed", 0, 255, items.red, v -> items.red = v, false));

        int previewX = layout.x() + sliderWidth;
        widgets.add(new ColorPreviewWidget(previewX + 20, layout.y(), sliderWidth, previewHeight,
                () -> items.red, () -> items.green, () -> items.blue, () -> items.alpha));

        layout.next(24);
        widgets.add(createColorSlider(layout, sliderWidth, "vm.config.renderGreen", 0, 255, items.green, v -> items.green = v, false));
        layout.next(24);
        widgets.add(createColorSlider(layout, sliderWidth, "vm.config.renderBlue", 0, 255, items.blue, v -> items.blue = v, false));
        layout.next(24);
        widgets.add(createColorSlider(layout, sliderWidth, "vm.config.renderAlpha", 0, 255, items.alpha, v -> items.alpha = v, true));

        return widgets;
    }

    private IntSliderOptionWidget createColorSlider(VerticalLayout layout, int width, String key, int min, int max, int value, IntConsumer setter, Boolean isPercentage) {
        return new IntSliderOptionWidget(layout.x(), layout.y(), width, 20, Component.translatable(key), min, max, () -> value, setter, isPercentage);
    }

    private BooleanOptionWidget bool(VerticalLayout layout, int width, String key, BooleanSupplier getter, Consumer<Boolean> setter) {
        BooleanOptionWidget widget = new BooleanOptionWidget(layout.x(), layout.y(), width, 20, Component.translatable(key), getter, setter);
        widget.tooltip(Component.translatable(key + ".tooltip"));
        return widget;
    }
}
