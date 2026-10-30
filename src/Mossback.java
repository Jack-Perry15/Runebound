public class Mossback extends Character {

	public Mossback() {
		this("Mossback", 140, 40, 60, 25, 70);
	}

	public Mossback(String name, int hp, int stamina, int mana, int speed, int defense) {
		super(name, "Earth", hp, stamina, mana, speed, defense, new Rune("Quake Rune", "Earth", 4));
	}

}
