package com.danielkkrafft.registrytemplates.datagen.provider;

import com.danielkkrafft.registrytemplates.AbstractRegistryTemplates;
import com.danielkkrafft.registrytemplates.template.RTAbstractItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class RTItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {
    public AbstractRegistryTemplates templates;

    public RTItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, AbstractRegistryTemplates templates) {
        super(output, Registries.ITEM, lookupProvider, (e) -> e.builtInRegistryHolder().key());
        this.templates = templates;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        templates.getAll(RTAbstractItem.class).forEach(t -> t.addItemTags(this));
    }

    @Override
    public TagAppender<Item, Item> tag(TagKey<Item> tag) {
        return super.tag(tag);
    }
}
