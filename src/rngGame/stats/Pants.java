package rngGame.stats;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class Pants.
 */
public class Pants extends Gear{


	public Pants(Rarity ty) {
		super("Pants", ty.getTextureName(), ty);
		Random gen = new Random();

		hp = switch(rarity) {
			case COMMON -> 3 + gen.nextInt(5); //5
			case UNCOMMON -> 5 + gen.nextInt(5);	//7
			case RARE -> 7 + gen.nextInt(5);	//9
			case VERYRARE -> 9 + gen.nextInt(5); //11
			case EPIC -> 12 + gen.nextInt(5); //14
			case LEGENDARY -> 15 + gen.nextInt(5); //17
			case GOD ->	18 + gen.nextInt(5); //20
			case VOID -> 23 + gen.nextInt(5); //25
		};

		dgc = switch(rarity) {
			case COMMON -> 0.01;
			case UNCOMMON -> 0.0125;
			case RARE -> 0.015;
			case VERYRARE -> 0.0175;
			case EPIC -> 0.02;
			case LEGENDARY -> 0.025;
			case GOD ->	0.0275;
			case VOID -> 0.03;
		};

		res = switch(rarity) {
			case COMMON -> 0.010;
			case UNCOMMON -> 0.0150;
			case RARE -> 0.0175;
			case VERYRARE -> 0.020;
			case EPIC -> 0.025;
			case LEGENDARY -> 0.0275;
			case GOD ->	0.030;
			case VOID -> 0.1/3;
		};

		atk = switch(rarity) {
			case VOID -> 17 + gen.nextInt(4);// 17-20
			default -> 0;
		};
	}
}
