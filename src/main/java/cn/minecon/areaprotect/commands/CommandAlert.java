package cn.minecon.areaprotect.commands;

import org.bukkit.command.CommandException;

@SuppressWarnings("serial")
public class CommandAlert extends CommandException {
	public CommandAlert(String msg) {
        super(msg);
    }
}
