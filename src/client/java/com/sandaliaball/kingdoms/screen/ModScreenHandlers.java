package com.sandaliaball.kingdoms.screen;

import com.sandaliaball.kingdoms.KingdomsMod;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<HavagerScreenHandler> HAVAGER_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(
            (syncId, inventory, data) -> new HavagerScreenHandler(syncId, inventory, data.entity(), data.inventory()),
            HavagerCodec.PACKET_CODEC
    );

    public static void registerScreenHandlers() {
        KingdomsMod.LOGGER.info("Registering Screen Handlers for " + KingdomsMod.MOD_ID);

        Registry.register(Registries.SCREEN_HANDLER, Identifier.of(KingdomsMod.MOD_ID, "havager_screen_handler"), HAVAGER_SCREEN_HANDLER);
    }
}
