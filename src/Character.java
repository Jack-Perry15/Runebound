import java.util.ArrayList;

public class Character {

	private String name;
	private String element;
	private int hp;
	private int maxHp;
	private int stamina;
	private int mana;
	private int maxMana;
	private int speed;
	private int defense;

	//every character starts with the 4 element runes plus their own special rune,
	//and can pick up more runes they find, up to maxRunes
	private ArrayList<Rune> runeRing = new ArrayList<Rune>();
	private static final int maxRunes = 9;

	public Character(String name, String element, int hp, int stamina, int mana, int speed, int defense, Rune specialRune) {
		this.name = name;
		this.element = element;
		this.hp = hp;
		this.maxHp = hp;
		this.stamina = stamina;
		this.mana = mana;
		this.maxMana = mana;
		this.speed = speed;
		this.defense = defense;

		runeRing.add(new Rune("Fire Rune", "Fire", 0));
		runeRing.add(new Rune("Water Rune", "Water", 1));
		runeRing.add(new Rune("Earth Rune", "Earth", 2));
		runeRing.add(new Rune("Air Rune", "Air", 3));
		runeRing.add(specialRune);
	}

	//distance is how many tiles away the target is (1 means right next to you)
	//target can be null if there's no enemy in the direction you're aiming
	//returns a message saying what happened so the game can show it on screen
	public String castSpell(ArrayList<Rune> chain, SpellBook book, Enemy target, int distance) {
		if (chain.size() < 1 || chain.size() > 3) {
			return "A spell needs 1 to 3 runes.";
		}

		int cost = book.getManaCost(chain.size());
		if (mana < cost) {
			return name + " doesn't have enough mana.";
		}
		mana = mana - cost;

		Spell spell = book.findSpell(chain);
		if (spell == null) {
			return "The spell fizzles out. That rune combo doesn't make a spell.";
		}

		//a character does 50% more with spells of their own element
		int amount = spell.getPower();
		if (spell.getElement().equals(element)) {
			amount = amount + amount / 2;
		}

		if (spell instanceof HealingSpell) {
			heal(amount);
			return name + " casts " + spell.getName() + " and heals " + amount + " HP.";
		} else if (spell instanceof MeleeSpell) {
			MeleeSpell melee = (MeleeSpell) spell;
			if (target == null) {
				return "";
			}
			if (distance <= 1 && target != null) {
				amount = amount + melee.getBonusDamage();
				target.takeDamage(amount);
				return name + " casts " + spell.getName() + " up close for " + amount + " " + spell.getElement() + " damage!";
			} else {
				return name + " casts " + spell.getName() + " but the enemy is too far away for a melee spell.";
			}
		} else if (spell instanceof RangedSpell) {
			RangedSpell ranged = (RangedSpell) spell;
			if (target == null) {
				return "";
			}
			if (distance <= ranged.getRange() && target != null) {
				target.takeDamage(amount);
				return name + " casts " + spell.getName() + " from " + distance + " tiles away for " + amount + " " + spell.getElement() + " damage!";
			} else {
				return name + " casts " + spell.getName() + " but the enemy is out of range.";
			}
		}
		return name + " casts " + spell.getName() + ".";
	}

	//used to slowly refill mana over time
	public void restoreMana(int amount) {
		mana = mana + amount;
		if (mana > maxMana) {
			mana = maxMana;
		}
	}

	public void heal(int amount) {
		hp = hp + amount;
		if (hp > maxHp) {
			hp = maxHp;
		}
	}

	public void takeDamage(int amount) {
		hp = hp - amount;
		if (hp < 0) {
			hp = 0;
		}
	}

	public boolean isAlive() {
		return hp > 0;
	}

	public String getName() {
		return name;
	}

	public String getElement() {
		return element;
	}

	public int getHp() {
		return hp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getStamina() {
		return stamina;
	}

	public int getMana() {
		return mana;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public int getSpeed() {
		return speed;
	}

	public int getDefense() {
		return defense;
	}

	public ArrayList<Rune> getRuneRing() {
		return runeRing;
	}

	//the special rune is always the 5th one in the ring
	public Rune getSpecialRune() {
		return runeRing.get(4);
	}

	//true if there's already a rune with this name in the ring
	public boolean hasRune(String runeName) {
		for (int i = 0; i < runeRing.size(); i++) {
			if (runeRing.get(i).getName().equals(runeName)) {
				return true;
			}
		}
		return false;
	}

	//puts a rune you found into the ring
	//returns false if the ring is full or you already have that rune
	public boolean addRune(Rune rune) {
		if (runeRing.size() >= maxRunes) {
			return false;
		}
		if (hasRune(rune.getName())) {
			return false;
		}
		runeRing.add(rune);
		return true;
	}

}
