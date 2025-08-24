package io.github.thatmadsage.randomstuffmod.effect;

import io.github.thatmadsage.randomstuffmod.RandomStuffMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMobEffects {
        // Create a Deferred Register to hold mob effects which will all be registered under the "randomstuffmod" namespace  
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, RandomStuffMod.MODID);

    public static final Holder<MobEffect> DARK_SHROUD = MOB_EFFECTS.register("dark_shroud", () -> new DarkShroudMobEffect(MobEffectCategory.NEUTRAL,0x000000));
}
