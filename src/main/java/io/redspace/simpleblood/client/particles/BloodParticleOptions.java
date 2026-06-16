package io.redspace.simpleblood.client.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.simpleblood.registry.ParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BloodParticleOptions(int color) implements ParticleOptions {
    public static final MapCodec<BloodParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("color").forGetter(BloodParticleOptions::color)
            ).apply(instance, BloodParticleOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BloodParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            BloodParticleOptions::color,
            BloodParticleOptions::new
    );

    public float red() {
        return red(color);
    }

    public float green() {
        return green(color);
    }

    public float blue() {
        return blue(color);
    }

    public static float red(int color) {
        return ((color >> 16) & 0xFF) / 255f;
    }

    public static float green(int color) {
        return ((color >> 8) & 0xFF) / 255f;
    }

    public static float blue(int color) {
        return (color & 0xFF) / 255f;
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.BLOOD_EMITTER.get();
    }
}
