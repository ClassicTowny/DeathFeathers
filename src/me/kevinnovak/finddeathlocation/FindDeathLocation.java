package me.kevinnovak.finddeathlocation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FindDeathLocation extends JavaPlugin implements Listener{
    public void onEnable() {
        Bukkit.getServer().getLogger().info("FindDeathLocation Enabled!");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }
  
    public void onDisable() {
        Bukkit.getServer().getLogger().info("FindDeathLocation Disabled!");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            Player player = e.getEntity();
            player.sendMessage(ChatColor.DARK_BLUE + "You Died");
            getConfig().set(player.getName() + ".X", player.getLocation().getBlockX());
            getConfig().set(player.getName() + ".Y", player.getLocation().getBlockY());
            getConfig().set(player.getName() + ".Z", player.getLocation().getBlockZ());
            getConfig().set(player.getName() + ".World", player.getLocation().getWorld().getName());
            saveConfig();
        }
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Console cannot run finddeath!");
            return true;
        }

        Player player = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("finddeath")) {
            String playername = player.getName();
            player.sendMessage("Location: " + getConfig().getString(playername + ".World"));
            World world = getServer().getWorld(getConfig().getString(playername + ".World"));
            int xPosition = Integer.parseInt(getConfig().getString(playername + ".X"));
            int yPosition = Integer.parseInt(getConfig().getString(playername + ".Y"));
            int zPosition = Integer.parseInt(getConfig().getString(playername + ".Z"));
            Location deathlocation = new Location(world, xPosition, yPosition, zPosition);
            int distanceToDeath = (int)deathlocation.distance(player.getLocation());
            player.sendMessage(ChatColor.GOLD + "Your death location is "+ distanceToDeath + " blocks away!");
        }
        return true;
    }
}