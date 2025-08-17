package io.github.thatmadsage.randomstuffmod.feature.exampleblock;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags.EntityTypes;

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
    
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (level.isClientSide()) return;
        if (entity instanceof Player) return;
        if (entity instanceof Cat) return;
        if (!(entity instanceof LivingEntity)) return;
        if (!entity.isAlive()) return;
        var entity_pos = entity.position();
        entity.kill();
        var cat = EntityType.CAT.spawn((ServerLevel) level, BlockPos.containing(entity_pos), MobSpawnType.CONVERSION);
        cat.setPos(entity_pos);
    }
}
