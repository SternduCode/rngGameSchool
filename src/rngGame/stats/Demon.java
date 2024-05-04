package rngGame.stats;

import java.util.Random;

import javafx.scene.image.Image;
import rngGame.entity.MonsterNPC;

public class Demon {

	private final Element element;

	Random gen = new Random();

	private final String mobName;

	private Gear[] Item4List = new Gear[4];

	private int atk;

	private double res;

	private int maxhp;

	private int currenthp;

	private double dgc;

	private int lvl,maxExp;

	private int currentexp = 0;

	private final MonsterNPC demon;

	public Demon(Element wahl, String mobName, MonsterNPC demon) {
		this.demon = demon;
		element = wahl;
		this.mobName = mobName;
		atk = element.getAtk() +gen.nextInt(6);
		res = element.getRes();
		maxhp = element.getHp() + gen.nextInt(11);
		dgc = element.getDgc();
		lvl = element.getLvl();
		currenthp = getMaxHp();
	}

	public void changeCurrenthp(int currenthp) {
		currenthp = Math.min(this.currenthp + currenthp, getMaxHp());
		currenthp = Math.max(currenthp, 0);
		this.currenthp = currenthp;
	}

	public int getAtk() {
		return atk+getAtkList();
	}

	public int getAtkList() {
		int atkList = 0;
		for (Gear element2 : Item4List) if(element2!=null) atkList += element2.getAtk();
		return atkList;
	}

	public int getCurrentExp() {
		return currentexp;
	}

	public int getCurrenthp() {
		return currenthp;
	}

	public MonsterNPC getDemon() {
		return demon;
	}

	public Image getDemonImg() {
		return getDemon().getImages().get(getDemon().getCurrentKey()).get(0);
	}

	public double getDgc() {
		return dgc + getDgcList();
	}

	public double getDgcList() {
		double dgcList = 0.0;
		for (Gear element2 : Item4List) if(element2!=null) dgcList += element2.getDgc();
		return dgcList;
	}

	public Element getElement() {
		return element;
	}

	public int getHpList() {
		int hpList = 0;
		for (Gear element2 : Item4List) if(element2!=null) hpList += element2.getHp();
		return hpList;
	}

	public Gear[] getItem4List() {
		return Item4List;
	}

	public int getLvl() {
		return lvl;
	}

	public int getMaxExp() {
		maxExp = lvl*lvl+lvl*5;
		return maxExp;
	}

	public int getMaxHp() {
		return maxhp+getHpList();
	}

	public String getMobName() {
		return mobName;
	}

	public double getRes() {
		return res + getResList();
	}

	public double getResList() {
		double resList = 0.0;
		for (Gear element2 : Item4List) if(element2!=null) resList += element2.getRes();
		return resList;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public void setCurrentExp(int currentexp) {
		if(currentexp < getMaxExp()) {
		this.currentexp = currentexp;
		} else {
			levelup(currentexp);
		}
	}

	public void setDgc(double dgc) {
		this.dgc = dgc;
	}

	public void setItem4List(Gear[] item4List) {
		Item4List = item4List;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public void setMaxHp(int hp) {
		maxhp = hp;
	}

	public void setRes(double res) {
		this.res = res;
	}

	@Override
	public String toString() {
		return "Demon [Element=" + element + ", mobName=" + mobName + ", atk=" + atk + ", res=" + res + ", hp=" + maxhp + ", dgc="
				+ dgc + ", lvl=" + lvl + "]";
	}
	
	public void levelup(int i) {
		System.out.println("LEVEL UP!");
			currentexp = i-maxExp;
			setLvl(getLvl()+1);
			       if(getLvl() % 5 == 1) {
				setAtk(atk+1);
			} else if(getLvl() % 5 == 2) {
				boolean hasMaxHp = currenthp == maxhp;
				setMaxHp(maxhp+1);
				if (hasMaxHp) {
					changeCurrenthp(1);
				}
			} else if(getLvl() % 5 == 3) {
				setRes(res+0.0025);
			} else if(getLvl() % 5 == 4) {
				setDgc(dgc+0.0015);
			} else if(getLvl() % 5 == 0) {
				boolean hasMaxHp = currenthp == maxhp;
				setMaxHp(maxhp+1);
				if (hasMaxHp) {
					changeCurrenthp(1);
				}
			}
			getMaxExp();
		}
	}

