package io.github.thatmadsage.randomstuffmod;

import io.github.thatmadsage.randomstuffmod.item.FlashlightItem;
import io.github.thatmadsage.randomstuffmod.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = RandomStuffMod.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = RandomStuffMod.MODID, value = Dist.CLIENT)
public class RandomStuffModClient {
    public RandomStuffModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        RandomStuffMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        RandomStuffMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        event.enqueueWork(() -> { // ItemProperties#register is not threadsafe, so we need to call it on the main thread
            ItemProperties.register(
                // The item to apply the property to.
                ModItems.FLASHLIGHT.get(),
                // The id of the property.
                ResourceLocation.fromNamespaceAndPath(RandomStuffMod.MODID, "flashlight_active"),
                // A reference to a method that calculates the override value.
                // Parameters are the used item stack, the level context, the player using the item,
                // and a random seed you can use.
                (stack, level, player, seed) -> ((FlashlightItem)stack.getItem()).isActive(stack)?1f:0f
            );
        });
    }
}
