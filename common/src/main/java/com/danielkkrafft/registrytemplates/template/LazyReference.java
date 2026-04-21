package com.danielkkrafft.registrytemplates.template;

import com.danielkkrafft.registrytemplates.RegistryTemplates;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class LazyReference<R, T extends R> {
    public ResourceKey<R> key;
    public Registry<R> registry;
    public Holder<R> holder; // Holders are not available before registry
    private T value;

    public LazyReference(ResourceKey<Registry<R>> registry, String path) {
        this.registry = (Registry<R>) BuiltInRegistries.REGISTRY.getValue(registry.identifier());
        this.key = ResourceKey.create(registry, Identifier.fromNamespaceAndPath(RegistryTemplates.INSTANCE.modid, path));
    }

    public T get() {
        if (value == null) value = (T) registry.getValue(key.identifier());
        return value;
    }

    public Holder<R> holder() {
        if (holder == null) holder = registry.get(key).get();
        return holder;
    }

    public String id() { return key.identifier().getPath(); }

}
