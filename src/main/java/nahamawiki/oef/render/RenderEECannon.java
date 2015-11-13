package nahamawiki.oef.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.model.ModelEECannon;
import nahamawiki.oef.tileentity.TileEntityEECannon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderEECannon extends TileEntitySpecialRenderer {

	private ModelEECannon model = new ModelEECannon(0, 0);
	private ModelEECannon powered = new ModelEECannon(0, 0);
	int lastTick;

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) {
		TileEntityEECannon machine;
		if (tileEntity instanceof TileEntityEECannon) {
			machine = (TileEntityEECannon) tileEntity;
		} else {
			return;
		}

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		ResourceLocation textures = new ResourceLocation(OEFCore.DOMEINNAME + "textures/models/EECannon.png");
		float pYawOffset = this.interpolateRotation(0, 0, 1);
		float pYaw = this.interpolateRotation(machine.getPrevRotationYaw(), machine.getRotationYaw(), 1);
		float pPitch = machine.getPrevRotationPitch() + (machine.getRotationPitch() - machine.getPrevRotationPitch());
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		model.render((Entity) null, 0, 0, 0, -(pYaw - pYawOffset), pPitch, 0.0625F);
		GL11.glPopMatrix();

		if (machine.getCreeper()) {
			if (machine.tick != lastTick) {
				lastTick = machine.tick;
				powered = new ModelEECannon(machine.tick, machine.tick);
			}
			GL11.glPushMatrix();
			GL11.glDepthMask(true);
			GL11.glMatrixMode(GL11.GL_TEXTURE);

			GL11.glTranslatef(machine.tick, machine.tick, 0.0F);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glEnable(GL11.GL_BLEND);
			float f4 = 0.5F;
			GL11.glColor4f(f4, f4, f4, 1.0F);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
			textures = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
			pYawOffset = this.interpolateRotation(0, 0, 1);
			pYaw = this.interpolateRotation(machine.getPrevRotationYaw(), machine.getRotationYaw(), 1);
			pPitch = machine.getPrevRotationPitch() + (machine.getRotationPitch() - machine.getPrevRotationPitch());

			Minecraft.getMinecraft().renderEngine.bindTexture(textures);
			powered.render((Entity) null, 0, 0, 0, -(pYaw - pYawOffset), pPitch, 0.0625F);
			GL11.glPopMatrix();
		}
	}

	private float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_) {
		float f3;

		for (f3 = p_77034_2_ - p_77034_1_; f3 < -180.0F; f3 += 360.0F) {
			;
		}

		while (f3 >= 180.0F) {
			f3 -= 360.0F;
		}

		return p_77034_1_ + p_77034_3_ * f3;
	}

}
