package facejup.mce.enums;

import facejup.mce.util.Chat;

public enum TeamType {
	
	FFA(Chat.translate("&bFree For All")),TWOTEAMS(Chat.translate("&bRed VS Blue")),FOURTEAMS(Chat.translate("&bFour Teams"));

	public String broadcast;
	TeamType(String broadcast)
	{
		this.broadcast = broadcast;
	}
}
