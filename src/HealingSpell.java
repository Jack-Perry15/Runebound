//a healing spell heals the caster instead of hurting an enemy
public class HealingSpell extends Spell {

	public HealingSpell(String name, String element, int power, String[] recipe) {
		super(name, element, power, recipe);
	}

	@Override
	public String getType() {
		return "Healing";
	}

}
