package nahamawiki.oef.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEECapacitor extends ModelBase {

	private boolean[] isConnecting = new boolean[6];
	private byte[] sideType = new byte[6];

	ModelRenderer core;
	ModelRenderer down;
	ModelRenderer up;
	ModelRenderer right;
	ModelRenderer left;
	ModelRenderer back;
	ModelRenderer front;
	ModelRenderer pole0;
	ModelRenderer pole1;
	ModelRenderer pole2;
	ModelRenderer pole3;
	ModelRenderer pole4;
	ModelRenderer pole5;
	ModelRenderer pole6;
	ModelRenderer pole7;
	ModelRenderer pole8;
	ModelRenderer pole9;
	ModelRenderer pole10;
	ModelRenderer pole11;
	ModelRenderer[] nozzleDown0 = new ModelRenderer[2];
	ModelRenderer[] nozzleDown1 = new ModelRenderer[2];
	ModelRenderer[] nozzleDown2 = new ModelRenderer[2];
	ModelRenderer[] nozzleDown3 = new ModelRenderer[2];
	ModelRenderer[] nozzleUp0 = new ModelRenderer[2];
	ModelRenderer[] nozzleUp1 = new ModelRenderer[2];
	ModelRenderer[] nozzleUp2 = new ModelRenderer[2];
	ModelRenderer[] nozzleUp3 = new ModelRenderer[2];
	ModelRenderer[] nozzleRight0 = new ModelRenderer[2];
	ModelRenderer[] nozzleRight1 = new ModelRenderer[2];
	ModelRenderer[] nozzleRight2 = new ModelRenderer[2];
	ModelRenderer[] nozzleRight3 = new ModelRenderer[2];
	ModelRenderer[] nozzleLeft0 = new ModelRenderer[2];
	ModelRenderer[] nozzleLeft1 = new ModelRenderer[2];
	ModelRenderer[] nozzleLeft2 = new ModelRenderer[2];
	ModelRenderer[] nozzleLeft3 = new ModelRenderer[2];
	ModelRenderer[] nozzleBack0 = new ModelRenderer[2];
	ModelRenderer[] nozzleBack1 = new ModelRenderer[2];
	ModelRenderer[] nozzleBack2 = new ModelRenderer[2];
	ModelRenderer[] nozzleBack3 = new ModelRenderer[2];
	ModelRenderer[] nozzleFront0 = new ModelRenderer[2];
	ModelRenderer[] nozzleFront1 = new ModelRenderer[2];
	ModelRenderer[] nozzleFront2 = new ModelRenderer[2];
	ModelRenderer[] nozzleFront3 = new ModelRenderer[2];

	public ModelEECapacitor() {
		textureWidth = 128;
		textureHeight = 64;

		core = new ModelRenderer(this, 0, 0);
		core.addBox(0F, 0F, 0F, 8, 8, 8);
		core.setRotationPoint(-4F, -4F, -4F);
		core.setTextureSize(64, 32);
		core.mirror = true;
		setRotation(core, 0F, 0F, 0F);
		down = new ModelRenderer(this, 32, 0);
		down.addBox(0F, 0F, 0F, 8, 12, 8);
		down.setRotationPoint(-4F, 4F, -4F);
		down.setTextureSize(64, 32);
		down.mirror = true;
		setRotation(down, 0F, 0F, 0F);
		up = new ModelRenderer(this, 32, 0);
		up.addBox(0F, 0F, 0F, 8, 12, 8);
		up.setRotationPoint(-4F, -16F, -4F);
		up.setTextureSize(64, 32);
		up.mirror = true;
		setRotation(up, 0F, 0F, 0F);
		right = new ModelRenderer(this, 32, 0);
		right.addBox(0F, 0F, 0F, 8, 12, 8);
		right.setRotationPoint(-4F, -4F, -4F);
		right.setTextureSize(64, 32);
		right.mirror = true;
		setRotation(right, 0F, 0F, 1.570796F);
		left = new ModelRenderer(this, 32, 0);
		left.addBox(0F, 0F, 0F, 8, 12, 8);
		left.setRotationPoint(4F, 4F, -4F);
		left.setTextureSize(64, 32);
		left.mirror = true;
		setRotation(left, 0F, 0F, -1.570796F);
		back = new ModelRenderer(this, 32, 0);
		back.addBox(0F, 0F, 0F, 8, 12, 8);
		back.setRotationPoint(-4F, 4F, 4F);
		back.setTextureSize(64, 32);
		back.mirror = true;
		setRotation(back, 1.570796F, 0F, 0F);
		front = new ModelRenderer(this, 32, 0);
		front.addBox(0F, 0F, 0F, 8, 12, 8);
		front.setRotationPoint(-4F, -4F, -4F);
		front.setTextureSize(64, 32);
		front.mirror = true;
		setRotation(front, -1.570796F, 0F, 0F);
		pole0 = new ModelRenderer(this, 0, 20);
		pole0.addBox(0F, 0F, 0F, 32, 4, 4);
		pole0.setRotationPoint(-16F, -16F, -16F);
		pole0.setTextureSize(64, 32);
		pole0.mirror = true;
		setRotation(pole0, 0F, 0F, 0F);
		pole1 = new ModelRenderer(this, 0, 20);
		pole1.addBox(0F, 0F, 0F, 32, 4, 4);
		pole1.setRotationPoint(-16F, -16F, 12F);
		pole1.setTextureSize(64, 32);
		pole1.mirror = true;
		setRotation(pole1, 0F, 0F, 0F);
		pole2 = new ModelRenderer(this, 0, 20);
		pole2.addBox(0F, 0F, 0F, 32, 4, 4);
		pole2.setRotationPoint(-16F, 12F, -16F);
		pole2.setTextureSize(64, 32);
		pole2.mirror = true;
		setRotation(pole2, 0F, 0F, 0F);
		pole3 = new ModelRenderer(this, 0, 20);
		pole3.addBox(0F, 0F, 0F, 32, 4, 4);
		pole3.setRotationPoint(-16F, 12F, 12F);
		pole3.setTextureSize(64, 32);
		pole3.mirror = true;
		setRotation(pole3, 0F, 0F, 0F);
		pole4 = new ModelRenderer(this, 72, 0);
		pole4.addBox(0F, 0F, 0F, 4, 24, 4);
		pole4.setRotationPoint(-16F, -12F, -16F);
		pole4.setTextureSize(64, 32);
		pole4.mirror = true;
		setRotation(pole4, 0F, 0F, 0F);
		pole5 = new ModelRenderer(this, 72, 0);
		pole5.addBox(0F, 0F, 0F, 4, 24, 4);
		pole5.setRotationPoint(-16F, -12F, 12F);
		pole5.setTextureSize(64, 32);
		pole5.mirror = true;
		setRotation(pole5, 0F, 0F, 0F);
		pole6 = new ModelRenderer(this, 72, 0);
		pole6.addBox(0F, 0F, 0F, 4, 24, 4);
		pole6.setRotationPoint(12F, -12F, -16F);
		pole6.setTextureSize(64, 32);
		pole6.mirror = true;
		setRotation(pole6, 0F, 0F, 0F);
		pole7 = new ModelRenderer(this, 72, 0);
		pole7.addBox(0F, 0F, 0F, 4, 24, 4);
		pole7.setRotationPoint(12F, -12F, 12F);
		pole7.setTextureSize(64, 32);
		pole7.mirror = true;
		setRotation(pole7, 0F, 0F, 0F);
		pole8 = new ModelRenderer(this, 72, 0);
		pole8.addBox(0F, 0F, 0F, 4, 24, 4);
		pole8.setRotationPoint(-16F, -12F, -12F);
		pole8.setTextureSize(64, 32);
		pole8.mirror = true;
		setRotation(pole8, 1.570796F, 0F, 0F);
		pole9 = new ModelRenderer(this, 72, 0);
		pole9.addBox(0F, 0F, 0F, 4, 24, 4);
		pole9.setRotationPoint(12F, -12F, -12F);
		pole9.setTextureSize(64, 32);
		pole9.mirror = true;
		setRotation(pole9, 1.570796F, 0F, 0F);
		pole10 = new ModelRenderer(this, 72, 0);
		pole10.addBox(0F, 0F, 0F, 4, 24, 4);
		pole10.setRotationPoint(-16F, 16F, -12F);
		pole10.setTextureSize(64, 32);
		pole10.mirror = true;
		setRotation(pole10, 1.570796F, 0F, 0F);
		pole11 = new ModelRenderer(this, 72, 0);
		pole11.addBox(0F, 0F, 0F, 4, 24, 4);
		pole11.setRotationPoint(12F, 16F, -12F);
		pole11.setTextureSize(64, 32);
		pole11.mirror = true;
		setRotation(pole11, 1.570796F, 0F, 0F);
		for (int i = 0; i < 2; i++) {
			nozzleDown0[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleDown0[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleDown0[i].setRotationPoint(-6F, 12F, -6F);
			nozzleDown0[i].setTextureSize(64, 32);
			nozzleDown0[i].mirror = true;
			setRotation(nozzleDown0[i], 0F, 0F, 0F);
			nozzleDown1[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleDown1[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleDown1[i].setRotationPoint(-6F, 12F, 4F);
			nozzleDown1[i].setTextureSize(64, 32);
			nozzleDown1[i].mirror = true;
			setRotation(nozzleDown1[i], 0F, 0F, 0F);
			nozzleDown2[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleDown2[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleDown2[i].setRotationPoint(-6F, 12F, -4F);
			nozzleDown2[i].setTextureSize(64, 32);
			nozzleDown2[i].mirror = true;
			setRotation(nozzleDown2[i], 0F, 0F, 0F);
			nozzleDown3[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleDown3[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleDown3[i].setRotationPoint(4F, 12F, -4F);
			nozzleDown3[i].setTextureSize(64, 32);
			nozzleDown3[i].mirror = true;
			setRotation(nozzleDown3[i], 0F, 0F, 0F);
			nozzleUp0[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleUp0[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleUp0[i].setRotationPoint(4F, -16F, 6F);
			nozzleUp0[i].setTextureSize(64, 32);
			nozzleUp0[i].mirror = true;
			setRotation(nozzleUp0[i], 0F, 1.570796F, 0F);
			nozzleUp1[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleUp1[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleUp1[i].setRotationPoint(-6F, -16F, 6F);
			nozzleUp1[i].setTextureSize(64, 32);
			nozzleUp1[i].mirror = true;
			setRotation(nozzleUp1[i], 0F, 1.570796F, 0F);
			nozzleUp2[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleUp2[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleUp2[i].setRotationPoint(-4F, -16F, 6F);
			nozzleUp2[i].setTextureSize(64, 32);
			nozzleUp2[i].mirror = true;
			setRotation(nozzleUp2[i], 0F, 1.570796F, 0F);
			nozzleUp3[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleUp3[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleUp3[i].setRotationPoint(-4F, -16F, -4F);
			nozzleUp3[i].setTextureSize(64, 32);
			nozzleUp3[i].mirror = true;
			setRotation(nozzleUp3[i], 0F, 1.570796F, 0F);
			nozzleRight0[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleRight0[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleRight0[i].setRotationPoint(-12F, -6F, 4F);
			nozzleRight0[i].setTextureSize(64, 32);
			nozzleRight0[i].mirror = true;
			setRotation(nozzleRight0[i], 0F, 0F, 1.570796F);
			nozzleRight1[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleRight1[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleRight1[i].setRotationPoint(-12F, -6F, -6F);
			nozzleRight1[i].setTextureSize(64, 32);
			nozzleRight1[i].mirror = true;
			setRotation(nozzleRight1[i], 0F, 0F, 1.570796F);
			nozzleRight2[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleRight2[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleRight2[i].setRotationPoint(-12F, -6F, -4F);
			nozzleRight2[i].setTextureSize(64, 32);
			nozzleRight2[i].mirror = true;
			setRotation(nozzleRight2[i], 0F, 0F, 1.570796F);
			nozzleRight3[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleRight3[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleRight3[i].setRotationPoint(-12F, 4F, -4F);
			nozzleRight3[i].setTextureSize(64, 32);
			nozzleRight3[i].mirror = true;
			setRotation(nozzleRight3[i], 0F, 0F, 1.570796F);
			nozzleLeft0[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleLeft0[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleLeft0[i].setRotationPoint(16F, -6F, 4F);
			nozzleLeft0[i].setTextureSize(64, 32);
			nozzleLeft0[i].mirror = true;
			setRotation(nozzleLeft0[i], 0F, 0F, 1.570796F);
			nozzleLeft1[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleLeft1[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleLeft1[i].setRotationPoint(16F, -6F, -6F);
			nozzleLeft1[i].setTextureSize(64, 32);
			nozzleLeft1[i].mirror = true;
			setRotation(nozzleLeft1[i], 0F, 0F, 1.570796F);
			nozzleLeft2[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleLeft2[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleLeft2[i].setRotationPoint(16F, -6F, -4F);
			nozzleLeft2[i].setTextureSize(64, 32);
			nozzleLeft2[i].mirror = true;
			setRotation(nozzleLeft2[i], 0F, 0F, 1.570796F);
			nozzleLeft3[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleLeft3[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleLeft3[i].setRotationPoint(16F, 4F, -4F);
			nozzleLeft3[i].setTextureSize(64, 32);
			nozzleLeft3[i].mirror = true;
			setRotation(nozzleLeft3[i], 0F, 0F, 1.570796F);
			nozzleBack0[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleBack0[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleBack0[i].setRotationPoint(4F, -6F, 12F);
			nozzleBack0[i].setTextureSize(64, 32);
			nozzleBack0[i].mirror = true;
			setRotation(nozzleBack0[i], 1.570796F, 0F, 1.570796F);
			nozzleBack1[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleBack1[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleBack1[i].setRotationPoint(-6F, -6F, 12F);
			nozzleBack1[i].setTextureSize(64, 32);
			nozzleBack1[i].mirror = true;
			setRotation(nozzleBack1[i], 1.570796F, 0F, 1.570796F);
			nozzleBack2[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleBack2[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleBack2[i].setRotationPoint(-4F, -6F, 12F);
			nozzleBack2[i].setTextureSize(64, 32);
			nozzleBack2[i].mirror = true;
			setRotation(nozzleBack2[i], 1.570796F, 0F, 1.570796F);
			nozzleBack3[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleBack3[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleBack3[i].setRotationPoint(-4F, 4F, 12F);
			nozzleBack3[i].setTextureSize(64, 32);
			nozzleBack3[i].mirror = true;
			setRotation(nozzleBack3[i], 1.570796F, 0F, 1.570796F);
			nozzleFront0[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleFront0[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleFront0[i].setRotationPoint(-6F, -6F, -16F);
			nozzleFront0[i].setTextureSize(64, 32);
			nozzleFront0[i].mirror = true;
			setRotation(nozzleFront0[i], 1.570796F, 0F, 1.570796F);
			nozzleFront1[i] = new ModelRenderer(this, 0, 28 + 6 * i);
			nozzleFront1[i].addBox(0F, 0F, 0F, 12, 4, 2);
			nozzleFront1[i].setRotationPoint(4F, -6F, -16F);
			nozzleFront1[i].setTextureSize(64, 32);
			nozzleFront1[i].mirror = true;
			setRotation(nozzleFront1[i], 1.570796F, 0F, 1.570796F);
			nozzleFront2[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleFront2[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleFront2[i].setRotationPoint(-4F, -6F, -16F);
			nozzleFront2[i].setTextureSize(64, 32);
			nozzleFront2[i].mirror = true;
			setRotation(nozzleFront2[i], 1.570796F, 0F, 1.570796F);
			nozzleFront3[i] = new ModelRenderer(this, 28, 28 + 12 * i);
			nozzleFront3[i].addBox(0F, 0F, 0F, 2, 4, 8);
			nozzleFront3[i].setRotationPoint(-4F, 4F, -16F);
			nozzleFront3[i].setTextureSize(64, 32);
			nozzleFront3[i].mirror = true;
			setRotation(nozzleFront3[i], 1.570796F, 0F, 1.570796F);
		}
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
		pole0.render(f5);
		pole1.render(f5);
		pole2.render(f5);
		pole3.render(f5);
		pole4.render(f5);
		pole5.render(f5);
		pole6.render(f5);
		pole7.render(f5);
		pole8.render(f5);
		pole9.render(f5);
		pole10.render(f5);
		pole11.render(f5);
		if (sideType[0] != 0) {
			nozzleUp0[sideType[0] - 1].render(f5);
			nozzleUp1[sideType[0] - 1].render(f5);
			nozzleUp2[sideType[0] - 1].render(f5);
			nozzleUp3[sideType[0] - 1].render(f5);
		}
		if (sideType[1] != 0) {
			nozzleDown0[sideType[1] - 1].render(f5);
			nozzleDown1[sideType[1] - 1].render(f5);
			nozzleDown2[sideType[1] - 1].render(f5);
			nozzleDown3[sideType[1] - 1].render(f5);
		}
		if (sideType[2] != 0) {
			nozzleFront0[sideType[2] - 1].render(f5);
			nozzleFront1[sideType[2] - 1].render(f5);
			nozzleFront2[sideType[2] - 1].render(f5);
			nozzleFront3[sideType[2] - 1].render(f5);
		}
		if (sideType[3] != 0) {
			nozzleBack0[sideType[3] - 1].render(f5);
			nozzleBack1[sideType[3] - 1].render(f5);
			nozzleBack2[sideType[3] - 1].render(f5);
			nozzleBack3[sideType[3] - 1].render(f5);
		}
		if (sideType[4] != 0) {
			nozzleRight0[sideType[4] - 1].render(f5);
			nozzleRight1[sideType[4] - 1].render(f5);
			nozzleRight2[sideType[4] - 1].render(f5);
			nozzleRight3[sideType[4] - 1].render(f5);
		}
		if (sideType[5] != 0) {
			nozzleLeft0[sideType[5] - 1].render(f5);
			nozzleLeft1[sideType[5] - 1].render(f5);
			nozzleLeft2[sideType[5] - 1].render(f5);
			nozzleLeft3[sideType[5] - 1].render(f5);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setConnectingArray(boolean[] isConnecting) {
		this.isConnecting = isConnecting;
	}

	public void setSideTypes(byte[] sideType) {
		this.sideType = sideType;
	}

}
