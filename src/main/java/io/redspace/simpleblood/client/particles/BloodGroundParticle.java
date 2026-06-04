package io.redspace.simpleblood.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.simpleblood.registry.ParticleRegistry;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class BloodGroundParticle extends TextureSheetParticle {
    private static final Vector3f ROTATION_VECTOR = Util.make(new Vector3f(0.5F, 0.5F, 0.5F), Vector3f::normalize);
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);
    private static final float DEGREES_90 = Mth.PI / 2f;

    public BloodGroundParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {

        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize *= 4f + (float) Math.random() * 3;
        this.lifetime = 200 + (int) (Math.random() * 200);
        this.gravity = 1.0F;
        this.pickSprite(spriteSet);

        this.rCol = ParticleRegistry.BLOOD_COLOR.x;
        this.gCol = ParticleRegistry.BLOOD_COLOR.y;
        this.bCol = ParticleRegistry.BLOOD_COLOR.z;
    }


    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        this.alpha = 1.0F - Mth.clamp(((float) this.age + partialTick - 90) / (float) this.lifetime, 0.2F, .7F);
//backface
//        this.renderRotatedParticle(buffer, camera, partialTick, (p_234005_) -> {
//            p_234005_.mul(Axis.YP.rotation(0));
//            p_234005_.mul(Axis.XP.rotation(-DEGREES_90));
//        });
        //frontface (up)
        float quadSize = this.getQuadSize(partialTick);
        if(this.age + partialTick <= SPLAT_IN_TIME){
            quadSize *= (this.age + partialTick) / (SPLAT_IN_TIME * 2f) + .5f;
        }
        float yrot = age + partialTick;
        this.renderRotatedParticle(buffer, camera, partialTick, quadSize, (p_234000_) -> {
            p_234000_.mul(Axis.YP.rotation(-(float) Math.PI + yrot * Mth.DEG_TO_RAD));
            p_234000_.mul(Axis.XP.rotation(DEGREES_90));
        });
    }

    private static final float SPLAT_IN_TIME = 1.5f;

    private void renderRotatedParticle(VertexConsumer pConsumer, Camera camera, float partialTick, float quadSize, Consumer<Quaternionf> pQuaternion) {
        Vec3 cameraPos = camera.getPosition();
        float localX = (float) (Mth.lerp(partialTick, this.xo, this.x) - cameraPos.x());
        float localY = (float) (Mth.lerp(partialTick, this.yo, this.y) - cameraPos.y()) + 0.01f + .005f * (this.age / (float) this.lifetime);
        float localZ = (float) (Mth.lerp(partialTick, this.zo, this.z) - cameraPos.z());
        Quaternionf quaternion = (new Quaternionf()).setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());

        pQuaternion.accept(quaternion);
        quaternion.transform(TRANSFORM_VECTOR);

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};


        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(quadSize * 0.5f); // vector is a 2x2 plane, cut in half
            vector3f.add(localX, localY, localZ);
        }

        Vec3 worldExtentMin = new Vec3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Vec3 worldExtentMax = new Vec3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        for (Vector3f corner : avector3f) {
            Vec3 worldCorner = cameraPos.add(corner.x(), corner.y(), corner.z());
            worldExtentMin = new Vec3(
                    Math.min(worldExtentMin.x, worldCorner.x),
                    Math.min(worldExtentMin.y, worldCorner.y),
                    Math.min(worldExtentMin.z, worldCorner.z)
            );
            worldExtentMax = new Vec3(
                    Math.max(worldExtentMax.x, worldCorner.x),
                    Math.max(worldExtentMax.y, worldCorner.y),
                    Math.max(worldExtentMax.z, worldCorner.z)
            );
        }
        int light = this.getLightColor(partialTick);

        for (BlockPos blockpos : BlockPos.betweenClosed(BlockPos.containing(worldExtentMin), BlockPos.containing(worldExtentMax))) {
            level.addParticle(ParticleTypes.FLAME, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 0, 0, 0);
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, blockpos.getX() , blockpos.getY(), blockpos.getZ() + 1, 0, 0, 0);
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, blockpos.getX() + 1, blockpos.getY(), blockpos.getZ(), 0, 0, 0);
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, blockpos.getX() + 1, blockpos.getY(), blockpos.getZ() + 1, 0, 0, 0);
            
        }

//        this.makeCornerVertex(pConsumer, avector3f[0], this.getU1(), this.getV1(), light);
//        this.makeCornerVertex(pConsumer, avector3f[1], this.getU1(), this.getV0(), light);
//        this.makeCornerVertex(pConsumer, avector3f[2], this.getU0(), this.getV0(), light);
//        this.makeCornerVertex(pConsumer, avector3f[3], this.getU0(), this.getV1(), light);
    }

    private void makeCornerVertex(VertexConsumer pConsumer, Vector3f pVertex, float pU, float pV, int pPackedLight) {
        pConsumer.addVertex(pVertex.x(), pVertex.y(), pVertex.z())
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(pU, pV)
                .setLight(pPackedLight);
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
            return new BloodGroundParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
