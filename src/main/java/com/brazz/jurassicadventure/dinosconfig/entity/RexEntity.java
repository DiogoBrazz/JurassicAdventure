package com.brazz.jurassicadventure.dinosconfig.entity;

import com.brazz.jurassicadventure.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob; // << IMPORT NECESSÁRIO
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

public class RexEntity extends AllDinos {

    public RexEntity(EntityType<? extends AllDinos> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // --- ATRIBUTOS ---
    public static AttributeSupplier.Builder createAttributes() {
        // << CORRIGIDO: Chamado a partir de Mob, não de AllDinos >>
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    // --- INTELIGÊNCIA ARTIFICIAL (IA / Goals) ---
    @Override
    protected void registerGoals() {
        // Metas comuns a todas as idades
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(Items.BEEF), false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        // --- IA DE BEBÊ (Condicional) ---
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D) {
            @Override
            public boolean canUse() {
                return RexEntity.this.isBaby() && super.canUse();
            }
        });

        // --- IA DE ADULTO (Condicional) ---
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false) {
            @Override
            public boolean canUse() {
                return !RexEntity.this.isBaby() && super.canUse();
            }
        });
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, (target) -> !this.isBaby()));
        
        // O próprio BreedGoal já tem uma verificação interna para isBaby()
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    }

    @Override
    protected void updateAttributesForAge() {
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (health == null || damage == null) return;
        
        health.removeModifiers();
        damage.removeModifiers();

        // << USA A MESMA LÓGICA DO RENDERER >>
        float progress = (float)(AllDinos.BABY_TO_JUVENILE_AGE - this.getAge()) / (float)AllDinos.BABY_TO_JUVENILE_AGE;
        // Garante que o progresso não seja negativo se algo der errado
        float growthBonus = Math.max(0, progress);

        health.addPermanentModifier(new AttributeModifier("growth_health", 180.0 * growthBonus, AttributeModifier.Operation.ADDITION));
        damage.addPermanentModifier(new AttributeModifier("growth_damage", 22.0 * growthBonus, AttributeModifier.Operation.ADDITION));

        // Cura o T-Rex para a nova vida máxima
        this.setHealth(this.getMaxHealth());
    }

    // --- LÓGICA DE ANIMAÇÃO (GeckoLib) ---
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

    // --- LÓGICA DE REPRODUÇÃO (Breeding) ---
    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.BEEF);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.REX.get().create(pLevel);
    }
}