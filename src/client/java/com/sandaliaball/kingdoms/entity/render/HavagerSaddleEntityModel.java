package com.sandaliaball.kingdoms.entity.render;

import com.sandaliaball.kingdoms.entity.ModEntities;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class HavagerSaddleEntityModel extends HavagerEntityModel {
    public static final EntityModelLayer HAVAGER_SADDLE = new EntityModelLayer(ModEntities.HAVAGER_ID, "saddle");

    final ModelPart saddle;

    public HavagerSaddleEntityModel(ModelPart root) {
        super(root);
        this.saddle = this.body.getChild("rotation").getChild("saddle");
    }

    @Override
    public void setAngles(HavagerEntityRenderState state) {
        super.setAngles(state);
        saddle.visible = state.hasPassengers;
    }
}
