package com.danielkkrafft.registrytemplates;

public class RegistryTemplates extends AbstractRegistryTemplates {
    public RegistryTemplates(String basePackage, String modid) {
        super(basePackage, modid);
    }
    public static RegistryTemplates getCurrent() { return (RegistryTemplates) AbstractRegistryTemplates.getCurrent(); }
}
