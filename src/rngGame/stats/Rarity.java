package rngGame.stats;

public enum Rarity {
	COMMON,UNCOMMON,RARE,VERYRARE,EPIC,LEGENDARY,GOD,VOID; 
	String textureName;
	


	Rarity() {
		textureName = toString();
	}
	
	public String getTextureName() {
		return textureName;
	}
}
