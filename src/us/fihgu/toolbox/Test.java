package us.fihgu.toolbox;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import us.fihgu.toolbox.http.FileContext;
import us.fihgu.toolbox.http.HTTPServer;
import us.fihgu.toolbox.http.StaticContextGenerator;
import us.fihgu.toolbox.network.NetworkUtils;
import us.fihgu.toolbox.packet.PacketUtils;


public class Test implements Listener
{
	public static int count = 0;
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		String parts[] = event.getMessage().split(" ");
		
		if(parts.length > 0 && parts[0].equalsIgnoreCase("/test"))
		{
			count++;
			String args[] = Arrays.asList(parts).subList(Math.min(1, parts.length - 1), parts.length).toArray(new String[]{});
			player.sendMessage("The test command has been used " + count + " times, args[" + args.length + "]");
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			String ip = Loader.instance.getConfig().getString("http.host");
			System.out.println(ip);
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			event.setCancelled(true);
		}
	}
	
	public void register(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public static void main(String[] args) throws UnknownHostException
	{
		
	}
	
	public static void backup()
	{
		System.out.println(NetworkUtils.getExternalIP());
		
		InetSocketAddress address = new InetSocketAddress(NetworkUtils.getLocalIP(), 80);
		System.out.println(address);
		HTTPServer server = new HTTPServer(address);
		server.debug = true;
		server.putContextGenerator("/file.zip", new StaticContextGenerator(new FileContext(Paths.get("D:/Minecraft/Minecraft Plugins/Spigot-1.9/Test Server/resourcepacks/TerrariaCraft.zip"))));
		server.startServer();
	}
}
