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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
import io.github.thatmadsage.randomstuffmod.block.Blocks;
import io.github.thatmadsage.randomstuffmod.item.FlashlightItem;
import io.github.thatmadsage.randomstuffmod.item.Items;
import io.github.thatmadsage.randomstuffmod.block.ExampleBlock;
import io.github.thatmadsage.randomstuffmod.component.DataComponents;
import io.github.thatmadsage.randomstuffmod.component.DataComponents.FlashlightActiveRecord;
import io.github.thatmadsage.randomstuffmod.effect.MobEffects;
import io.netty.buffer.ByteBuf;

@Mod(RandomStuffMod.MODID)
public class RandomStuffMod {
    public static final String MODID = "randomstuffmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);  
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.randomstuffmod"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> Items.FLASHLIGHT.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(Items.FLASHLIGHT.get());
                output.accept(Items.EXAMPLE_BLOCK_ITEM.get());
            }).build());


    public RandomStuffMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        Blocks.BLOCKS.register(modEventBus);
        Items.ITEMS.register(modEventBus);
        DataComponents.DATA_COMPONENTS.register(modEventBus);
        MobEffects.MOB_EFFECTS.register(modEventBus);
        
        CREATIVE_MODE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
