package com.danielkkrafft.registrytemplates;

import com.danielkkrafft.registrytemplates.template.RTAbstractItem;
import com.danielkkrafft.registrytemplates.util.ModContent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

@ModContent
public class TestContent {
    public static final RTAbstractItem<Item, ?> ITEM = new RTAbstractItem<>("test_item", Item::new).flatModel().setLocalEN(() -> "Test Item").tag(ItemTags.STONE_CRAFTING_MATERIALS);
}
