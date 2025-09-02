package com.brazz.jurassicadventure.machines.electricfence;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.util.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

// << CORREÇÃO: Agora estende BaseEntityBlock, a classe base correta >>
public class ElectricFenceWireBlock extends BaseEntityBlock {

    // --- PROPRIEDADES DE CONEXÃO (Apenas horizontais) ---
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty EAST = BooleanProperty.create("east");

    // --- FORMA FÍSICA (VOXELSHAPE) PARA CERCA VERTICAL ---
    private static final VoxelShape SHAPE_POST = Block.box(7, 0, 7, 9, 16, 9);
    private static final VoxelShape SHAPE_SIDE_NORTH = Block.box(7, 0, 0, 9, 16, 7);
    private static final VoxelShape SHAPE_SIDE_SOUTH = Block.box(7, 0, 9, 9, 16, 16);
    private static final VoxelShape SHAPE_SIDE_WEST = Block.box(0, 0, 7, 7, 16, 9);
    private static final VoxelShape SHAPE_SIDE_EAST = Block.box(9, 0, 7, 16, 16, 9);

    public ElectricFenceWireBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false).setValue(SOUTH, false)
                .setValue(WEST, false).setValue(EAST, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape finalShape = SHAPE_POST;
        if (pState.getValue(NORTH)) { finalShape = Shapes.or(finalShape, SHAPE_SIDE_NORTH); }
        if (pState.getValue(SOUTH)) { finalShape = Shapes.or(finalShape, SHAPE_SIDE_SOUTH); }
        if (pState.getValue(WEST)) { finalShape = Shapes.or(finalShape, SHAPE_SIDE_WEST); }
        if (pState.getValue(EAST)) { finalShape = Shapes.or(finalShape, SHAPE_SIDE_EAST); }
        return finalShape;
    }

    // --- LÓGICA DE CONEXÃO VISUAL ---
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
                .setValue(NORTH, canConnect(level, pos, Direction.NORTH))
                .setValue(SOUTH, canConnect(level, pos, Direction.SOUTH))
                .setValue(WEST, canConnect(level, pos, Direction.WEST))
                .setValue(EAST, canConnect(level, pos, Direction.EAST));
    }

    private boolean canConnect(LevelAccessor level, BlockPos pos, Direction direction) {
        BlockState neighborState = level.getBlockState(pos.relative(direction));
        BlockEntity be = level.getBlockEntity(pos.relative(direction));
        // Conecta se o vizinho for outra cerca/pilar ou tiver capacidade de energia (e for sólido na nossa direção)
        return (be instanceof ElectricFenceBlockEntity) || 
               (neighborState.isFaceSturdy(level, pos.relative(direction), direction.getOpposite()));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH, SOUTH, WEST, EAST);
    }

    // --- LÓGICA DE DANO (CHOQUE) ---
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
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ElectricFenceBlockEntity(pPos, pState);
    }

    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) return null;
        return createTickerHelper(pBlockEntityType, ModBlockEntities.ELECTRIC_FENCE_BE.get(),
                ElectricFenceBlockEntity::tick);
    }
}