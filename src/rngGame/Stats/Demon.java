package rngGame.Stats;

import java.util.Random;

import rngGame.tile.Difficulty;

public class Demon {
	private Element element;
	private Difficulty difficult;
	
	
	private String mobName;
	private int atk;
	private double res;
	private int hp;
	private double dgc;
	private int lvl;
	
	public Demon(Element wahl , String mobName) {
		this.element = wahl;
		this.mobName = mobName;
		this.atk = element.getAtk();
		this.res = element.getRes();
		this.hp = element.getHp();
		this.dgc = element.getDgc();
		this.lvl = element.getLvl();
	}

	@Override
	public String toString() {
		return "Demon [Element=" + element + ", mobName=" + mobName + ", atk=" + atk + ", res=" + res + ", hp=" + hp + ", dgc="
				+ dgc + ", lvl=" + lvl + "]";
	}
	
	

		
		

	
	
	
	
	
}
