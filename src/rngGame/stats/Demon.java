package rngGame.stats;

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

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = element.getAtk() + atk;
	}

	public double getRes() {
		return res;
	}

	public void setRes(double res) {
		this.res = element.getRes() + res;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = element.getHp() + hp;
	}

	public double getDgc() {
		return dgc;
	}

	public void setDgc(double dgc) {
		this.dgc = element.getDgc()+dgc;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}
	
	@Override
	public String toString() {
		return "Demon [Element=" + element + ", mobName=" + mobName + ", atk=" + atk + ", res=" + res + ", hp=" + hp + ", dgc="
				+ dgc + ", lvl=" + lvl + "]";
	}
	
	

		
		

	
	
	
	
	
}
