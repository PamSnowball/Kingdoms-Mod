package com.sandaliaball.kingdoms;

import com.sandaliaball.kingdoms.entity.HavagerEntity;
import com.sandaliaball.kingdoms.entity.ModEntities;
import com.sandaliaball.kingdoms.screen.ModScreenHandlers;
import com.sandaliaball.kingdoms.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KingdomsMod implements ModInitializer {
    public static final String MOD_ID = "kingdoms";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModEntities.registerModEntities();
        ModScreenHandlers.registerScreenHandlers();

        ModWorldGeneration.generateWorldGen();

        FabricDefaultAttributeRegistry.register(ModEntities.HAVAGER, HavagerEntity.createMobAttributes());
    }
}
