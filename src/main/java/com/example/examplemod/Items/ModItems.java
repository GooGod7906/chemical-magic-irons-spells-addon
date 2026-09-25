package com.example.examplemod.Items;

import com.example.examplemod.ChemicalMagic;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.createItems(ChemicalMagic.MOD_ID);

    public static final Supplier<HeadMountedGasBottleItem> HEAD_MOUNTED_GAS_BOTTLE =
            ITEMS.register("head_mounted_gas_bottle", () ->
                    new HeadMountedGasBottleItem(new Item.Properties().stacksTo(1)));

    private ModItems() {
    }
}
