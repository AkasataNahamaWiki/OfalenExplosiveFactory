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

	private final ModelEECannon model = new ModelEECannon();

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
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		this.model.render((Entity) null, 0, 0, 0, machine.getRotationPitch(), machine.getRotationYaw(), 0.0625F);
		GL11.glPopMatrix();
	}

}
