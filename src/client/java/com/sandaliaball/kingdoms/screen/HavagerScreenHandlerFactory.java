package com.sandaliaball.kingdoms.screen;

import com.sandaliaball.kingdoms.entity.HavagerEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public record HavagerScreenHandlerFactory(HavagerEntity entity) implements ExtendedScreenHandlerFactory<HavagerCodec> {
    @Override
    public HavagerCodec getScreenOpeningData(ServerPlayerEntity player) {
        return new HavagerCodec(entity, entity.getItems());
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("text.Kingdoms.entity.havager");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new HavagerScreenHandler(syncId, inventory, entity, entity.getItems());
    }
}
