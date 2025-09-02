package com.brazz.jurassicadventure.dinosconfig.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AllDinos extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // --- SISTEMA DE IDADE E CRESCIMENTO ---
    public enum AgeStage { BEBE, JUVENIL, ADULTO }
    public static final int JUVENILE_AGE_TICKS = 24000;
    public static final int ADULT_AGE_TICKS = 72000;
    protected int ageInTicks;
    
    // << CORRIGIDO: O tipo aqui agora é 'Animal', que é a classe que estamos estendendo >>
    protected AllDinos(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("DinoAge", this.ageInTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.ageInTicks = pCompound.getInt("DinoAge");
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            AgeStage previousStage = getAgeStage();
            if (this.ageInTicks < ADULT_AGE_TICKS) {
                this.ageInTicks++;
            }
            // Se o estágio mudou (ex: de BEBE para JUVENIL)...
            if (getAgeStage() != previousStage) {
                // ...atualiza tudo de uma vez.
                this.refreshDimensions(); // Recalcula a hitbox
                this.updateAttributesForAge(); // Atualiza os status
                // A IA não precisa mais ser atualizada aqui, pois é definida uma vez com condições.
            }
        }
    }
    
    // Método que as classes filhas (TRex, etc.) são obrigadas a implementar
    protected abstract void updateAttributesForAge();

    @Override
    public boolean isBaby() {
        return this.getAgeStage() == AgeStage.BEBE;
    }

    public AgeStage getAgeStage() {
        if (this.ageInTicks < JUVENILE_AGE_TICKS) return AgeStage.BEBE;
        if (this.ageInTicks < ADULT_AGE_TICKS) return AgeStage.JUVENIL;
        return AgeStage.ADULTO;
    }
    
    public int getAgeInTicks() {
        return this.ageInTicks;
    }

    // --- LÓGICA DO GECKOLIB ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        // A lógica de animação específica de cada dino irá aqui
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}