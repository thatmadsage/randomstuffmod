package io.github.thatmadsage.randomstuffmod.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.thatmadsage.randomstuffmod.RandomStuffMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = 
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RandomStuffMod.MODID);

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
}
