package com.sandaliaball.kingdoms.entity.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.equipment.EquipmentModelLoader;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.trim.ArmorTrim;

import static com.sandaliaball.kingdoms.entity.render.HavagerSaddleEntityModel.HAVAGER_SADDLE;

public class HavagerSaddleRenderer extends FeatureRenderer<HavagerEntityRenderState, HavagerEntityModel> {
    private final HavagerSaddleEntityModel model;
    private final SpriteAtlasTexture atlas;

    public HavagerSaddleRenderer(
            EntityRendererFactory.Context context,
            FeatureRendererContext<HavagerEntityRenderState, HavagerEntityModel> renderer
    ) {
        super(renderer);
        this.model = new HavagerSaddleEntityModel(context.getPart(HAVAGER_SADDLE));
        this.atlas = context.getModelManager().getAtlas(TexturedRenderLayers.ARMOR_TRIMS_ATLAS_TEXTURE);
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, HavagerEntityRenderState state, float f, float g) {
        ItemStack itemStack = state.saddleStack;
        EquippableComponent equippableComponent = itemStack.get(DataComponentTypes.EQUIPPABLE);
        if (equippableComponent != null && equippableComponent.assetId().isPresent()) {;
            model.setAngles(state);

            ArmorTrim armorTrim = itemStack.get(DataComponentTypes.TRIM);
            if (armorTrim != null) {
                Sprite sprite = atlas.getSprite(HAVAGER_SADDLE.id());
                VertexConsumer vertexConsumer2 = sprite.getTextureSpecificVertexConsumer(
                        vertexConsumerProvider.getBuffer(TexturedRenderLayers.getArmorTrims(armorTrim.pattern().value().decal()))
                );
                model.render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV);
            }
        }
    }
}