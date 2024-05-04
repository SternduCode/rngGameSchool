package rngGame.stats;

public class Use extends Item {

	private int hp;

	public Use(Rarity itemRarity) {
		super("Use", itemRarity.getTextureName(), itemRarity);
		hp = switch (getRarity()) {
			case COMMON -> 10;
			case UNCOMMON -> 20;
			case RARE -> 40;
			case VERY_RARE -> 50;
			case EPIC -> 70;
			case LEGENDARY -> 90;
			case GOD -> 130;
			case VOID -> 180;
		};
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

}
