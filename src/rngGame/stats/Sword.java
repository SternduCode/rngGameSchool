package rngGame.stats;

import java.util.Random;

public class Sword extends Item{

	private Rarity rarity;
	private int hp;
	private double res;
	private double dgc;
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
		
		hp = switch(this.rarity) {
		case VOID -> 23 + gen.nextInt(5);//
		default -> 0;
		};
		
		res = switch(this.rarity) {
		case VOID -> 0.1/3; 
		default -> 0;
		};
		
		dgc = switch(this.rarity) {
		case VOID -> 0.03;
		default -> 0;
		};
	}
	
	@Override
	public String toString() {
		return "Sword [rarity=" + rarity + ", hp=" + hp + ", res=" + res + ", dgc=" + dgc + ", atk=" + atk + "]";
	}

	public int getAtk() {
		return atk;
	}
	
	public int getHp() {
		return hp;
	}

	public double getRes() {
		return res;
	}

	public double getDgc() {
		return dgc;
	}

}
