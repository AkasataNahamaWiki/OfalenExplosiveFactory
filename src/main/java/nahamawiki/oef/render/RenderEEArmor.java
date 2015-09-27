package nahamawiki.oef.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderEEArmor extends RenderPlayer {

	private static final ResourceLocation texture_armor = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

	@Override
	protected int shouldRenderPass(AbstractClientPlayer p_77032_1_, int p_77032_2_, float p_77032_3_) {
		int i = this.shouldRenderPass(p_77032_1_, p_77032_2_, p_77032_3_);
		return i;
	}

	public int shouldRenderPass(EntityPlayer p_77032_1_, int p_77032_2_, float p_77032_3_) {
		ItemStack itemstack = p_77032_1_.inventory.armorItemInSlot(3 - p_77032_2_);
		if (itemstack != null) {
			Item item = itemstack.getItem();

			if (item instanceof ItemArmor) {
				ItemArmor itemarmor = (ItemArmor) item;
				this.bindTexture(RenderBiped.getArmorResource(p_77032_1_, itemstack, p_77032_2_, null));
				ModelBiped modelbiped = p_77032_2_ == 2 ? this.modelArmor : this.modelArmorChestplate;
				modelbiped.bipedHead.showModel = p_77032_2_ == 0;
				modelbiped.bipedHeadwear.showModel = p_77032_2_ == 0;
				modelbiped.bipedBody.showModel = p_77032_2_ == 1 || p_77032_2_ == 2;
				modelbiped.bipedRightArm.showModel = p_77032_2_ == 1;
				modelbiped.bipedLeftArm.showModel = p_77032_2_ == 1;
				modelbiped.bipedRightLeg.showModel = p_77032_2_ == 2 || p_77032_2_ == 3;
				modelbiped.bipedLeftLeg.showModel = p_77032_2_ == 2 || p_77032_2_ == 3;
				float f1 = p_77032_1_.ticksExisted + p_77032_3_;
				this.bindTexture(texture_armor);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glLoadIdentity();
				float f2 = f1 * 0.01F;
				float f3 = f1 * 0.01F;
				GL11.glTranslatef(f2, f3, 0.0F);
				this.setRenderPassModel(modelbiped);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glEnable(GL11.GL_BLEND);
				float f4 = 0.5F;
				GL11.glColor4f(f4, f4, f4, 1.0F);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			}
		}
		return super.shouldRenderPass(p_77032_1_, p_77032_2_, p_77032_3_);
	}

}
