package io.redspace.simpleblood.client.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class BloodGroundParticleType extends ParticleType<BloodGroundParticleOptions> {
    public BloodGroundParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    @Override
    public MapCodec<BloodGroundParticleOptions> codec() {
        return BloodGroundParticleOptions.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, BloodGroundParticleOptions> streamCodec() {
        return BloodGroundParticleOptions.STREAM_CODEC;
    }
}
