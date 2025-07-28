package com.sandaliaball.kingdoms.entity.render;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.item.ItemStack;

public class HavagerEntityRenderState extends LivingEntityRenderState {
    public float headAngle;
    public boolean leftChest = false;
    public boolean rightChest = false;

    public ItemStack saddleStack = ItemStack.EMPTY;
    public boolean hasPassengers;
}
