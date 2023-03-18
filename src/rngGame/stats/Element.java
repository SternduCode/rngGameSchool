	package rngGame.stats;

import java.util.Random;

import rngGame.tile.Difficulty;

public enum Element {
	Fire(4, 0.01, 20, 0.005, 1), 
	Water(4, 0.01, 20, 0.005, 1), 
	Plant(4, 0.01, 20, 0.005, 1), 
	Shadow(6, 0.02, 24, 0.01, 1), 
	Light(6, 0.02, 24, 0.01, 1), 
	Void(10, 0.03, 30, 0.015, 1),
	SupernovaGuardians(15, 0.05, 40, 0.03, 1);

	private int atk;
	private double res;
	private int hp;
	private double dgc;
	private int lvl;



	Element(int atkNeu, double resNeu, int hpNeu, double dgcNeu, int lvl){
		this.atk = atkNeu;
		this.res = resNeu;
		this.hp = hpNeu;
		this.dgc = dgcNeu;
		this.lvl = lvl;
	}
	
	public int getAtk() {
		return atk;
	}

	public double getRes() {
		return res;
	}

	public int getHp() {
		return hp;
	}

	public double getDgc() {
		return dgc;
	}
	
	public int getLvl() {
		return lvl;
	}
}
