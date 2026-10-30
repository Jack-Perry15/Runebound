//a ranged spell can hit an enemy from far away, as long as they are inside its range
public class RangedSpell extends Spell {

	private int range;

	public RangedSpell(String name, String element, int power, String[] recipe, int range) {
		super(name, element, power, recipe);
		this.range = range;
	}

	public int getRange() {
		return range;
	}

	@Override
	public String getType() {
		return "Ranged";
	}

}
