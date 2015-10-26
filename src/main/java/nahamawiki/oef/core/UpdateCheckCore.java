package nahamawiki.oef.core;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Level;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.relauncher.Side;
import nahamawiki.oef.OEFCore;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class UpdateCheckCore {

	private UpdateInfo updateInfo = null;
	private ModContainer container = Loader.instance().activeModContainer();
	private boolean isCompleted = false;

	public boolean isCompleted() {
		return this.isCompleted;
	}

	public UpdateInfo getUpdateInfo() {
		return this.updateInfo;
	}

	@SuppressWarnings("unchecked")
	public void checkUpdate() {
		new Thread("OEF update check") {
			@Override
			public void run() {
				try {
					// Update info reception
					String receivedData;
					try {
						URL url = new URL(OEFCore.meta.updateUrl);
						InputStream con = url.openStream();
						receivedData = new String(ByteStreams.toByteArray(con));
						con.close();
						OEFCore.logger.debug("receivedData:%n%s", receivedData);
					} catch (IOException e) {
						OEFCore.logger.log(Level.WARN, "Failed to receive update info.");
						return;
					}

					// Convert into Json
					List<Map<String, Object>> updateInfoList;
					try {
						updateInfoList = new Gson().fromJson(receivedData, List.class);
					} catch (JsonSyntaxException e) {
						OEFCore.logger.log(Level.WARN, "Malformed update info.");
						return;
					}

					// Retrieve update info for this MC version
					Map<String, String> updateInfoJson = findUpdateInfoForMcVersion(updateInfoList);

					if (updateInfoJson == null) {
						OEFCore.logger.info("No update info for this MC version.");
						return;
					}

					String currVersion = container.getVersion();
					currVersion = currVersion.substring(0, currVersion.indexOf("-"));

					String newVersion = updateInfoJson.get("version");
					if (!currVersion.equals(newVersion)) {
						updateInfo = new UpdateInfo();
						updateInfo.version = updateInfoJson.get("version");
						updateInfo.downloadUrl = updateInfoJson.get("downloadUrl");
					}
				} finally {
					isCompleted = true;
				}
			}

			/**
			 * Retrieve update info for current MC version
			 *
			 * @param list
			 * @return
			 */
			private Map<String, String> findUpdateInfoForMcVersion(List<Map<String, Object>> list) {
				String currentVer = container.getVersion();
				for (Map<String, Object> map : list) {
					boolean isMatched = container.acceptableMinecraftVersionRange()
							.containsVersion(new DefaultArtifactVersion((String) map.get("mcversion")));
					if (isMatched) {
						return (Map<String, String>) map.get("updateinfo");
					}
				}
				return null;
			}
		}.start();
	}

	protected void notifyUpdateChat(ICommandSender sender, Side side) {
		if (side == Side.SERVER) {
			sender.addChatMessage(new ChatComponentTranslation(
					"info.message.update", updateInfo.version, updateInfo.downloadUrl));
		} else {
			ChatStyle style = new ChatStyle();
			style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, updateInfo.downloadUrl));

			sender.addChatMessage(new ChatComponentTranslation(
					"info.message.update", updateInfo.version));
			sender.addChatMessage(new ChatComponentTranslation(
					"info.message.update.link").setChatStyle(style));
		}

	}

	private TrayIcon icon;
	Image creeper;

	protected void notifyUpdateBalloon() {
		icon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				BalloonDL();
			}
		});
		icon.displayMessage(StatCollector.translateToLocal("info.message.update"), StatCollector.translateToLocal("info.message.update.link.2"), TrayIcon.MessageType.INFO);
	}

	public void showBalloon() {
		SystemTray tray = SystemTray.getSystemTray();
		PopupMenu menu = new PopupMenu();
		MenuItem item = new MenuItem(StatCollector.translateToLocal("info.message.dl"));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				BalloonDL();
			}
		});
		menu.add(item);

		try {
			creeper = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(OEFCore.DOMEINNAME + "textures/blocks/EELamp-1.png")).getInputStream());

			icon = new TrayIcon(creeper, "OEF", menu);
			icon.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					BalloonDL();
				}
			});

			tray.add(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void BalloonDL() {
		try {
			URL url = new URL("http://www63.atwiki.jp/akasatanahama/");
			Desktop.getDesktop().browse(url.toURI());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyUpdate(ICommandSender sender, Side side) {
		if (updateInfo != null) {
			if (OEFConfigCore.updateType.equalsIgnoreCase("CHAT")) {
				this.notifyUpdateChat(sender, side);
			}

			else if (OEFConfigCore.updateType.equalsIgnoreCase("BALLOON")) {
				this.notifyUpdateBalloon();
			}

			else if (!OEFConfigCore.updateType.equalsIgnoreCase("NONE")) {
				this.notifyUpdateChat(sender, side);
			}

			OEFCore.logger.info("Update available!");
		}

	}

	public static class UpdateInfo {
		public String version;
		public String downloadUrl;
	}
}
