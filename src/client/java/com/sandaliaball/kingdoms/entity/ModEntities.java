package com.sandaliaball.kingdoms.entity;

import com.sandaliaball.kingdoms.KingdomsMod;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.mixin.loot.LootTableAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {
    private static final RegistryKey<EntityType<?>> HAVAGER_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(KingdomsMod.MOD_ID, "havager"));

    public static final Identifier HAVAGER_ID = Identifier.of(KingdomsMod.MOD_ID, "havager");
    public static final EntityType<HavagerEntity> HAVAGER =
            EntityType.Builder.create(HavagerEntity::new, SpawnGroup.MISC).dimensions(2F, 2F).build(HAVAGER_KEY);

    public static void registerModEntities() {
        KingdomsMod.LOGGER.info("Registering Mod Entities for " + KingdomsMod.MOD_ID);

        Registry.register(Registries.ENTITY_TYPE, HAVAGER_ID, HAVAGER);
    }
}
