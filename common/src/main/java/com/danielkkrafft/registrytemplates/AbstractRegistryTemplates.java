package com.danielkkrafft.registrytemplates;

import com.danielkkrafft.registrytemplates.datagen.provider.RTItemTagsProvider;
import com.danielkkrafft.registrytemplates.datagen.provider.RTLanguageProvider;
import com.danielkkrafft.registrytemplates.datagen.provider.RTModelProvider;
import com.danielkkrafft.registrytemplates.platform.Services;
import com.danielkkrafft.registrytemplates.template.RTDataProvider;
import com.danielkkrafft.registrytemplates.template.RegistryTemplate;
import com.danielkkrafft.registrytemplates.util.ModContent;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractRegistryTemplates {

    public static Map<String, AbstractRegistryTemplates> INSTANCES = new HashMap<>();
    public static AbstractRegistryTemplates CURRENT = null; // TODO include a static track() queue that resolves once the instance is created

    public final String basePackage;
    public final String modid;
    public final List<RegistryTemplate<?>> templateMap = new ArrayList<>();

    public AbstractRegistryTemplates(String basePackage, String modid) {
        INSTANCES.put(modid, this);
        CURRENT = this;
        this.basePackage = basePackage;
        this.modid = modid;
    }

    public void load() {
        ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(basePackage)
                .scan();

        ClassInfoList implementingClasses2 = scanResult.getClassesWithAnnotation(ModContent.class);

        try {
            for (Class<Object> clazz : implementingClasses2.loadClasses(Object.class)) {
                System.out.println("Loading: " + clazz.getSimpleName());
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        scanResult.close();

        createProviders();

        getAll(RegistryTemplate.class).forEach(RegistryTemplate::register);
        Services.PLATFORM.completeRegistration();
        CURRENT = null;
    }

    public void track(RegistryTemplate<?> template) {
        templateMap.add(template);
        template.owner = this;
    }

    public <T extends RegistryTemplate<?>> List<T> getAll(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for (RegistryTemplate<?> template : templateMap) {
            if (clazz.isAssignableFrom(template.getClass())) result.add((T) template);
        }
        return result;
    }

    public static <T extends RegistryTemplate<?>> List<T> getAllInclusive(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for (Map.Entry<String, AbstractRegistryTemplates> entry : INSTANCES.entrySet()) {
            for (RegistryTemplate<?> template : entry.getValue().templateMap) {
                if (clazz.isAssignableFrom(template.getClass())) result.add((T) template);
            }
        }
        return result;
    }

    public static <T> void register(ResourceKey<Registry<T>> registry, ResourceKey<T> key, Supplier<T> supplier) {
        Services.PLATFORM.register(registry, key, supplier);
    }

    public void createProviders() {
        new RTDataProvider<>(RTItemTagsProvider::new);
        new RTDataProvider<>(RTLanguageProvider::new);
        new RTDataProvider<>(RTModelProvider::new);
    }
}
