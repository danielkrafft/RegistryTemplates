package com.danielkkrafft.registrytemplates;

import com.danielkkrafft.registrytemplates.platform.Services;
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

public class RegistryTemplates {

    public static RegistryTemplates INSTANCE;

    public final String basePackage;
    public final String modid;
    public final Map<Class<?>, List<RegistryTemplate<?>>> templateMap = new HashMap<>();

    public RegistryTemplates(String basePackage, String modid) {
        this.basePackage = basePackage;
        this.modid = modid;
    }

    public void load() {
        INSTANCE = this;

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

        getAll(RegistryTemplate.class).forEach(RegistryTemplate::register);
        Services.PLATFORM.completeRegistration();
    }

    public <T extends RegistryTemplate<?>> void track(Class<T> clazz, T template) {
        templateMap.putIfAbsent(clazz, new ArrayList<>());
        templateMap.get(clazz).add(template);
    }

    public <T extends RegistryTemplate<?>> List<T> getAll(Class<T> clazz) {
        return (List<T>) templateMap.get(clazz);
    }

    public static <T> void register(ResourceKey<Registry<T>> registry, ResourceKey<T> key, Supplier<T> supplier) {
        Services.PLATFORM.register(registry, key, supplier);
    }
}
