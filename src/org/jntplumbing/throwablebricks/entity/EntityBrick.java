package org.jntplumbing.throwablebricks.entity;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.event.CraftEventFactory;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jntplumbing.throwablebricks.event.BrickContactEvent;

import net.minecraft.server.v1_11_R1.AxisAlignedBB;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.Blocks;
import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityItem;
import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.EnumMoveType;
import net.minecraft.server.v1_11_R1.EnumParticle;
import net.minecraft.server.v1_11_R1.ItemStack;
import net.minecraft.server.v1_11_R1.Items;
import net.minecraft.server.v1_11_R1.MathHelper;
import net.minecraft.server.v1_11_R1.MinecraftServer;
import net.minecraft.server.v1_11_R1.MovingObjectPosition;
import net.minecraft.server.v1_11_R1.MovingObjectPosition.EnumMovingObjectType;
import net.minecraft.server.v1_11_R1.Vec3D;
import net.minecraft.server.v1_11_R1.World;

public class EntityBrick extends EntityItem {

	public Entity entityHit;
	public EntityLiving shooter;
	
	public EntityBrick(World world, double d0, double d1, double d2, ItemStack itemstack, EntityLiving shooter) {
		super(world, d0, d1, d2, new ItemStack(Items.BRICK));
		this.shooter = shooter;
		this.pickupDelay = Integer.MAX_VALUE;
	}
	
	@Override
	public void A_() {
		if (getItemStack().isEmpty()) {
			die();
			return;
		}
		/*if (!(this.world.isClientSide)) {*/
			//setFlag(6, aO()); //Update if it's glowing or not. We won't do this.
		/*}*/
		
		U(); //Updates portal stuff and death stuff
		this.M = this.locX;
		this.N = this.locY;
		this.O = this.locZ;

		this.lastX = this.locX;
		this.lastY = this.locY;
		this.lastZ = this.locZ;
		double d00 = this.motX;
		double d11 = this.motY;
		double d22 = this.motZ;

		if (!(isNoGravity())) {
			this.motY -= 0.03999999910593033D;
		}

		if (this.world.isClientSide)
			this.noclip = false;
		else {
			this.noclip = i(this.locX, (getBoundingBox().b + getBoundingBox().e) / 2.0D, this.locZ);
		}

		move(EnumMoveType.SELF, this.motX, this.motY, this.motZ);
		
		float drag = 0.98F;

		if (this.onGround) {
			drag = this.world
					.getType(new BlockPosition(MathHelper.floor(this.locX),
							MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ)))
					.getBlock().frictionFactor * 0.98F;
		}
		
		this.motX *= drag;
		this.motY *= 0.9800000190734863D;
		this.motZ *= drag;
		
		if (this.onGround) {
			this.motY *= -0.5D;
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
				if (entity1 == this.entityHit) {
					flag = true;
				} else if ((this.shooter != null) && (this.ticksLived < 2) && (this.entityHit == null) && (this.shooter == entity1)) {
					this.entityHit = entity1;
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

		/*if (this.entityHit != null) {
			if (flag)
				this.aw = 2;
			else if (this.aw-- <= 0) {
				this.entityHit = null;
			}
		}*/

		if (entity != null) {
			movingobjectposition = new MovingObjectPosition(entity);
		}

		if (movingobjectposition != null) {
			if ((movingobjectposition.type == MovingObjectPosition.EnumMovingObjectType.BLOCK) && (this.world.getType(movingobjectposition.a()).getBlock() == Blocks.PORTAL)) {
				e(movingobjectposition.a());
			} else {
				onCollide(movingobjectposition);

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
		float f2 = getGravity();

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
	
	private void onCollide(MovingObjectPosition mop) {
		Location loc = new Location(world.getWorld(), mop.pos.x, mop.pos.y, mop.pos.z);
		Player player = ((EntityPlayer)shooter).getBukkitEntity();
		Item item = (Item) this.getBukkitEntity();
		if (mop.type == EnumMovingObjectType.BLOCK) {
			BrickContactEvent event = new BrickContactEvent(item, loc, player, loc.getBlock());
			Bukkit.getPluginManager().callEvent(event);
		} else if (mop.type == EnumMovingObjectType.ENTITY) {
			BrickContactEvent event = new BrickContactEvent(item, loc, player, (LivingEntity)mop.entity.getBukkitEntity());
			Bukkit.getPluginManager().callEvent(event);
		}
		
	}

	public float getGravity() {
		return 0.03F;
	}

}
