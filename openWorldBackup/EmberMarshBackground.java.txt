import java.awt.*;

public class EmberMarshBackground extends Background {

	public EmberMarshBackground() {
		super("ember", "Ember Marsh", "emberGrid.png", "emberLevel.txt");
	}

	@Override
	protected void addParticles(double viewX, double viewY, double viewWidth, double viewHeight) {
		if (particles.size() < 70 && Math.random() < 0.25) {
			double x = viewX + Math.random() * viewWidth;
			double y = viewY + viewHeight + 5;
			double moveX = (Math.random() - 0.5) * 0.2;
			double moveY = -0.25 - Math.random() * 0.45;
			Color spark = new Color(255, 150, 50, 240);
			if (Math.random() < 0.3) {
				spark = new Color(255, 220, 120, 240);
			}
			particles.add(new Particle(x, y, moveX, moveY, 0.25, 400 + (int) (Math.random() * 400), 0.8, spark));
		}
	}

	@Override
	public void drawEffects(Graphics2D g, double cameraX, double cameraY, double zoom, int screenWidth, int screenHeight, int playerScreenX, int playerScreenY) {
		super.drawEffects(g, cameraX, cameraY, zoom, screenWidth, screenHeight, playerScreenX, playerScreenY);
		g.setColor(new Color(255, 80, 20, 18));
		g.fillRect(0, 0, screenWidth, screenHeight);
	}

	@Override
	public Color getMapColor() {
		return new Color(232, 184, 150);
	}

}
