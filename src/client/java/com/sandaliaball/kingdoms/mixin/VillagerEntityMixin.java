package com.sandaliaball.kingdoms.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.sandaliaball.kingdoms.entity.HavagerEntity;
import com.sandaliaball.kingdoms.entity.ModEntities;
import com.sandaliaball.kingdoms.world.gen.ModEntitySpawns;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.sensor.GolemLastSeenSensor;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity {
    public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "summonGolem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LargeEntitySpawnHelper;trySpawnAt(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;IIILnet/minecraft/entity/LargeEntitySpawnHelper$Requirements;Z)Ljava/util/Optional;"
            )
    )
    private void injectBeforeSpawn(ServerWorld world, long time, int requiredCount, CallbackInfo ci, @Local Box box, @Local List<VillagerEntity> list) {
        int requiredCount2 = requiredCount * 2;

        boolean hasHavager = world.getNonSpectatingEntities(HavagerEntity.class, box).isEmpty();
        if (!hasHavager && list.size() >= requiredCount2) {
            if (LargeEntitySpawnHelper.trySpawnAt(
                    ModEntities.HAVAGER, SpawnReason.MOB_SUMMONED, world, this.getBlockPos(), 10, 8, 6,
                    ModEntitySpawns.HAVAGER, false
            ).isPresent()) {
                list.forEach(GolemLastSeenSensor::rememberIronGolem);
            }
        }
    }
}
