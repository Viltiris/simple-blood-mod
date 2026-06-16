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

public record BloodGroundParticleOptions(int color) implements ParticleOptions {
    public static final MapCodec<BloodGroundParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("color").forGetter(BloodGroundParticleOptions::color)
            ).apply(instance, BloodGroundParticleOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BloodGroundParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            BloodGroundParticleOptions::color,
            BloodGroundParticleOptions::new
    );

    public float red() {
        return BloodParticleOptions.red(color);
    }

    public float green() {
        return BloodParticleOptions.green(color);
    }

    public float blue() {
        return BloodParticleOptions.blue(color);
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.BLOOD_GROUND_PARTICLE.get();
    }
}
