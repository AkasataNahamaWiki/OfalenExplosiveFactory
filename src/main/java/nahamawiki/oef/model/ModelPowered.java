package nahamawiki.oef.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPowered extends ModelBase{
	public final ModelRenderer base;
	public final ModelRenderer base2;
	public final ModelRenderer[] block = new ModelRenderer[6];

	public ModelPowered(int xoff , int yoff) {
		
		base = new ModelRenderer(this, 0, 0);
		base.setTextureOffset(xoff, yoff);
		base.addBox(0F, 0F, 0F, 16, 16, 16);
		base.setRotationPoint(-8F, -8F, -8F);
		
		base2 = new ModelRenderer(this, 0, 0);
		this.textureHeight = 16;
		this.textureWidth = 16;
		for(int i = 0; i < 6; i++)
		{
			block[i] = new ModelRenderer(this, 0, 0);
			block[i].textureHeight = 16;
			block[i].textureWidth = 16;
			switch(i)
			{
				case 0:
				{
					block[i].addBox(0f, 0f, 0f, 16, 0, 16);
					break;
				}
				
				case 1:
				{
					block[i].addBox(0f, 16f, 0f, 16, 0, 16);
					break;
				}
				
				case 2:
				{
					block[i].addBox(0f, 0f, 0f, 16, 16, 0);
					break;
				}
				
				case 3:
				{
					block[i].addBox(0f, 0f, 16f, 16, 16, 0);
					break;
				}
				
				case 4:
				{
					block[i].addBox(0f, 0f, 0f, 0, 16, 16);
					break;
				}
				
				case 5:
				{
					block[i].addBox(16f, 0f, 0f, 0, 16, 16);
					break;
				}
			}
			block[i].rotateAngleZ = (float) Math.PI;
			
			block[i].setRotationPoint(8F, 8F, -8F);
		}
			
	}

	@Override
	public void render(Entity entity, float x, float y, float z, float xoff, float yoff, float size) {
	}


}
