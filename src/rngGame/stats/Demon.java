package rngGame.stats;

import java.util.Random;

import javafx.scene.image.Image;
import rngGame.entity.MonsterNPC;

// TODO: Auto-generated Javadoc
/**
 * The Class Demon.
 */
public class Demon {

	/** The element. */
	private final Element element;

	/** The gen. */
	Random gen = new Random();

	/** The mob name. */
	private final String mobName;

	/** The Item 4 list. */
	private Gear[] Item4List = new Gear[4];

	/** The atk. */
	private int atk;

	/** The res. */
	private double res;

	/** The hp. */
	private int maxhp;

	/** The currenthp. */
	private int currenthp;

	/** The dgc. */
	private double dgc;


	/** The lvl. */
	private int lvl,maxExp;

	/** The currentexp. */
	private int currentexp = 0;

	/** The demon. */
	private final MonsterNPC demon;


	/**
	 * Instantiates a new demon.
	 *
	 * @param wahl the wahl
	 * @param mobName the mob name
	 * @param demon the demon
	 */
	public Demon(Element wahl , String mobName, MonsterNPC demon) {
		this.demon = demon;
		element = wahl;
		this.mobName = mobName;
		atk = element.getAtk()+gen.nextInt(6);
		res = element.getRes();
		maxhp = element.getHp()+ gen.nextInt(11);
		dgc = element.getDgc();
		lvl = element.getLvl();
		currenthp = getMaxHp();
	}

	/**
	 * Change currenthp.
	 *
	 * @param currenthp the currenthp
	 */
	public void changeCurrenthp(int currenthp) {
		currenthp = Math.min(this.currenthp+currenthp, getMaxHp());
		currenthp = Math.max(currenthp, 0);
		this.currenthp = currenthp;
	}

	/**
	 * Gets the atk.
	 *
	 * @return the atk
	 */
	public int getAtk() {
		return atk+getAtkList();
	}

	/**
	 * Gets the atk list.
	 *
	 * @return the atk list
	 */
	public int getAtkList() {
		int atkList = 0;
		for (Gear element2 : Item4List) if(element2!=null) atkList += element2.getAtk();
		return atkList;
	}

	/**
	 * Gets the current exp.
	 *
	 * @return the current exp
	 */
	public int getCurrentExp() {
		return currentexp;
	}

	/**
	 * Gets the currenthp.
	 *
	 * @return the currenthp
	 */
	public int getCurrenthp() {
		return currenthp;
	}

	/**
	 * Gets the demon.
	 *
	 * @return the demon
	 */
	public MonsterNPC getDemon() {
		return demon;
	}

	/**
	 * Gets the demon img.
	 *
	 * @return the demon img
	 */
	public Image getDemonImg() {
		return getDemon().getImages().get(getDemon().getCurrentKey()).get(0);
	}

	/**
	 * Gets the dgc.
	 *
	 * @return the dgc
	 */
	public double getDgc() {
		return dgc + getDgcList();
	}

	/**
	 * Gets the dgc list.
	 *
	 * @return the dgc list
	 */
	public double getDgcList() {
		double dgcList = 0.0;
		for (Gear element2 : Item4List) if(element2!=null) dgcList += element2.getDgc();
		return dgcList;
	}

	/**
	 * Gets the element.
	 *
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}

	/**
	 * Gets the hp list.
	 *
	 * @return the hp list
	 */
	public int getHpList() {
		int hpList = 0;
		for (Gear element2 : Item4List) if(element2!=null) hpList += element2.getHp();
		return hpList;
	}

	/**
	 * Gets the item 4 list.
	 *
	 * @return the item 4 list
	 */
	public Gear[] getItem4List() {
		return Item4List;
	}

	/**
	 * Gets the lvl.
	 *
	 * @return the lvl
	 */
	public int getLvl() {
		return lvl;
	}

	/**
	 * Gets the max exp.
	 *
	 * @return the max exp
	 */
	public int getMaxExp() {
		maxExp = lvl*lvl+lvl*5;
		return maxExp;
	}

	/**
	 * Gets the hp.
	 *
	 * @return the hp
	 */
	public int getMaxHp() {
		return maxhp+getHpList();
	}

	/**
	 * Gets the mob name.
	 *
	 * @return the mob name
	 */
	public String getMobName() {
		return mobName;
	}

	/**
	 * Gets the res.
	 *
	 * @return the res
	 */
	public double getRes() {
		return res + getResList();
	}

	/**
	 * Gets the res list.
	 *
	 * @return the res list
	 */
	public double getResList() {
		double resList = 0.0;
		for (Gear element2 : Item4List) if(element2!=null) resList += element2.getRes();
		return resList;
	}

	/**
	 * Sets the atk.
	 *
	 * @param atk the new atk
	 */
	public void setAtk(int atk) {
		this.atk = element.getAtk() + atk;
	}


	/**
	 * Sets the current exp.
	 *
	 * @param currentexp the new current exp
	 */
	public void setCurrentExp(int currentexp) {
		this.currentexp = currentexp;
	}


	/**
	 * Sets the dgc.
	 *
	 * @param dgc the new dgc
	 */
	public void setDgc(double dgc) {
		this.dgc = element.getDgc()+dgc;
	}


	/**
	 * Sets the item 4 list.
	 *
	 * @param item4List the new item 4 list
	 */
	public void setItem4List(Gear[] item4List) {
		Item4List = item4List;
	}


	/**
	 * Sets the lvl.
	 *
	 * @param lvl the new lvl
	 */
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	/**
	 * Sets the hp.
	 *
	 * @param hp the new hp
	 */
	public void setMaxHp(int hp) {
		maxhp = element.getHp() + hp;
	}

	/**
	 * Sets the res.
	 *
	 * @param res the new res
	 */
	public void setRes(double res) {
		this.res = element.getRes() + res;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Demon [Element=" + element + ", mobName=" + mobName + ", atk=" + atk + ", res=" + res + ", hp=" + maxhp + ", dgc="
				+ dgc + ", lvl=" + lvl + "]";
	}

	public boolean testExp(int c) {
		if(c == maxExp) {
			return true;
		} else {
			return false;
		}
	}
	
	public void levelup() {
		if(testExp(currentexp)) {
			currentexp = 0;
			getMaxExp();
			setLvl(getLvl()+1);
			       if(getLvl() % 5 == 1) {
				setAtk(getAtk()+1);
			} else if(getLvl() % 5 == 2) {
				setMaxHp(getMaxHp()+1);
			} else if(getLvl() % 5 == 3) {
				setRes(getRes()+0.0025);
			} else if(getLvl() % 5 == 4) {
				setDgc(getDgc()+0.0015);
			} else if(getLvl() % 5 == 0) {
				setMaxHp(getMaxHp()+1);
			}
		}
	}
}
