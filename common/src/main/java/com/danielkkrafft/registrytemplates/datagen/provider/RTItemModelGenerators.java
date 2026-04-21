package com.danielkkrafft.registrytemplates.datagen.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.color.item.Firework;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.MapColor;
import net.minecraft.client.color.item.Potion;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.BundleSelectedItemSpecialRenderer;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.conditional.Broken;
import net.minecraft.client.renderer.item.properties.conditional.BundleHasSelectedItem;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.conditional.FishingRodCast;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngle;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngleState;
import net.minecraft.client.renderer.item.properties.numeric.CrossbowPull;
import net.minecraft.client.renderer.item.properties.numeric.Time;
import net.minecraft.client.renderer.item.properties.numeric.UseCycle;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.client.renderer.item.properties.select.Charge;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.client.renderer.special.TridentSpecialRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;

public class RTItemModelGenerators {
    private static final ItemTintSource BLANK_LAYER = ItemModelUtils.constantTint(-1);
    public static final Identifier TRIM_PREFIX_HELMET = prefixForSlotTrim("helmet");
    public static final Identifier TRIM_PREFIX_CHESTPLATE = prefixForSlotTrim("chestplate");
    public static final Identifier TRIM_PREFIX_LEGGINGS = prefixForSlotTrim("leggings");
    public static final Identifier TRIM_PREFIX_BOOTS = prefixForSlotTrim("boots");
    public static final List<RTItemModelGenerators.TrimMaterialData> TRIM_MATERIAL_MODELS = List.of(
            new RTItemModelGenerators.TrimMaterialData(MaterialAssetGroup.QUARTZ, TrimMaterials.QUARTZ),
            new RTItemModelGenerators.TrimMaterialData(MaterialAssetGroup.IRON, TrimMaterials.IRON),
            new RTItemModelGenerators.TrimMaterialData(MaterialAssetGroup.NETHERITE, TrimMaterials.NETHERITE),
            new RTItemModelGenerators.TrimMaterialData(MaterialAssetGroup.REDSTONE, TrimMaterials.REDSTONE),
            new RTItemModelGenerators.TrimMaterialData(MaterialAssetGroup.COPPER, TrimMaterials.COPPER),
            new RTItemModelGenerators.TrimMaterialData(MaterialAssetGroup.GOLD, TrimMaterials.GOLD),
            new RTItemModelGenerators.TrimMaterialData(MaterialAssetGroup.EMERALD, TrimMaterials.EMERALD),
            new RTItemModelGenerators.TrimMaterialData(MaterialAssetGroup.DIAMOND, TrimMaterials.DIAMOND),
            new RTItemModelGenerators.TrimMaterialData(MaterialAssetGroup.LAPIS, TrimMaterials.LAPIS),
            new RTItemModelGenerators.TrimMaterialData(MaterialAssetGroup.AMETHYST, TrimMaterials.AMETHYST),
            new RTItemModelGenerators.TrimMaterialData(MaterialAssetGroup.RESIN, TrimMaterials.RESIN)
    );
    public final ItemModelOutput itemModelOutput;
    public final BiConsumer<Identifier, ModelInstance> modelOutput;

    public static Identifier prefixForSlotTrim(String slotName) {
        return Identifier.withDefaultNamespace("trims/items/" + slotName + "_trim");
    }

    public RTItemModelGenerators(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        this.itemModelOutput = itemModelOutput;
        this.modelOutput = modelOutput;
    }

