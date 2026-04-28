package com.danielkkrafft.registrytemplates.datagen.provider;

import com.danielkkrafft.registrytemplates.AbstractRegistryTemplates;
import com.danielkkrafft.registrytemplates.template.RTAbstractBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RTBlockLootSubProvider extends BlockLootSubProvider {

    public final HolderLookup.Provider lookupProvider;
    public final AbstractRegistryTemplates templates;
    public final List<Block> knownBlocks = new ArrayList<>();

    protected RTBlockLootSubProvider(HolderLookup.Provider lookupProvider, AbstractRegistryTemplates templates) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        this.lookupProvider = lookupProvider;
        this.templates = templates;
    }

    @Override
    public void generate() {
        templates.getAll(RTAbstractBlock.class).forEach(t -> {
            t.generateBlockLoot(this);
        });
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
