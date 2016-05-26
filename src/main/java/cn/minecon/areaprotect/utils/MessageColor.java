package cn.minecon.areaprotect.utils;

import org.bukkit.ChatColor;

public class MessageColor {
	public static String replaceColors(String input) {
        input = input.replace("<BLACK>", ChatColor.BLACK.toString());
        input = input.replace("<DARK_BLUE>", ChatColor.DARK_BLUE.toString());
        input = input.replace("<DARK_GREEN>", ChatColor.DARK_GREEN.toString());
        input = input.replace("<DARK_AQUA>", ChatColor.DARK_AQUA.toString());
        input = input.replace("<DARK_RED>", ChatColor.DARK_RED.toString());
        input = input.replace("<DARK_PURPLE>", ChatColor.DARK_PURPLE.toString());
        input = input.replace("<GOLD>", ChatColor.GOLD.toString());
        input = input.replace("<GRAY>", ChatColor.GRAY.toString());
        input = input.replace("<DARK_GRAY>", ChatColor.DARK_GRAY.toString());
        input = input.replace("<BLUE>", ChatColor.BLUE.toString());
        input = input.replace("<GREEN>", ChatColor.GREEN.toString());
        input = input.replace("<AQUA>", ChatColor.AQUA.toString());
        input = input.replace("<RED>", ChatColor.RED.toString());
        input = input.replace("<LIGHT_PURPLE>", ChatColor.LIGHT_PURPLE.toString());
        input = input.replace("<YELLOW>", ChatColor.YELLOW.toString());
        input = input.replace("<WHITE>", ChatColor.WHITE.toString());
        input = input.replace("<BOLD>", ChatColor.BOLD.toString());
        input = input.replace("<UNDERLINE>", ChatColor.UNDERLINE.toString());
        input = input.replace("<ITALIC>", ChatColor.ITALIC.toString());
        input = input.replace("<STRIKE>", ChatColor.STRIKETHROUGH.toString());
        input = input.replace("<MAGIC>", ChatColor.MAGIC.toString());
        input = input.replace("<RESET>", ChatColor.RESET.toString());
        return input;
    }
}
