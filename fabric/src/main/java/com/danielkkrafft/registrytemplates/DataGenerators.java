package com.danielkkrafft.registrytemplates;

import com.danielkkrafft.registrytemplates.template.RTDataProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGenerators implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) { // api data is only generated on neoforge side to avoid headaches
        FabricDataGenerator.Pack pack = generator.createPack();
        for (RTDataProvider<?> provider : RegistryTemplates.INSTANCES.get(generator.getModId()).getAll(RTDataProvider.class)) {
            pack.addProvider((o, r) -> provider.supplier.apply(o, r, provider.owner));
        }
    }
}
