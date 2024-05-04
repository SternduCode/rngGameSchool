package rngGame.stats;

import java.util.Random;

public class Helmet extends Gear {

	public Helmet(Rarity itemRarity) {
		super("Helmet", itemRarity.getTextureName(), itemRarity);
		Random gen = new Random();
		setHp(switch (getRarity()) {
			case COMMON -> 3 + gen.nextInt(5);         //  5
			case UNCOMMON -> 5 + gen.nextInt(5);       //  7
			case RARE -> 7 + gen.nextInt(5);           //  9
			case VERY_RARE -> 9 + gen.nextInt(5);      // 11
			case EPIC -> 12 + gen.nextInt(5);          // 14
			case LEGENDARY -> 15 + gen.nextInt(5);     // 17
			case GOD -> 18 + gen.nextInt(5);           // 20
			case VOID -> 23 + gen.nextInt(5);          // 25
		});
		setDgc(switch (getRarity()) {
			case COMMON -> 0.01;
			case UNCOMMON -> 0.0125;
			case RARE -> 0.015;
			case VERY_RARE -> 0.0175;
			case EPIC -> 0.02;
			case LEGENDARY -> 0.025;
			case GOD -> 0.0275;
			case VOID -> 0.03;
		});
		setRes(switch (getRarity()) {
			case COMMON -> 0.010;
			case UNCOMMON -> 0.0150;
			case RARE -> 0.0175;
			case VERY_RARE -> 0.020;
			case EPIC -> 0.025;
			case LEGENDARY -> 0.0275;
			case GOD -> 0.030;
			case VOID -> 0.1 / 3;
		});
		setAtk(switch (getRarity()) {
			case VOID -> 17 + gen.nextInt(4); // 17-20
			default -> 0;
		});
	}

}
