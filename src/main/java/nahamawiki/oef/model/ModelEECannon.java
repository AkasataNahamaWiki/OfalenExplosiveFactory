package nahamawiki.oef.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelEECannon extends ModelBase {

	ModelRenderer pole;
	ModelRenderer barrel;
	ModelRenderer base;
	
	private static final float yawOffset = 0;
	private static final float pitchOffset = 0;

	public ModelEECannon(int xoff, int yoff) {
		textureWidth = 128;
		textureHeight = 32;

		pole = new ModelRenderer(this, 48, 0);
		if(xoff != 0 && yoff != 0)pole.setTextureOffset(xoff, yoff);
		pole.addBox(-3F, 0F, -3F, 6, 8, 6);
		pole.setRotationPoint(0F, -3F, 0F);
		pole.setTextureSize(128, 32);
		setRotation(pole, 0F, 0F, 0F);
		barrel = new ModelRenderer(this, 72, 0);
		if(xoff != 0 && yoff != 0)barrel.setTextureOffset(xoff, yoff);
		barrel.addBox(-2F, -2F, 0F, 4, 4, 8);
		barrel.setRotationPoint(0F, 2F, 0F);
		barrel.setTextureSize(128, 32);
		setRotation(barrel, 0F, 0F, 0F);
		base = new ModelRenderer(this, 0, 0);
		if(xoff != 0 && yoff != 0)base.setTextureOffset(xoff, yoff);
		base.addBox(0F, 0F, 0F, 16, 5, 16);
		base.setRotationPoint(-8F, -8F, -8F);
		base.setTextureSize(128, 32);
		setRotation(base, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float x, float y, float z, float yaw, float pitch, float size) {
		setRotation(pole, 0F, yaw + this.yawOffset, 0F);
		setRotation(barrel, pitch + this.pitchOffset, yaw + this.yawOffset, 0F);
		pole.render(size);
		barrel.render(size);
		base.render(size);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX =MathHelper.wrapAngleTo180_float( x  / (180F / (float)Math.PI));
		model.rotateAngleY =MathHelper.wrapAngleTo180_float( y  / (180F / (float)Math.PI));
		model.rotateAngleZ =MathHelper.wrapAngleTo180_float( z  / (180F / (float)Math.PI));
	}

}
