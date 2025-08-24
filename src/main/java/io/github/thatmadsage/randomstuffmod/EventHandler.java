package io.github.thatmadsage.randomstuffmod;

import io.github.thatmadsage.randomstuffmod.effect.ModMobEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class EventHandler {
    @SubscribeEvent
    public static void affectDamage(LivingIncomingDamageEvent event) {
        var entity = event.getEntity();
        if (
            entity instanceof LivingEntity living_entity 
            && living_entity.hasEffect(ModMobEffects.DARK_SHROUD)
            && !living_entity.hasEffect(MobEffects.GLOWING)
        ) {
            event.setCanceled(true);
        }
    }
}
