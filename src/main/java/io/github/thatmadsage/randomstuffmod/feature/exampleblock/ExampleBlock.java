package io.github.thatmadsage.randomstuffmod.feature.exampleblock;

import java.util.List;

import io.github.thatmadsage.randomstuffmod.RandomStuffMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ExampleBlock extends Block {

    public List<Block> BLOCKS_TO_BREAK = List.of(Blocks.DIRT, Blocks.GRASS_BLOCK);

    public ExampleBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.isClientSide()) return;
        level.scheduleTick(pos, level.getBlockState(pos).getBlock(), 10);
    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        for (var neighbour_pos: List.of(pos.above(), pos.below(), pos.east(), pos.west(), pos.north(), pos.south())) {
            var neighbour_block = level.getBlockState(neighbour_pos).getBlock();
            if (BLOCKS_TO_BREAK.contains(neighbour_block)) {
                level.destroyBlock(neighbour_pos, false);
                level.setBlock(neighbour_pos, this.defaultBlockState(), UPDATE_ALL);
            }
        }
    }
    
}
