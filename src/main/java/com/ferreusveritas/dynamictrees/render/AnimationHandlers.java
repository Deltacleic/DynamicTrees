package com.ferreusveritas.dynamictrees.render;

import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * This class hold different animation handlers for EntityFallingTree.
 * The idea is that a unique animation could be used for a certain circumstance.
 * 
 * @author ferreusveritas
 *
 */
public class AnimationHandlers {
	
	public static final AnimationHandler defaultAnimationHandler = new AnimationHandler() {
		
		@Override
		public void initMotion(EntityFallingTree entity) {
			entity.motionY = 0.48;
			entity.motionX = 0.3 * (entity.world.rand.nextFloat() - 0.5f);
			entity.motionZ = 0.3 * (entity.world.rand.nextFloat() - 0.5f);
		}
		
		@Override
		public void handleMotion(EntityFallingTree entity) {
			entity.motionY -= 0.03;//Gravity
			//entity.motionY = 0.0;
			entity.posX += entity.motionX;
			entity.posY += entity.motionY;
			entity.posZ += entity.motionZ;
			entity.rotationYaw += 2;
			entity.rotationPitch += 6;
			
			entity.rotationPitch = MathHelper.wrapDegrees(entity.rotationPitch);
			entity.rotationYaw = MathHelper.wrapDegrees(entity.rotationYaw);
			
		}
		
		@Override
		public void dropPayload(EntityFallingTree entity) {
			World world = entity.world;
			BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
			entity.getPayload().forEach(i -> Block.spawnAsEntity(world, pos, i));
			entity.getDestroyData().leavesDrops.forEach(bis -> Block.spawnAsEntity(world, entity.getDestroyData().cutPos.add(bis.pos), bis.stack));
		}
		
		public boolean shouldDie(EntityFallingTree entity) {
			return entity.ticksExisted > 30;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void renderTransform(EntityFallingTree entity, float entityYaw, float partialTicks) {
			
			float yaw = MathHelper.wrapDegrees(com.ferreusveritas.dynamictrees.util.MathHelper.angleDegreesInterpolate(entity.prevRotationYaw, entity.rotationYaw, partialTicks));
			float pit = MathHelper.wrapDegrees(com.ferreusveritas.dynamictrees.util.MathHelper.angleDegreesInterpolate(entity.prevRotationPitch, entity.rotationPitch, partialTicks));			
			
			Vec3d mc = entity.getMassCenter();
			GlStateManager.translate(mc.x, mc.y, mc.z);
			GlStateManager.rotate(-yaw, 0, 1, 0);
			GlStateManager.rotate(pit, 1, 0, 0);
			GlStateManager.translate(-mc.x - 0.5, -mc.y, -mc.z - 0.5);
		}
	};
	
	public static final AnimationHandler demoAnimationHandler = new AnimationHandler() {
		
		@Override
		public void initMotion(EntityFallingTree entity) { }
		
		@Override
		public void handleMotion(EntityFallingTree entity) {
			entity.motionY = 0.0;
			entity.posX += entity.motionX;
			entity.posY += entity.motionY;
			entity.posZ += entity.motionZ;
			entity.rotationYaw += 6;
			entity.rotationPitch += 2;
			
			entity.rotationPitch = MathHelper.wrapDegrees(entity.rotationPitch);
			entity.rotationYaw = MathHelper.wrapDegrees(entity.rotationYaw);
		}
		
		@Override
		public void dropPayload(EntityFallingTree entity) { }
		
		@Override
		public boolean shouldDie(EntityFallingTree entity) {
			return false;
		}
		
		@Override
		public void renderTransform(EntityFallingTree entity, float entityYaw, float partialTicks) {
			
			float yaw = MathHelper.wrapDegrees(com.ferreusveritas.dynamictrees.util.MathHelper.angleDegreesInterpolate(entity.prevRotationYaw, entity.rotationYaw, partialTicks));
			float pit = MathHelper.wrapDegrees(com.ferreusveritas.dynamictrees.util.MathHelper.angleDegreesInterpolate(entity.prevRotationPitch, entity.rotationPitch, partialTicks));			
			
			Vec3d mc = entity.getMassCenter();
			GlStateManager.translate(mc.x, mc.y, mc.z);
			GlStateManager.rotate(-yaw, 0, 1, 0);
			GlStateManager.rotate(pit, 1, 0, 0);
			GlStateManager.translate(-mc.x - 0.5, -mc.y, -mc.z - 0.5);			
		}
		
	};
	
}