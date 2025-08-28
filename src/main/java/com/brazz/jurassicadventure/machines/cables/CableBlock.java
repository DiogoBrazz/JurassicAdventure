// PACOTE CORRETO: src/main/java/com/brazz/jurassicadventure/machines/cables/CableBlock.java
package com.brazz.jurassicadventure.machines.cables;

import com.brazz.jurassicadventure.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter; // << IMPORT ADICIONADO
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
import net.minecraft.world.phys.shapes.CollisionContext; // << IMPORT ADICIONADO
import net.minecraft.world.phys.shapes.Shapes; // << IMPORT ADICIONADO
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

public class CableBlock extends BaseEntityBlock {
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    // Formas de cada parte do cabo para a hitbox.
    private static final VoxelShape SHAPE_CORE = Block.box(5, 5, 5, 11, 11, 11);
    private static final VoxelShape SHAPE_DOWN = Block.box(5, 0, 5, 11, 5, 11);
    private static final VoxelShape SHAPE_UP = Block.box(5, 11, 5, 11, 16, 11);
    private static final VoxelShape SHAPE_NORTH = Block.box(5, 5, 0, 11, 11, 5);
    private static final VoxelShape SHAPE_SOUTH = Block.box(5, 5, 11, 11, 11, 16);
    private static final VoxelShape SHAPE_WEST = Block.box(0, 5, 5, 5, 11, 11);
    private static final VoxelShape SHAPE_EAST = Block.box(11, 5, 5, 16, 11, 11);

    public CableBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false).setValue(SOUTH, false)
                .setValue(WEST, false).setValue(EAST, false)
                .setValue(UP, false).setValue(DOWN, false));
    }

    // --- MÉTODO QUE FALTAVA ---
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        // Começa com a forma do núcleo central
        VoxelShape finalShape = SHAPE_CORE;

        // Combina o núcleo com os "braços" de conexão necessários, com base no estado do bloco
        if (pState.getValue(DOWN)) {
            finalShape = Shapes.or(finalShape, SHAPE_DOWN);
        }
        if (pState.getValue(UP)) {
            finalShape = Shapes.or(finalShape, SHAPE_UP);
        }
        if (pState.getValue(NORTH)) {
            finalShape = Shapes.or(finalShape, SHAPE_NORTH);
        }
        if (pState.getValue(SOUTH)) {
            finalShape = Shapes.or(finalShape, SHAPE_SOUTH);
        }
        if (pState.getValue(WEST)) {
            finalShape = Shapes.or(finalShape, SHAPE_WEST);
        }
        if (pState.getValue(EAST)) {
            finalShape = Shapes.or(finalShape, SHAPE_EAST);
        }
        return finalShape;
    }
    // --- FIM DO MÉTODO QUE FALTAVA ---

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.getState(pContext.getLevel(), pContext.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return this.getState(pLevel, pCurrentPos);
    }

    private BlockState getState(LevelAccessor level, BlockPos pos) {
        return this.defaultBlockState()
                .setValue(NORTH, canConnect(level, pos.north()))
                .setValue(SOUTH, canConnect(level, pos.south()))
                .setValue(WEST, canConnect(level, pos.west()))
                .setValue(EAST, canConnect(level, pos.east()))
                .setValue(UP, canConnect(level, pos.above()))
                .setValue(DOWN, canConnect(level, pos.below()));
    }

    private boolean canConnect(LevelAccessor level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        return (be != null && be.getCapability(ForgeCapabilities.ENERGY).isPresent()) || level.getBlockState(pos).is(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CableBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }
        return createTickerHelper(pBlockEntityType, ModBlockEntities.CABLE_BLOCK_ENTITY.get(), CableBlockEntity::tick);
    }
}