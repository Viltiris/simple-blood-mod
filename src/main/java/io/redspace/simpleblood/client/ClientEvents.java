package io.redspace.simpleblood.client;

import io.redspace.simpleblood.client.particles.BloodGroundParticle;
import io.redspace.simpleblood.client.particles.BloodParticle;
import io.redspace.simpleblood.registry.ParticleRegistry;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

public class ClientEvents {
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.BLOOD_PARTICLE.get(), BloodParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.BLOOD_GROUND_PARTICLE.get(), BloodGroundParticle.Provider::new);
    }
}
