package com.sandaliaball.kingdoms;

import com.sandaliaball.kingdoms.entity.ModEntities;
import com.sandaliaball.kingdoms.entity.render.HavagerEntityModel;
import com.sandaliaball.kingdoms.entity.render.HavagerEntityRenderer;
import com.sandaliaball.kingdoms.entity.render.HavagerSaddleEntityModel;
import com.sandaliaball.kingdoms.screen.HavagerScreen;
import com.sandaliaball.kingdoms.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class KingdomsClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(HavagerEntityModel.HAVAGER, HavagerEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HavagerSaddleEntityModel.HAVAGER_SADDLE, HavagerEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.HAVAGER, HavagerEntityRenderer::new);

        HandledScreens.register(ModScreenHandlers.HAVAGER_SCREEN_HANDLER, HavagerScreen::new);
    }
}
