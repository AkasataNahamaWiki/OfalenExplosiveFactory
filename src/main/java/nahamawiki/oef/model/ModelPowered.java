package nahamawiki.oef.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPowered extends ModelBase {

	public final ModelRenderer base;

	public ModelPowered(int x, int y) {
		base = new ModelRenderer(this, 0, 0);
		base.setTextureOffset(x, y);
		base.addBox(0F, 0F, 0F, 16, 16, 16);
		base.setRotationPoint(-8F, -8F, -8F);
	}

	@Override
	public void render(Entity entity, float x, float y, float z, float xoff, float yoff, float size) {}

}
