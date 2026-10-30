import java.util.ArrayList;

public class SpellBook {

	private ArrayList<Spell> spells = new ArrayList<Spell>();

	private ArrayList<Rune> runes = new ArrayList<Rune>();

	public SpellBook() {
		runes.add(new Rune("Fire Rune", "Fire", 0));
		runes.add(new Rune("Water Rune", "Water", 1));
		runes.add(new Rune("Earth Rune", "Earth", 2));
		runes.add(new Rune("Air Rune", "Air", 3));

		runes.add(new Rune("Quake Rune", "Earth", 4));
		runes.add(new Rune("Riptide Rune", "Water", 5));
		runes.add(new Rune("Glimmer Rune", "Air", 6));
		runes.add(new Rune("Cinder Rune", "Fire", 7));

		runes.add(new Rune("Lightning Rune", "Lightning", 8));
		runes.add(new Rune("Ice Rune", "Ice", 9));
		runes.add(new Rune("Venom Rune", "Venom", 10));
		runes.add(new Rune("Shadow Rune", "Shadow", 11));

		//1 rune spells
		spells.add(new RangedSpell("Ember Bolt", "Fire", 12, new String[] {"Fire Rune"}, 5));
		spells.add(new HealingSpell("Tide Mend", "Water", 15, new String[] {"Water Rune"}));
		spells.add(new MeleeSpell("Stone Fist", "Earth", 12, new String[] {"Earth Rune"}, 3));
		spells.add(new RangedSpell("Gust", "Air", 10, new String[] {"Air Rune"}, 6));

		//1 rune spells made from each character's special rune
		spells.add(new MeleeSpell("Quake", "Earth", 20, new String[] {"Quake Rune"}, 5));
		spells.add(new MeleeSpell("Riptide", "Water", 18, new String[] {"Riptide Rune"}, 6));
		spells.add(new RangedSpell("Glimmer Dart", "Air", 18, new String[] {"Glimmer Rune"}, 8));
		spells.add(new RangedSpell("Flame Breath", "Fire", 20, new String[] {"Cinder Rune"}, 3));

		//2 rune spells
		spells.add(new MeleeSpell("Mud Slide", "Earth", 26, new String[] {"Earth Rune", "Water Rune"}, 5));
		spells.add(new RangedSpell("Firestorm", "Fire", 26, new String[] {"Fire Rune", "Air Rune"}, 6));
		spells.add(new HealingSpell("Healing Rain", "Water", 30, new String[] {"Water Rune", "Air Rune"}));
		spells.add(new RangedSpell("Sandstorm", "Air", 24, new String[] {"Earth Rune", "Air Rune"}, 5));
		spells.add(new RangedSpell("Steam Burst", "Water", 24, new String[] {"Fire Rune", "Water Rune"}, 4));
		spells.add(new MeleeSpell("Magma Fist", "Fire", 28, new String[] {"Fire Rune", "Earth Rune"}, 5));

		//2 rune spells with a special rune
		spells.add(new MeleeSpell("Landslide", "Earth", 40, new String[] {"Quake Rune", "Earth Rune"}, 8));
		spells.add(new MeleeSpell("Whirlpool", "Water", 38, new String[] {"Riptide Rune", "Water Rune"}, 8));
		spells.add(new RangedSpell("Firefly Tempest", "Air", 38, new String[] {"Glimmer Rune", "Air Rune"}, 9));
		spells.add(new RangedSpell("Inferno", "Fire", 40, new String[] {"Cinder Rune", "Fire Rune"}, 4));

		//3 rune spells
		spells.add(new RangedSpell("Swamp Storm", "Water", 45, new String[] {"Earth Rune", "Water Rune", "Air Rune"}, 6));
		spells.add(new MeleeSpell("Volcano", "Fire", 50, new String[] {"Fire Rune", "Earth Rune", "Air Rune"}, 10));
		spells.add(new HealingSpell("Hot Spring", "Water", 50, new String[] {"Fire Rune", "Water Rune", "Air Rune"}));

		//3 rune spells with a special rune
		spells.add(new MeleeSpell("Sinkhole", "Earth", 60, new String[] {"Quake Rune", "Earth Rune", "Water Rune"}, 12));
		spells.add(new RangedSpell("Hurricane", "Water", 58, new String[] {"Riptide Rune", "Water Rune", "Air Rune"}, 7));
		spells.add(new RangedSpell("Wildfire Swarm", "Air", 60, new String[] {"Glimmer Rune", "Air Rune", "Fire Rune"}, 8));
		spells.add(new MeleeSpell("Lava Flood", "Fire", 62, new String[] {"Cinder Rune", "Fire Rune", "Earth Rune"}, 12));

		//spells made with runes you find in the world
		spells.add(new RangedSpell("Spark Bolt", "Lightning", 14, new String[] {"Lightning Rune"}, 6));
		spells.add(new RangedSpell("Storm Surge", "Lightning", 30, new String[] {"Lightning Rune", "Water Rune"}, 5));
		spells.add(new MeleeSpell("Thunderclap", "Lightning", 32, new String[] {"Lightning Rune", "Air Rune"}, 6));
		spells.add(new RangedSpell("Frost Shard", "Ice", 13, new String[] {"Ice Rune"}, 5));
		spells.add(new MeleeSpell("Glacier Slam", "Ice", 30, new String[] {"Ice Rune", "Water Rune"}, 6));
		spells.add(new RangedSpell("Blizzard", "Ice", 28, new String[] {"Ice Rune", "Air Rune"}, 6));
		spells.add(new RangedSpell("Venom Spit", "Venom", 12, new String[] {"Venom Rune"}, 4));
		spells.add(new MeleeSpell("Toxic Bog", "Venom", 30, new String[] {"Venom Rune", "Earth Rune"}, 6));
		spells.add(new HealingSpell("Antidote Mist", "Venom", 25, new String[] {"Venom Rune", "Water Rune"}));
		spells.add(new MeleeSpell("Shadow Claw", "Shadow", 16, new String[] {"Shadow Rune"}, 5));
		spells.add(new RangedSpell("Shade Bolt", "Shadow", 28, new String[] {"Shadow Rune", "Air Rune"}, 6));
		spells.add(new RangedSpell("Ghostfire", "Shadow", 30, new String[] {"Shadow Rune", "Fire Rune"}, 5));
		spells.add(new RangedSpell("Tempest", "Lightning", 60, new String[] {"Lightning Rune", "Water Rune", "Air Rune"}, 7));
		spells.add(new MeleeSpell("Plague Mire", "Venom", 60, new String[] {"Venom Rune", "Shadow Rune", "Earth Rune"}, 12));

		//more 2 rune spells so each found rune works with all 4 elements
		spells.add(new RangedSpell("Plasma Blast", "Lightning", 30, new String[] {"Lightning Rune", "Fire Rune"}, 5));
		spells.add(new MeleeSpell("Static Quake", "Lightning", 30, new String[] {"Lightning Rune", "Earth Rune"}, 6));
		spells.add(new RangedSpell("Frostfire", "Ice", 28, new String[] {"Ice Rune", "Fire Rune"}, 5));
		spells.add(new RangedSpell("Frozen Boulder", "Ice", 30, new String[] {"Ice Rune", "Earth Rune"}, 4));
		spells.add(new RangedSpell("Acid Flame", "Venom", 28, new String[] {"Venom Rune", "Fire Rune"}, 5));
		spells.add(new RangedSpell("Poison Cloud", "Venom", 26, new String[] {"Venom Rune", "Air Rune"}, 6));
		spells.add(new MeleeSpell("Dark Tide", "Shadow", 30, new String[] {"Shadow Rune", "Water Rune"}, 6));
		spells.add(new MeleeSpell("Grave Hands", "Shadow", 32, new String[] {"Shadow Rune", "Earth Rune"}, 6));

		//more 3 rune spells
		spells.add(new RangedSpell("Hailstorm", "Ice", 58, new String[] {"Ice Rune", "Water Rune", "Air Rune"}, 7));
		spells.add(new RangedSpell("Eclipse", "Shadow", 60, new String[] {"Shadow Rune", "Fire Rune", "Air Rune"}, 7));
	}

