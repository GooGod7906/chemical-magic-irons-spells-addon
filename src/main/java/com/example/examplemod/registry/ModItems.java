package com.example.examplemod.registry;

import com.example.examplemod.ChemicalMagic;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.createItems(ChemicalMagic.MOD_ID);

    private ModItems() {
    }

    public static <T extends Item> Supplier<T> register(String id, Supplier<T> item) {
        return ITEMS.register(id, item);
    }
}
