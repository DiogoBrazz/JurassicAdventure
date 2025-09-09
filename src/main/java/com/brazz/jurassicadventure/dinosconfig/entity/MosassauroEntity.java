package com.brazz.jurassicadventure.dinosconfig.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class MosassauroEntity extends AbstractWaterDinoEntity {

    public MosassauroEntity(EntityType<? extends AbstractWaterDinoEntity> entityType, Level level) {
        super(entityType, level);
    }

    // --- 1. ATRIBUTOS DO DINOSSAURO ---
    // --- ESCALA DO DINO BEBE ---
    private static final float DINO_INITIAL_SCALE = 0.07F;
    // --- SOMBRA DO DINO BEBE ---
    public static final float SHADOW_RADIUS = 2.0F;
    // --- TEMPO DE CRESCIMENTO ---
    private static final int GROWTH_TIME = 48000;  // 40 minutos para crescer
    // --- VIDA ---
    private static final double BABY_MAX_HEALTH = 8.0D;
    private static final double ADULT_MAX_HEALTH = 120.0D; 
    // --- ATAQUE ---
    private static final double BABY_ATTACK_DAMAGE = 2.5D;
    private static final double ADULT_ATTACK_DAMAGE = 26.0D; 
    private static final double DINO_ATTACK_KNOCKBACK = 0.5D;
    // --- ARMADURA E RESISTENCIA ---
    private static final double DINO_ARMOR = 1.0D; 
    private static final double DINO_KNOCKBACK_RESISTANCE = 1.0D;
    // --- MOVIMENTAÇÃO ---
    private static final double DINO_MOVEMENT_SPEED = 1.0D;
    private static final double DINO_FOLLOW_RANGE = 30.0D;
    // --- HITBOX ---
    private static final float BABY_WIDTH = 0.25F;
    private static final float BABY_HEIGHT = 0.3F;

    public static final float ADULT_WIDTH = 3.0F;
    public static final float ADULT_HEIGHT = 4.0F;

    // --- 2. CONFIGURAÇÕES ---
    @Override
    public int getMaxGrowthAge() { return GROWTH_TIME; }
    @Override
    public float getBabyWidth() { return BABY_WIDTH; }
    @Override
    public float getBabyHeight() { return BABY_HEIGHT; }
    @Override
    public float getAdultWidth() { return ADULT_WIDTH; }
    @Override
    public float getAdultHeight() { return ADULT_HEIGHT; }
    @Override
    public double getBaseHealth() { return BABY_MAX_HEALTH; } 
    @Override
    public double getMaxHealthAttribute() { return ADULT_MAX_HEALTH; } 
    @Override
    public double getBaseAttackDamage() { return BABY_ATTACK_DAMAGE; } 
    @Override
    public double getMaxAttackDamage() { return ADULT_ATTACK_DAMAGE; } 

    // --- 3. MÉTODOS QUE AINDA SÃO ESPECÍFICOS ---

    // Atributos são únicos para cada dino
    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes() 
                .add(Attributes.MAX_HEALTH, ADULT_MAX_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, DINO_MOVEMENT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, ADULT_ATTACK_DAMAGE)
                .add(Attributes.ATTACK_KNOCKBACK, DINO_ATTACK_KNOCKBACK)
                .add(Attributes.ARMOR, DINO_ARMOR)
                .add(Attributes.KNOCKBACK_RESISTANCE, DINO_KNOCKBACK_RESISTANCE)
                .add(Attributes.FOLLOW_RANGE, DINO_FOLLOW_RANGE);
    }

    // ---------- SEÇÃO 3: IA (GOALS) ESPECÍFICOS DO MOSASSAURO ----------
    @Override
    protected void registerGoals() {
        super.registerGoals(); // <-- IMPORTANTE: Chama os goals básicos de nado da classe mãe

        // Adiciona os goals de combate e comportamento únicos do Mosassauro
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.4D, true));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(Items.COD, Items.SALMON), false));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Animal.class, true));
    }


    public float getScale() {
        // Fórmula de escala visual para o renderer
        return DINO_INITIAL_SCALE + ((float)this.getDinoAge() / (float)this.getMaxGrowthAge()) * (1.0F - DINO_INITIAL_SCALE);
    }
}