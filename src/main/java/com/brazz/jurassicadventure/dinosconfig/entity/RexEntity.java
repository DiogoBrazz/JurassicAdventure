package com.brazz.jurassicadventure.dinosconfig.entity;

import com.brazz.jurassicadventure.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class RexEntity extends AllDinos {
    private static final UUID HEALTH_MODIFIER_ID = UUID.fromString("7b5a8e0f-8d2a-4a6c-8a2a-4a9e1eaf83e0");
    private static final UUID DAMAGE_MODIFIER_ID = UUID.fromString("8c6b9f1e-9e3b-5b7d-9b3b-5b0f2fbf94f1");
    private static final UUID SPEED_MODIFIER_ID = UUID.fromString("9d7c0a2f-ae4c-4e8d-ac2c-5e1f3bfa85f2");

    private static final double ADULT_HEALTH = 200.0D;
    private static final double ADULT_DAMAGE = 25.0D;
    private static final double ADULT_SPEED = 0.25D;
    private static final double BABY_HEALTH = 20.0D;
    private static final double BABY_DAMAGE = 2.5D;
    private static final double BABY_SPEED = 0.15D;

    public RexEntity(EntityType<? extends AllDinos> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, ADULT_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, ADULT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, ADULT_DAMAGE)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1D, Ingredient.of(Items.BEEF), false));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        boolean isFromIncubator = pDataTag != null && pDataTag.getBoolean("IsIncubatorBaby");
        
        if (isFromIncubator) {
            this.setAge(MAX_BABY_AGE);
        } else if (pReason != MobSpawnType.BREEDING) {
            this.setAge(ADULT_AGE);
        }
        
        this.updateAttributesForAge();
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
    
    @Override
    protected void updateAttributesForAge() {
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        
        if (health == null || damage == null || speed == null) return;
        
        health.removeModifier(HEALTH_MODIFIER_ID);
        damage.removeModifier(DAMAGE_MODIFIER_ID);
        speed.removeModifier(SPEED_MODIFIER_ID);

        if (this.isBaby()) {
            float progress = this.getGrowthProgress();
            
            double healthValue = BABY_HEALTH + (ADULT_HEALTH - BABY_HEALTH) * progress;
            double damageValue = BABY_DAMAGE + (ADULT_DAMAGE - BABY_DAMAGE) * progress;
            double speedValue = BABY_SPEED + (ADULT_SPEED - BABY_SPEED) * progress;

            health.addPermanentModifier(new AttributeModifier(HEALTH_MODIFIER_ID, "growth_health", healthValue - ADULT_HEALTH, AttributeModifier.Operation.ADDITION));
            damage.addPermanentModifier(new AttributeModifier(DAMAGE_MODIFIER_ID, "growth_damage", damageValue - ADULT_DAMAGE, AttributeModifier.Operation.ADDITION));
            speed.addPermanentModifier(new AttributeModifier(SPEED_MODIFIER_ID, "growth_speed", speedValue - ADULT_SPEED, AttributeModifier.Operation.ADDITION));
        }

        if (this.getHealth() > this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    public float getScale() {
        if (this.isBaby()) {
            float progress = this.getGrowthProgress();
            return 0.3f + 0.7f * progress;
        }
        return 1.0f;
    }

    // --- LÓGICA DE ANIMAÇÃO ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
        } else {
            event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.BEEF);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        RexEntity baby = ModEntities.REX.get().create(pLevel);
        if (baby != null) {
            baby.setAge(MAX_BABY_AGE);
        }
        return baby;
    }
}