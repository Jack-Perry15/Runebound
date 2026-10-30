public class Rune {

	private String name;
	private String element;

	//which row of runeGrid.png has this rune's picture
	private int spriteRow;

	//where the rune is lying in the world before you pick it up (top left corner)
	private int x;
	private int y;

	public Rune(String name, String element, int spriteRow) {
		this.name = name;
		this.element = element;
		this.spriteRow = spriteRow;
	}

	public String getName() {
		return name;
	}

	public String getElement() {
		return element;
	}

	public int getSpriteRow() {
		return spriteRow;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
