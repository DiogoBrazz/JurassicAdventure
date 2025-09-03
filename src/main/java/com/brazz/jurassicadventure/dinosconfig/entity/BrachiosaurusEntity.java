package com.brazz.jurassicadventure.dinosconfig.entity; // Verifique se este é o seu package correto

import com.brazz.jurassicadventure.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

// --- IMPORTAÇÕES DA GECKOLIB (INCLUINDO A QUE FALTAVA) ---
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import net.minecraftforge.common.ForgeMod; // Adicione este import se for usar SWIM_SPEED

public class BrachiosaurusEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public BrachiosaurusEntity(EntityType<? extends BrachiosaurusEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        // Se a entidade estiver a andar...
        if (event.isMoving()) {
            // ...manda tocar a animação "walk" em loop.
            event.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
        } 
        // Se não estiver a andar (ou seja, está parada)...
        else {
            // ...manda tocar a animação "idle" em loop.
            event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.1D, Ingredient.of(Items.BEEF), false));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        // --- ALVOS ---
        // Prioridade 1: Se alguém me atacar, essa pessoa torna-se o meu alvo.
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    // O método 'createAttributes' agora usa 'Mob'
    // Em BrachiosaurusEntity.java

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                // --- Atributos de Vida e Defesa (GRANDE) ---
                .add(Attributes.MAX_HEALTH, 400.0D) // Vida extremamente alta (200 corações)
                .add(Attributes.ARMOR, 18.0D) // Armadura natural muito forte, quase como diamante
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D) // 100% imune a ser empurrado por ataques

                // --- Atributos de Movimento (PESADO) ---
                .add(Attributes.MOVEMENT_SPEED, 0.13D) // Mais lento que um jogador (0.25)
                .add(ForgeMod.SWIM_SPEED.get(), 0.8D)  // Nada de forma desajeitada

                // --- Atributos de Combate (FORTE) ---
                .add(Attributes.ATTACK_DAMAGE, 30.0D)      // Um dano de pisão ou cabeçada muito forte (10 corações)
                .add(Attributes.ATTACK_KNOCKBACK, 3.0D)    // Empurra os alvos para muito longe com um ataque

                // --- Atributos de IA ---
                .add(Attributes.FOLLOW_RANGE, 24.0D);      // Não precisa de um grande alcance, pois é um herbívoro pacífico
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.BEEF);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.BRACHIOSAURUS.get().create(pLevel);
    }
}