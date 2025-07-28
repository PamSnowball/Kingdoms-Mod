package com.sandaliaball.kingdoms.world.gen;

import com.sandaliaball.kingdoms.entity.ModEntities;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;

public class ModEntitySpawns {
    public static final LargeEntitySpawnHelper.Requirements HAVAGER = (world, pos, state, abovePos, aboveState) -> {
        if (!state.isOf(Blocks.COBWEB) && !state.isOf(Blocks.CACTUS) && !state.isOf(Blocks.GLASS_PANE) && !(state.getBlock() instanceof StainedGlassPaneBlock) && !(state.getBlock() instanceof StainedGlassBlock) && !(state.getBlock() instanceof LeavesBlock) && !state.isOf(Blocks.CONDUIT) && !state.isOf(Blocks.ICE) && !state.isOf(Blocks.TNT) && !state.isOf(Blocks.GLOWSTONE) && !state.isOf(Blocks.BEACON) && !state.isOf(Blocks.SEA_LANTERN) && !state.isOf(Blocks.FROSTED_ICE) && !state.isOf(Blocks.TINTED_GLASS) && !state.isOf(Blocks.GLASS)) {
            return (aboveState.isAir() || aboveState.isLiquid()) && (state.isSolid() || state.isOf(Blocks.POWDER_SNOW));
        } else {
            return false;
        }
    };

    public static void addSpawns() {
        SpawnRestriction.register(ModEntities.HAVAGER, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
    }
}
