import java.awt.*;
import java.awt.image.BufferedImage;

public class SunkenCavernsBackground extends Background {

	private BufferedImage darkness;

	public SunkenCavernsBackground() {
		super("caverns", "Sunken Caverns", "cavernsGrid.png", "cavernsLevel.txt");
	}

	@Override
	protected void addParticles(double viewX, double viewY, double viewWidth, double viewHeight) {
		if (particles.size() < 50 && Math.random() < 0.1) {
			double x = viewX + Math.random() * viewWidth;
			double y = viewY + Math.random() * viewHeight;
			Color dust = new Color(150, 200, 255, 150);
			particles.add(new Particle(x, y, 0.02, 0.05, 0.05, 600, 0.6, dust));
		}
		if (Math.random() < 0.02) {
			double x = viewX + Math.random() * viewWidth;
			Color drip = new Color(140, 210, 255, 220);
			particles.add(new Particle(x, viewY, 0, 1.6, 0, 200, 0.8, drip));
		}
	}

	@Override
	public void drawEffects(Graphics2D g, double cameraX, double cameraY, double zoom, int screenWidth, int screenHeight, int playerScreenX, int playerScreenY) {
		super.drawEffects(g, cameraX, cameraY, zoom, screenWidth, screenHeight, playerScreenX, playerScreenY);

		int size = 400;
		if (darkness == null) {
			darkness = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					double dx = (x - size / 2) / (size / 2.0);
					double dy = (y - size / 2) / (size / 2.0);
					double distance = Math.sqrt(dx * dx + dy * dy);
					int alpha = (int) ((distance - 0.25) * 330);
					if (alpha < 0) {
						alpha = 0;
					}
					if (alpha > 200) {
						alpha = 200;
					}
					darkness.setRGB(x, y, alpha << 24);
				}
			}
		}

		int drawSize = screenWidth * 2;
		int left = playerScreenX - drawSize / 2;
		int top = playerScreenY - drawSize / 2;
		g.drawImage(darkness, left, top, drawSize, drawSize, null);
		g.setColor(new Color(0, 0, 0, 200));
		if (left > 0) {
			g.fillRect(0, 0, left, screenHeight);
		}
		if (top > 0) {
			g.fillRect(0, 0, screenWidth, top);
		}
		if (left + drawSize < screenWidth) {
			g.fillRect(left + drawSize, 0, screenWidth, screenHeight);
		}
		if (top + drawSize < screenHeight) {
			g.fillRect(0, top + drawSize, screenWidth, screenHeight);
		}
	}

	@Override
	public Color getMapColor() {
		return new Color(176, 192, 224);
	}

}
