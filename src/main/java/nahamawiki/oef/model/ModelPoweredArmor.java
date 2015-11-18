package nahamawiki.oef.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPoweredArmor extends ModelBase {
	// モデルの直方体を代入する変数
	ModelRenderer base;

	public ModelPoweredArmor() {
		// テクスチャの縦と横のサイズ
		textureWidth = 64;
		textureHeight = 32;

		// モデルの形を作る
		base = new ModelRenderer(this, 0, 0);
		base.addBox(0, 0, 0, 16, 32, 16);
		base.setRotationPoint(-8F, 50F, -8F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		// 描画
		base.render(f5);
	}

}
