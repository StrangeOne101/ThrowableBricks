package org.jntplumbing.throwablebricks.entity;

import java.util.List;

import org.bukkit.craftbukkit.v1_11_R1.event.CraftEventFactory;

import net.minecraft.server.v1_11_R1.AxisAlignedBB;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.Blocks;
import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityItem;
import net.minecraft.server.v1_11_R1.EnumParticle;
import net.minecraft.server.v1_11_R1.ItemStack;
import net.minecraft.server.v1_11_R1.Items;
import net.minecraft.server.v1_11_R1.MathHelper;
import net.minecraft.server.v1_11_R1.MovingObjectPosition;
import net.minecraft.server.v1_11_R1.Vec3D;
import net.minecraft.server.v1_11_R1.World;

public class EntityBrick extends EntityItem {

	public EntityBrick(World world, double d0, double d1, double d2, ItemStack itemstack) {
		super(world, d0, d1, d2, new ItemStack(Items.BRICK));
	}
	
	@Override
	public void A_() {
		this.M = this.locX;
		this.N = this.locY;
		this.O = this.locZ;
		super.A_();
		if (this.shake > 0) {
			this.shake -= 1;
		}

		if (this.inGround) {
			if (this.world.getType(new BlockPosition(this.blockX, this.blockY, this.blockZ)).getBlock() == this.inBlockId) {
				this.au += 1;
				if (this.au == 1200) {
					die();
				}

				return;
			}

			this.inGround = false;
			this.motX *= this.random.nextFloat() * 0.2F;
			this.motY *= this.random.nextFloat() * 0.2F;
			this.motZ *= this.random.nextFloat() * 0.2F;
			this.au = 0;
			this.av = 0;
		} else {
			this.av += 1;
		}

		Vec3D vec3d = new Vec3D(this.locX, this.locY, this.locZ);
		Vec3D vec3d1 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
		MovingObjectPosition movingobjectposition = this.world.rayTrace(vec3d, vec3d1);

		vec3d = new Vec3D(this.locX, this.locY, this.locZ);
		vec3d1 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
		if (movingobjectposition != null) {
			vec3d1 = new Vec3D(movingobjectposition.pos.x, movingobjectposition.pos.y, movingobjectposition.pos.z);
		}

		Entity entity = null;
		List list = this.world.getEntities(this, getBoundingBox().b(this.motX, this.motY, this.motZ).g(1.0D));
		double d0 = 0.0D;
		boolean flag = false;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = (Entity) list.get(i);

			if (entity1.isInteractable()) {
				if (entity1 == this.c) {
					flag = true;
				} else if ((this.shooter != null) && (this.ticksLived < 2) && (this.c == null) && (this.shooter == entity1)) {
					this.c = entity1;
					flag = true;
				} else {
					flag = false;
					AxisAlignedBB axisalignedbb = entity1.getBoundingBox().g(0.300000011920929D);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.b(vec3d, vec3d1);

					if (movingobjectposition1 != null) {
						double d1 = vec3d.distanceSquared(movingobjectposition1.pos);

						if ((d1 < d0) || (d0 == 0.0D)) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}
		}

		if (this.c != null) {
			if (flag)
				this.aw = 2;
			else if (this.aw-- <= 0) {
				this.c = null;
			}
		}

		if (entity != null) {
			movingobjectposition = new MovingObjectPosition(entity);
		}

		if (movingobjectposition != null) {
			if ((movingobjectposition.type == MovingObjectPosition.EnumMovingObjectType.BLOCK) && (this.world.getType(movingobjectposition.a()).getBlock() == Blocks.PORTAL)) {
				e(movingobjectposition.a());
			} else {
				a(movingobjectposition);

				if (this.dead) {
					CraftEventFactory.callProjectileHitEvent(this, movingobjectposition);
				}
			}

		}

		this.locX += this.motX;
		this.locY += this.motY;
		this.locZ += this.motZ;
		float f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);

		this.yaw = (float) (MathHelper.c(this.motX, this.motZ) * 57.2957763671875D);

		for (this.pitch = (float) (MathHelper.c(this.motY, f) * 57.2957763671875D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F)
			;
		while (this.pitch - this.lastPitch >= 180.0F) {
			this.lastPitch += 360.0F;
		}

		while (this.yaw - this.lastYaw < -180.0F) {
			this.lastYaw -= 360.0F;
		}

		while (this.yaw - this.lastYaw >= 180.0F) {
			this.lastYaw += 360.0F;
		}

		this.pitch = (this.lastPitch + (this.pitch - this.lastPitch) * 0.2F);
		this.yaw = (this.lastYaw + (this.yaw - this.lastYaw) * 0.2F);
		float f1 = 0.99F;
		float f2 = j();

		if (isInWater()) {
			for (int j = 0; j < 4; ++j) {
				this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX - (this.motX * 0.25D), this.locY - (this.motY * 0.25D), this.locZ - (this.motZ * 0.25D), this.motX, this.motY, this.motZ, new int[0]);
			}

			f1 = 0.8F;
		}

		this.motX *= f1;
		this.motY *= f1;
		this.motZ *= f1;
		if (!(isNoGravity())) {
			this.motY -= f2;
		}

		setPosition(this.locX, this.locY, this.locZ);
	}

}
