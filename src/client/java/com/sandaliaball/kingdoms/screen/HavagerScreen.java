package com.sandaliaball.kingdoms.screen;

import com.sandaliaball.kingdoms.KingdomsMod;
import com.sandaliaball.kingdoms.entity.HavagerEntity;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class HavagerScreen extends HandledScreen<HavagerScreenHandler> {
    private static final Identifier SLOT_TEXTURE = Identifier.ofVanilla("container/slot");

    private static final Identifier CHEST_SLOTS_TEXTURE_1 = Identifier.of(KingdomsMod.MOD_ID, "textures/gui/sprites/container/base_havager_chest_slots.png");
    private static final Identifier CHEST_SLOTS_TEXTURE_2 = Identifier.of(KingdomsMod.MOD_ID, "textures/gui/sprites/container/havager_chest_slots.png");

    private static final Identifier TEXTURE = Identifier.of(KingdomsMod.MOD_ID, "textures/gui/havager_gui.png");

    private final HavagerEntity entity;
    private float mouseX;
    private float mouseY;

    public HavagerScreen(HavagerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        this.backgroundHeight += 18 * 3;

        this.entity = handler.entity;
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        int i = (width - backgroundWidth) / 2;
        int j = (height - backgroundHeight) / 2;

        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0.0F, 0.0F, backgroundWidth, backgroundHeight, 256, 256);
        if (entity.getChests() > 0) {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, CHEST_SLOTS_TEXTURE_1, 90, 54, 0, 0, i + 79, j + 17, 18 * 5, 18 * 3);
            if (entity.getChests() > 1) {
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, CHEST_SLOTS_TEXTURE_2, 144, 54, 0, 0, i + 25, j + 71, 18 * 8, 18 * 3);
            }
        }

        if (this.entity.canUseSlot(EquipmentSlot.SADDLE) && this.entity.getType().isIn(EntityTypeTags.CAN_EQUIP_SADDLE)) {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, SLOT_TEXTURE, i + 7, j + 17, 18, 18);
        }

        InventoryScreen.drawEntity(context, i + 26, j + 18, i + 78, j + 70, 17, 0.2F, this.mouseX, this.mouseY, this.entity);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.render(context, mouseX, mouseY, deltaTicks);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
