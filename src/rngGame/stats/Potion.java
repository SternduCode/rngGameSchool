package rngGame.stats;

import java.util.Random;

public class Potion extends Item {

	public final int hp;

	public Potion(Rarity itemRarity) {
		super("Potion", itemRarity.getTextureName(), itemRarity);
		Random gen = new Random();
		hp = switch (getRarity()) {
			case COMMON -> 8 + gen.nextInt(5);           //  10
			case UNCOMMON -> 15 + gen.nextInt(11);       //  20
			case RARE -> 35 + gen.nextInt(11);           //  40
			case VERY_RARE -> 40 + gen.nextInt(21);      //  50
			case EPIC -> 55 + gen.nextInt(31);           //  70
			case LEGENDARY -> 75 + gen.nextInt(31);      //  90
			case GOD -> 110 + gen.nextInt(41);           // 130
			case VOID -> 155 + gen.nextInt(51);          // 180
		};
	}

	public String toString() {
		return "Potion [rarity=" + getRarity() + ", hp=" + hp + "]";
	}

}
