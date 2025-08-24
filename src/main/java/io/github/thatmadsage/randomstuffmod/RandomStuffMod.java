package io.github.thatmadsage.randomstuffmod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import io.github.thatmadsage.randomstuffmod.feature.exampleblock.ExampleBlock;
import io.github.thatmadsage.randomstuffmod.feature.flashlight.FlashlightItem;
import io.netty.buffer.ByteBuf;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RandomStuffMod.MODID)
public class RandomStuffMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "randomstuffmod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "randomstuffmod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "randomstuffmod" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "randomstuffmod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);  
    // Create a Deferred Register to hold DataComponents which will all be registered under the "randomstuffmod" namespace  
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    // dude why is maing a tag that much code
        public record FlashlightActiveRecord(boolean active) {}
        public static final Codec<FlashlightActiveRecord> FLASHLIGHT_ACTIVE_CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.BOOL.fieldOf("active").forGetter(FlashlightActiveRecord::active)
        ).apply(instance, FlashlightActiveRecord::new)
    );
        public static final StreamCodec<ByteBuf, FlashlightActiveRecord> FLASHLIGHT_ACTIVE_STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL, FlashlightActiveRecord::active,
        FlashlightActiveRecord::new
    );
        public static final DeferredHolder<DataComponentType<?>, DataComponentType<FlashlightActiveRecord>> FLASHLIGHT_ACTIVE = DATA_COMPONENTS.registerComponentType(
        "flashlight_active",
        builder -> builder
            // The codec to read/write the data to disk
            .persistent(FLASHLIGHT_ACTIVE_CODEC)
            // The codec to read/write the data across the network
            .networkSynchronized(FLASHLIGHT_ACTIVE_STREAM_CODEC)
    );


    // Creates a new Block with the id "randomstuffmod:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", 
    registryName -> new ExampleBlock(
            BlockBehaviour.Properties.of()
        )
    );
    // Creates a new BlockItem with the id "randomstuffmod:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // Creates a new food item with the id "randomstuffmod:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> FLASHLIGHT = ITEMS.registerItem(
        "flashlight", registryName -> new FlashlightItem(new Item.Properties().component(FLASHLIGHT_ACTIVE.get(), new FlashlightActiveRecord(false)))
        );

    // Creates a creative tab with the id "randomstuffmod:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.randomstuffmod")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> FLASHLIGHT.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(FLASHLIGHT.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public RandomStuffMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so data components get registered
        DATA_COMPONENTS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (RandomStuffMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
