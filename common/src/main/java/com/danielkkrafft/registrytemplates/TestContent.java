package com.danielkkrafft.registrytemplates;

import com.danielkkrafft.registrytemplates.template.RTAbstractBlock;
import com.danielkkrafft.registrytemplates.template.RTAbstractItem;
import com.danielkkrafft.registrytemplates.util.ModContent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@ModContent
public class TestContent {
    public static final RTAbstractItem<Item, ?> ITEM = new RTAbstractItem<>("test_item", Item::new).flatModel().setLocalEN(() -> "Test Item").tag(ItemTags.STONE_CRAFTING_MATERIALS);
    public static final RTAbstractBlock<Block, ?> BLOCK = new RTAbstractBlock<>("test_block", Block::new).basicModel().dropSelf().setLocalEN(() -> "Test Block").tag(BlockTags.NEEDS_STONE_TOOL);
}
