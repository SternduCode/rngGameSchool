package rngGame.stats;

import java.util.Random;

public class Helmet extends Item{
	private Rarity rarity;
	private int hp;
	private double res;
	private double dgc;
	private int atk;
	

	public Helmet(Rarity ty) {
		super("Helmet",ty.getTextureName());
		this.rarity = ty;
		Random gen = new Random();
		
		hp = switch(this.rarity) {
		case COMMON -> 3 + gen.nextInt(5); //5
		case UNCOMMON -> 5 + gen.nextInt(5);	//7
		case RARE -> 7 + gen.nextInt(5);	//9
		case VERYRARE -> 9 + gen.nextInt(5); //11
		case EPIC -> 12 + gen.nextInt(5); //14
		case LEGENDARY -> 15 + gen.nextInt(5); //17
		case GOD ->	18 + gen.nextInt(5); //20
		case VOID -> 23 + gen.nextInt(5); //25
		};
		
		dgc = switch(this.rarity) { 
		case COMMON -> 0.01;
		case UNCOMMON -> 0.0125;
		case RARE -> 0.015;
		case VERYRARE -> 0.0175;
		case EPIC -> 0.02;
		case LEGENDARY -> 0.025;
		case GOD ->	0.0275;
		case VOID -> 0.03;
		};
		
		res = switch(this.rarity) {
		case COMMON -> 0.010; 
		case UNCOMMON -> 0.0150;	
		case RARE -> 0.0175;	
		case VERYRARE -> 0.020; 
		case EPIC -> 0.025; 
		case LEGENDARY -> 0.0275; 
		case GOD ->	0.030; 
		case VOID -> 0.1/3; 
		};
		
		atk = switch(this.rarity) {
		case VOID -> 17 + gen.nextInt(4);// 17-20
		default -> 0;
		};
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

	public int getAtk() {
		return atk;
	}
	
	@Override
	public String toString() {
		return "Helmet [rarity=" + rarity + ", hp=" + hp + ", res=" + res + ", dgc=" + dgc + ", atk=" + atk + "]";
	}


}
