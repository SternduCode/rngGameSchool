package rngGame.stats;

import java.util.Random;

public class Sword extends Item{

	private Rarity rarity;
	private int atk;
	
	

	public Sword(Rarity ty) {
		super("Sword",ty.getTextureName());
		this.rarity = ty;
		Random gen = new Random();
		atk = switch(this.rarity) {
		case COMMON -> 2 + gen.nextInt(3); 							// 2-4
		case UNCOMMON -> 3 + gen.nextInt(3);						// 3-5
		case RARE -> 5 + gen.nextInt(3);							// 5-7
		case VERYRARE -> 7 + gen.nextInt(4); 						// 7-10
		case EPIC -> 9 + gen.nextInt(4); 							// 9-12
		case LEGENDARY -> 11 + gen.nextInt(4);						// 11-14
		case GOD ->	13 + gen.nextInt(4);							// 13-16
		case VOID -> 17 + gen.nextInt(4);							// 17-20
		};
	}
	
	public int getAtk() {
		return atk;
	}

}
