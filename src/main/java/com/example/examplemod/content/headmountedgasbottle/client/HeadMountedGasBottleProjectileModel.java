package com.example.examplemod.content.headmountedgasbottle.client;

import com.example.examplemod.ChemicalMagic;
import com.example.examplemod.content.headmountedgasbottle.entity.HeadMountedGasBottleProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HeadMountedGasBottleProjectileModel extends GeoModel<HeadMountedGasBottleProjectile> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(
            ChemicalMagic.MOD_ID, "geo/head_mounted_gas_bottle.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ChemicalMagic.MOD_ID, "textures/entity/head_mounted_gas_bottle.png");
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(
            ChemicalMagic.MOD_ID, "animations/head_mounted_gas_bottle.animation.json");

    @Override
    public ResourceLocation getModelResource(HeadMountedGasBottleProjectile animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(HeadMountedGasBottleProjectile animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(HeadMountedGasBottleProjectile animatable) {
        return ANIMATIONS;
    }
}
