package com.danielkkrafft.registrytemplates;

import net.minecraft.core.Registry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class RegistryTemplates extends AbstractRegistryTemplates {

    private Map<Registry<?>, DeferredRegister<?>> deferredRegisters = new HashMap<>();
    public IEventBus eventBus;
    public static RegistryTemplates getCurrent() { return (RegistryTemplates) AbstractRegistryTemplates.getCurrent(); }


    public RegistryTemplates(String basePackage, String modid, IEventBus eventBus) {
        super(basePackage, modid);
        this.eventBus = eventBus;
        eventBus.addListener(GatherDataEvent.Client.class, e -> {
            DataGenerators.gatherClientData(e, this);
        });
    }

    public <T> DeferredRegister<T> getDeferredRegister(Registry<T> registry) {
        deferredRegisters.putIfAbsent(registry, DeferredRegister.create(registry, modid));
        return (DeferredRegister<T>) deferredRegisters.get(registry);
    }

    public void completeRegistration() {
        deferredRegisters.forEach((_, value) -> value.register(eventBus));
    }
}
