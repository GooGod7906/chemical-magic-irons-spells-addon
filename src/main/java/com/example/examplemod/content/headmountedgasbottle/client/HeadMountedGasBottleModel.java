package com.example.examplemod.content.headmountedgasbottle.client;

import com.example.examplemod.ChemicalMagic;
import com.example.examplemod.content.headmountedgasbottle.item.HeadMountedGasBottleItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HeadMountedGasBottleModel extends GeoModel<HeadMountedGasBottleItem> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(
            ChemicalMagic.MOD_ID, "geo/head_mounted_gas_bottle.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ChemicalMagic.MOD_ID, "textures/entity/head_mounted_gas_bottle.png");
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(
            ChemicalMagic.MOD_ID, "animations/head_mounted_gas_bottle.animation.json");

    @Override
    public ResourceLocation getModelResource(HeadMountedGasBottleItem animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(HeadMountedGasBottleItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(HeadMountedGasBottleItem animatable) {
        return ANIMATIONS;
    }
}
