package nahamawiki.oef.core;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class OEFConfigCore {

	public static String updateType;
	public static int maxTier;

	/** Configファイルを読み込む */
	public static void loadConfig(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile(), true);
		cfg.load();
		updateType = cfg.getString("UpdateNotifyType", "General", "CHAT", "");
		maxTier = cfg.getInt("maxTier", "General", 256, 0, Integer.MAX_VALUE, "The maximum number of connections");
		cfg.save();
	}

}
