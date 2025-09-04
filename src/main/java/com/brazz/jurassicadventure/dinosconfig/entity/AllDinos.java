package com.brazz.jurassicadventure.dinosconfig.entity;

// --- IMPORTS ADICIONADOS ---
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
// --- FIM DOS IMPORTS ADICIONADOS ---

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public abstract class AllDinos extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public enum AgeStage { BEBE, JUVENIL, ADULTO }
    public static final int BABY_TO_JUVENILE_AGE = -24000;
    public static final int JUVENILE_TO_ADULT_AGE = 0;

    protected AllDinos(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    // << ALTERADO: Não precisamos mais do nosso 'ageInTicks', pois usamos o sistema do Minecraft >>
    
    @Override
    public void aiStep() {
        AgeStage previousStage = getAgeStage();
        
        // A chamada super.aiStep() JÁ CUIDA de incrementar a idade do animal a cada tick.
        super.aiStep();

        if (!this.level().isClientSide()) {
            // << MELHORIA: Atualiza os atributos periodicamente (a cada 5 segundos) para um crescimento de força mais suave >>
            if (this.tickCount % 100 == 0) {
                this.updateAttributesForAge();
            }
            
            // Se o estágio mudou, força uma atualização imediata da hitbox e dos atributos.
            if (getAgeStage() != previousStage) {
                this.refreshDimensions();
                this.updateAttributesForAge();
            }
        }
    }
    
    // Método que as classes filhas são obrigadas a implementar.
    protected abstract void updateAttributesForAge();

    public AgeStage getAgeStage() {
        int age = this.getAge();
        if (age < JUVENILE_TO_ADULT_AGE) return AgeStage.BEBE;
        // A lógica para JUVENIL pode ser adicionada aqui se você mudar a constante BABY_TO_JUVENILE_AGE
        return AgeStage.ADULTO;
    }
    
    // --- LÓGICA DO GECKOLIB ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}