package com.brazz.jurassicadventure.dinosconfig.entity;

import com.brazz.jurassicadventure.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class AllosaurusEntity extends AbstractDinoEntity {

    public AllosaurusEntity(EntityType<? extends AbstractDinoEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // --- 1. ATRIBUTOS DO DINOSSAURO ---
    // --- ESCALA DO DINO BEBE ---
    private static final float DINO_INITIAL_SCALE = 0.1F;
    // --- TEMPO DE CRESCIMENTO ---
    private static final int GROWTH_TIME = 36000;  // 30 minutos para crescer
    // --- VIDA ---
    private static final double BABY_MAX_HEALTH = 5.0D;
    private static final double ADULT_MAX_HEALTH = 80.0D;
    // --- ATAQUE ---
    private static final double BABY_ATTACK_DAMAGE = 1.5D;
    private static final double ADULT_ATTACK_DAMAGE = 18.0D;
    private static final double DINO_ATTACK_KNOCKBACK = 0.8D;
    // --- ARMADURA E RESISTENCIA ---
    private static final double DINO_ARMOR = 0.5D;
    private static final double DINO_KNOCKBACK_RESISTANCE = 0.6D;
    // --- MOVIMENTAÇÃO ---
    private static final double DINO_MOVEMENT_SPEED = 0.28D;
    private static final double DINO_SWIM_SPEED = 1.0D;
    private static final double DINO_FOLLOW_RANGE = 32.0D;
    // --- HITBOX ---
    private static final float BABY_WIDTH = 0.3F;
    private static final float BABY_HEIGHT = 0.45F;
    public static final float ADULT_WIDTH = 2.8F;
    public static final float ADULT_HEIGHT = 3.8F;

    // --- REPRODUÇÃO ---
    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.BEEF);
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
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(Items.BEEF), false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Animal.class, true));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.ALLOSAURUS.get().create(pLevel);
    }

    @Override
    public float getScale() {
        return DINO_INITIAL_SCALE + ((float)this.getDinoAge() / (float)this.getMaxGrowthAge()) * (1.0F - DINO_INITIAL_SCALE);
    }
}
