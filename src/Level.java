import java.util.Queue;

public class Level {

	private Queue<Enemy> enemies;
	private Background background;
	private Platform[][] platforms;

	public Level(Queue<Enemy> enemies, Background background, Platform[][] platforms) {
		this.enemies = enemies;
		this.background = background;
		this.platforms = platforms;
	}

	public Queue<Enemy> getEnemies() {
		return enemies;
	}

	public Background getBackground() {
		return background;
	}

	public Platform[][] getPlatforms() {
		return platforms;
	}

}
