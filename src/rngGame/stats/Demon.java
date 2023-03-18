package rngGame.stats;

import java.util.Random;

import javafx.scene.image.Image;
import rngGame.entity.MonsterNPC;
import rngGame.tile.Difficulty;

// TODO: Auto-generated Javadoc
/**
 * The Class Demon.
 */
public class Demon {

	/** The element. */
	private final Element element;

	

	/** The difficult. */
	private Difficulty difficult;

	/** The gen. */
	Random gen = new Random();

	/** The mob name. */
	private final String mobName;

	

	/** The atk. */
	private int atk;

	/** The res. */
	private double res;

	/** The hp. */
	private int hp;

	/** The dgc. */
	private double dgc;

	/** The lvl. */
	private int lvl,maxExp;
	
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
		hp = element.getHp()+ gen.nextInt(11);
		dgc = element.getDgc();
		lvl = element.getLvl();
		
	}

	/**
	 * Gets the atk.
	 *
	 * @return the atk
	 */
	public int getAtk() {
		return atk;
	}

	/**
	 * Gets the dgc.
	 *
	 * @return the dgc
	 */
	public double getDgc() {
		return dgc*100;
	}

	/**
	 * Gets the hp.
	 *
	 * @return the hp
	 */
	public int getHp() {
		return hp;
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
	 * Gets the res.
	 *
	 * @return the res
	 */
	public double getRes() {
		return res * 100;
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
	 * Sets the dgc.
	 *
	 * @param dgc the new dgc
	 */
	public void setDgc(double dgc) {
		this.dgc = element.getDgc()+dgc;
	}

	/**
	 * Sets the hp.
	 *
	 * @param hp the new hp
	 */
	public void setHp(int hp) {
		this.hp = element.getHp() + hp;
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
	 * Sets the res.
	 *
	 * @param res the new res
	 */
	public void setRes(double res) {
		this.res = element.getRes() + res;
	}
	
	public Image getDemonImg() {
		return getDemon().getImages().get(getDemon().getCurrentKey()).get(0);
	}
	
	public MonsterNPC getDemon() {
		return demon;
	}
	
	public String getMobName() {
		return mobName;
	}
	
	public Element getElement() {
		return element;
	}
	
	public int getMaxExp() {
		maxExp = (lvl*lvl)+(lvl*5);
		return maxExp;
	}
	
	public int getCurrentExp() {
		return currentexp;
	}

	public void setCurrentExp(int currentexp) {
		this.currentexp = currentexp;
	}


	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Demon [Element=" + element + ", mobName=" + mobName + ", atk=" + atk + ", res=" + res + ", hp=" + hp + ", dgc="
				+ dgc + ", lvl=" + lvl + "]";
	}











}