    public void declareCustomModelItem(Item item) {
        this.itemModelOutput.accept(item, ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item)));
    }

    public Identifier createFlatItemModel(Item item, ModelTemplate template) {
        return template.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this.modelOutput);
    }

    public void generateFlatItem(Item item, ModelTemplate template) {
        this.itemModelOutput.accept(item, ItemModelUtils.plainModel(this.createFlatItemModel(item, template)));
    }

    public Identifier createFlatItemModel(Item item, String suffix, ModelTemplate template) {
        return template.create(
                ModelLocationUtils.getModelLocation(item, suffix), TextureMapping.layer0(TextureMapping.getItemTexture(item, suffix)), this.modelOutput
        );
    }

    public Identifier createFlatItemModel(Item item, Item textureDonor, ModelTemplate template) {
        return template.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(textureDonor), this.modelOutput);
    }

    public void generateFlatItem(Item item, Item textureDonor, ModelTemplate template) {
        this.itemModelOutput.accept(item, ItemModelUtils.plainModel(this.createFlatItemModel(item, textureDonor, template)));
    }

    public void generateItemWithTintedOverlay(Item item, ItemTintSource overlayTint) {
        this.generateItemWithTintedOverlay(item, "_overlay", overlayTint);
    }

    public void generateItemWithTintedOverlay(Item item, String overlaySuffix, ItemTintSource overlayTint) {
        Identifier model = this.generateLayeredItem(item, TextureMapping.getItemTexture(item), TextureMapping.getItemTexture(item, overlaySuffix));
        this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, BLANK_LAYER, overlayTint));
    }

    public void generateItemWithTintedBaseLayer(Item item, int defaultColor) {
        Material tintedLayer = TextureMapping.getItemTexture(item);
        Material untintedLayer = TextureMapping.getItemTexture(item, "_overlay");
        Identifier model = ModelLocationUtils.getModelLocation(item);
        ModelTemplates.TWO_LAYERED_ITEM.create(model, TextureMapping.layered(tintedLayer, untintedLayer), this.modelOutput);
        this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new Dye(defaultColor)));
    }

    public List<RangeSelectItemModel.Entry> createCompassModels(Item compass) {
        List<RangeSelectItemModel.Entry> overrides = new ArrayList<>();
        ItemModel.Unbaked base = ItemModelUtils.plainModel(this.createFlatItemModel(compass, "_16", ModelTemplates.FLAT_ITEM));
        overrides.add(ItemModelUtils.override(base, 0.0F));

        for (int i = 1; i < 32; i++) {
            int textureIndex = Mth.positiveModulo(i - 16, 32);
            ItemModel.Unbaked overrideModel = ItemModelUtils.plainModel(
                    this.createFlatItemModel(compass, String.format(Locale.ROOT, "_%02d", textureIndex), ModelTemplates.FLAT_ITEM)
            );
            overrides.add(ItemModelUtils.override(overrideModel, i - 0.5F));
        }

        overrides.add(ItemModelUtils.override(base, 31.5F));
        return overrides;
    }

    public void generateStandardCompassItem(Item compass) {
        List<RangeSelectItemModel.Entry> overrides = this.createCompassModels(compass);
        this.itemModelOutput
                .accept(
                        compass,
                        ItemModelUtils.conditional(
                                ItemModelUtils.hasComponent(DataComponents.LODESTONE_TRACKER),
                                ItemModelUtils.rangeSelect(new CompassAngle(true, CompassAngleState.CompassTarget.LODESTONE), 32.0F, overrides),
                                ItemModelUtils.rangeSelect(new CompassAngle(true, CompassAngleState.CompassTarget.SPAWN), 32.0F, overrides)
                        )
                );
    }

    public void generateRecoveryCompassItem(Item compass) {
        this.itemModelOutput
                .accept(
                        compass, ItemModelUtils.rangeSelect(new CompassAngle(true, CompassAngleState.CompassTarget.RECOVERY), 32.0F, this.createCompassModels(compass))
                );
    }

    public void generateClockItem(Item clock) {
        List<RangeSelectItemModel.Entry> overrides = new ArrayList<>();
        ItemModel.Unbaked base = ItemModelUtils.plainModel(this.createFlatItemModel(clock, "_00", ModelTemplates.FLAT_ITEM));
        overrides.add(ItemModelUtils.override(base, 0.0F));

        for (int i = 1; i < 64; i++) {
            ItemModel.Unbaked overrideModel = ItemModelUtils.plainModel(
                    this.createFlatItemModel(clock, String.format(Locale.ROOT, "_%02d", i), ModelTemplates.FLAT_ITEM)
            );
            overrides.add(ItemModelUtils.override(overrideModel, i - 0.5F));
        }

        overrides.add(ItemModelUtils.override(base, 63.5F));
        this.itemModelOutput
                .accept(
                        clock,
                        ItemModelUtils.inOverworld(
                                ItemModelUtils.rangeSelect(new Time(true, Time.TimeSource.DAYTIME), 64.0F, overrides),
                                ItemModelUtils.rangeSelect(new Time(true, Time.TimeSource.RANDOM), 64.0F, overrides)
                        )
                );
    }

    public Identifier generateLayeredItem(Item target, Material layer0, Material layer1) {
        return ModelTemplates.TWO_LAYERED_ITEM.create(target, TextureMapping.layered(layer0, layer1), this.modelOutput);
    }

    public Identifier generateLayeredItem(Identifier target, Material layer0, Material layer1) {
        return ModelTemplates.TWO_LAYERED_ITEM.create(target, TextureMapping.layered(layer0, layer1), this.modelOutput);
    }

    public void generateLayeredItem(Identifier target, Material layer0, Material layer1, Material layer2) {
        ModelTemplates.THREE_LAYERED_ITEM.create(target, TextureMapping.layered(layer0, layer1, layer2), this.modelOutput);
    }

    public void generateTrimmableItem(Item armor, ResourceKey<EquipmentAsset> equipmentAssetId, Identifier slotTrimPrefix, boolean hasDyedLayer) {
        Identifier modelLocation = ModelLocationUtils.getModelLocation(armor);
        Material itemTexture = TextureMapping.getItemTexture(armor);
        Material overlayTexture = TextureMapping.getItemTexture(armor, "_overlay");
        List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> cases = new ArrayList<>(TRIM_MATERIAL_MODELS.size());

        for (RTItemModelGenerators.TrimMaterialData material : TRIM_MATERIAL_MODELS) {
            Identifier trimModelLocation = modelLocation.withSuffix("_" + material.assets().base().suffix() + "_trim");
            Material trimOverlayTexture = new Material(slotTrimPrefix.withSuffix("_" + material.assets().assetId(equipmentAssetId).suffix()));
            ItemModel.Unbaked trimModel;
            if (hasDyedLayer) {
                this.generateLayeredItem(trimModelLocation, itemTexture, overlayTexture, trimOverlayTexture);
                trimModel = ItemModelUtils.tintedModel(trimModelLocation, new Dye(-6265536));
            } else {
                this.generateLayeredItem(trimModelLocation, itemTexture, trimOverlayTexture);
                trimModel = ItemModelUtils.plainModel(trimModelLocation);
            }

            cases.add(ItemModelUtils.when(material.materialKey, trimModel));
        }

        ItemModel.Unbaked untrimmedModel;
        if (hasDyedLayer) {
            ModelTemplates.TWO_LAYERED_ITEM.create(modelLocation, TextureMapping.layered(itemTexture, overlayTexture), this.modelOutput);
            untrimmedModel = ItemModelUtils.tintedModel(modelLocation, new Dye(-6265536));
        } else {
            ModelTemplates.FLAT_ITEM.create(modelLocation, TextureMapping.layer0(itemTexture), this.modelOutput);
            untrimmedModel = ItemModelUtils.plainModel(modelLocation);
        }

        this.itemModelOutput.accept(armor, ItemModelUtils.select(new TrimMaterialProperty(), untrimmedModel, cases));
    }

    public void generateBundleModels(Item bundle) {
        ItemModel.Unbaked closedModel = ItemModelUtils.plainModel(this.createFlatItemModel(bundle, ModelTemplates.FLAT_ITEM));
        Identifier openBackCover = this.generateBundleCoverModel(bundle, ModelTemplates.BUNDLE_OPEN_BACK_INVENTORY, "_open_back");
        Identifier openFrontCover = this.generateBundleCoverModel(bundle, ModelTemplates.BUNDLE_OPEN_FRONT_INVENTORY, "_open_front");
        ItemModel.Unbaked openModel = ItemModelUtils.composite(
                ItemModelUtils.plainModel(openBackCover), new BundleSelectedItemSpecialRenderer.Unbaked(), ItemModelUtils.plainModel(openFrontCover)
        );
        ItemModel.Unbaked inGuiModel = ItemModelUtils.conditional(new BundleHasSelectedItem(), openModel, closedModel);
        this.itemModelOutput.accept(bundle, ItemModelUtils.select(new DisplayContext(), closedModel, ItemModelUtils.when(ItemDisplayContext.GUI, inGuiModel)));
    }

    public Identifier generateBundleCoverModel(Item item, ModelTemplate template, String suffix) {
        Material texture = TextureMapping.getItemTexture(item, suffix);
        return template.create(item, TextureMapping.layer0(texture), this.modelOutput);
    }

    public void generateBow(Item item) {
        ItemModel.Unbaked bowModel = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
        ItemModel.Unbaked pulling0 = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_pulling_0", ModelTemplates.BOW));
        ItemModel.Unbaked pulling1 = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_pulling_1", ModelTemplates.BOW));
        ItemModel.Unbaked pulling2 = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_pulling_2", ModelTemplates.BOW));
        this.itemModelOutput
                .accept(
                        item,
                        ItemModelUtils.conditional(
                                ItemModelUtils.isUsingItem(),
                                ItemModelUtils.rangeSelect(
                                        new UseDuration(false), 0.05F, pulling0, ItemModelUtils.override(pulling1, 0.65F), ItemModelUtils.override(pulling2, 0.9F)
                                ),
                                bowModel
                        )
                );
    }

    public void generateCrossbow(Item item) {
        ItemModel.Unbaked crossbowModel = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
        ItemModel.Unbaked pulling0 = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_pulling_0", ModelTemplates.CROSSBOW));
        ItemModel.Unbaked pulling1 = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_pulling_1", ModelTemplates.CROSSBOW));
        ItemModel.Unbaked pulling2 = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_pulling_2", ModelTemplates.CROSSBOW));
        ItemModel.Unbaked loadedArrow = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_arrow", ModelTemplates.CROSSBOW));
        ItemModel.Unbaked loadedFirework = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_firework", ModelTemplates.CROSSBOW));
        this.itemModelOutput
                .accept(
                        item,
                        ItemModelUtils.select(
                                new Charge(),
                                ItemModelUtils.conditional(
                                        ItemModelUtils.isUsingItem(),
                                        ItemModelUtils.rangeSelect(
                                                new CrossbowPull(), pulling0, ItemModelUtils.override(pulling1, 0.58F), ItemModelUtils.override(pulling2, 1.0F)
                                        ),
                                        crossbowModel
                                ),
                                ItemModelUtils.when(CrossbowItem.ChargeType.ARROW, loadedArrow),
                                ItemModelUtils.when(CrossbowItem.ChargeType.ROCKET, loadedFirework)
                        )
                );
    }

    public void generateBooleanDispatch(Item item, ConditionalItemModelProperty property, ItemModel.Unbaked modelOnTrue, ItemModel.Unbaked modelOnFalse) {
        this.itemModelOutput.accept(item, ItemModelUtils.conditional(property, modelOnTrue, modelOnFalse));
    }

    public void generateElytra(Item item) {
        ItemModel.Unbaked normalElytra = ItemModelUtils.plainModel(this.createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked brokenElytra = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_broken", ModelTemplates.FLAT_ITEM));
        this.generateBooleanDispatch(item, new Broken(), brokenElytra, normalElytra);
    }

    public void generateBrush(Item item) {
        ItemModel.Unbaked base = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
        ItemModel.Unbaked brushing0 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_brushing_0"));
        ItemModel.Unbaked brushing1 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_brushing_1"));
        ItemModel.Unbaked brushing2 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_brushing_2"));
        this.itemModelOutput
                .accept(
                        item,
                        ItemModelUtils.rangeSelect(
                                new UseCycle(10.0F),
                                0.1F,
                                base,
                                ItemModelUtils.override(brushing0, 0.25F),
                                ItemModelUtils.override(brushing1, 0.5F),
                                ItemModelUtils.override(brushing2, 0.75F)
                        )
                );
    }

    public void generateFishingRod(Item item) {
        ItemModel.Unbaked normal = ItemModelUtils.plainModel(this.createFlatItemModel(item, ModelTemplates.FLAT_HANDHELD_ROD_ITEM));
        ItemModel.Unbaked cast = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_cast", ModelTemplates.FLAT_HANDHELD_ROD_ITEM));
        this.generateBooleanDispatch(item, new FishingRodCast(), cast, normal);
    }

    public void generateGoatHorn(Item item) {
        ItemModel.Unbaked normal = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
        ItemModel.Unbaked tooting = ItemModelUtils.plainModel(ModelLocationUtils.decorateItemModelLocation("tooting_goat_horn"));
        this.generateBooleanDispatch(item, ItemModelUtils.isUsingItem(), tooting, normal);
    }

    public void generateShield(Item item) {
        ItemModel.Unbaked normal = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(item), new ShieldSpecialRenderer.Unbaked());
        ItemModel.Unbaked blocking = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(item, "_blocking"), new ShieldSpecialRenderer.Unbaked());
        this.itemModelOutput
                .accept(item, ItemModelUtils.conditional(ShieldSpecialRenderer.DEFAULT_TRANSFORMATION, ItemModelUtils.isUsingItem(), blocking, normal));
    }

    public static ItemModel.Unbaked createFlatModelDispatch(ItemModel.Unbaked flatModel, ItemModel.Unbaked inHandModel) {
        return ItemModelUtils.select(
                new DisplayContext(),
                inHandModel,
                ItemModelUtils.when(List.of(ItemDisplayContext.GUI, ItemDisplayContext.GROUND, ItemDisplayContext.FIXED, ItemDisplayContext.ON_SHELF), flatModel)
        );
    }

    public void generateSpyglass(Item item) {
        ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(this.createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked inHandModel = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_in_hand"));
        this.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel));
    }

    public void generateTrident(Item item) {
        ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(this.createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked inHandNormalModel = ItemModelUtils.specialModel(
                ModelLocationUtils.getModelLocation(item, "_in_hand"), new TridentSpecialRenderer.Unbaked()
        );
        ItemModel.Unbaked inHandThrowingModel = ItemModelUtils.specialModel(
                ModelLocationUtils.getModelLocation(item, "_throwing"), new TridentSpecialRenderer.Unbaked()
        );
        ItemModel.Unbaked inHandModel = ItemModelUtils.conditional(
                TridentSpecialRenderer.DEFAULT_TRANSFORMATION, ItemModelUtils.isUsingItem(), inHandThrowingModel, inHandNormalModel
        );
        this.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel));
    }

    public void generateSpear(Item item) {
        ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(this.createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked inHandModel = ItemModelUtils.plainModel(
                ModelTemplates.SPEAR_IN_HAND.create(item, TextureMapping.layer0(TextureMapping.getItemTexture(item, "_in_hand")), this.modelOutput)
        );
        this.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel), new ClientItem.Properties(true, false, 1.95F));
    }

    public void addPotionTint(Item item, Identifier model) {
        this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new Potion()));
    }

    public void generatePotion(Item item) {
        Identifier model = this.generateLayeredItem(
                item, new Material(Identifier.withDefaultNamespace("item/potion_overlay")), TextureMapping.getItemTexture(item)
        );
        this.addPotionTint(item, model);
    }

    public void generateTippedArrow(Item item) {
        Identifier model = this.generateLayeredItem(item, TextureMapping.getItemTexture(item, "_head"), TextureMapping.getItemTexture(item, "_base"));
        this.addPotionTint(item, model);
    }

    public void generateDyedItem(Item item, int defaultColor) {
        Identifier model = this.createFlatItemModel(item, ModelTemplates.FLAT_ITEM);
        this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new Dye(defaultColor)));
    }

    public void generateTwoLayerDyedItem(Item item) {
        Material baseLayer = TextureMapping.getItemTexture(item);
        Material tintedLayer = TextureMapping.getItemTexture(item, "_overlay");
        Identifier plainModel = ModelTemplates.FLAT_ITEM.create(item, TextureMapping.layer0(baseLayer), this.modelOutput);
        Identifier dyedModel = ModelLocationUtils.getModelLocation(item, "_dyed");
        ModelTemplates.TWO_LAYERED_ITEM.create(dyedModel, TextureMapping.layered(baseLayer, tintedLayer), this.modelOutput);
        this.itemModelOutput
                .accept(
                        item,
                        ItemModelUtils.conditional(
                                ItemModelUtils.hasComponent(DataComponents.DYED_COLOR),
                                ItemModelUtils.tintedModel(dyedModel, BLANK_LAYER, new Dye(0)),
                                ItemModelUtils.plainModel(plainModel)
                        )
                );
    }

    public void run() {}

    public record TrimMaterialData(MaterialAssetGroup assets, ResourceKey<TrimMaterial> materialKey) {
    }
}
