package nahamawiki.oef.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import nahamawiki.oef.model.ModelEECapacitor;
import nahamawiki.oef.tileentity.TileEntityEECapacitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderEECapacitor extends TileEntitySpecialRenderer {

	private final ModelEECapacitor model = new ModelEECapacitor();

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) {
		TileEntityEECapacitor machine;
		if (tileEntity instanceof TileEntityEECapacitor) {
			machine = (TileEntityEECapacitor) tileEntity;
		} else {
			return;
		}
		model.setConnectingArray(machine.getConnectingArray());
		model.setSideTypes(machine.getSideTypes());
		String textureName = "EECapacitor";
		textureName += "-" + machine.getLevel(machine.getBlockMetadata()) + "-" + machine.isHoldingEE();
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		ResourceLocation textures = new ResourceLocation(OEFCore.DOMEINNAME + "textures/models/duct/" + textureName + ".png");
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		this.model.render((Entity) null, 0, 0, 0, 0, 0, 0.03125F);
		GL11.glPopMatrix();
	}

}
