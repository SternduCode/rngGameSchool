package rngGame.stats;

import java.util.Random;

public class Sword extends Gear {

	public Sword(Rarity itemRarity) {
		super("Sword", itemRarity.getTextureName(), itemRarity);
		Random gen = new Random();
		setAtk(switch (getRarity()) {
			case COMMON -> 2 + gen.nextInt(3);     // 2-4
			case UNCOMMON -> 3 + gen.nextInt(3);   // 3-5
			case RARE -> 5 + gen.nextInt(3);       // 5-7
			case VERY_RARE -> 7 + gen.nextInt(4);  // 7-10
			case EPIC -> 9 + gen.nextInt(4);       // 9-12
			case LEGENDARY -> 11 + gen.nextInt(4); // 11-14
			case GOD -> 13 + gen.nextInt(4);       // 13-16
			case VOID -> 17 + gen.nextInt(4);      // 17-20
		});
		setHp(switch (getRarity()) {
			case VOID -> 23 + gen.nextInt(5);
			default -> 0;
		});
		setRes(switch (getRarity()) {
			case VOID -> 0.1 / 3;
			default -> 0.0;
		});
		setDgc(switch (getRarity()) {
			case VOID -> 0.03;
			default -> 0.0;
		});
	}

}
