package com.danielkkrafft.registrytemplates;

import com.danielkkrafft.registrytemplates.template.RTDataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class DataGenerators {

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) { // TODO I'm confused about the ordering for data runs
        for (RTDataProvider<?> provider : RegistryTemplates.INSTANCE.getAll(RTDataProvider.class)) {
            event.createProvider((output, p) -> provider.supplier.apply(output, p));
        }
    }
}
