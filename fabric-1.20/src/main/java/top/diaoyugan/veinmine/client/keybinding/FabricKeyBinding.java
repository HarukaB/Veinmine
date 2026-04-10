package top.diaoyugan.veinmine.client.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import top.diaoyugan.veinmine.client.KeyBinding;

public class FabricKeyBinding {
    public static void onInitialize() {
        registerKeyBindingFromConfig();
    }

    public static void registerKeyBindingFromConfig() {
        int keyCode = KeyBinding.defaultKey();
        InputConstants.Key key = InputConstants.Type.KEYSYM.getOrCreate(keyCode);

        KeyBinding.BINDING = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.vm.switch",
                key.getValue(),
                KeyBinding.VM_CATEGORY
        ));
    }
}
