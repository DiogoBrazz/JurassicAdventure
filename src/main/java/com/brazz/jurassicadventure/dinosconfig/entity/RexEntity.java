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

    public RexEntity(EntityType<? extends AllDinos> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        // << CORREÇÃO FINAL: Usar Mob.createMobAttributes() é a forma mais segura >>
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        boolean isFromIncubator = pDataTag != null && pDataTag.getBoolean("IsIncubatorBaby");
        
        if (isFromIncubator) {
            this.setAge(MAX_BABY_AGE);
        } else if (pReason != MobSpawnType.BREEDING) {
            // Só vira adulto se não for de reprodução E não for da incubadora
            this.setAge(0);
        }
        
        this.updateAttributesForAge();
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
    
    @Override
    protected void updateAttributesForAge() {
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (health == null || damage == null) return;
        
        health.removeModifier(HEALTH_MODIFIER_ID);
        damage.removeModifier(DAMAGE_MODIFIER_ID);

        if (this.isBaby()) {
            // A idade vai de -24000 (nascimento) a -1 (quase adulto)
            // Esta fórmula converte para um progresso de 0.0 a 1.0
            float progress = 1.0f + ((float) this.getAge() / (float) -MAX_BABY_AGE);
            
            // Garante que o progresso nunca saia do intervalo [0, 1]
            float growthBonus = Math.max(0, Math.min(1, progress));

            // Usa a variável 'growthBonus' que acabamos de declarar
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
        return ModEntities.REX.get().create(pLevel);
    }
}