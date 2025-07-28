package com.sandaliaball.kingdoms.entity;

import com.sandaliaball.kingdoms.entity.ai.goal.HavagerEatGrassGoal;
import com.sandaliaball.kingdoms.screen.HavagerScreenHandlerFactory;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.inventory.StackWithSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;

public class HavagerEntity extends PathAwareEntity implements RideableInventory {
    private static final TrackedData<Boolean> LEFT_CHEST = DataTracker.registerData(HavagerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> RIGHT_CHEST = DataTracker.registerData(HavagerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private SimpleInventory items = null;
    private int eatGrassTimer;
    private HavagerEatGrassGoal eatGrassGoal;
    private int chests = 0;

    public HavagerEntity(EntityType<? extends HavagerEntity> entityType, World world) {
        super(entityType, world);

        //From Ravager
        this.setPathfindingPenalty(PathNodeType.LEAVES, 0.0F);

        PillagerEntity e0; //Compare
        VillagerEntity e1; //Compare
        RavagerEntity e2; //Compare

        SheepEntity e6; //Eat Grass X
        CamelEntity e3; //Rideable Jump
        IronGolemEntity e5; //Village Spawn

        DonkeyEntity e4; //Rideable - Chest
    }

    @Override
    protected void initGoals() {
        this.eatGrassGoal = new HavagerEatGrassGoal(this);
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.0));
        this.goalSelector.add(5, this.eatGrassGoal);
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.4));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));
        this.goalSelector.add(7, new LookAtEntityGoal(this, MobEntity.class, 4.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected void mobTick(ServerWorld world) {
        this.eatGrassTimer = this.eatGrassGoal.getTimer();
        super.mobTick(world);
    }

    @Override
    public void tickMovement() {
        if (this.getWorld().isClient) this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
        super.tickMovement();
        if (this.isAlive()) {
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                    this.heal(1.0F);
                }
            }

            EntityAttributeInstance speed = this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
            speed.setBaseValue(this.isImmobile() ? 0.0 : MathHelper.lerp(0.1, speed.getBaseValue(), this.getTarget() != null ? 0.35 : 0.3));
        }
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 80.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.75)
                .add(EntityAttributes.STEP_HEIGHT, 1.0);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(RIGHT_CHEST, false);
        builder.add(LEFT_CHEST, false);
    }

    @Override
    public boolean canUseQuadLeashAttachmentPoint() {
        return true;
    }

    private boolean hasChest() {
        return hasRightChest() || hasLeftChest();
    }

    public boolean hasRightChest() {
        return this.dataTracker.get(RIGHT_CHEST);
    }

    public boolean hasLeftChest() {
        return this.dataTracker.get(LEFT_CHEST);
    }

    public void setHasChest(Entity entity, boolean hasChest) {
        if (hasChest()) {
            if (!hasLeftChest()) setHasLeftChest(hasChest);
            if (!hasRightChest()) setHasRightChest(hasChest);
        } else {
            Vec3d entityPos = this.getPos();
            Vec3d playerPos = entity.getPos();
            Vec3d toPlayer = playerPos.subtract(entityPos).normalize();

            float yaw = entity.getYaw(); // degrees
            double yawRad = Math.toRadians(yaw);
            Vec3d entityForward = new Vec3d(-Math.sin(yawRad), 0, Math.cos(yawRad)).normalize();

            double crossY = entityForward.crossProduct(toPlayer).y;

            if (crossY > 0) {
                setHasLeftChest(hasChest);
            } else {
                setHasRightChest(hasChest);
            }
            onChestedStatusChanged();
        }
    }

    public void setHasRightChest(boolean hasChest) {
        this.dataTracker.set(RIGHT_CHEST, hasChest);
    }

    public void setHasLeftChest(boolean hasChest) {
        this.dataTracker.set(LEFT_CHEST, hasChest);
    }

    @Override
    protected void dropInventory(ServerWorld world) {
        super.dropInventory(world);
        if (this.hasLeftChest()) {
            this.dropItem(world, Blocks.CHEST);
            this.setHasLeftChest(false);
        }
        if (this.hasRightChest()) {
            this.dropItem(world, Blocks.CHEST);
            this.setHasRightChest(false);
        }
        if (this.items != null) {
            for (int i = 0; i < this.items.size(); i++) {
                ItemStack itemStack = this.items.getStack(i);
                if (!itemStack.isEmpty() && !EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP)) {
                    this.dropStack(world, itemStack);
                }
            }
        }
    }

    public static int getInventorySize(int chests) {
        return switch (chests) {
            case 1 -> 15;
            case 2 -> 39;
            default -> 0;
        };
    }

    public void onChestedStatusChanged() {
        updateChests();
        SimpleInventory simpleInventory = this.items;
        this.items = new SimpleInventory(getInventorySize(chests));
        if (simpleInventory != null) {
            int i = Math.min(simpleInventory.size(), this.items.size());

            for (int j = 0; j < i; j++) {
                ItemStack itemStack = simpleInventory.getStack(j);
                if (!itemStack.isEmpty()) {
                    this.items.setStack(j, itemStack.copy());
                }
            }
        }
    }

    @Override
    protected RegistryEntry<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, EquippableComponent equippableComponent) {
        return slot == EquipmentSlot.SADDLE ? SoundEvents.ENTITY_HORSE_SADDLE : super.getEquipSound(slot, stack, equippableComponent);
    }

    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        return slot != EquipmentSlot.SADDLE ? super.canUseSlot(slot) : this.isAlive();
    }

    @Override
    protected boolean canDispenserEquipSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.BODY || slot == EquipmentSlot.SADDLE || super.canDispenserEquipSlot(slot);
    }

    @Override
    public boolean isPushable() {
        return !this.hasPassengers();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.isLiquid()) {
            BlockState blockState = this.getWorld().getBlockState(pos.up());
            BlockSoundGroup blockSoundGroup = state.getSoundGroup();
            if (blockState.isOf(Blocks.SNOW)) {
                blockSoundGroup = blockState.getSoundGroup();
            }

            if (this.isWooden(blockSoundGroup)) {
                this.playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
            } else {
                this.playSound(SoundEvents.ENTITY_HORSE_STEP, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
            }
        }
    }

    private boolean isWooden(BlockSoundGroup soundGroup) {
        return soundGroup == BlockSoundGroup.WOOD
                || soundGroup == BlockSoundGroup.NETHER_WOOD
                || soundGroup == BlockSoundGroup.NETHER_STEM
                || soundGroup == BlockSoundGroup.CHERRY_WOOD
                || soundGroup == BlockSoundGroup.BAMBOO_WOOD;
    }

    @Override
    public int getLimitPerChunk() {
        return 3;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        if (!this.getWorld().isClient && (!this.hasPassengers() || this.hasPassenger(player))) {
            player.openHandledScreen(new HavagerScreenHandlerFactory(this));
        }
    }

    protected void putPlayerOnBack(PlayerEntity player) {
        if (!this.getWorld().isClient) {
            player.setYaw(this.getYaw());
            player.setPitch(this.getPitch());
            player.startRiding(this);
        }
    }

    @Override
    public boolean isImmobile() {
        return super.isImmobile() && this.hasPassengers() && this.hasSaddleEquipped() || this.eatGrassTimer > 0;
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putBoolean("LeftChestedHavager", this.hasLeftChest());
        view.putBoolean("RightChestedHavager", this.hasRightChest());
        if (this.hasChest()) {
            WriteView.ListAppender<StackWithSlot> listAppender = view.getListAppender("Items", StackWithSlot.CODEC);

            for (int i = 0; i < this.items.size(); i++) {
                ItemStack itemStack = this.items.getStack(i);
                if (!itemStack.isEmpty()) {
                    listAppender.add(new StackWithSlot(i, itemStack));
                }
            }
        }
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.setHasLeftChest(view.getBoolean("LeftChestedHavager", false));
        this.setHasRightChest(view.getBoolean("RightChestedHavager", false));
        this.updateChests();
        this.onChestedStatusChanged();
        if (this.hasChest()) {
            for (StackWithSlot stackWithSlot : view.getTypedListView("Items", StackWithSlot.CODEC)) {
                if (stackWithSlot.isValidSlot(this.items.size())) {
                    this.items.setStack(stackWithSlot.slot(), stackWithSlot.stack());
                }
            }
        }
    }

    public void updateChests() {
        int i = 0;
        if (hasRightChest()) i++;
        if (hasLeftChest()) i++;
        this.chests = i;
    }

    @Override
    public StackReference getStackReference(int mappedIndex) {
        if (mappedIndex == 499) {
            return new StackReference() {
                @Override
                public ItemStack get() {
                    return hasChest() ? new ItemStack(Items.CHEST, chests) : ItemStack.EMPTY;
                }

                @Override
                public boolean set(ItemStack stack) {
                    if (stack.isEmpty()) {
                        if (hasLeftChest()) setHasLeftChest(false);
                        if (hasRightChest()) setHasRightChest(false);
                        if (hasChest()) onChestedStatusChanged();

                        return true;
                    } else if (stack.isOf(Items.CHEST)) {
                        if (!hasChest()) {
                            setHasChest(stack.getHolder(), true);
                            onChestedStatusChanged();
                        }

                        return true;
                    } else {
                        return false;
                    }
                }
            };
        } else {
            int i = mappedIndex - 500;
            if (i >= 0 && i < this.items.size()) {
                return StackReference.of(this.items, i);
            } else {
                return super.getStackReference(mappedIndex);
            }
        }
    }


    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.hasSaddleEquipped() && this.getFirstPassenger() instanceof PlayerEntity playerEntity
                ? playerEntity : super.getControllingPassenger();
    }


    @Nullable
    private Vec3d locateSafeDismountingPos(Vec3d offset, LivingEntity passenger) {
        double d = this.getX() + offset.x;
        double e = this.getBoundingBox().minY;
        double f = this.getZ() + offset.z;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (EntityPose entityPose : passenger.getPoses()) {
            mutable.set(d, e, f);
            double g = this.getBoundingBox().maxY + 0.75;

            do {
                double h = this.getWorld().getDismountHeight(mutable);
                if (mutable.getY() + h > g) {
                    break;
                }

                if (Dismounting.canDismountInBlock(h)) {
                    Box box = passenger.getBoundingBox(entityPose);
                    Vec3d vec3d = new Vec3d(d, mutable.getY() + h, f);
                    if (Dismounting.canPlaceEntityAt(this.getWorld(), passenger, box.offset(vec3d))) {
                        passenger.setPose(entityPose);
                        return vec3d;
                    }
                }

                mutable.move(Direction.UP);
            } while (!(mutable.getY() < g));
        }

        return null;
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Vec3d vec3d = getPassengerDismountOffset(this.getWidth(), passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.RIGHT ? 90.0F : -90.0F));
        Vec3d vec3d2 = this.locateSafeDismountingPos(vec3d, passenger);
        if (vec3d2 != null) {
            return vec3d2;
        } else {
            Vec3d vec3d3 = getPassengerDismountOffset(this.getWidth(), passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.LEFT ? 90.0F : -90.0F));
            Vec3d vec3d4 = this.locateSafeDismountingPos(vec3d3, passenger);
            return vec3d4 != null ? vec3d4 : this.getPos();
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.hasPassengers()) {
            return super.interactMob(player, hand);
        }
        if (player.shouldCancelInteraction()) {
            this.openInventory(player);
            return ActionResult.SUCCESS;
        }

        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isEmpty()) {
            if (this.chests != 2 && itemStack.isOf(Items.CHEST)) {
                this.setHasChest(player, true);
                this.playAddChestSound();
                itemStack.decrementUnlessCreative(1, player);
                this.onChestedStatusChanged();

                return ActionResult.SUCCESS;
            }
            ActionResult actionResult = itemStack.useOnEntity(player, this, hand);
            if (actionResult.isAccepted()) {
                return actionResult;
            }
        }
        this.putPlayerOnBack(player);
        return ActionResult.SUCCESS;
    }

    @Override
    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
        super.tickControlled(controllingPlayer, movementInput);
        Vec2f vec2f = new Vec2f(controllingPlayer.getPitch() * 0.5F, controllingPlayer.getYaw());;
        this.setRotation(vec2f.y, vec2f.x);
        this.lastYaw = this.bodyYaw = this.headYaw = this.getYaw();
    }


    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float f = controllingPlayer.sidewaysSpeed * 0.5F;
        float g = controllingPlayer.forwardSpeed;
        if (g <= 0.0F) {
            g *= 0.25F;
        }

        return new Vec3d(f, 0.0, g);
    }

    @Override
    protected void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
        super.updatePassengerPosition(passenger, positionUpdater);
        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).bodyYaw = this.bodyYaw;
        }
    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    protected void playAddChestSound() {
        this.playSound(SoundEvents.ENTITY_DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    public int getMaxHeadRotation() {
        return 30;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.SET_SHEEP_EAT_GRASS_TIMER_OR_PRIME_TNT_MINECART) {
            this.eatGrassTimer = HavagerEatGrassGoal.EAT_GRASS_TIMER;
        } else {
            super.handleStatus(status);
        }

        super.handleStatus(status);
    }

    @Override
    public boolean canSpawn(WorldView world) {
        BlockPos blockPos = this.getBlockPos();
        BlockPos blockPos2 = blockPos.down();
        BlockState blockState = world.getBlockState(blockPos2);
        if (!blockState.hasSolidTopSurface(world, blockPos2, this)) {
            return false;
        } else {
            for (BlockPos target : BlockPos.iterate(blockPos.add(-1, 0, -1), blockPos.add(1, 2, 1))) {
                BlockState blockState2 = world.getBlockState(target);
                if (!SpawnHelper.isClearForSpawn(world, target, blockState2, blockState2.getFluidState(), ModEntities.HAVAGER)) {
                    return false;
                }
            }

            return SpawnHelper.isClearForSpawn(world, blockPos, world.getBlockState(blockPos), Fluids.EMPTY.getDefaultState(), ModEntities.HAVAGER)
                    && world.doesNotIntersectEntities(this);
        }
    }

    public float getHeadAngle(float tickProgress) {
        int startEnd = HavagerEatGrassGoal.EAT_GRASS_TIMER / 10;
        float hr = (float) (Math.PI / 180.0);
        if (this.eatGrassTimer > startEnd && this.eatGrassTimer <= HavagerEatGrassGoal.EAT_GRASS_TIMER - startEnd) {
            float f = (this.eatGrassTimer - startEnd - tickProgress) / (HavagerEatGrassGoal.EAT_GRASS_TIMER - startEnd * 2);
            return ((float) (Math.PI / 5) + 0.21991149F * MathHelper.sin(f * 28.7F)) / hr;
        } else {
            return this.eatGrassTimer > 0 ? (float) (Math.PI / 5) / hr : this.getLerpedPitch(tickProgress);
        }
    }

    public boolean areInventoriesDifferent(Inventory inventory) {
        return this.items != inventory;
    }

    public int getChests() {
        return chests;
    }

    public SimpleInventory getItems() {
        return items;
    }
}