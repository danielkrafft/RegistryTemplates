package com.danielkkrafft.registrytemplates.datagen.provider;

import com.danielkkrafft.registrytemplates.RegistryTemplates;
import com.danielkkrafft.registrytemplates.template.RTAbstractItem;
import com.danielkkrafft.registrytemplates.template.RTDataProvider;
import com.danielkkrafft.registrytemplates.util.ModContent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

@ModContent
public class RTItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {
    public static final RTDataProvider<RTItemTagsProvider> PROVIDER = new RTDataProvider<RTItemTagsProvider>(RTItemTagsProvider::new);

    public RTItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.ITEM, lookupProvider, (e) -> e.builtInRegistryHolder().key());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        RegistryTemplates.INSTANCE.getAll(RTAbstractItem.class).forEach(t -> t.addItemTags(this));
    }

    @Override
    public TagAppender<Item, Item> tag(TagKey<Item> tag) {
        return super.tag(tag);
    }
}
