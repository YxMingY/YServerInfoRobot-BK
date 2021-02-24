package yxmingy.serverinforobot;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;



public class Main extends JavaPlugin implements Listener {
	private Robot robot;
	private YamlManager conf;
	@SuppressWarnings("deprecation")
	public void onEnable() {
		conf = new YamlManager(this,null,"config");
		getLogger().info("YServerInfoRobot in Enabled! Author: xMing.");
		getServer().getPluginManager().registerEvents(this, this);
		robot = new Robot(conf.getInt("机器人TCP端口",5700));
		robot.start();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		int count = getServer().getOnlinePlayers().size();
		String msgString = conf.getString("玩家进服消息");
		msgString = msgString.replaceAll("\\[服务器名\\]",conf.getString("服务器名"));
		msgString = msgString.replaceAll("\\[玩家名\\]", event.getPlayer().getName());
		msgString = msgString.replaceAll("\\[在线人数\\]", String.valueOf(count));
		robot.send(msgString);
		getLogger().info("已发送: "+msgString);
	}

	public void onDisable() {
		robot.stop = true;
	}
}
