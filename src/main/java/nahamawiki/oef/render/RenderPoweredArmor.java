package nahamawiki.oef.render;

import java.util.Random;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.entity.EntityPoweredArmor;
import nahamawiki.oef.model.ModelPoweredArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPoweredArmor extends RenderLiving {
	private static final ResourceLocation texture = new ResourceLocation(OEFCore.DOMEINNAME + "textures/models/null.png");
	private static final ResourceLocation texture_armor = new ResourceLocation(OEFCore.DOMEINNAME + "textures/models/armor.png");

	int tick;

	/** The creeper model. */
	private static ModelBase creeperModel = new ModelPoweredArmor();

	public RenderPoweredArmor() {
		super(creeperModel, 0F);
	}

	/**
	 * A method used to render a creeper's powered form as a pass model.
	 */
	protected int renderArmorPassModel(EntityPoweredArmor living, int par2, float par3) {
		GL11.glDepthMask(true);
		if (par2 == 1) {
			tick++;
			Random rand = new Random();
			float f1 = par3 + tick;
			this.bindTexture(texture_armor);
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glLoadIdentity();
			float f2 = f1 * 0.00045F;
			float f3 = f1 * 0.00045F;
			GL11.glTranslatef(f2, f3, 0.0F);
			this.setRenderPassModel(this.creeperModel);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glEnable(GL11.GL_BLEND);
			float f4 = 0.5F;
			GL11.glColor4f(f4, f4, f4, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			return 1;
		}

		if (par2 == 2) {
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
		}

		return -1;
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	@Override
	protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
		return this.renderArmorPassModel((EntityPoweredArmor) par1EntityLiving, par2, par3);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return texture;
	}

	@Override
	protected int inheritRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
		return -1;
	}
	
	 public void doRender(EntityLiving entity, double x, double y, double z, float yaw, float pitch)
	    {
	        if(entity instanceof EntityPoweredArmor)
	        {
	        	super.doRender((EntityPoweredArmor)entity, x, y, z, yaw, pitch);
	        }
	    }
	 
	 public void doRender(EntityPoweredArmor entity, double x, double y, double z, float yaw, float pitch)
	    {
		 	EntityPlayer player = entity.getOwner();
		 	if(player != null)
		 	{
		 		super.doRender((EntityLivingBase)entity, player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		 	}
	    }

}
