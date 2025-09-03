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
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

public class ElectricFenceWireBlock extends BaseEntityBlock {

    // --- PROPRIEDADES DE CONEXÃO (igual aos muros de pedra) ---
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty NORTH_WALL = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH_WALL = BooleanProperty.create("south");
    public static final BooleanProperty WEST_WALL = BooleanProperty.create("west");
    public static final BooleanProperty EAST_WALL = BooleanProperty.create("east");

    // --- FORMA FÍSICA (VOXELSHAPE) ---
    private static final VoxelShape POST = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
    private static final VoxelShape NORTH = Block.box(7.0, 0.0, 0.0, 9.0, 16.0, 7.0);
    private static final VoxelShape SOUTH = Block.box(7.0, 0.0, 9.0, 9.0, 16.0, 16.0);
    private static final VoxelShape WEST = Block.box(0.0, 0.0, 7.0, 7.0, 16.0, 9.0);
    private static final VoxelShape EAST = Block.box(9.0, 0.0, 7.0, 16.0, 16.0, 9.0);

    public ElectricFenceWireBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(UP, true)
                .setValue(NORTH_WALL, false)
                .setValue(SOUTH_WALL, false)
                .setValue(WEST_WALL, false)
                .setValue(EAST_WALL, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape shape = POST;

        if (pState.getValue(NORTH_WALL)) {
            shape = Shapes.or(shape, NORTH);
        }
        if (pState.getValue(SOUTH_WALL)) {
            shape = Shapes.or(shape, SOUTH);
        }
        if (pState.getValue(WEST_WALL)) {
            shape = Shapes.or(shape, WEST);
        }
        if (pState.getValue(EAST_WALL)) {
            shape = Shapes.or(shape, EAST);
        }

        return shape;
    }

    // --- LÓGICA DE CONEXÃO VISUAL ---
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.getState(pContext.getLevel(), pContext.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pDirection.getAxis().isHorizontal()) {
            return this.getState(pLevel, pCurrentPos);
        }
        return pState;
    }

    private BlockState getState(LevelAccessor level, BlockPos pos) {
        boolean north = shouldConnectTo(level, pos.north(), Direction.SOUTH);
        boolean south = shouldConnectTo(level, pos.south(), Direction.NORTH);
        boolean west  = shouldConnectTo(level, pos.west(), Direction.EAST);
        boolean east  = shouldConnectTo(level, pos.east(), Direction.WEST);

        // Conexão reta (sem pilar)
        boolean straightNS = north && south && !east && !west;
        boolean straightEW = east && west && !north && !south;

        // Se for reta pura, tira o post. Se tiver 3 lados (como b, f, h, d), mantém o post.
        boolean up = !(straightNS || straightEW);

        return level.getBlockState(pos).getBlock() == this
                ? level.getBlockState(pos)
                    .setValue(UP, up)
                    .setValue(NORTH_WALL, north)
                    .setValue(SOUTH_WALL, south)
                    .setValue(WEST_WALL, west)
                    .setValue(EAST_WALL, east)
                : this.defaultBlockState()
                    .setValue(UP, up)
                    .setValue(NORTH_WALL, north)
                    .setValue(SOUTH_WALL, south)
                    .setValue(WEST_WALL, west)
                    .setValue(EAST_WALL, east);
    }

    private boolean shouldConnectTo(LevelAccessor level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);
        
        // Conecta com blocos do mesmo tipo
        if (state.is(this)) {
            return true;
        }
        
        // Conecta com blocos que têm capacidade de energia
        if (be != null && be.getCapability(ForgeCapabilities.ENERGY).isPresent()) {
            return true;
        }
        
        return false;
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(UP, NORTH_WALL, SOUTH_WALL, WEST_WALL, EAST_WALL);
    }

    // --- LÓGICA DE DANO (CHOQUE) ---
    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide()) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof ElectricFenceBlockEntity fenceBe) {
                if (fenceBe.getEnergyStorage().getEnergyStored() > 0) {
                    DamageSource damageSource = new DamageSource(pLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.ELECTRIC_SHOCK));
                    pEntity.hurt(damageSource, 8.0F);
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