public class Cindertail extends Character {

	public Cindertail() {
		this("Cindertail", 90, 55, 90, 65, 35);
	}

	public Cindertail(String name, int hp, int stamina, int mana, int speed, int defense) {
		super(name, "Fire", hp, stamina, mana, speed, defense, new Rune("Cinder Rune", "Fire", 7));
	}

}
