package com.px8042.pxsyringe.recipe;

import com.mojang.serialization.MapCodec;
import com.px8042.pxsyringe.registry.ModItems;
import com.px8042.pxsyringe.registry.ModRecipeSerializers;
import com.px8042.pxsyringe.tags.ModItemTags;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public final class PotionSyringeRecipe extends CustomRecipe {
    public static final PotionSyringeRecipe INSTANCE = new PotionSyringeRecipe();
    public static final MapCodec<PotionSyringeRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, PotionSyringeRecipe> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != 2) {
            return false;
        }

        boolean foundSyringe = false;
        boolean foundPotion = false;

        for (ItemStack stack : input.items()) {
            if (stack.isEmpty()) {
                continue;
            }

            if (isSyringe(stack) && !foundSyringe) {
                foundSyringe = true;
            } else if (isPotionSource(stack) && !foundPotion) {
                foundPotion = true;
            } else {
                return false;
            }
        }

        return foundSyringe && foundPotion;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack source = findPotionSource(input);
        ItemStack syringe = findSyringe(input);
        PotionContents contents = source.get(DataComponents.POTION_CONTENTS);
        if (contents == null || syringe.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = syringe.is(ModItems.NETHERITE_SYRINGE.get())
                ? new ItemStack(ModItems.NETHERITE_POTION_SYRINGE.get())
                : new ItemStack(ModItems.POTION_SYRINGE.get());
        result.set(DataComponents.POTION_CONTENTS, contents);

        Float durationScale = source.get(DataComponents.POTION_DURATION_SCALE);
        if (durationScale != null) {
            result.set(DataComponents.POTION_DURATION_SCALE, durationScale);
        }

        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (isPotionSource(stack)) {
                remainders.set(slot, stack.copyWithCount(1));
                continue;
            }

            ItemStackTemplate craftingRemainder = stack.getCraftingRemainder();
            if (craftingRemainder != null) {
                remainders.set(slot, craftingRemainder.create());
            }
        }

        return remainders;
    }

    @Override
    public RecipeSerializer<PotionSyringeRecipe> getSerializer() {
        return ModRecipeSerializers.POTION_SYRINGE.get();
    }

    private static ItemStack findPotionSource(CraftingInput input) {
        for (ItemStack stack : input.items()) {
            if (isPotionSource(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack findSyringe(CraftingInput input) {
        for (ItemStack stack : input.items()) {
            if (isSyringe(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static boolean isSyringe(ItemStack stack) {
        return stack.is(ModItems.SYRINGE.get()) || stack.is(ModItems.NETHERITE_SYRINGE.get());
    }

    private static boolean isPotionSource(ItemStack stack) {
        return !stack.isEmpty()
                && stack.has(DataComponents.POTION_CONTENTS)
                && (stack.getItem() instanceof PotionItem || stack.is(ModItemTags.POTION_SOURCES));
    }
}
