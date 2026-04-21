package com.danielkkrafft.registrytemplates.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public abstract class BlockUtil {
    public static Identifier getTexturePath(Block block) {
        Identifier blockKey = BuiltInRegistries.BLOCK.getKey(block);
        return Identifier.fromNamespaceAndPath(blockKey.getNamespace(), "block/" + blockKey.getPath());
    }

    public static String id(Block block) { return BuiltInRegistries.BLOCK.getKey(block).getPath(); }
}
