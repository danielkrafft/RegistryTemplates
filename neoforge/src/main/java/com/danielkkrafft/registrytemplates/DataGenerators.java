package com.danielkkrafft.registrytemplates;

import com.danielkkrafft.registrytemplates.template.RTDataProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class DataGenerators {

    public static void gatherClientData(GatherDataEvent.Client event, RegistryTemplates templates) {
        System.out.println("Gathering Client Data");
        for (RTDataProvider<?> provider : templates.getAll(RTDataProvider.class)) {
            System.out.println("Providing: " + provider);
            event.createProvider((output, p) -> provider.supplier.apply(output, p, templates));
        }
    }
}
