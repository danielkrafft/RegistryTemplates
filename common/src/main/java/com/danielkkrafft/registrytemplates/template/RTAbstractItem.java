package com.danielkkrafft.registrytemplates.template;

import com.danielkkrafft.registrytemplates.AbstractRegistryTemplates;
import com.danielkkrafft.registrytemplates.client.model.RTModel;
import com.danielkkrafft.registrytemplates.datagen.provider.RTItemTagsProvider;
import com.danielkkrafft.registrytemplates.datagen.provider.RTLanguageProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class RTAbstractItem<T extends Item, R extends RTAbstractItem<T, R>> extends RegistryTemplate<R> {

    protected Function<Item.Properties, T> itemFactory;
    protected Function<T, RTModel> modelFactory;
    protected TagKey<Item>[] tags;

    public LazyReference<Item, T> ITEM;

    public RTAbstractItem(String id, Function<Item.Properties, T> itemFactory) {
        super(id);
        this.itemFactory = itemFactory;
        ITEM = new LazyReference<>(Registries.ITEM, id);
    }

    // Accessors

    public T get() { return ITEM.get(); }
    public ResourceKey<Item> key() { return ITEM.key; }
    public Identifier identifier() { return key().identifier(); }

    // Configs

    @SafeVarargs
    public final R tag(TagKey<Item>... tags) {
        this.tags = tags;
        return (R) this;
    }

    public R setModel(Function<T, RTModel> modelFactory) {
        this.modelFactory = modelFactory;
        return (R) this;
    }

    public R flatModel() {
        this.modelFactory = RTModel.FlatItem::new;
        return (R) this;
    }

    public R handheldModel() {
        this.modelFactory = RTModel.HandheldItem::new;
        return (R) this;
    }

    // Registry

    public void addItemTags(RTItemTagsProvider provider) {
        if (tags == null) return;
        for (TagKey<Item> tag : tags) {
            provider.tag(tag).add(get());
        }
    }

    @Override
    public void registerModels() {
        if (modelFactory != null) modelFactory.apply(get());
    }

    @Override
    public void register() {
        AbstractRegistryTemplates.register(Registries.ITEM, key(), () -> itemFactory.apply(new Item.Properties().setId(key())));
    }

    @Override
    public void addTranslations(RTLanguageProvider provider) {
        provider.add("en_us", get().getDescriptionId(), Component.literal(getLocalEN()));
    }

    // TODO creative tab
}
