package com.sandaliaball.kingdoms.screen;

import com.sandaliaball.kingdoms.entity.HavagerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record HavagerCodec(HavagerEntity entity, Inventory inventory) {
    public static final PacketCodec<PacketByteBuf, HavagerCodec> PACKET_CODEC = PacketCodec.of(
            (data, buf) -> {
                buf.writeInt(data.entity.getId());
                buf.writeVarInt(data.entity.getChests());
            },
            buf -> {
                int entityId = buf.readInt();
                int chests = buf.readVarInt();

                System.out.println("EntityId = " + entityId);
                System.out.println("Chests = " + chests);

                Inventory inventory = new SimpleInventory(HavagerEntity.getInventorySize(chests));
                Entity entity = MinecraftClient.getInstance().world.getEntityById(entityId);

                if (!(entity instanceof HavagerEntity havager)) {
                    throw new IllegalStateException("HavagerEntity não encontrado no cliente para id " + entityId);
                }

                return new HavagerCodec(havager, inventory);
            }
    );
}
