package nahamawiki.oef.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.ofalenmod.model.ModelLaser;
import nahamawiki.oef.entity.EntityCannonLaser;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderCannonLaser extends Render {

	private final ResourceLocation bulletTextures;
	protected ModelBase modelBullet;

	public RenderCannonLaser(ModelBase model, String color) {
		super();
		this.modelBullet = model;
		this.shadowSize = 0.0F;
		if (color == "EP" || color == "BO") {
			this.bulletTextures = new ResourceLocation("oef:textures/models/" + color + ".png");
		} else {
			this.bulletTextures = new ResourceLocation("ofalenmod:textures/entity/" + color + ".png");
		}
	}

	public void renderLaser(EntityCannonLaser entity, double x, double y, double z, float par5, float par6) {
		ModelLaser model = (ModelLaser) this.modelBullet;

		this.bindEntityTexture(entity);
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(2.0F, 2.0F, 2.0F, 1.0F);
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par6, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par6, -1.0F, 0.0F, 0.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		model.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	protected ResourceLocation getTexture(EntityCannonLaser par1EntityArrow) {
		return bulletTextures;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getTexture((EntityCannonLaser) entity);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float par5, float par6) {
		this.renderLaser((EntityCannonLaser) entity, x, y, z, par5, par6);
	}

}
