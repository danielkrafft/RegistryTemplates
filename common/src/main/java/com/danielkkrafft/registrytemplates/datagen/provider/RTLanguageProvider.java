package com.danielkkrafft.registrytemplates.datagen.provider;

import com.danielkkrafft.registrytemplates.AbstractRegistryTemplates;
import com.danielkkrafft.registrytemplates.template.RegistryTemplate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public class RTLanguageProvider implements DataProvider {

    private static final Codec<Map<String, Component>> CODEC = Codec.unboundedMap(Codec.STRING, ComponentSerialization.CODEC);

    private final Map<String, Map<String, Component>> data = new TreeMap<>();
    private final PackOutput output;
    private final AbstractRegistryTemplates templates;

    public RTLanguageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, AbstractRegistryTemplates templates) {
        this.output = output;
        this.templates = templates;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        templates.getAll(RegistryTemplate.class).forEach(t -> t.addTranslations(this));

        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (Map.Entry<String, Map<String, Component>> language : data.entrySet()) {
            String locale = language.getKey();
            Map<String, Component> localeData = language.getValue();

            futures.add(save(localeData, cachedOutput, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(templates.modid).resolve("lang").resolve(locale + ".json")));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}));
    }

    @Override
    public String getName() {
        return "All languages for all RegistryTemplate implementations";
    }

    private CompletableFuture<?> save(Map<String, Component> langData, CachedOutput cache, Path target) {
        final JsonElement json = CODEC.encode(langData, JsonOps.INSTANCE, new JsonObject()).getOrThrow();
        return DataProvider.saveStable(cache, json, target);
    }

    public void add(String locale, String key, Component value) {
        if (!data.containsKey(locale)) data.put(locale, new TreeMap<>());
        Map<String, Component> langData = data.get(locale);

        if (langData.put(key, value) != null) throw new IllegalStateException("Duplicate translation key " + key);
    }
}
