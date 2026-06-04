package io.redspace.simpleblood.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.simpleblood.registry.ParticleRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static net.minecraft.world.level.ClipContext.Block.*;
import static net.minecraft.world.level.ClipContext.Fluid.NONE;

public class BloodParticle extends TextureSheetParticle {

    float scaleTransition;
    public BloodParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {

        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.xd = xd;
        this.yd = yd * 2 + .05f;
        this.zd = zd;
        this.quadSize *= 1f + (float) Math.random();
        this.scale(2.5f);
        this.lifetime = 30 + (int) (Math.random() * 10);
        this.gravity = 1.0F;
        this.pickSprite(spriteSet);

        this.rCol = ParticleRegistry.BLOOD_COLOR.x;
        this.gCol = ParticleRegistry.BLOOD_COLOR.y;
        this.bCol = ParticleRegistry.BLOOD_COLOR.z;

        this.scaleTransition = 1f + (float) Math.random();
    }


    @Override
    public void tick() {
        super.tick();
        if (this.onGround) {
            Vec3 groundLevel = level.clip(new ClipContext(this.getPos().add(0, 0.6, 0), this.getPos(), VISUAL, NONE, CollisionContext.empty())).getLocation();
            this.level.addParticle(ParticleRegistry.BLOOD_GROUND_PARTICLE.get(), groundLevel.x, groundLevel.y, groundLevel.z, 0.0D, 0.0D, 0.0D);
            this.remove();
        }
    }

    @Override
    public float getQuadSize(float partialTick) {
        float scaleMult = (this.age + partialTick) > scaleTransition ? 1f : (this.age + partialTick) / (scaleTransition * 2f) + .5f;
        return super.getQuadSize(partialTick) * scaleMult;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new BloodParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
