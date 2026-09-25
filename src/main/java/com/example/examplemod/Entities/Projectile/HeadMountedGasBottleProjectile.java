package com.example.examplemod.Entities.Projectile;

import com.example.examplemod.Items.ModItems;
import com.example.examplemod.Spells.HeadMountedGasBottleSpell;
import com.example.examplemod.Spells.Spells;
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
        return ModItems.HEAD_MOUNTED_GAS_BOTTLE.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setSpellLevel(int spellLevel) {
        this.getEntityData().set(SPELL_LEVEL, Math.max(1, spellLevel));
    }

    public void setRandomRoll(float randomRoll) {
        this.getEntityData().set(RANDOM_ROLL, randomRoll);
    }

    public float getRandomRoll() {
        return this.getEntityData().get(RANDOM_ROLL);
    }

    private int getSpellLevel() {
        return this.getEntityData().get(SPELL_LEVEL);
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
        if (!(this.level() instanceof ServerLevel serverLevel)
                || !(result.getEntity() instanceof LivingEntity target)
                || !(this.getOwner() instanceof LivingEntity caster)
                || target == caster) {
            discard();
            return;
        }

        HeadMountedGasBottleSpell spell = (HeadMountedGasBottleSpell) Spells.HEAD_MOUNTED_GAS_BOTTLE_SPELL.get();
        spell.applyHitEffects(serverLevel, this, caster, target, getSpellLevel());
        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        discard();
    }
}
