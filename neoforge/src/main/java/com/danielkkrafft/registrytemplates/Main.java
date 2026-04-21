package com.danielkkrafft.registrytemplates;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class Main {
    public static IEventBus EVENT_BUS;

    public Main(IEventBus eventBus) {
        EVENT_BUS = eventBus;
        new NeoRegistryTemplates("com.danielkkrafft.registrytemplates", Constants.MOD_ID).load();
    }
}