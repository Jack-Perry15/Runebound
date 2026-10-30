public class Croakfang extends Character {

	public Croakfang() {
		this("Croakfang", 80, 90, 70, 95, 30);
	}

	public Croakfang(String name, int hp, int stamina, int mana, int speed, int defense) {
		super(name, "Water", hp, stamina, mana, speed, defense, new Rune("Riptide Rune", "Water", 5));
	}

	public void dash() {

	}

}
