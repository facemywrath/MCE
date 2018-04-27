package facejup.mce.enums;

import facejup.mce.util.Chat;

public enum MatchType {
	NORMAL(true, ""), RANDOM(false, Chat.translate("&5Random Kits Mode")), ONEFORALL(false, Chat.translate("&5One For All Mode: &b&l%KIT%")), SUDDENDEATH(true, Chat.translate("&5Sudden Death Mode!")), BOSS(true, Chat.translate("&5Boss Mode: Boss is &b%PLAYER%"));
	
	public String broadcast;
	public boolean selectkits;
	MatchType(boolean kits, String broadcast)
	{
		this.selectkits = kits;
		this.broadcast = broadcast;
	}
	public static MatchType getTypeByName(String str)
	{
		if(str.equalsIgnoreCase("ofa"))
			return MatchType.ONEFORALL;
		for(MatchType type : MatchType.values())
		{
			if(type.name().equalsIgnoreCase(str))
				return type;
		}
		return null;
	}
}
