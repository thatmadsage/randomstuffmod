package io.github.thatmadsage.randomstuffmod.block;

import io.github.thatmadsage.randomstuffmod.RandomStuffMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RandomStuffMod.MODID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", 
    registryName -> new ExampleBlock(
            BlockBehaviour.Properties.of()
        )
    );
}
