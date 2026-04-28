package com.danielkkrafft.registrytemplates.platform;

import com.danielkkrafft.registrytemplates.AbstractRegistryTemplates;
import com.danielkkrafft.registrytemplates.RegistryTemplates;
import com.danielkkrafft.registrytemplates.platform.services.IPlatformHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import java.util.function.Supplier;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public <T> void register(ResourceKey<Registry<T>> registry, ResourceKey<T> key, Supplier<T> supplier) {
        Registry<T> reg = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(registry.identifier());
        ((RegistryTemplates)AbstractRegistryTemplates.CURRENT).getDeferredRegister(reg).register(key.identifier().getPath(), supplier);
    }

    @Override
    public void completeRegistration() {
        ((RegistryTemplates)AbstractRegistryTemplates.CURRENT).completeRegistration();
    }
}