	public Spell findSpell(ArrayList<Rune> chain) {
		for (int i = 0; i < spells.size(); i++) {
			if (spells.get(i).matches(chain)) {
				return spells.get(i);
			}
		}
		return null;
	}

	public int getManaCost(int runeCount) {
		if (runeCount == 1) {
			return 10;
		} else if (runeCount == 2) {
			return 25;
		} else {
			return 45;
		}
	}

	public int getSpellCount() {
		return spells.size();
	}

	public int getSpellIndex(Spell spell) {
		return spells.indexOf(spell);
	}

	public Spell getSpell(int index) {
		return spells.get(index);
	}

	public int getRuneCount() {
		return runes.size();
	}

	public Rune getRune(int index) {
		return runes.get(index);
	}

	public Rune findRune(String runeName) {
		for (int i = 0; i < runes.size(); i++) {
			if (runes.get(i).getName().equals(runeName)) {
				return runes.get(i);
			}
		}
		return null;
	}

	public int countSpellsWith(String runeName) {
		int count = 0;
		for (int i = 0; i < spells.size(); i++) {
			String[] recipe = spells.get(i).getRecipe();
			for (int j = 0; j < recipe.length; j++) {
				if (recipe[j].equals(runeName)) {
					count++;
				}
			}
		}
		return count;
	}

}
