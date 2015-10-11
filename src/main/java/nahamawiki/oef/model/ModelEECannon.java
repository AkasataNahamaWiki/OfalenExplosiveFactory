package nahamawiki.oef.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEECannon extends ModelBase {

	ModelRenderer pole;
	ModelRenderer barrel;
	ModelRenderer base;

	public ModelEECannon() {
		textureWidth = 128;
		textureHeight = 32;

		pole = new ModelRenderer(this, 48, 0);
		pole.addBox(-3F, 0F, -3F, 6, 8, 6);
		pole.setRotationPoint(0F, -3F, 0F);
		pole.setTextureSize(128, 32);
		setRotation(pole, 0F, 0F, 0F);
		barrel = new ModelRenderer(this, 72, 0);
		barrel.addBox(-2F, -2F, 0F, 4, 4, 8);
		barrel.setRotationPoint(0F, 2F, 0F);
		barrel.setTextureSize(128, 32);
		setRotation(barrel, 0F, 0F, 0F);
		base = new ModelRenderer(this, 0, 0);
		base.addBox(0F, 0F, 0F, 16, 5, 16);
		base.setRotationPoint(-8F, -8F, -8F);
		base.setTextureSize(128, 32);
		setRotation(base, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotation(pole, 0F, f4, 0F);
		setRotation(barrel, f3, f4, 0F);
		pole.render(f5);
		barrel.render(f5);
		base.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
