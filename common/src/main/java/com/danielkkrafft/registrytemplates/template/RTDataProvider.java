package com.danielkkrafft.registrytemplates.template;

import com.danielkkrafft.registrytemplates.AbstractRegistryTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.apache.commons.lang3.function.TriFunction;

import java.util.concurrent.CompletableFuture;

public class RTDataProvider<T extends DataProvider> extends RegistryTemplate<RTDataProvider<T>> {

    public TriFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, AbstractRegistryTemplates, T> supplier;

    public RTDataProvider(TriFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, AbstractRegistryTemplates, T> supplier) {
        super(null);
        this.supplier = supplier;
    }
}
