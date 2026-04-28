package com.danielkkrafft.registrytemplates.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class LootTableUtil {
    public static LootTable.Builder of(Item item, int minQuantity, int maxQuantity) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(minQuantity, maxQuantity)))));
    }

    public static LootTable.Builder of(Item item, int quantity) {
        return LootTableUtil.of(item, quantity, quantity);
    }

    public static LootTable.Builder of(Item item) {
        return LootTableUtil.of(item, 1);
    }
}