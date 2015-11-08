package nahamawiki.oef.render;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.model.ModelEECapacitor;
import nahamawiki.oef.model.ModelPowered;
import nahamawiki.oef.tileentity.TileEntityEECapacitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEECapacitor extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture_armor = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
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
		
		if(machine.getCreeper())
		{
			int f1 = machine.tick;
			ModelPowered model = new ModelPowered(f1 , f1);
			GL11.glPushMatrix();
			GL11.glDepthMask(true);
	        this.bindTexture(texture_armor);
	        GL11.glMatrixMode(GL11.GL_TEXTURE);
	        //GL11.glLoadIdentity();
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

	        //model.render((Entity) null, 0, 0, 0, f2, f3, 0.0625F);
	        model.base.render(0.0625f);

	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
	}

}
