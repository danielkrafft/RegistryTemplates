package com.danielkkrafft.registrytemplates.datagen.provider;

import com.danielkkrafft.registrytemplates.RegistryTemplates;
import com.danielkkrafft.registrytemplates.client.model.RTModel;
import com.danielkkrafft.registrytemplates.template.RTDataProvider;
import com.danielkkrafft.registrytemplates.template.RegistryTemplate;
import com.danielkkrafft.registrytemplates.util.ModContent;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@ModContent
public class RTModelProvider implements DataProvider {
    public static final RTDataProvider<RTModelProvider> PROVIDER = new RTDataProvider<RTModelProvider>(RTModelProvider::new);

    private final PackOutput.PathProvider blockStatePathProvider;
    private final PackOutput.PathProvider itemInfoPathProvider;
    private final PackOutput.PathProvider modelPathProvider;

    public RTModelProvider(PackOutput output) {
        this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.itemInfoPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
        this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        RTModelProvider.ItemInfoCollector itemModels = new RTModelProvider.ItemInfoCollector();
        RTModelProvider.BlockStateGeneratorCollector blockStateGenerators = new RTModelProvider.BlockStateGeneratorCollector();
        RTModelProvider.SimpleModelCollector simpleModels = new RTModelProvider.SimpleModelCollector();
        registerModels(new RTBlockModelGenerators(blockStateGenerators, itemModels, simpleModels), new RTItemModelGenerators(itemModels, simpleModels));
        return CompletableFuture.allOf(
                blockStateGenerators.save(cache, this.blockStatePathProvider),
                simpleModels.save(cache, this.modelPathProvider),
                itemModels.save(cache, this.itemInfoPathProvider)
        );
    }

    protected void registerModels(RTBlockModelGenerators blockModels, RTItemModelGenerators itemModels) {
        RegistryTemplates.INSTANCE.getAll(RegistryTemplate.class).forEach(RegistryTemplate::registerModels);
        RTModel.ALL_MODELS.forEach(m -> m.registerModels(blockModels, itemModels));
        RTModel.ALL_MODELS.clear();
        blockModels.run();
        itemModels.run();
    }

    @Override
    public final String getName() {
        return "Model Definitions";
    }

    private static class BlockStateGeneratorCollector implements Consumer<BlockModelDefinitionGenerator> {
        private final Map<Block, BlockModelDefinitionGenerator> generators = new HashMap<>();

        public void accept(BlockModelDefinitionGenerator generator) {
            Block block = generator.block();
            BlockModelDefinitionGenerator prev = this.generators.put(block, generator);
            if (prev != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        }

        public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
            Map<Block, BlockStateModelDispatcher> definitions = Maps.transformValues(this.generators, BlockModelDefinitionGenerator::create);
            Function<Block, Path> pathGetter = block -> pathProvider.json(block.builtInRegistryHolder().key().identifier());
            return DataProvider.saveAll(cache, BlockStateModelDispatcher.CODEC, pathGetter, definitions);
        }
    }

    private static class ItemInfoCollector implements ItemModelOutput {
        private final Map<Item, ClientItem> itemInfos = new HashMap<>();
        private final Map<Item, Item> copies = new HashMap<>();

        @Override
        public void accept(Item item, ItemModel.Unbaked model, ClientItem.Properties properties) {
            this.register(item, new ClientItem(model, properties));
        }

        public void register(Item item, ClientItem itemInfo) {
            ClientItem prev = this.itemInfos.put(item, itemInfo);
            if (prev != null) {
                throw new IllegalStateException("Duplicate item model definition for " + item);
            }
        }

        @Override
        public void copy(Item donor, Item acceptor) {
            this.copies.put(acceptor, donor);
        }

        public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
            return DataProvider.saveAll(cache, ClientItem.CODEC, item -> pathProvider.json(item.builtInRegistryHolder().key().identifier()), this.itemInfos);
        }
    }

    private static class SimpleModelCollector implements BiConsumer<Identifier, ModelInstance> {
        private final Map<Identifier, ModelInstance> models = new HashMap<>();

        public void accept(Identifier id, ModelInstance contents) {
            Supplier<JsonElement> prev = this.models.put(id, contents);
            if (prev != null) {
                throw new IllegalStateException("Duplicate model definition for " + id);
            }
        }

        public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
            return DataProvider.saveAll(cache, Supplier::get, pathProvider::json, this.models);
        }
    }
}
