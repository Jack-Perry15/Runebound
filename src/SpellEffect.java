public class SpellEffect {

	private int row;
	private int direction;
	private double x;
	private double y;
	private double moveX;
	private double moveY;
	private int travelTime;
	private int timer = 0;

	//how many ticks each part lasts (the game ticks about 200 times a second)
	private static final int startTime = 15;
	private static final int peakTime = 50;
	private static final int fadeTime = 30;

	//x and y are the top left corner of the picture
	//moveX and moveY are how many pixels it moves each tick while traveling
	//travelTime is how many ticks it travels for (0 means it stays still)
	public SpellEffect(int row, int direction, double x, double y, double moveX, double moveY, int travelTime) {
		this.row = row;
		this.direction = direction;
		this.x = x;
		this.y = y;
		this.moveX = moveX;
		this.moveY = moveY;
		this.travelTime = travelTime;
	}

	public void update() {
		timer++;
		if (timer > startTime && timer <= startTime + travelTime) {
			x = x + moveX;
			y = y + moveY;
		}
	}

	//0 = start frame, 1 = peak frame, 2 = fade frame
	public int getFrame() {
		if (timer < startTime) {
			return 0;
		} else if (timer < startTime + travelTime + peakTime) {
			return 1;
		} else {
			return 2;
		}
	}

	public boolean isFinished() {
		return timer >= startTime + travelTime + peakTime + fadeTime;
	}

	public int getRow() {
		return row;
	}

	public int getDirection() {
		return direction;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

}
