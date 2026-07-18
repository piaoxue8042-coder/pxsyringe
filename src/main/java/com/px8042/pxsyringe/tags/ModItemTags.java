package com.px8042.pxsyringe.tags;

import com.px8042.pxsyringe.PxSyringe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModItemTags {
    public static final TagKey<Item> POTION_SOURCES = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(PxSyringe.MOD_ID, "potion_sources")
    );

    private ModItemTags() {
    }
}
