package com.example.examplemod.Client;

import com.example.examplemod.Entities.Projectile.HeadMountedGasBottleProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HeadMountedGasBottleProjectileRenderer extends GeoEntityRenderer<HeadMountedGasBottleProjectile> {
    public HeadMountedGasBottleProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new HeadMountedGasBottleProjectileModel());
        withScale(4.3f);
        this.shadowRadius = 0.15f;
    }

    @Override
    protected void applyRotations(HeadMountedGasBottleProjectile projectile, PoseStack poseStack,
                                  float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(projectile, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        poseStack.mulPose(Axis.XP.rotationDegrees(-projectile.getXRot()));
    }
}
