package nahamawiki.oef.core;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

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
