package com.danielkkrafft.registrytemplates;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class Main {

    public Main(IEventBus eventBus) {
        new RegistryTemplates("com.danielkkrafft.registrytemplates", Constants.MOD_ID, eventBus).load();
    }
}