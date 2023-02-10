package rngGame.Stats;

import java.util.Random;

import rngGame.tile.Difficulty;

public class Demon {
	private Element enu;
	private Difficulty difficult;
	
	
	private String mobName;
	private int atk;
	private double res;
	private int hp;
	private double dgc;
	private int lvl;
	
	public Demon(Element wahl , String mobName) {
		this.enu = wahl;
		this.mobName = mobName;
		this.atk = enu.getAtk();
		this.res = enu.getRes();
		this.hp = enu.getHp();
		this.dgc = enu.getDgc();
		this.lvl = enu.getLvl();
	}
	

		
		

	
	
	
	
	
}
