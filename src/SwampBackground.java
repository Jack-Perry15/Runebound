import java.awt.Color;

public class SwampBackground extends Background {

	public SwampBackground() {
		super("swamp", "The Swamp", "swampGrid.png", "swampLevel.txt");
	}

	@Override
	protected void addParticles(double viewX, double viewY, double viewWidth, double viewHeight) {
		if (particles.size() < 40 && Math.random() < 0.08) {
			double x = viewX + Math.random() * viewWidth;
			double y = viewY + viewHeight * 0.3 + Math.random() * viewHeight * 0.7;
			double moveX = (Math.random() - 0.5) * 0.12;
			double moveY = (Math.random() - 0.5) * 0.08;
			Color glow = new Color(220, 250, 120, 230);
			particles.add(new Particle(x, y, moveX, moveY, 0.15, 500 + (int) (Math.random() * 500), 1.0, glow));
		}
	}

	@Override
	public Color getMapColor() {
		return new Color(196, 214, 168);
	}

}
