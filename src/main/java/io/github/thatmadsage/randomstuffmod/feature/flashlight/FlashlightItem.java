package io.github.thatmadsage.randomstuffmod.feature.flashlight;

import java.util.ArrayList;
import java.util.List;

import io.github.thatmadsage.randomstuffmod.RandomStuffMod;
import io.github.thatmadsage.randomstuffmod.RandomStuffMod.FlashlightActiveRecord;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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
        tooltipComponents.add(Component.literal("State: " + (isActive(stack) ? "on": "off")));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!isActive(stack) || level.isClientSide || !(isSelected || (entity instanceof Player player && player.getOffhandItem() == (stack)))) {
            return;
        }
        var look_angle = entity.getLookAngle();
        var raycast_start = entity.getEyePosition();
        var raycast_end = raycast_start.add(look_angle.normalize().scale(6d));
        
        var max_vec = maxVec(List.of(raycast_start, raycast_end));
        var min_vec = minVec(List.of(raycast_start, raycast_end));
        var size = max_vec.subtract(min_vec);
        var center = max_vec.add(min_vec).scale(0.5);

        var bounds = AABB.ofSize(center, size.x+1, size.y+1, size.z+1);

        var entities = level.getEntities(entity, bounds); 

        for (var target_entity : entities) {
            if (target_entity instanceof LivingEntity living_entity){
                if (target_entity.getBoundingBox().clip(raycast_start, raycast_end).isPresent() && living_entity.hasLineOfSight(entity)){
                    living_entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 2));
                }
            }
        }
    }

    public Vec3 maxVec(Iterable<Vec3> vecs){
        var first_item = vecs.iterator().next();
        var x = first_item.x;
        var y = first_item.y;
        var z = first_item.z;
        for(var vec : vecs){
            x = Math.max(x, vec.x);
            y = Math.max(y, vec.y);
            z = Math.max(z, vec.z);
        }
        return new Vec3(x, y, z);
    }

    public Vec3 minVec(Iterable<Vec3> vecs){
        var first_item = vecs.iterator().next();
        var x = first_item.x;
        var y = first_item.y;
        var z = first_item.z;
        for(var vec : vecs){
            x = Math.min(x, vec.x);
            y = Math.min(y, vec.y);
            z = Math.min(z, vec.z);
        }
        return new Vec3(x, y, z);
    }

    

    public boolean isActive(ItemStack stack) {
        return stack.get(RandomStuffMod.FLASHLIGHT_ACTIVE).active();
    }

}
