public class Glimmerswarm extends Character {

	public Glimmerswarm() {
		this("Glimmerswarm", 70, 45, 110, 55, 20);
	}

	public Glimmerswarm(String name, int hp, int stamina, int mana, int speed, int defense) {
		super(name, "Air", hp, stamina, mana, speed, defense, new Rune("Glimmer Rune", "Air", 6));
	}

}
