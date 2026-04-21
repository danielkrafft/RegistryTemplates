package com.danielkkrafft.registrytemplates.client.model;

import com.danielkkrafft.registrytemplates.datagen.provider.RTBlockModelGenerators;
import com.danielkkrafft.registrytemplates.datagen.provider.RTItemModelGenerators;
import com.danielkkrafft.registrytemplates.util.BlockUtil;
import com.danielkkrafft.registrytemplates.util.ItemUtil;
import net.minecraft.client.data.models.model.ModelTemplates;
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
}
