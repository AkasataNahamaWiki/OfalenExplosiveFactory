package nahamawiki.oef.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.model.ModelEEConductor;
import nahamawiki.oef.tileentity.TileEntityEEConductor;
import nahamawiki.oef.tileentity.TileEntityEEItemImporter;
import nahamawiki.oef.tileentity.TileEntityEEItemTransporter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderEEConductor extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture_armor = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	ModelEEConductor model = new ModelEEConductor();

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) {
		TileEntityEEConductor machine;
		if (tileEntity instanceof TileEntityEEConductor) {
			machine = (TileEntityEEConductor) tileEntity;
		} else {
			return;
		}
		model.setConnectingArray(machine.getConnectingArray());
		String textureName = "EEConductor";
		if (tileEntity instanceof TileEntityEEItemTransporter) {
			textureName = "EETransporter";
			if (tileEntity instanceof TileEntityEEItemImporter) {
				textureName = "EEImporter";
			}
		}
		textureName += "-" + machine.getLevel(machine.getBlockMetadata()) + "-" + machine.isHoldingEE();
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		ResourceLocation textures = new ResourceLocation(OEFCore.DOMEINNAME + "textures/models/duct/" + textureName + ".png");
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		model.setOffset(0, 0);
		model.render((Entity) null, 0, 0, 0, 0, 0, 0.03125F);
		GL11.glPopMatrix();

		if (machine.getCreeper()) {
			int f1 = machine.tick;
			model.setOffset(f1, f1);
			model.setConnectingArray(machine.getConnectingArray());
			GL11.glPushMatrix();
			GL11.glDepthMask(true);
			this.bindTexture(texture_armor);
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			// GL11.glLoadIdentity();
			float f2 = f1;
			float f3 = f1;
			GL11.glTranslatef(f2, f3, 0.0F);
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
			Minecraft.getMinecraft().renderEngine.bindTexture(texture_armor);

			// model.render((Entity) null, 0, 0, 0, f2, f3, 0.0625F);
			model.render((Entity) null, 0, 0, 0, 0, 0, 0.03125F);

			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
	}

}
