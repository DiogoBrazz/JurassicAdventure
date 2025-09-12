package com.brazz.jurassicadventure.machines.electricfence;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.machines.cables.CableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

public class ElectricFencePillarBlock extends BaseEntityBlock{

    // --- PROPRIEDADES DO BLOCO ---
    // Estas propriedades guardarão o estado das conexões para o blockstate ler.
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH_FENCE = BooleanProperty.create("north_fence");
    public static final BooleanProperty SOUTH_FENCE = BooleanProperty.create("south_fence");
    public static final BooleanProperty WEST_FENCE = BooleanProperty.create("west_fence");
    public static final BooleanProperty EAST_FENCE = BooleanProperty.create("east_fence");
    public static final BooleanProperty NORTH_CABLE = BooleanProperty.create("north_cable");
    public static final BooleanProperty SOUTH_CABLE = BooleanProperty.create("south_cable");
    public static final BooleanProperty WEST_CABLE = BooleanProperty.create("west_cable");
    public static final BooleanProperty EAST_CABLE = BooleanProperty.create("east_cable");

    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 16, 15);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public ElectricFencePillarBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(UP, false).setValue(DOWN, false)
                .setValue(NORTH_FENCE, false).setValue(SOUTH_FENCE, false)
                .setValue(WEST_FENCE, false).setValue(EAST_FENCE, false)
                .setValue(NORTH_CABLE, false).setValue(SOUTH_CABLE, false)
                .setValue(WEST_CABLE, false).setValue(EAST_CABLE, false));
    }

    // --- LÓGICA DE CONEXÃO AVANÇADA ---

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.getState(pContext.getLevel(), pContext.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return this.getState(pLevel, pCurrentPos);
    }
    
    // Este método agora detecta todos os tipos de vizinhos e atualiza as propriedades
    private BlockState getState(LevelAccessor level, BlockPos pos) {
        return this.defaultBlockState()
                // Conexões Verticais (com outros pilares)
                .setValue(UP, level.getBlockState(pos.above()).getBlock() instanceof ElectricFencePillarBlock)
                .setValue(DOWN, level.getBlockState(pos.below()).getBlock() instanceof ElectricFencePillarBlock)
                // Conexões com Fios de Cerca
                .setValue(NORTH_FENCE, level.getBlockState(pos.north()).getBlock() instanceof ElectricFenceWireBlock)
                .setValue(SOUTH_FENCE, level.getBlockState(pos.south()).getBlock() instanceof ElectricFenceWireBlock)
                .setValue(WEST_FENCE, level.getBlockState(pos.west()).getBlock() instanceof ElectricFenceWireBlock)
                .setValue(EAST_FENCE, level.getBlockState(pos.east()).getBlock() instanceof ElectricFenceWireBlock)
                // Conexões com Cabos de Energia
                .setValue(NORTH_CABLE, level.getBlockEntity(pos.north()) instanceof CableBlockEntity)
                .setValue(SOUTH_CABLE, level.getBlockEntity(pos.south()) instanceof CableBlockEntity)
                .setValue(WEST_CABLE, level.getBlockEntity(pos.west()) instanceof CableBlockEntity)
                .setValue(EAST_CABLE, level.getBlockEntity(pos.east()) instanceof CableBlockEntity);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(UP, DOWN, NORTH_FENCE, SOUTH_FENCE, WEST_FENCE, EAST_FENCE, NORTH_CABLE, SOUTH_CABLE, WEST_CABLE, EAST_CABLE);
    }
    
    // --- MÉTODOS DE BLOCK ENTITY ---
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ElectricFenceBlockEntity(pPos, pState); // Continua usando a mesma BE de energia
    }
    
    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) return null;
        return createTickerHelper(pBlockEntityType, ModBlockEntities.ELECTRIC_FENCE_BE.get(),
                ElectricFenceBlockEntity::tick);
    }
}