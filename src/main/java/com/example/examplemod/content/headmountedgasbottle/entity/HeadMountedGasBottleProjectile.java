package com.example.examplemod.content.headmountedgasbottle.entity;

import com.example.examplemod.content.headmountedgasbottle.HeadMountedGasBottleContent;
import com.example.examplemod.content.headmountedgasbottle.HeadMountedGasBottleSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HeadMountedGasBottleProjectile extends ThrowableItemProjectile implements GeoEntity {
    private static final EntityDataAccessor<Integer> SPELL_LEVEL =
            SynchedEntityData.defineId(HeadMountedGasBottleProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> RANDOM_ROLL =
            SynchedEntityData.defineId(HeadMountedGasBottleProjectile.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public HeadMountedGasBottleProjectile(EntityType<? extends HeadMountedGasBottleProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public HeadMountedGasBottleProjectile(EntityType<? extends HeadMountedGasBottleProjectile> entityType,
                                          LivingEntity shooter, Level level) {
        super(entityType, shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return HeadMountedGasBottleContent.ITEM.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setSpellLevel(int spellLevel) {
        getEntityData().set(SPELL_LEVEL, Math.max(1, spellLevel));
    }

    public void setRandomRoll(float randomRoll) {
        getEntityData().set(RANDOM_ROLL, randomRoll);
    }

    public float getRandomRoll() {
        return getEntityData().get(RANDOM_ROLL);
    }

    private int getSpellLevel() {
        return getEntityData().get(SPELL_LEVEL);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.015;
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 velocity = getDeltaMovement();
        if (velocity.lengthSqr() > 1.0E-7) {
            setYRot((float) (Mth.atan2(velocity.x, velocity.z) * Mth.RAD_TO_DEG));
            setXRot((float) (Mth.atan2(velocity.y, velocity.horizontalDistance()) * Mth.RAD_TO_DEG));
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPELL_LEVEL, 1);
        builder.define(RANDOM_ROLL, 0.0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("SpellLevel", getSpellLevel());
        compound.putFloat("RandomRoll", getRandomRoll());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setSpellLevel(compound.getInt("SpellLevel"));
        setRandomRoll(compound.getFloat("RandomRoll"));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!(level() instanceof ServerLevel serverLevel)
                || !(result.getEntity() instanceof LivingEntity target)
                || !(getOwner() instanceof LivingEntity caster)
                || target == caster) {
            discard();
            return;
        }

        HeadMountedGasBottleSpell spell = (HeadMountedGasBottleSpell) HeadMountedGasBottleContent.SPELL.get();
        spell.applyHitEffects(serverLevel, this, caster, target, getSpellLevel());
        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        discard();
    }
}
