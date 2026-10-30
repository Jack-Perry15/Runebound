public class Room {

	private int x;
	private int y;
	private int width;
	private int height;
	private boolean visited = false;

	public Room(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean contains(double pointX, double pointY) {
		return pointX >= x && pointX < x + width && pointY >= y && pointY < y + height;
	}

	public void markVisited() {
		visited = true;
	}

	public boolean isVisited() {
		return visited;
	}

	public int getX() {
		return x;
	}

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
