package nahamawiki.oef.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEEConductor extends ModelBase {

	private boolean[] isConnecting = new boolean[6];

	ModelRenderer core;
	ModelRenderer down;
	ModelRenderer up;
	ModelRenderer right;
	ModelRenderer left;
	ModelRenderer back;
	ModelRenderer front;

	public ModelEEConductor() {
		textureWidth = 64;
		textureHeight = 32;

		core = new ModelRenderer(this, 0, 0);
		core.addBox(0F, 0F, 0F, 4, 4, 4);
		core.setRotationPoint(-2F, -2F, -2F);
		core.setTextureSize(64, 32);
		core.mirror = true;
		setRotation(core, 0F, 0F, 0F);
		down = new ModelRenderer(this, 0, 8);
		down.addBox(0F, 0F, 0F, 4, 6, 4);
		down.setRotationPoint(-2F, 2F, -2F);
		down.setTextureSize(64, 32);
		down.mirror = true;
		setRotation(down, 0F, 0F, 0F);
		up = new ModelRenderer(this, 0, 8);
		up.addBox(0F, 0F, 0F, 4, 6, 4);
		up.setRotationPoint(-2F, -8F, -2F);
		up.setTextureSize(64, 32);
		up.mirror = true;
		setRotation(up, 0F, 0F, 0F);
		right = new ModelRenderer(this, 0, 8);
		right.addBox(0F, 0F, 0F, 4, 6, 4);
		right.setRotationPoint(-2F, -2F, -2F);
		right.setTextureSize(64, 32);
		right.mirror = true;
		setRotation(right, 0F, 0F, 1.570796F);
		left = new ModelRenderer(this, 0, 8);
		left.addBox(0F, 0F, 0F, 4, 6, 4);
		left.setRotationPoint(2F, 2F, -2F);
		left.setTextureSize(64, 32);
		left.mirror = true;
		setRotation(left, 0F, 0F, -1.570796F);
		back = new ModelRenderer(this, 0, 8);
		back.addBox(0F, 0F, 0F, 4, 6, 4);
		back.setRotationPoint(-2F, 2F, 2F);
		back.setTextureSize(64, 32);
		back.mirror = true;
		setRotation(back, 1.570796F, 0F, 0F);
		front = new ModelRenderer(this, 0, 8);
		front.addBox(0F, 0F, 0F, 4, 6, 4);
		front.setRotationPoint(-2F, -2F, -2F);
		front.setTextureSize(64, 32);
		front.mirror = true;
		setRotation(front, -1.570796F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		core.render(f5);
		if (isConnecting[0])
			up.render(f5);
		if (isConnecting[1])
			down.render(f5);
		if (isConnecting[2])
			front.render(f5);
		if (isConnecting[3])
			back.render(f5);
		if (isConnecting[4])
			right.render(f5);
		if (isConnecting[5])
			left.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setConnectingArray(boolean[] isConnecting) {
		this.isConnecting = isConnecting;
	}

}