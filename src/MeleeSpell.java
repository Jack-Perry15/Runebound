//a melee spell only hits if the enemy is right next to you, but it hits extra hard
public class MeleeSpell extends Spell {

	private int bonusDamage;

	public MeleeSpell(String name, String element, int power, String[] recipe, int bonusDamage) {
		super(name, element, power, recipe);
		this.bonusDamage = bonusDamage;
	}

	public int getBonusDamage() {
		return bonusDamage;
	}

	@Override
	public String getType() {
		return "Melee";
	}

}
