package com.px8042.pxsyringe.registry;

import com.px8042.pxsyringe.PxSyringe;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PxSyringe.MOD_ID);

    public static final Supplier<CreativeModeTab> MAIN = TABS.register(
            "main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.pxsyringe.main"))
                    .icon(() -> new ItemStack(ModItems.SYRINGE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SYRINGE.get());
                        output.accept(ModItems.NETHERITE_SYRINGE.get());
                    })
                    .build()
    );

    private ModCreativeModeTabs() {
    }

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
