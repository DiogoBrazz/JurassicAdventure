package com.brazz.jurassicadventure.machines.electricfence;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.util.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallBlock; // << MUDANÇA PRINCIPAL: Herda de WallBlock
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

// << CORREÇÃO: Agora estende WallBlock, que gerencia todas as conexões automaticamente >>
// Implementamos IEntityBlock manualmente porque WallBlock não implementa, mas precisamos da BlockEntity.
public class ElectricFenceWireBlock extends WallBlock implements net.minecraft.world.level.block.EntityBlock {

    public ElectricFenceWireBlock(Properties pProperties) {
        super(pProperties);
        // O estado padrão com todas as conexões é gerenciado pela classe WallBlock.
        // Não precisamos mais definir NORTH, SOUTH, etc. manualmente.
    }

    // --- LÓGICA DE DANO (CHOQUE) ---
    // Esta lógica é específica do seu mod, então a mantemos.
    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide()) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof ElectricFenceBlockEntity fenceBe) {
                if (fenceBe.getEnergyStorage().getEnergyStored() > 0) {
                    DamageSource damageSource = new DamageSource(pLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.ELECTRIC_SHOCK));
                    pEntity.hurt(damageSource, 8.0F); // 8.0F = 4 corações de dano
                }
            }
        }
        super.entityInside(pState, pLevel, pPos, pEntity);
    }


    // --- MÉTODOS DE BLOCK ENTITY ---
    // Toda a lógica de VoxelShape, updateShape, getStateForPlacement e createBlockStateDefinition
    // foi removida, pois WallBlock cuida de tudo isso.
    // Mantemos apenas os métodos necessários para a BlockEntity.

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ElectricFenceBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) return null;
        return createTickerHelper(pBlockEntityType, ModBlockEntities.ELECTRIC_FENCE_BE.get(),
                ElectricFenceBlockEntity::tick);
    }
}
