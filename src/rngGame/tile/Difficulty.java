package rngGame.tile;

public enum Difficulty {

	EASY(2, 3, 4), MIDDLE(4, 5, 6), HARD(6, 7, 8);

	private final int bigMaps, middleMaps, smallMaps;

	Difficulty(int bigMaps, int middleMaps, int smallMaps) {
		this.bigMaps = bigMaps;
		this.middleMaps = middleMaps;
		this.smallMaps = smallMaps;
	}

	public int getBigMaps() { return bigMaps; }

	public int getMiddleMaps() { return middleMaps; }

	public int getSmallMaps() { return smallMaps; }

}
