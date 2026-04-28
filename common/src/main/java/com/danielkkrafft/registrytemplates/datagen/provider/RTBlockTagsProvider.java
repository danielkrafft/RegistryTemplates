package com.danielkkrafft.registrytemplates.datagen.provider;

import com.danielkkrafft.registrytemplates.AbstractRegistryTemplates;
import com.danielkkrafft.registrytemplates.template.RTAbstractBlock;
import com.danielkkrafft.registrytemplates.template.RTAbstractItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class RTBlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {
    public AbstractRegistryTemplates templates;

    public RTBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, AbstractRegistryTemplates templates) {
        super(output, Registries.BLOCK, lookupProvider, (e) -> e.builtInRegistryHolder().key());
        this.templates = templates;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        templates.getAll(RTAbstractBlock.class).forEach(t -> t.addBlockTags(this));
    }

    @Override
    public TagAppender<Block, Block> tag(TagKey<Block> tag) {
        return super.tag(tag);
    }
}
