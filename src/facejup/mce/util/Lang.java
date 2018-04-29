package facejup.mce.util;

import java.util.Arrays;
import java.util.List;

public class Lang {

	public static String Tag = Chat.translate("&9&l(&b&l&oMC&f&l&oElim&9&l) &a&o");
	public static String NoPerm = Tag + Chat.translate("&cYou do not have permission to perform this command!");
	public static String ConsoleUse = Tag + Chat.translate("&cYou must be a player to use this command!");
	public static String InvalidSyn = Tag + Chat.translate("&cInvalid Syntax! Use /arena help to display help information!");
	public static String NullPlayer = Tag + Chat.translate("&cThat player could not be found!");
	public static String CurrentlyPlaying = Tag + Chat.translate("&cYou are currently playing!");
	public static String MatchNotRunning = Tag + Chat.translate("&cThere is no match currently running!");
	public static String NotEnoughMana = Tag + Chat.translate("&cNot enough mana");
	public static String afkcheckTag = Chat.translate("&9&l(&b&l&oAFK&r&l&oCheck&9&l): &c&l");
	
	public static void autoBroadcast()
	{
		List<String> broadcasts = Arrays.asList("&7&oYou can use &8/kits info &7&oto learn more about the different kits","&7&oWe try to balance the kits. While some are obviously better against others, they can be easily countered as well.","&7&oIf the server's messing up, there's a good chance we know about it. Wait for the automatic restart every hour to see if it still is.","&7&oFeeling left out? Try typing &8/spectate","&7&oDiscord Link: https://discord.gg/sSAmbh7","&7&oAll maps are made by our community. Feel free to upload your own in our discord!","&7&oThe compass points to the player closest to you in an arena. It is also used to change kits!", "&7&oMost food can be used to heal you! Rightclick with it in your hand when you're low.", "&7&oType &8/ach &7&oor &8/achievements &7&oto view our custom achievements.", "&7&oYou can unlock kits through the &5&lKit Selector &7&oor purchase them in the &8/buy &7&omenu!");
		int random = Numbers.getRandom(0, broadcasts.size()-1);
		Chat.bc("&9&l(&b&l&oBroad&f&l&ocast&9&l) " + Chat.translate(broadcasts.get(random)));
	}
}
