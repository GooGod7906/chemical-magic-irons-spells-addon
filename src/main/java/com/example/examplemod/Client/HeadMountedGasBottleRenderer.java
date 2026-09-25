package com.example.examplemod.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.example.examplemod.Items.HeadMountedGasBottleItem;
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
import com.mojang.blaze3d.vertex.VertexConsumer;

public class HeadMountedGasBottleRenderer extends GeoArmorRenderer<HeadMountedGasBottleItem> {
    private static final float MODEL_SCALE = 4.3f;
    private static final float VANILLA_HUMANOID_HEAD_OFFSET = 26.5f;
    private static final float HEAD_OFFSET_DOWN = 0.25f;
    private static final float OTHER_HUMANOID_CORRECTION = 0.875f;
    private static final float SNEAKING_HEAD_Y = 3.15f;

    public HeadMountedGasBottleRenderer() {
        super(new HeadMountedGasBottleModel());
        // The geo model is authored in compact bottle units; enlarge it to head scale.
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
        if (!isReRender && entity != null && this.head != null) {
            // Keep the established zombie/skeleton position unchanged. Other humanoid
            // models retain the height-based anchor.
            boolean useVanillaHumanoidAnchor = entity instanceof Zombie
                    || entity instanceof AbstractSkeleton
                    || entity instanceof Player;
            float headOffset = useVanillaHumanoidAnchor
                    ? VANILLA_HUMANOID_HEAD_OFFSET
                    : (entity.getBbHeight() - HEAD_OFFSET_DOWN) * 16.0f;
            this.head.setPosY(this.head.getPosY() + headOffset);

            if (entity instanceof Player && this.baseModel != null && this.baseModel.crouching) {
                // GeoArmorRenderer scales the custom bone position by 4.3f. Remove
                // vanilla's crouching head translation so it is not amplified.
                this.head.setPosY(this.head.getPosY() + SNEAKING_HEAD_Y);
            }

            if (!useVanillaHumanoidAnchor) {
                // Apply this after GeoArmorRenderer's model scale/flip setup so the
                // correction has a stable, visible direction for other humanoids.
                poseStack.translate(0.0f, -OTHER_HUMANOID_CORRECTION / 16.0f, 0.0f);
            }
        }
    }

    @Override
    public GeoBone getHeadBone(GeoModel<HeadMountedGasBottleItem> model) {
        return model.getBone("root").orElse(null);
    }
}
