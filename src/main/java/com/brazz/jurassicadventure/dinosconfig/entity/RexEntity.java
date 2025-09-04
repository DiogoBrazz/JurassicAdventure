package com.brazz.jurassicadventure.dinosconfig.entity;

import com.brazz.jurassicadventure.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.UUID; // << IMPORT ADICIONADO

public class RexEntity extends AllDinos {

    // UUIDs únicos para nossos modificadores de atributos, para que possamos removê-los de forma confiável.
    private static final UUID HEALTH_MODIFIER_ID = UUID.fromString("7b5a8e0f-8d2a-4a6c-8a2a-4a9e1eaf83e0");
    private static final UUID DAMAGE_MODIFIER_ID = UUID.fromString("8c6b9f1e-9e3b-5b7d-9b3b-5b0f2fbf94f1");

    public RexEntity(EntityType<? extends AllDinos> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        // Garante que o dinossauro nasça como um bebê
        this.setAge(BABY_TO_JUVENILE_AGE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(Items.BEEF), false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D) {
            @Override
            public boolean canUse() {
                return RexEntity.this.isBaby() && super.canUse();
            }
        });

        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false) {
            @Override
            public boolean canUse() {
                return !RexEntity.this.isBaby() && super.canUse();
            }
        });
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, (target) -> !this.isBaby()));
        
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    }

    // << CORRIGIDO: Este método agora aplica os atributos da maneira correta >>
    @Override
    protected void updateAttributesForAge() {
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (health == null || damage == null) return;
        
        // Remove os modificadores antigos antes de adicionar os novos
        health.removeModifier(HEALTH_MODIFIER_ID);
        damage.removeModifier(DAMAGE_MODIFIER_ID);

        float progress = 1.0f - ((float) this.getAge() / (float) BABY_TO_JUVENILE_AGE);
        float growthBonus = Math.max(0, Math.min(1, progress)); // Garante que o bônus fique entre 0.0 e 1.0

        double healthBonus = 180.0 * growthBonus;
        double damageBonus = 22.0 * growthBonus;

        health.addPermanentModifier(new AttributeModifier(HEALTH_MODIFIER_ID, "growth_health", healthBonus, AttributeModifier.Operation.ADDITION));
        damage.addPermanentModifier(new AttributeModifier(DAMAGE_MODIFIER_ID, "growth_damage", damageBonus, AttributeModifier.Operation.ADDITION));

        // Ajusta a saúde atual para não morrer ao crescer
        if (this.getHealth() > this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

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

    // --- REPRODUÇÃO ---
    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.BEEF);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        RexEntity baby = ModEntities.REX.get().create(pLevel);
        if (baby != null) {
            baby.setAge(BABY_TO_JUVENILE_AGE); // Garante que é um bebê
        }
        return baby;
    }
}