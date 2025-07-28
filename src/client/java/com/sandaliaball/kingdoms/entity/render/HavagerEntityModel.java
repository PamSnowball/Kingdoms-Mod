package com.sandaliaball.kingdoms.entity.render;

import com.sandaliaball.kingdoms.entity.ModEntities;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.util.math.MathHelper;

public class HavagerEntityModel extends EntityModel<HavagerEntityRenderState> {
    public static final EntityModelLayer HAVAGER = new EntityModelLayer(ModEntities.HAVAGER_ID, "main");

    final ModelPart havager;
    final ModelPart body;
    final ModelPart rightChest;
    final ModelPart leftChest;
    final ModelPart rightHindLeg;
    final ModelPart leftHindLeg;
    final ModelPart rightFrontLeg;
    final ModelPart leftFrontLeg;
    final ModelPart neck;
    final ModelPart head;
    final ModelPart horns;

    public HavagerEntityModel(ModelPart root) {
        super(root);
        this.havager = root.getChild("havager");
        this.body = this.havager.getChild("body");
        this.rightChest = this.body.getChild("right_chest");
        this.leftChest = this.body.getChild("left_chest");
        this.rightHindLeg = this.havager.getChild("right_hind_leg");
        this.leftHindLeg = this.havager.getChild("left_hind_leg");
        this.rightFrontLeg = this.havager.getChild("right_front_leg");
        this.leftFrontLeg = this.havager.getChild("left_front_leg");
        this.neck = this.havager.getChild("neck");
        this.head = this.havager.getChild("head");
        this.horns = this.head.getChild("horns");

        SheepEntityModel e; //Eat Grass
        RavagerEntityModel f; //Walk
        DonkeyEntityModel g; //Chest
        HorseEntityModel h; //Saddle
        HorseSaddleEntityModel i; //Saddle

    }

    protected static ModelData getHavager() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData havager = modelPartData.addChild("havager", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 24.0F, 0.0F));

        ModelPartData body = havager.addChild("body", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 0.0F, 0.0F));

        ModelPartData rotation = body.addChild(
                "rotation", ModelPartBuilder.create()
                .uv(0, 30).cuboid(-6.5F, -7.0F, -10.0F, 13.0F, 14.0F, 20.0F, new Dilation(0.0F))
                .uv(0, 65).cuboid(-5.5F, 7.0F, -10.0F, 11.0F, 12.0F, 18.0F, new Dilation(0.0F)),
                ModelTransform.of(0.0F, -19.0F, 1.5F, 1.5708F, 0.0F, 0.0F)
        );
        rotation.addChild(
                "saddle", ModelPartBuilder.create()
                        .uv(33, 97).cuboid(-7.0F, -4.5F, -11.0F, 14.0F, 9.0F, 22.0F, new Dilation(0.0F)),
                ModelTransform.origin(0.0F, 3.5F, 0.0F)
        );

        body.addChild(
                "right_chest", ModelPartBuilder.create()
                        .uv(68, 41).cuboid(-7.0F, -7.0F, -2.5F, 14.0F, 14.0F, 4.0F, new Dilation(0.0F))
                        .uv(104, 41).cuboid(-1.0F, -4.0F, -3.5F, 2.0F, 4.0F, 1.0F, new Dilation(0.0F)),
                ModelTransform.of(-7.0F, -20.0F, 7.5F, 0.0F, 1.5708F, 0.0F)
        );

        body.addChild(
                "left_chest", ModelPartBuilder.create()
                .uv(68, 41).cuboid(-7.0F, -7.0F, -2.5F, 14.0F, 14.0F, 4.0F, new Dilation(0.0F))
                .uv(104, 41).cuboid(-1.0F, -4.0F, -3.5F, 2.0F, 4.0F, 1.0F, new Dilation(0.0F)),
                ModelTransform.of(7.0F, -20.0F, 7.5F, 0.0F, -1.5708F, 0.0F)
        );

        havager.addChild("right_hind_leg", ModelPartBuilder.create().uv(56, 4).cuboid(-3.5F, 0.0F, -3.5F, 7.0F, 30.0F, 7.0F, new Dilation(0.0F)), ModelTransform.origin(-7.5F, -30.0F, 18.5F));
        havager.addChild("left_hind_leg", ModelPartBuilder.create().uv(56, 4).mirrored().cuboid(-3.5F, 0.0F, -3.5F, 7.0F, 30.0F, 7.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.origin(7.5F, -30.0F, 18.5F));
        havager.addChild("right_front_leg", ModelPartBuilder.create().uv(84, 4).cuboid(-3.5F, 0.0F, -3.5F, 7.0F, 30.0F, 7.0F, new Dilation(0.0F)), ModelTransform.origin(-7.5F, -30.0F, -2.5F));
        havager.addChild("left_front_leg", ModelPartBuilder.create().uv(84, 4).mirrored().cuboid(-3.5F, 0.0F, -3.5F, 7.0F, 30.0F, 7.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.origin(7.5F, -30.0F, -2.5F));

        havager.addChild("neck", ModelPartBuilder.create().uv(57, 59).cuboid(-5.5F, -6.0F, -8.0F, 11.0F, 12.0F, 16.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -21.0F, -7.0F));

        ModelPartData head = havager.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, -12.0F, -14.5F, 14.0F, 16.0F, 14.0F, new Dilation(0.0F))
                .uv(42, 0).cuboid(-2.0F, -2.0F, -18.5F, 4.0F, 7.0F, 4.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -16.0F, -7.0F));

        head.addChild(
                "horns", ModelPartBuilder.create()
                .uv(0, 30).cuboid(-9.0F, -14.0F, -3.0F, 2.0F, 14.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 30).cuboid(7.0F, -14.0F, -3.0F, 2.0F, 14.0F, 4.0F, new Dilation(0.0F)),
                ModelTransform.of(0.0F, -6.0F, -7.0F, 2.0944F, 0.0F, 0.0F)
        );

        return modelData;
    }

    public static TexturedModelData getTexturedModelData() {
        return TexturedModelData.of(getHavager(), 128, 128);
    }

    public void setAngles(HavagerEntityRenderState state) {
        super.setAngles(state);

        float hr = (float) (Math.PI / 180.0);
        this.head.pitch = state.headAngle * hr;
        this.head.yaw = state.relativeHeadYaw * hr;

        float hx = state.limbSwingAnimationProgress;
        float jx = 0.4F * state.limbSwingAmplitude;
        float a = MathHelper.cos(hx * 0.6662F);
        float b = MathHelper.cos(hx * 0.6662F + (float) Math.PI);
        this.rightHindLeg.pitch = a * jx;
        this.leftHindLeg.pitch = b * jx;
        this.rightFrontLeg.pitch = b * jx;
        this.leftFrontLeg.pitch = a * jx;

        this.leftChest.visible = state.leftChest;
        this.rightChest.visible = state.rightChest;
    }
}