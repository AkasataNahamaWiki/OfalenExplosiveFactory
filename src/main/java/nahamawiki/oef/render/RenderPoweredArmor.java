package nahamawiki.oef.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.entity.EntityPoweredArmor;
import nahamawiki.oef.model.ModelPoweredArmor;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderPoweredArmor extends RenderLiving {

	private static final ResourceLocation texture = new ResourceLocation(OEFCore.DOMEINNAME + "textures/models/null.png");
	private static final ResourceLocation texture_armor = new ResourceLocation(OEFCore.DOMEINNAME + "textures/models/armor.png");

	int tick;
	int lastLivingTick;

	private static ModelPoweredArmor creeperModel = new ModelPoweredArmor();

	public RenderPoweredArmor() {
		super(creeperModel, 0F);
	}

	protected int renderArmorPassModel(EntityPoweredArmor living, int par2, float par3) {
		GL11.glDepthMask(true);
		living.updateCoord();

		tick += 5;
		if (par2 == 1) {
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
			float f4 = 0.75F;
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

	@Override
	public void doRender(EntityLiving entity, double x, double y, double z, float yaw, float pitch) {
		if (entity instanceof EntityPoweredArmor) {
			EntityPoweredArmor armor = (EntityPoweredArmor) entity;
			armor.updateCoord();
		}
		super.doRender(entity, x, y, z, 0, 0);
	}

}
