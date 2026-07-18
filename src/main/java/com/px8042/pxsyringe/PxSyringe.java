package com.px8042.pxsyringe;

import com.px8042.pxsyringe.registry.ModCreativeModeTabs;
import com.px8042.pxsyringe.registry.ModItems;
import com.px8042.pxsyringe.registry.ModRecipeSerializers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(PxSyringe.MOD_ID)
public final class PxSyringe {
    public static final String MOD_ID = "pxsyringe";

    public PxSyringe(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
    }
}
