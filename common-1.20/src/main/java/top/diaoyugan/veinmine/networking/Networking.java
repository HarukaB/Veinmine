package top.diaoyugan.veinmine.networking;

import net.minecraft.resources.ResourceLocation;

import static top.diaoyugan.veinmine.Constants.ID;

public final class Networking {
    public static ResourceLocation id(String name) {
        return new ResourceLocation(ID, name);
    }

    private Networking() {}
}
