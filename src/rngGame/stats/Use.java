package rngGame.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class Use.
 */
public class Use extends Item{

	/** The hp. */
	int hp;



	/**
	 * Instantiates a new use.
	 *
	 * @param ty the ty
	 */
	public Use(Rarity ty) {
		super("Use", ty.getTextureName(), ty);

		hp = switch(rarity) {
			case COMMON -> 10;
			case UNCOMMON -> 20;
			case RARE -> 40;
			case VERY_RARE -> 50;
			case EPIC -> 70;
			case LEGENDARY -> 90;
			case GOD ->	130;
			case VOID -> 180;
		};
	}

}
