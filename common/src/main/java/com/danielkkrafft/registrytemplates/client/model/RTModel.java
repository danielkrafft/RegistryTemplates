package com.danielkkrafft.registrytemplates.client.model;

import com.danielkkrafft.registrytemplates.datagen.provider.RTBlockModelGenerators;
import com.danielkkrafft.registrytemplates.datagen.provider.RTItemModelGenerators;
import com.danielkkrafft.registrytemplates.mixin.ModelTemplateMixin;
import com.danielkkrafft.registrytemplates.util.BlockUtil;
import com.danielkkrafft.registrytemplates.util.ItemUtil;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public abstract class RTModel {
    public static List<RTModel> ALL_MODELS = new ArrayList<>();
    public abstract void registerModels(RTBlockModelGenerators blockModels, RTItemModelGenerators itemModels);
    public RTModel() { ALL_MODELS.add(this); }

    public static Material material(Identifier identifier, String suffix) {
        return new Material(identifier.withSuffix(suffix), false);
    }

    public static abstract class Trivial extends RTModel {
        public Identifier mainTextureLocation;

        public Trivial(Identifier tex) { setTexture(tex); }
        public Trivial setTexture(Identifier identifier) { this.mainTextureLocation = identifier; return this; }
        public Trivial setTexture(Block block) { return this.setTexture(BlockUtil.getTexturePath(block)); }
        public Trivial setTexture(ItemLike itemLike) { return this.setTexture(ItemUtil.getTexturePath(itemLike.asItem())); }
    }

    public static abstract class TrivialItem extends Trivial {
        public final Item item;
        public TrivialItem(Item item) {
            super(ItemUtil.getTexturePath(item));
            this.item = item;
        }
    }

    public static class FlatItem extends TrivialItem {
        public FlatItem(Item item) { super(item); }

        @Override
        public void registerModels(RTBlockModelGenerators blockModels, RTItemModelGenerators itemModels) {
            itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        }
    }

    public static class HandheldItem extends TrivialItem {
        public HandheldItem(Item item) { super(item); }

        @Override
        public void registerModels(RTBlockModelGenerators blockModels, RTItemModelGenerators itemModels) {
            itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
        }
    }

    public static abstract class TrivialBlock extends Trivial {
        public final Block block;
        private final int variants;

        public TrivialBlock(Block block, int variants) {
            super(BlockUtil.getTexturePath(block));
            this.block = block;
            this.variants = variants;
        }
        public TrivialBlock(Block block) { this(block ,1); }

        public abstract void registerModels(RTBlockModelGenerators blockModels, RTItemModelGenerators itemModels, List<String> suffixes);

        @Override
        public void registerModels(RTBlockModelGenerators blockModels, RTItemModelGenerators itemModels) {
            List<String> suffixes = new ArrayList<>();
            for (int i = 0; i < variants; i++) {
                suffixes.add(i == 0 ? "" : "_" + i);
            }
            registerModels(blockModels, itemModels, suffixes);
        }

        public List<Variant> getVariants(ModelTemplate template, List<String> suffixes, RTBlockModelGenerators blockModels) {
            return suffixes.stream().map(suffix ->
                    RTBlockModelGenerators.plainModel(template.createWithSuffix(block, suffix, getTextureMapping(template, suffix), blockModels.modelOutput))
            ).toList();
        }

        public TextureMapping getTextureMapping(ModelTemplate template, String suffix) {
            TextureMapping result = new TextureMapping();
            for (TextureSlot slot : ((ModelTemplateMixin)template).getRequiredSlots()) result.put(slot, material(mainTextureLocation, suffix));
            result.put(TextureSlot.PARTICLE, material(mainTextureLocation, suffix));
            return result;
        }
    }

    public static abstract class TrivialBlockTemplate extends TrivialBlock {
        public ModelTemplate template;
        public boolean flatItem = false;

        public TrivialBlockTemplate(Block block, int variants, ModelTemplate base) {
            super(block, variants);
            this.template = base;
        }

        @Override
        public void registerModels(RTBlockModelGenerators blockModels, RTItemModelGenerators itemModels, List<String> suffixes) {
            List<Variant> variantsList = getVariants(template, suffixes, blockModels);
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, RTBlockModelGenerators.variants(variantsList.toArray(new Variant[0]))));
            if (flatItem) blockModels.registerSimpleFlatItemModel(block);
            else blockModels.registerSimpleItemModel(block, variantsList.getFirst().modelLocation());
        }
    }

    public static class Cube extends TrivialBlockTemplate {
        public Cube(Block block, int variants) { super(block, variants, ModelTemplates.CUBE_ALL); }
        public Cube(Block block) { this(block, 1); }
    }
}
