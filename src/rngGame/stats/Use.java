package rngGame.stats;

public class Use extends Item{

	Rarity rarity;
	int hp;
	
	

	public Use(Rarity ty) {
		super("Use",ty.getTextureName());
		this.rarity = ty;
		
		hp = switch(this.rarity) {
		case COMMON -> 10;
		case UNCOMMON -> 20;
		case RARE -> 40;
		case VERYRARE -> 50;
		case EPIC -> 70;
		case LEGENDARY -> 90;
		case GOD ->	130;
		case VOID -> 180;
		};
	}

}
