package rngGame.stats

enum class Element(
	@JvmField val atk: Int,
	@JvmField val res: Double,
	@JvmField val hp: Int,
	@JvmField val dgc: Double,
	@JvmField val lvl: Int
) {

	Fire(4, 0.01, 20, 0.005, 1),
	Water(4, 0.01, 20, 0.005, 1),
	Plant(4, 0.01, 20, 0.005, 1),
	Shadow(6, 0.02, 24, 0.01, 1),
	Light(6, 0.02, 24, 0.01, 1),
	Void(10, 0.03, 30, 0.015, 1),
	DimensionMaster(15, 0.05, 40, 0.03, 1)

}
