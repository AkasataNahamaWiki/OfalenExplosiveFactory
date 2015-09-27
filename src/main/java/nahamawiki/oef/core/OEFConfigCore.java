package nahamawiki.oef.core;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class OEFConfigCore {

	public static String updateType;

	/** Configファイルを読み込む */
	public static void loadConfig(FMLPreInitializationEvent event) {

		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile(), true);
		cfg.load();
		
		updateType = cfg.getString("UpdateNotifyType", "General", "CHAT", "");
		
		cfg.save();

	}

}
