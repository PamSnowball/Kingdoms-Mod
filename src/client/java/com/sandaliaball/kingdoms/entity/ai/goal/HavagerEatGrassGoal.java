package com.sandaliaball.kingdoms.entity.ai.goal;

import com.sandaliaball.kingdoms.entity.HavagerEntity;
import com.sandaliaball.kingdoms.util.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.EnumSet;
import java.util.function.Predicate;

public class HavagerEatGrassGoal extends Goal {
    public static final int EAT_GRASS_TIMER = 100;

    private static final Predicate<BlockState> EDIBLE_PREDICATE = state -> state.isIn(BlockTags.EDIBLE_FOR_SHEEP);
    private final HavagerEntity mob;
    private final World world;
    private int timer;

    public HavagerEatGrassGoal(HavagerEntity mob) {
        this.mob = mob;
        this.world = mob.getWorld();
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));

        EatGrassGoal e;
    }

    @Override
    public boolean canStart() {
        if (this.mob.getRandom().nextInt(1000) != 0) {
            return false;
        } else {
            BlockPos blockPos = this.mob.getBlockPos();
            return EDIBLE_PREDICATE.test(this.world.getBlockState(blockPos)) ||
                    this.world.getBlockState(blockPos.down()).isOf(Blocks.GRASS_BLOCK);
        }
    }

    @Override
    public void start() {
        this.timer = this.getTickCount(EAT_GRASS_TIMER);
        this.world.sendEntityStatus(this.mob, EntityStatuses.SET_SHEEP_EAT_GRASS_TIMER_OR_PRIME_TNT_MINECART);
        this.mob.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.timer = 0;
    }

    @Override
    public boolean shouldContinue() {
        return this.timer > 0;
    }

    public int getTimer() {
        return this.timer;
    }

    @Override
    public void tick() {
        this.timer = Math.max(0, this.timer - 1);
        if (this.timer == this.getTickCount(4)) {
            BlockPos blockPos = this.mob.getBlockPos();
            boolean griefing = castToServerWorld(this.world).getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
            if (EDIBLE_PREDICATE.test(this.world.getBlockState(blockPos))) {
                if (griefing) MathUtils.iterateSurroundings(blockPos, 0.5, (pos) -> world.breakBlock(pos, false));
                this.mob.onEatingGrass();
            } else {
                BlockPos blockPos2 = blockPos.down();
                if (this.world.getBlockState(blockPos2).isOf(Blocks.GRASS_BLOCK)) {
                    if (griefing) MathUtils.iterateSurroundings(blockPos2, 0.75, (pos) -> {
                        this.world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(Blocks.GRASS_BLOCK.getDefaultState()));
                        this.world.setBlockState(pos, Blocks.DIRT.getDefaultState(), Block.NOTIFY_LISTENERS);
                    });
                    this.mob.onEatingGrass();
                }
            }
        }
    }
}