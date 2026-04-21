package com.danielkkrafft.registrytemplates;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class NeoRegistryTemplates extends RegistryTemplates {
    public static NeoRegistryTemplates INSTANCE;

    private Map<Registry<?>, DeferredRegister<?>> deferredRegisters = new HashMap<>();

    public NeoRegistryTemplates(String basePackage, String modid) {
        super(basePackage, modid);
    }

    @Override
    public void load() {
        INSTANCE = this;
        super.load();
    }

    public <T> DeferredRegister<T> getDeferredRegister(Registry<T> registry) {
        deferredRegisters.putIfAbsent(registry, DeferredRegister.create(registry, modid));
        return (DeferredRegister<T>) deferredRegisters.get(registry);
    }

    public void completeRegistration() {
        deferredRegisters.forEach((_, value) -> value.register(Main.EVENT_BUS));
    }
}
