package com.example.examplemod.content.headmountedgasbottle.client;

import com.example.examplemod.content.headmountedgasbottle.entity.HeadMountedGasBottleProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HeadMountedGasBottleProjectileRenderer extends GeoEntityRenderer<HeadMountedGasBottleProjectile> {
    public HeadMountedGasBottleProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new HeadMountedGasBottleProjectileModel());
        withScale(4.3f);
        shadowRadius = 0.15f;
    }

    @Override
    protected void applyRotations(HeadMountedGasBottleProjectile projectile, PoseStack poseStack,
                                  float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(projectile, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        float pitch = Mth.lerp(partialTick, projectile.xRotO, projectile.getXRot());
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(projectile.getRandomRoll()));
    }
}
