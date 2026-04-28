package com.danielkkrafft.registrytemplates.datagen.provider;

import com.danielkkrafft.registrytemplates.AbstractRegistryTemplates;
import com.danielkkrafft.registrytemplates.mixin.BlockLootSubProviderMixin;
import com.danielkkrafft.registrytemplates.template.RTAbstractBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;
import java.util.function.BiConsumer;

public class RTBlockLootSubProvider extends BlockLootSubProvider {

    public final HolderLookup.Provider lookupProvider;
    public final AbstractRegistryTemplates templates;
    public final List<Block> knownBlocks = new ArrayList<>();
    public final FeatureFlagSet enabledFeatures;

    protected RTBlockLootSubProvider(HolderLookup.Provider lookupProvider, AbstractRegistryTemplates templates) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        this.lookupProvider = lookupProvider;
        this.templates = templates;
        this.enabledFeatures = FeatureFlags.REGISTRY.allFlags();
    }

    @Override public void generate() {}

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        templates.getAll(RTAbstractBlock.class).forEach(t -> {
            t.generateBlockLoot(this);
        });

        Set<ResourceKey<LootTable>> seen = new HashSet();

        for(Block block : getKnownBlocks()) {
            if (block.isEnabled(this.enabledFeatures)) {
                block.getLootTable().ifPresent((lootTable) -> {
                    if (seen.add(lootTable)) {
                        LootTable.Builder builder = (LootTable.Builder)((BlockLootSubProviderMixin)this).getMap().remove(lootTable);
                        if (builder == null) {
                            throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", lootTable.identifier(), BuiltInRegistries.BLOCK.getKey(block)));
                        }

                        output.accept(lootTable, builder);
                    }

                });
            }
        }

        if (!((BlockLootSubProviderMixin)this).getMap().isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + String.valueOf(((BlockLootSubProviderMixin)this).getMap().keySet()));
        }
    }

    public void add(Block block, LootTable.Builder builder) {
        super.add(block, builder);
        knownBlocks.add(block);
    }

    // overrides NeoForge validation
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }
}
