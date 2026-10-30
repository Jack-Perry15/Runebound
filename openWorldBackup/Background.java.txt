import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Background {

	public static final int tileSize = 16;
	public static final int imageScale = 2;
	public static final int cellWidth = 32;
	public static final int cellHeight = 18;

	private String name;
	private String displayName;
	private BufferedImage grid;
	private char[][] tiles;
	private int mapWidth;
	private int mapHeight;
	private ArrayList<Room> rooms = new ArrayList<Room>();
	private ArrayList<BiomeExit> exits = new ArrayList<BiomeExit>();
	private int startX;
	private int startY;

	protected ArrayList<Particle> particles = new ArrayList<Particle>();

	private BufferedImage mapImage;

	public Background(String name, String displayName, String gridFile, String levelFile) {
		this.name = name;
		this.displayName = displayName;
		loadLevel(levelFile);
		try {
			grid = ImageIO.read(new File(gridFile));
		} catch (IOException e) {
			System.out.println("Could not load " + gridFile + ": " + e);
			grid = null;
		}
		buildMap();
	}

	private void loadLevel(String fileName) {
		Scanner in;
		try {
			in = new Scanner(new File(fileName));
		} catch (IOException e) {
			System.out.println("Could not load " + fileName + ": " + e);
			tiles = new char[1][1];
			mapWidth = 1;
			mapHeight = 1;
			return;
		}

		in.next();
		int roomCount = in.nextInt();
		for (int i = 0; i < roomCount; i++) {
			int col = in.nextInt();
			int row = in.nextInt();
			int wide = in.nextInt();
			int tall = in.nextInt();
			int size = tileSize;
			rooms.add(new Room(col * cellWidth * size, row * cellHeight * size, wide * cellWidth * size, tall * cellHeight * size));
		}

		in.next();
		int exitCount = in.nextInt();
		for (int i = 0; i < exitCount; i++) {
			String side = in.next();
			String target = in.next();
			int spotX = in.nextInt();
			int spotY = in.nextInt();
			exits.add(new BiomeExit(side, target, spotX * tileSize + tileSize, (spotY + 1) * tileSize));
		}

		in.next();
		startX = in.nextInt() * tileSize + tileSize;
		startY = (in.nextInt() + 1) * tileSize;

		in.next();
		mapWidth = in.nextInt();
		mapHeight = in.nextInt();
		in.nextLine();

		tiles = new char[mapHeight][mapWidth];
		for (int row = 0; row < mapHeight; row++) {
			String line = in.nextLine();
			for (int col = 0; col < mapWidth; col++) {
				tiles[row][col] = line.charAt(col);
			}
		}
		in.close();
	}

	public boolean isSolid(int col, int row) {
		if (col < 0 || row < 0 || col >= mapWidth || row >= mapHeight) {
			return false;
		}
		return tiles[row][col] == '#';
	}

	public boolean isPlatform(int col, int row) {
		if (col < 0 || row < 0 || col >= mapWidth || row >= mapHeight) {
			return false;
		}
		return tiles[row][col] == '=';
	}

	public Room findRoom(double x, double y) {
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).contains(x, y)) {
				return rooms.get(i);
			}
		}
		return null;
	}

	public BiomeExit findExit(String side) {
		for (int i = 0; i < exits.size(); i++) {
			if (exits.get(i).getSide().equals(side)) {
				return exits.get(i);
			}
		}
		return null;
	}

	public BiomeExit findExitTo(String biomeName) {
		for (int i = 0; i < exits.size(); i++) {
			if (exits.get(i).getTarget().equals(biomeName)) {
				return exits.get(i);
			}
		}
		return null;
	}

	public void draw(Graphics2D g, double cameraX, double cameraY, double zoom, int screenWidth, int screenHeight) {
		if (grid == null) {
			return;
		}
		double pixelSize = zoom / imageScale;

		double imageX = cameraX * imageScale;
		double imageY = cameraY * imageScale;
		int sourceX = (int) Math.floor(imageX);
		int sourceY = (int) Math.floor(imageY);
		int sourceWidth = (int) Math.ceil(screenWidth / pixelSize) + 2;
		int sourceHeight = (int) Math.ceil(screenHeight / pixelSize) + 2;

		int drawX = (int) Math.round(-(imageX - sourceX) * pixelSize);
		int drawY = (int) Math.round(-(imageY - sourceY) * pixelSize);
		int drawWidth = (int) Math.round(sourceWidth * pixelSize);
		int drawHeight = (int) Math.round(sourceHeight * pixelSize);

		g.drawImage(grid, drawX, drawY, drawX + drawWidth, drawY + drawHeight,
				sourceX, sourceY, sourceX + sourceWidth, sourceY + sourceHeight, null);
	}

	public void update(double viewX, double viewY, double viewWidth, double viewHeight) {
		synchronized (particles) {
			for (int i = particles.size() - 1; i >= 0; i--) {
				particles.get(i).update();
				if (particles.get(i).isDead()) {
					particles.remove(i);
				}
			}
			addParticles(viewX, viewY, viewWidth, viewHeight);
		}
	}

	protected void addParticles(double viewX, double viewY, double viewWidth, double viewHeight) {

	}

	public void drawEffects(Graphics2D g, double cameraX, double cameraY, double zoom, int screenWidth, int screenHeight, int playerScreenX, int playerScreenY) {
		synchronized (particles) {
			for (int i = 0; i < particles.size(); i++) {
				Particle p = particles.get(i);
				int size = (int) Math.max(1, p.getSize() * zoom);
				int x = (int) ((p.getX() - cameraX) * zoom);
				int y = (int) ((p.getY() - cameraY) * zoom);
				Color c = p.getColor();
				int alpha = (int) (c.getAlpha() * p.getFade());
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha / 4));
				g.fillRect(x - size, y - size, size * 3, size * 3);
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
				g.fillRect(x, y, size, size);
			}
		}
	}

	public Color getMapColor() {
		return new Color(222, 214, 188);
	}

	private void buildMap() {
		mapImage = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
		int paper = getMapColor().getRGB();
		int ink = new Color(40, 32, 26).getRGB();
		for (int row = 0; row < mapHeight; row++) {
			for (int col = 0; col < mapWidth; col++) {
				if (isSolid(col, row)) {
					boolean edge = false;
					if (!isSolid(col - 1, row) || !isSolid(col + 1, row) || !isSolid(col, row - 1) || !isSolid(col, row + 1)) {
						edge = true;
					}
					if (edge) {
						mapImage.setRGB(col, row, ink);
					}
				} else if (isPlatform(col, row)) {
					mapImage.setRGB(col, row, ink);
				} else {
					mapImage.setRGB(col, row, paper);
				}
			}
		}
	}

	public void drawMap(Graphics2D g, int mapX, int mapY, int scale) {
		if (mapImage == null) {
			buildMap();
		}
		for (int i = 0; i < rooms.size(); i++) {
			Room room = rooms.get(i);
			if (room.isVisited()) {
				int col = room.getX() / tileSize;
				int row = room.getY() / tileSize;
				int wide = room.getWidth() / tileSize;
				int tall = room.getHeight() / tileSize;
				int x = mapX + col * scale;
				int y = mapY + row * scale;
				g.drawImage(mapImage, x, y, x + wide * scale, y + tall * scale, col, row, col + wide, row + tall, null);
				g.setColor(new Color(40, 32, 26, 120));
				g.drawRect(x, y, wide * scale, tall * scale);
			}
		}

		g.setFont(new Font("Arial", Font.BOLD, scale * 5 + 6));
		for (int i = 0; i < exits.size(); i++) {
			BiomeExit exit = exits.get(i);
			Room room = findRoom(exit.getSpawnX(), exit.getSpawnY() - tileSize);
			if (room != null && room.isVisited()) {
				int x = mapX + exit.getSpawnX() / tileSize * scale;
				int y = mapY + exit.getSpawnY() / tileSize * scale;
				String label = "to " + displayNameFor(exit.getTarget());
				int textWidth = g.getFontMetrics().stringWidth(label);
				g.setColor(new Color(245, 238, 215));
				g.fillRect(x - 3, y - 3, 7, 7);
				g.setColor(new Color(40, 32, 26));
				g.drawRect(x - 3, y - 3, 7, 7);
				int labelX = x + 8;
				if (labelX + textWidth > mapX + mapWidth * scale) {
					labelX = x - 8 - textWidth;
				}
				int labelY = y + 4;
				if (exit.getSide().equals("top")) {
					labelY = y + scale * 6;
				}
				g.setColor(new Color(0, 0, 0, 160));
				g.drawString(label, labelX + 2, labelY + 2);
				g.setColor(new Color(245, 238, 215));
				g.drawString(label, labelX, labelY);
			}
		}
	}

	public static String displayNameFor(String name) {
		if (name.equals("swamp")) {
			return "The Swamp";
		} else if (name.equals("ember")) {
			return "Ember Marsh";
		} else if (name.equals("caverns")) {
			return "Sunken Caverns";
		} else if (name.equals("canopy")) {
			return "The Canopy";
		}
		return name;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public double getGravity() {
		return 0.035;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getWidthInPixels() {
		return mapWidth * tileSize;
	}

	public int getHeightInPixels() {
		return mapHeight * tileSize;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

}
