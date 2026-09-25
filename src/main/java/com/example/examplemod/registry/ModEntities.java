package com.example.examplemod.registry;

import com.example.examplemod.ChemicalMagic;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ChemicalMagic.MOD_ID);

    private ModEntities() {
    }

    public static <T extends EntityType<?>> Supplier<T> register(String id, Supplier<T> entityType) {
        return ENTITY_TYPES.register(id, entityType);
    }
}
