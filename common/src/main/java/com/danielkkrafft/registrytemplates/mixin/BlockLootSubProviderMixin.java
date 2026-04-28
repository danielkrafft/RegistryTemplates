package com.danielkkrafft.registrytemplates.mixin;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(BlockLootSubProvider.class)
public interface BlockLootSubProviderMixin {
    @Accessor("map")
    Map<ResourceKey<LootTable>, LootTable.Builder> getMap();
}
