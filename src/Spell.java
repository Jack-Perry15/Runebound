import java.util.ArrayList;

public class Spell {

	private String name;
	private String element;
	private int power;
	private String[] recipe;

	public Spell(String name, String element, int power, String[] recipe) {
		this.name = name;
		this.element = element;
		this.power = power;
		this.recipe = recipe;
	}

	//the order the runes were picked in doesn't matter
	public boolean matches(ArrayList<Rune> chain) {
		if (chain.size() != recipe.length) {
			return false;
		}

		for (int i = 0; i < recipe.length; i++) {
			boolean found = false;
			for (int j = 0; j < chain.size(); j++) {
				if (chain.get(j).getName().equals(recipe[i])) {
					found = true;
				}
			}
			if (found == false) {
				return false;
			}
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public String getElement() {
		return element;
	}

	public int getPower() {
		return power;
	}

	public String[] getRecipe() {
		return recipe;
	}

	//true if the spell's art flies across the screen instead of staying in one spot
	public boolean travels() {
		if (name.equals("Ember Bolt") || name.equals("Stone Fist") || name.equals("Gust")
				|| name.equals("Riptide") || name.equals("Glimmer Dart") || name.equals("Flame Breath")
				|| name.equals("Mud Slide") || name.equals("Sandstorm") || name.equals("Steam Burst")
				|| name.equals("Magma Fist") || name.equals("Landslide") || name.equals("Lava Flood")
				|| name.equals("Spark Bolt") || name.equals("Frost Shard") || name.equals("Venom Spit")
				|| name.equals("Shade Bolt") || name.equals("Plasma Blast") || name.equals("Frozen Boulder")
				|| name.equals("Dark Tide")) {
			return true;
		}
		return false;
	}

	public String getType() {
		return "Spell";
	}

}
