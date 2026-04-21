package com.danielkkrafft.registrytemplates.datagen.provider;

import com.danielkkrafft.registrytemplates.RegistryTemplates;
import com.danielkkrafft.registrytemplates.template.RTDataProvider;
import com.danielkkrafft.registrytemplates.template.RegistryTemplate;
import com.danielkkrafft.registrytemplates.util.ModContent;
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

@ModContent
public class RTLanguageProvider implements DataProvider {
    public static final RTDataProvider<RTLanguageProvider> PROVIDER = new RTDataProvider<RTLanguageProvider>(RTLanguageProvider::new);

    private static final Codec<Map<String, Component>> CODEC = Codec.unboundedMap(Codec.STRING, ComponentSerialization.CODEC);

    private final Map<String, Map<String, Map<String, Component>>> data = new TreeMap<>();
    private final PackOutput output;

    public RTLanguageProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        RegistryTemplates.INSTANCE.getAll(RegistryTemplate.class).forEach(t -> t.addTranslations(this));

        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (Map.Entry<String, Map<String, Map<String, Component>>> modLanguage : data.entrySet()) {
            String modid = modLanguage.getKey();

            for (Map.Entry<String, Map<String, Component>> language : modLanguage.getValue().entrySet()) {
                String locale = language.getKey();
                Map<String, Component> localeData = language.getValue();

                futures.add(save(localeData, cachedOutput, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(modid).resolve("lang").resolve(locale + ".json")));
            }
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

    public void add(String modid, String locale, String key, Component value) {
        if (!data.containsKey(modid)) data.put(modid, new TreeMap<>());
        Map<String, Map<String, Component>> modData = data.get(modid);

        if (!modData.containsKey(locale)) modData.put(locale, new TreeMap<>());
        Map<String, Component> langData = modData.get(locale);

        if (langData.put(key, value) != null) throw new IllegalStateException("Duplicate translation key " + key);
    }
}
