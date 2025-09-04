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
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import java.util.UUID;

public class RexEntity extends AllDinos {

    private static final UUID HEALTH_MODIFIER_ID = UUID.fromString("7b5a8e0f-8d2a-4a6c-8a2a-4a9e1eaf83e0");
    private static final UUID DAMAGE_MODIFIER_ID = UUID.fromString("8c6b9f1e-9e3b-5b7d-9b3b-5b0f2fbf94f1");

    public RexEntity(EntityType<? extends AllDinos> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        // Usar Mob.createMobAttributes() é a forma mais fundamental e segura
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D) // Vida de bebê
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D) // Dano de bebê
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D); // Alcance da IA
    }
    
    // << CORREÇÃO PRINCIPAL: Este método controla o spawn >>
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        
        // Verifica se recebemos a "ordem" da incubadora.
        boolean isFromIncubator = pDataTag != null && pDataTag.getBoolean("IsIncubatorBaby");
        
        // Se a razão do spawn for reprodução OU se veio da incubadora, nasce bebê.
        if (pReason == MobSpawnType.BREEDING || isFromIncubator) {
            this.setAge(BABY_TO_JUVENILE_AGE); // Força a ser bebê
        } else {
            // Para todos os outros casos (ovo de spawn, comando /summon), nasce adulto.
            this.setAge(0);
        }
        
        this.updateAttributesForAge();
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
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

    @Override
    protected void updateAttributesForAge() {
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (health == null || damage == null) return;
        
        health.removeModifier(HEALTH_MODIFIER_ID);
        damage.removeModifier(DAMAGE_MODIFIER_ID);

        if (this.isBaby()) {
            float progress = 1.0f - ((float) this.getAge() / (float) BABY_TO_JUVENILE_AGE);
            float growthBonus = Math.max(0, Math.min(1, progress));

            double healthBonus = 180.0 * growthBonus;
            double damageBonus = 22.0 * growthBonus;

            health.addPermanentModifier(new AttributeModifier(HEALTH_MODIFIER_ID, "growth_health", healthBonus, AttributeModifier.Operation.ADDITION));
            damage.addPermanentModifier(new AttributeModifier(DAMAGE_MODIFIER_ID, "growth_damage", damageBonus, AttributeModifier.Operation.ADDITION));
        }

        if (this.getHealth() > this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
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

    // --- REPRODUÇÃO ---
    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.BEEF);
    }

    // << CORREÇÃO: O método correto para reprodução >>
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        // A lógica do Minecraft já garante que o filhote gerado aqui terá a idade de bebê (-24000)
        return ModEntities.REX.get().create(pLevel);
    }
}