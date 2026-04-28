package com.danielkkrafft.registrytemplates.template;

import com.danielkkrafft.registrytemplates.AbstractRegistryTemplates;
import com.danielkkrafft.registrytemplates.datagen.provider.RTLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.apache.commons.lang3.function.TriFunction;

import java.util.concurrent.CompletableFuture;

/**
 * Adds a DataProvider to the current mod's data run.
 * <p>
 * Usage:
 * <p>
 * {@code public static RTDataProvider<ExampleProvider> EXAMPLE = new RTDataProvider<ExampleProvider>((o, p, t) -> new ExampleProvider(o, p, t));}
 *
 * @param <T> The DataProvider subclass
 */
public class RTDataProvider<T extends DataProvider> extends RegistryTemplate<RTDataProvider<T>> {

    public TriFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, AbstractRegistryTemplates, T> supplier;

    public RTDataProvider(TriFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, AbstractRegistryTemplates, T> supplier) {
        super(null);
        this.supplier = supplier;
    }
}
