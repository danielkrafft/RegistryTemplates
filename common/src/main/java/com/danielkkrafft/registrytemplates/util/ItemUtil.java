package com.danielkkrafft.registrytemplates.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ItemUtil {
    public static Identifier getTexturePath(Item item) {
        Identifier itemKey = BuiltInRegistries.ITEM.getKey(item);
        return Identifier.fromNamespaceAndPath(itemKey.getNamespace(), "item/" + itemKey.getPath());
    }

    public static String id(Item item) { return BuiltInRegistries.ITEM.getKey(item).getPath(); }
    public static Identifier getKey(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }
}
