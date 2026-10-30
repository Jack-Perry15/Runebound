//a ledge you can stand on
//you can jump up through it from below, and double tap down to drop through it
public class Platform {

	private int x;
	private int y;
	private int width;
	private int height;

	public Platform(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	//the top of the platform (where your feet go)
	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
