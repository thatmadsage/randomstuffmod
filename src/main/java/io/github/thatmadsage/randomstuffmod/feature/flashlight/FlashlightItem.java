package io.github.thatmadsage.randomstuffmod.feature.flashlight;

import java.util.List;

import io.github.thatmadsage.randomstuffmod.RandomStuffMod;
import io.github.thatmadsage.randomstuffmod.RandomStuffMod.FlashlightActiveRecord;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.level.Level;

public class FlashlightItem extends Item {

    public FlashlightItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand){
        ItemStack stack = player.getItemInHand(usedHand);
        level.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.LEVER_CLICK,
            SoundSource.NEUTRAL,
            0.3F,
            1.5f
        );
        FlashlightActiveRecord flashlight_active = stack.get(RandomStuffMod.FLASHLIGHT_ACTIVE);
        stack.set(RandomStuffMod.FLASHLIGHT_ACTIVE, new FlashlightActiveRecord(!flashlight_active.active()));
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        tooltipComponents.add(Component.literal("State: " + (stack.get(RandomStuffMod.FLASHLIGHT_ACTIVE).active() ? "on": "off")));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public static float getFlashlightActiveOverride(ItemStack stack) {
        return stack.get(RandomStuffMod.FLASHLIGHT_ACTIVE).active() ? 1f: 0f;
    }
}
