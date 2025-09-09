package com.brazz.jurassicadventure.dinosconfig.entity;

import com.brazz.jurassicadventure.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class AnkylosaurusEntity extends AbstractDinoEntity {

    public AnkylosaurusEntity(EntityType<? extends AbstractDinoEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // --- 1. ATRIBUTOS DO DINOSSAURO ---
    // --- ESCALA DO DINO BEBE ---
    private static final float DINO_INITIAL_SCALE = 0.12F;
    // --- TEMPO DE CRESCIMENTO ---
    private static final int GROWTH_TIME = 48000;  // 40 minutos para crescer
    // --- VIDA ---
    private static final double BABY_MAX_HEALTH = 10.0D;
    private static final double ADULT_MAX_HEALTH = 120.0D;
    // --- ATAQUE ---
    private static final double BABY_ATTACK_DAMAGE = 1.0D;
    private static final double ADULT_ATTACK_DAMAGE = 15.0D;
    private static final double DINO_ATTACK_KNOCKBACK = 1.8D;
    // --- ARMADURA E RESISTENCIA ---
    private static final double DINO_ARMOR = 15.0D;
    private static final double DINO_KNOCKBACK_RESISTANCE = 1.0D;
    // --- MOVIMENTAÇÃO ---
    private static final double DINO_MOVEMENT_SPEED = 0.18D;
    private static final double DINO_SWIM_SPEED = 1.0D;
    private static final double DINO_FOLLOW_RANGE = 20.0D;
    // --- HITBOX ---
    private static final float BABY_WIDTH = 0.5F;
    private static final float BABY_HEIGHT = 0.4F;
    public static final float ADULT_WIDTH = 3.5F;
    public static final float ADULT_HEIGHT = 2.5F;

    // --- REPRODUÇÃO ---
    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.WHEAT);
    }

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

    // --- 3. MÉTODOS ESPECÍFICOS ---
    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, ADULT_MAX_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, DINO_MOVEMENT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, ADULT_ATTACK_DAMAGE)
                .add(Attributes.ATTACK_KNOCKBACK, DINO_ATTACK_KNOCKBACK)
                .add(Attributes.ARMOR, DINO_ARMOR)
                .add(Attributes.KNOCKBACK_RESISTANCE, DINO_KNOCKBACK_RESISTANCE)
                .add(Attributes.FOLLOW_RANGE, DINO_FOLLOW_RANGE)
                .add(ForgeMod.SWIM_SPEED.get(), DINO_SWIM_SPEED);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true)); // Só ataca se provocado
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this)); // Fica agressivo ao ser atacado
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.ANKYLOSAURUS.get().create(pLevel);
    }

    @Override
    public float getScale() {
        return DINO_INITIAL_SCALE + ((float)this.getDinoAge() / (float)this.getMaxGrowthAge()) * (1.0F - DINO_INITIAL_SCALE);
    }
}
