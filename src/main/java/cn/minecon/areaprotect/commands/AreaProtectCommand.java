package cn.minecon.areaprotect.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.utils.MessageColor;

public class AreaProtectCommand implements CommandExecutor {
	private final List<CommandSub> commands;
	
	public AreaProtectCommand(AreaProtect plugin) {
		commands = new ArrayList<CommandSub>();
		registerSubCommand(new AdminCommand(plugin));
		registerSubCommand(new SelectCommand(plugin));
		registerSubCommand(new CreateCommand(plugin));
		registerSubCommand(new InfoCommand(plugin));
		registerSubCommand(new FlagCommand(plugin));
		registerSubCommand(new PlayerCommand(plugin));
		registerSubCommand(new DeleteCommand(plugin));
		registerSubCommand(new ListCommand(plugin));
		registerSubCommand(new MyCommand(plugin));
		registerSubCommand(new TPCommand(plugin));
		registerSubCommand(new TPSetCommand(plugin));
		registerSubCommand(new SetOwnerCommand(plugin));
		registerSubCommand(new ServerLandCommand(plugin));
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
            final CommandSub sub = findExecutor(args[0]);
            if (sub != null) {
                if (sub.getPermissionNode() == null || sender.hasPermission(sub.getPermissionNode())) {
                    if (!sub.allowConsoleSender()) {
                        if (sender instanceof ConsoleCommandSender) {
                        	sender.sendMessage(Config.getMessage("PlayerContextRequired"));
                        	return true;
                        }
                    }
                    
                    try {
                    	if (sub.onCommand(sender, args)) {
                            return true;
                        }
                    } catch (CommandAlert e) {
                    	sender.sendMessage(e.getMessage());
                    	return true;
                    }
                    
                    sender.sendMessage(Config.getMessage("HelpHeader"));
                    showCommandHelp(sender, label, sub);
                    return true;
                } else {
                	sender.sendMessage(Config.getMessage("NoPermissionForThisCommand"));
                    return true;
                }
            }
        }
		
		sender.sendMessage(Config.getMessage("HelpHeader"));
        for (CommandSub sub : commands) {
            if (sender.hasPermission(sub.getPermissionNode())) {
            	showCommandHelp(sender, label, sub);
            }
        }
        
		return true;
	}
	
	private void showCommandHelp(CommandSender sender, String label, CommandSub sub) {
        final String commandString = "/" + label + " " + sub.getCommand();
        final String key = sub.getHelp();
        if (key == null) {
        	sender.sendMessage(commandString);
        	return;
        }
        for (String message : Config.getConfig().getStringList("Messages." + key)) {
        	sender.sendMessage(commandString + " " + MessageColor.replaceColors(message));
        }
	}
	
	public synchronized void registerSubCommand(CommandSub sub) {
		commands.add(sub);
    }

    public synchronized void unregisterSubCommand(Class<?> unreg) {
        final Iterator<CommandSub> it = commands.iterator();
        while(it.hasNext()) {
            final CommandSub cmd = it.next();
            if (cmd.getClass().equals(unreg)) {
                it.remove();
            }
        }
    }

    public CommandSub findExecutor(String cmd) {
        for (CommandSub sub : commands) {
            if (sub.getCommand().equalsIgnoreCase(cmd)) {
                return sub;
            }
        }
        return null;
    }
}
