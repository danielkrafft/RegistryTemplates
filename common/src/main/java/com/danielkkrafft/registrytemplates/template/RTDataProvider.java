package com.danielkkrafft.registrytemplates.template;

import com.danielkkrafft.registrytemplates.RegistryTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RTDataProvider<T extends DataProvider> extends RegistryTemplate<RTDataProvider<T>> {

    public BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> supplier;

    public RTDataProvider(BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> supplier) {
        super(null);
        this.supplier = supplier;
        RegistryTemplates.INSTANCE.track(RTDataProvider.class, this);
    }

    public RTDataProvider(Function<PackOutput, T> supplier) {
        this((output, _) -> supplier.apply(output));
    }

    public RTDataProvider(T supplier) {
        this((_, _) -> supplier);
    }
}
