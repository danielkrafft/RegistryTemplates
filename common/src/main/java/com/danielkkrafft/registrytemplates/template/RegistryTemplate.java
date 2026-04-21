package com.danielkkrafft.registrytemplates.template;

import com.danielkkrafft.registrytemplates.RegistryTemplates;
import com.danielkkrafft.registrytemplates.datagen.provider.RTLanguageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class RegistryTemplate<T extends RegistryTemplate<T>> {

    public String id;
    private Supplier<String> localEN = () -> ""; // local might be referenced before it exists

    public RegistryTemplate(String id) {
        RegistryTemplates.INSTANCE.track(RegistryTemplate.class, this);
        this.id = id;
    }

    public void register() {}

    public T setLocalEN(Supplier<String> local) { this.localEN = local; return (T) this; }
    public String getLocalEN() { return this.localEN.get(); }

    public void registerModels() {}
    public void addTranslations(RTLanguageProvider provider) {}
}
