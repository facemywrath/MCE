package facejup.mce.enums;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class Reward {
	
	private int coinReward;
	private Kit kitReward;
	
	public Reward(int coinReward)
	{
		this.coinReward = coinReward;
	}
	
	public Reward(Kit kit)
	{
		this.kitReward = kit;
	}
	
	public Pair<Integer, Kit> getReward()
	{
		return Pair.of(coinReward, kitReward);
	}

}
