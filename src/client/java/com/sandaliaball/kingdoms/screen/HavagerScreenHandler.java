package com.sandaliaball.kingdoms.screen;

import com.sandaliaball.kingdoms.entity.HavagerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class HavagerScreenHandler extends ScreenHandler {
    private static final Identifier EMPTY_SADDLE_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/saddle");

    final Inventory inventory;
    final HavagerEntity entity;

    private static class SaddleSlot extends Slot {
        private final MobEntity entity;

        public SaddleSlot(HavagerEntity entity, int index, int x, int y) {
            super(entity.createEquipmentInventory(EquipmentSlot.SADDLE), index, x, y);

            this.entity = entity;
        }

        @Override
        public void setStack(ItemStack stack, ItemStack previousStack) {
            entity.onEquipStack(EquipmentSlot.SADDLE, previousStack, stack);
            super.setStack(stack, previousStack);
        }

        @Override
        public int getMaxItemCount() {
            return 1;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return entity.canEquip(stack, EquipmentSlot.SADDLE);
        }

        @Override
        public boolean isEnabled() {
            return entity.canUseSlot(EquipmentSlot.SADDLE);
        }

        @Override
        public boolean canTakeItems(PlayerEntity playerEntity) {
            ItemStack itemStack = this.getStack();
            return super.canTakeItems(playerEntity) && (itemStack.isEmpty() || playerEntity.isCreative());
        }

        @Override
        public Identifier getBackgroundSprite() {
            return EMPTY_SADDLE_SLOT_TEXTURE;
        }
    }

    public HavagerScreenHandler(int syncId, PlayerInventory playerInventory, HavagerEntity entity, Inventory inventory) {
        super(ModScreenHandlers.HAVAGER_SCREEN_HANDLER, syncId);

        //Creates an inventory if it wasn't initialized
        if (inventory == null || inventory.isEmpty()) {
            entity.onChestedStatusChanged();
            inventory = entity.getItems();
        }

        this.inventory = inventory;
        this.entity = entity;

        this.inventory.onOpen(playerInventory.player);

        this.addSlot(new SaddleSlot(entity, 0, 8, 18));

        if (entity.getChests() > 0) {
            addSlots(3, 5, 0, 0, 80, 18, 0);
            if (entity.getChests() > 1) {
                addSlots(3, 8, 3, 0, 26, 18, 15);
            }
        }
        this.addPlayerSlots(playerInventory, 8, 138);
    }

    private void addSlots(int rows, int columns, int startRow, int startColumn, int startX, int startY, int slots) {
        for (int row = 0; row < rows; row++) for (int column = 0; column < columns; column++) {
            this.addSlot(new Slot(inventory, slots + column + row * columns, startX + (column + startColumn) * 18, startY + (row + startRow) * 18));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return !this.entity.areInventoriesDifferent(this.inventory)
                && this.inventory.canPlayerUse(player)
                && this.entity.isAlive()
                && player.canInteractWithEntity(this.entity, 4.0);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            int i = 1 + this.inventory.size();
            if (invSlot < i) {
                if (!this.insertItem(originalStack, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).canInsert(originalStack) && !this.getSlot(0).hasStack()) {
                if (!this.insertItem(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.inventory.isEmpty() || !this.insertItem(originalStack, 0, i, false)) {
                int j = i + 27;
                int l = j + 9;
                if (invSlot >= j && invSlot < l) {
                    if (!this.insertItem(originalStack, i, j, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (invSlot < j) {
                    if (!this.insertItem(originalStack, j, l, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(originalStack, j, j, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }
}
