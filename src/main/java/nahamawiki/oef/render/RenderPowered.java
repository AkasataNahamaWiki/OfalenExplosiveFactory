package nahamawiki.oef.render;

import nahamawiki.oef.model.ModelPowered;
import nahamawiki.oef.tileentity.TileEntityEEMachineBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPowered extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture_armor = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) {
		TileEntityEEMachineBase machine;
		if (tileEntity instanceof TileEntityEEMachineBase) {
			machine = (TileEntityEEMachineBase) tileEntity;
		} else {
			return;
		}
		int f1 = machine.tick;
		ModelPowered model = new ModelPowered(f1 , f1);

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		for(int i = 0; i < 6; i++)
		{
			String name = tileEntity.getWorldObj().getBlock(machine.xCoord, machine.yCoord, machine.zCoord).getBlockTextureFromSide(i).getIconName().substring(4);
			ResourceLocation textures = new ResourceLocation("oef:textures/blocks/" + name + ".png");
			Minecraft.getMinecraft().renderEngine.bindTexture(textures);
			model.block[i].render(0.0625F);
		}
		//model.base2.render(0.0625f);
		GL11.glPopMatrix();

		if(machine.getCreeper())
		{
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