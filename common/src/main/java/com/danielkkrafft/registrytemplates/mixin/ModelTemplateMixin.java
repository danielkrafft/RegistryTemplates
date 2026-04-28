package com.danielkkrafft.registrytemplates.mixin;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ModelTemplate.class)
public interface ModelTemplateMixin {
    @Accessor("requiredSlots")
    Set<TextureSlot> getRequiredSlots();
}
