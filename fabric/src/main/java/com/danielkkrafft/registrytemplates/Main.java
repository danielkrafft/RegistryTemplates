package com.danielkkrafft.registrytemplates;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Constants.LOG.info("Hello Fabric world!");
        new RegistryTemplates("com.danielkkrafft.registrytemplates", Constants.MOD_ID).load();
    }
}
