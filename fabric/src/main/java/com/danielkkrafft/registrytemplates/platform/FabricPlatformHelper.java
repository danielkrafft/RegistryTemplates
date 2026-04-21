package com.danielkkrafft.registrytemplates.platform;

import com.danielkkrafft.registrytemplates.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T> void register(ResourceKey<Registry<T>> registry, ResourceKey<T> key, Supplier<T> value) {
        Registry<T> reg = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(registry.identifier());
        if (reg == null) throw new IllegalArgumentException("Invalid registry: " + registry);
        Registry.register(reg, key, value.get());
    }
}
