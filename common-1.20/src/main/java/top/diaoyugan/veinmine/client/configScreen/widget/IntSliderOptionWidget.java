package top.diaoyugan.veinmine.client.configScreen.widget;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class IntSliderOptionWidget extends AbstractSliderButton {

    private final int min;
    private final int max;
    private final IntSupplier getter;
    private final IntConsumer setter;
    private final Component label;
    private final boolean isPercentage;
    private String customValueKey;

    public IntSliderOptionWidget(int x, int y, int width, int height, Component label,
                                 int min, int max, IntSupplier getter, IntConsumer setter,
                                 boolean isPercentage) {
        super(x, y, width, height, label, (getter.getAsInt() - min) / (double) (max - min));
        this.min = min;
        this.max = max;
        this.getter = getter;
        this.setter = setter;
        this.label = label;
        this.isPercentage = isPercentage;
        updateMessage();
    }

    public void tooltip(Component tooltip) { setTooltip(Tooltip.create(tooltip)); }

    @Override
    protected void updateMessage() {
        int value = getValue();
        Component message;
        if (customValueKey != null) {
            message = Component.empty().append(label).append(Component.literal(": "))
                    .append(Component.translatable(customValueKey, value));
        } else if (isPercentage) {
            double percent = (value - min) * 100.0 / (max - min);
            message = Component.literal(String.format("%s: %.0f%%", label.getString(), percent));
        } else {
            message = Component.literal(label.getString() + ": " + value);
        }
        setMessage(message);
    }

    @Override
    protected void applyValue() { setter.accept(getValue()); }

    private int getValue() { return min + (int) Math.round(value * (max - min)); }

    public void setCustomValueKey(String key) {
        this.customValueKey = key;
        updateMessage();
    }
}
