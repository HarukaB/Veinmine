package top.diaoyugan.veinmine.client;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String VM_CATEGORY = "key.category.vein_mine.switch";
    public static KeyMapping BINDING;

    public static int defaultKey() {
        return GLFW.GLFW_KEY_GRAVE_ACCENT;
    }
}
