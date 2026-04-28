package com.danielkkrafft.registrytemplates.template;

import com.danielkkrafft.registrytemplates.AbstractRegistryTemplates;
import com.danielkkrafft.registrytemplates.client.model.RTModel;
import com.danielkkrafft.registrytemplates.datagen.provider.RTBlockLootSubProvider;
import com.danielkkrafft.registrytemplates.datagen.provider.RTBlockTagsProvider;
import com.danielkkrafft.registrytemplates.datagen.provider.RTLanguageProvider;
import com.danielkkrafft.registrytemplates.util.LootTableUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Registers a new block
 * <p>
 * Usage:
 * <p>
 * {@code public static RTAbstractBlock<Block, ?> EXAMPLE = new RTAbstractBlock<>("example_block", Block::new).basicModel().dropSelf().setLocalEN(() -> "Example Block"); }
 *
 * @param <T> The Block subclass for this block's behavior (this allows methods like get() to return the correct Block subclass without a cast)
 * @param <R> The RegistryTemplate subclass (this allows methods like setModel to be chained, even in subclasses, without a cast)
 */
public class RTAbstractBlock<T extends Block, R extends RTAbstractBlock<T, R>> extends RegistryTemplate<R> {

    protected Function<BlockBehaviour.Properties, T> blockFactory;
    protected Function<T, RTModel> modelFactory;
    protected BiFunction<RTBlockLootSubProvider, Block, LootTable.Builder> lootFactory;

    protected TagKey<Block>[] tags;

    protected LazyReference<Block, T> BLOCK;
    protected RTAbstractItem<BlockItem, ?> ITEM;

    public RTAbstractBlock(String id, Function<BlockBehaviour.Properties, T> blockFactory) {
        super(id);
        this.blockFactory = blockFactory;

        BLOCK = new LazyReference<>(Registries.BLOCK, id);
        ITEM = new RTAbstractItem<>(id, p -> new BlockItem(BLOCK.get(), p));
    }

    // Accessors

    public T get() { return BLOCK.get(); }
    public ResourceKey<Block> key() { return BLOCK.key; }
    public Identifier identifier() { return BLOCK.key.identifier(); }

    // Configs

    @Override
    public R setLocalEN(Supplier<String> local) {
        super.setLocalEN(local);
        ITEM.setLocalEN(local);
        return (R) this;
    }

    public R setLoot(Function<RTBlockLootSubProvider, LootTable.Builder> lootFactory) {
        this.lootFactory = (p, b) -> lootFactory.apply(p);
        return (R) this;
    }

    public R setLoot(BiFunction<RTBlockLootSubProvider, Block, LootTable.Builder> lootFactory) {
        this.lootFactory = lootFactory;
        return (R) this;
    }

    public R dropSelf() {
        this.lootFactory = (p, b) -> LootTableUtil.of(ITEM.get());
        return (R) this;
    }

    @SafeVarargs
    public final R tag(TagKey<Block>... tags) {
        this.tags = tags;
        return (R) this;
    }

    @SafeVarargs
    public final R itemTag(TagKey<Item>... tags) {
        ITEM.tag(tags);
        return (R) this;
    }

    public R setModel(Function<T, RTModel> model) {
        this.modelFactory = model;
        return (R) this;
    }

    public R basicModel() {
        this.modelFactory = b -> new RTModel.Cube(b);
        return (R) this;
    }

    // Registry

    @Override
    public void register() {
        AbstractRegistryTemplates.register(Registries.BLOCK, key(), () -> blockFactory.apply(BlockBehaviour.Properties.of().setId(key())));
    }

    @Override
    public void addTranslations(RTLanguageProvider provider) {
        provider.add("en_us", get().getDescriptionId(), Component.literal(getLocalEN()));
    }

    public void generateBlockLoot(RTBlockLootSubProvider provider) {
        if (lootFactory != null) provider.add(get(), lootFactory.apply(provider, get()));
    }

    @Override
    public void registerModels() {
        if (modelFactory != null) modelFactory.apply(get());
    }

    public void addBlockTags(RTBlockTagsProvider provider) {
        if (tags == null) return;
        for (TagKey<Block> tag : tags) {
            provider.tag(tag).add(get());
        }
    }
}
