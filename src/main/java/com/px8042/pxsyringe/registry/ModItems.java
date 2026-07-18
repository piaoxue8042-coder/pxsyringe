package com.px8042.pxsyringe.registry;

import com.px8042.pxsyringe.PxSyringe;
import com.px8042.pxsyringe.item.PotionSyringeItem;
import java.util.Arrays;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemLore;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PxSyringe.MOD_ID);

    public static final DeferredItem<Item> SYRINGE = ITEMS.register(
            "syringe",
            () -> new Item(
                    properties("syringe")
                            .stacksTo(16)
                            .component(
                                    DataComponents.LORE,
                                    lore("tooltip.pxsyringe.syringe.recipe")
                            )
            )
    );

    public static final DeferredItem<PotionSyringeItem> POTION_SYRINGE = ITEMS.register(
            "potion_syringe",
            () -> new PotionSyringeItem(
                    properties("potion_syringe")
                            .stacksTo(16)
                            .component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                            .component(DataComponents.POTION_DURATION_SCALE, 1.0F)
                            .component(
                                    DataComponents.LORE,
                                    lore(
                                            "tooltip.pxsyringe.potion_syringe.self",
                                            "tooltip.pxsyringe.potion_syringe.target"
                                    )
                            )
            )
    );

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static Item.Properties properties(String name) {
        ResourceKey<Item> key = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(PxSyringe.MOD_ID, name)
        );
        return new Item.Properties().setId(key);
    }

    private static ItemLore lore(String... translationKeys) {
        List<Component> lines = Arrays.stream(translationKeys)
                .<Component>map(Component::translatable)
                .toList();
        List<Component> styledLines = lines.stream()
                .map(line -> line.copy().withStyle(ChatFormatting.GRAY))
                .map(Component.class::cast)
                .toList();
        return new ItemLore(lines, styledLines);
    }
}
