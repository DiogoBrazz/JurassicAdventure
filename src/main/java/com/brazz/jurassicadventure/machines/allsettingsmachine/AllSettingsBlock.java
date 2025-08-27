package com.brazz.jurassicadventure.machines.allsettingsmachine;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.items.ItemStackHandler;

import org.jetbrains.annotations.Nullable;



public abstract class AllSettingsBlock<T extends BlockEntity> extends BaseEntityBlock{

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AllSettingsBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    // Esta interface garante que a entidade de bloco tem um método getItemHandler().
    public interface ItemStackHandlerProvider {
        ItemStackHandler getItemHandler();
    }

    // Define o estado do bloco quando é colocado no mundo.
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        // Ele pega na direção para a qual o jogador está a olhar e define a frente do bloco na direção oposta.
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    // Estes métodos garantem que o bloco roda corretamente se for movido por pistões ou clonado.
    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        // Pega a direção atual do bloco
        Direction currentFacing = state.getValue(FACING);
        // Calcula a nova direção baseada no espelhamento
        Direction newFacing = mirror.mirror(currentFacing);
        // Retorna o BlockState atualizado
        return state.setValue(FACING, newFacing);
    }

     // 5. ADICIONAMOS A NOSSA PROPRIEDADE À DEFINIÇÃO DE ESTADOS DO BLOCO
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    //serve para dizer ao Minecraft como o bloco deve ser renderizado no mundo.
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

}