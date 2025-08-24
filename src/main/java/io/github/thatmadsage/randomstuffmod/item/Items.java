package io.github.thatmadsage.randomstuffmod.item;

import io.github.thatmadsage.randomstuffmod.RandomStuffMod;
import io.github.thatmadsage.randomstuffmod.block.Blocks;
import io.github.thatmadsage.randomstuffmod.component.DataComponents;
import io.github.thatmadsage.randomstuffmod.component.DataComponents.FlashlightActiveRecord;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Items {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RandomStuffMod.MODID);

    public static final DeferredItem<Item> FLASHLIGHT = ITEMS.registerItem(
        "flashlight", 
        registryName -> new FlashlightItem(
            new Item.Properties()
                .component(DataComponents.FLASHLIGHT_ACTIVE.get(), new FlashlightActiveRecord(false))
        )
    );
    
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
        "example_block", 
        Blocks.EXAMPLE_BLOCK
    );
}
