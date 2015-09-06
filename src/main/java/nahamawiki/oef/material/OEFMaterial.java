package nahamawiki.oef.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class OEFMaterial extends Material {

	public OEFMaterial(MapColor mapColor) {
		super(mapColor);
		this.setRequiresTool();
	}

}
