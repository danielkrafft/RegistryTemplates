package com.danielkkrafft.registrytemplates;

import com.danielkkrafft.registrytemplates.template.RTDataProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;

public class DataGenerators implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        for (RTDataProvider<?> provider : RegistryTemplates.INSTANCE.getAll(RTDataProvider.class)) {
            pack.addProvider((o, r) -> provider.supplier.apply(o, r));
        }
    }
}
