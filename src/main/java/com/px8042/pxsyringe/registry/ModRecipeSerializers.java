package com.px8042.pxsyringe.registry;

import com.px8042.pxsyringe.PxSyringe;
import com.px8042.pxsyringe.recipe.PotionSyringeRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, PxSyringe.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PotionSyringeRecipe>> POTION_SYRINGE =
            RECIPE_SERIALIZERS.register(
                    "potion_syringe",
                    () -> new RecipeSerializer<>(PotionSyringeRecipe.MAP_CODEC, PotionSyringeRecipe.STREAM_CODEC)
            );

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
