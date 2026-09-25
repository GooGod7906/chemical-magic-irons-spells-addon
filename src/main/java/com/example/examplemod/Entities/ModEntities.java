package com.example.examplemod.Entities;

import com.example.examplemod.ChemicalMagic;
import com.example.examplemod.Entities.Projectile.HeadMountedGasBottleProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ChemicalMagic.MOD_ID);

    public static final Supplier<EntityType<HeadMountedGasBottleProjectile>> HEAD_MOUNTED_GAS_BOTTLE_PROJECTILE =
            ENTITY_TYPES.register("head_mounted_gas_bottle_projectile", () ->
                    EntityType.Builder.<HeadMountedGasBottleProjectile>of(
                                    HeadMountedGasBottleProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("head_mounted_gas_bottle_projectile"));

    private ModEntities() {
    }
}
