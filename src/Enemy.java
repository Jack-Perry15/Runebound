public class Enemy {

	private int hp;
	private int maxHp;
	private int damage;

	private int x;
	private int y;
	private int deadTimer = 0;

	public Enemy(int hp, int damage) {
		this.hp = hp;
		this.maxHp = hp;
		this.damage = damage;
	}

	public boolean isAlive() {
		return hp > 0;
	}

	public void takeDamage(int amount) {
		hp = hp - amount;
		if (hp < 0) {
			hp = 0;
		}
	}

	public void addDeadTime() {
		deadTimer++;
	}

	public int getDeadTime() {
		return deadTimer;
	}

	//brings the enemy back with full hp
	public void revive() {
		hp = maxHp;
		deadTimer = 0;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	//how far away (in pixels) the enemy can hit you from
	public int getReach() {
		return 40;
	}

	public void attack(Character target) {
		target.takeDamage(damage);
	}

	public String getName() {
		return "Enemy";
	}

	public int getHp() {
		return hp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getDamage() {
		return damage;
	}

}
