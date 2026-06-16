package io.redspace.simpleblood.client.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class BloodParticleType extends ParticleType<BloodParticleOptions> {
    public BloodParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    @Override
    public MapCodec<BloodParticleOptions> codec() {
        return BloodParticleOptions.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, BloodParticleOptions> streamCodec() {
        return BloodParticleOptions.STREAM_CODEC;
    }
}
