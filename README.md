# RegistryTemplates

This mod provides configurable templates to register modded content in a few lines or less.

## Table of Contents
1. [Quick Setup Guide](#quick-setup-guide)
2. [Usage](#usage)

## Quick Setup Guide

### Neoforge:

Add the following dependency to your build script:

```implementation "com.danielkkrafft.registrytemplates:registrytemplates-neoforge:1.0.0-alpha"```

Add the following to your mod's main class constructor:

```
public Main(IEventBus eventBus) {
    new RegistryTemplates("com.danielkkrafft.examplemod", Constants.MOD_ID, eventBus).load(); // use your own base package and modid
}
```

### Fabric

Add the following dependency to your build script:

```implementation "com.danielkkrafft.registrytemplates:registrytemplates-fabric:1.0.0-alpha"```

Add the following to your mod's main initialization hook:

```
@Override
public void onInitialize() {
    new RegistryTemplates("com.danielkkrafft.examplemod", Constants.MOD_ID).load(); // use your own base package and modid
}
```

Add the following entrypoint to your fabric.mod.json:

```
"entrypoints": {
    "fabric-datagen": [
        "com.danielkkrafft.registrytemplates.DataGenerators"
    ]
}
```

### Multiloader

Follow the Neoforge and Fabric steps for their respective modules, then add the following dependency to your common module's build script:

```implementation "com.danielkkrafft.registrytemplates:registrytemplates-common:1.0.0-alpha"```

## Usage

You can declare a RegistryTemplate anywhere in your project:

```
@ModContent // make sure to include this annotation so the class will load automatically
public class Example {
    public static final RTAbstractItem<Item, ?> ITEM = new RTAbstractItem<>("example_item", Item::new).flatModel().setLocalEN(() -> "Example Item");
}
```

The above example generates an item called "example_item" with a flat item model and English localization. That's all you need!