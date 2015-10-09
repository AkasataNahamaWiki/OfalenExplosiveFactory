package nahamawiki.oef.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.model.ModelEEConductor;
import nahamawiki.oef.tileentity.TileEntityEEConductor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RendererEEConductor extends TileEntitySpecialRenderer {

	private final ModelEEConductor model = new ModelEEConductor();

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) {
		TileEntityEEConductor machine;
		if (tileEntity instanceof TileEntityEEConductor) {
			machine = (TileEntityEEConductor) tileEntity;
		} else {
			return;
		}
		this.model.setConnectingArray(machine.getConnectingArray());
		String textureName = "EEConductor";
		// textureName += "-" + machine.getLevel(machine.getBlockMetadata()) + "-" + machine.isHoldingEE();
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		ResourceLocation textures = new ResourceLocation(OEFCore.DOMEINNAME + "textures/models/" + textureName + ".png");
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		this.model.render((Entity) null, 0, 0, 0, 0, 0, 0.0625F);
		GL11.glPopMatrix();
	}

}
