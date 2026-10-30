public class BiomeExit {

	private String side;
	private String target;
	private int spawnX;
	private int spawnY;

	public BiomeExit(String side, String target, int spawnX, int spawnY) {
		this.side = side;
		this.target = target;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
	}

	public String getSide() {
		return side;
	}

	public String getTarget() {
		return target;
	}

	public int getSpawnX() {
		return spawnX;
	}

	public int getSpawnY() {
		return spawnY;
	}

}
