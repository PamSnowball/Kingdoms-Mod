package com.sandaliaball.kingdoms.entity.render;

import com.sandaliaball.kingdoms.KingdomsMod;
import com.sandaliaball.kingdoms.entity.HavagerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class HavagerEntityRenderer extends MobEntityRenderer<HavagerEntity, HavagerEntityRenderState, HavagerEntityModel> {
    public HavagerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new HavagerEntityModel(context.getPart(HavagerEntityModel.HAVAGER)), 0.95F);
        this.addFeature(new HavagerSaddleRenderer(context, this));
    }

    @Override
    public Identifier getTexture(HavagerEntityRenderState state) {
        return Identifier.of(KingdomsMod.MOD_ID, "textures/entity/havager/havager.png");
    }

    @Override
    public HavagerEntityRenderState createRenderState() {
        return new HavagerEntityRenderState();
    }

    @Override
    public void updateRenderState(HavagerEntity entity, HavagerEntityRenderState state, float f) {
        super.updateRenderState(entity, state, f);
        state.headAngle = entity.getHeadAngle(f);
        state.rightChest = entity.hasRightChest();
        state.leftChest = entity.hasLeftChest();

        state.saddleStack = entity.getEquippedStack(EquipmentSlot.SADDLE).copy();
        state.hasPassengers = entity.hasPassengers();
    }
}

