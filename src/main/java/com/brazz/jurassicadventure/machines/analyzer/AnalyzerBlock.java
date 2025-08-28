package com.brazz.jurassicadventure.machines.analyzer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

import org.jetbrains.annotations.Nullable;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsBlock;

public class AnalyzerBlock extends AllSettingsBlock<AnalyzerBlockEntity> {

    public AnalyzerBlock(Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AnalyzerBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof AnalyzerBlockEntity analyzerEntity) {
                // Pegamos no inventário diretamente da nossa entidade de bloco
                ItemStackHandler itemHandler = analyzerEntity.getItemHandler();
                // Percorremos cada slot do inventário, do primeiro ao último
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    // Usamos o método padrão e seguro do Minecraft para dropar o item de um bloco
                    Block.popResource(pLevel, pPos, itemHandler.getStackInSlot(i));
                }
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
            BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof AnalyzerBlockEntity) {
                // abrir um menu
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (MenuProvider) entity, pPos);
            } else {
                throw new IllegalStateException("O nosso fornecedor de container está em falta!");
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
            BlockEntityType<T> pBlockEntityType) {
        // Esta função garante que a lógica de "tick" só corre no lado do servidor.
        if (pLevel.isClientSide()) {
            return null;
        }

        // Diz ao jogo para chamar o método "tick" da nossa AnalyzerBlockEntity a cada tick.
        return createTickerHelper(pBlockEntityType, ModBlockEntities.ANALYZER_BLOCK_ENTITY.get(),
        (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

}