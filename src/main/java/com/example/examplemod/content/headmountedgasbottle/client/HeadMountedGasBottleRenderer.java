package com.example.examplemod.content.headmountedgasbottle.client;

import com.example.examplemod.content.headmountedgasbottle.item.HeadMountedGasBottleItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class HeadMountedGasBottleRenderer extends GeoArmorRenderer<HeadMountedGasBottleItem> {
    private static final float MODEL_SCALE = 4.3f;
    private static final float VANILLA_HUMANOID_HEAD_OFFSET = 26.5f;
    private static final float HEAD_OFFSET_DOWN = 0.25f;
    private static final float OTHER_HUMANOID_CORRECTION = 0.875f;
    private static final float SNEAKING_HEAD_Y = 3.15f;

    public HeadMountedGasBottleRenderer() {
        super(new HeadMountedGasBottleModel());
        withScale(MODEL_SCALE);
    }

    @Override
    public void preRender(PoseStack poseStack, HeadMountedGasBottleItem animatable, BakedGeoModel model,
                          @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, colour);

        Entity entity = getCurrentEntity();
        if (!isReRender && entity != null && head != null) {
            boolean useVanillaHumanoidAnchor = entity instanceof Zombie
                    || entity instanceof AbstractSkeleton
                    || entity instanceof Player;
            float headOffset = useVanillaHumanoidAnchor
                    ? VANILLA_HUMANOID_HEAD_OFFSET
                    : (entity.getBbHeight() - HEAD_OFFSET_DOWN) * 16.0f;
            head.setPosY(head.getPosY() + headOffset);

            if (entity instanceof Player && baseModel != null && baseModel.crouching) {
                head.setPosY(head.getPosY() + SNEAKING_HEAD_Y);
            }

            if (!useVanillaHumanoidAnchor) {
                poseStack.translate(0.0f, -OTHER_HUMANOID_CORRECTION / 16.0f, 0.0f);
            }
        }
    }

    @Override
    public GeoBone getHeadBone(GeoModel<HeadMountedGasBottleItem> model) {
        return model.getBone("root").orElse(null);
    }
}
