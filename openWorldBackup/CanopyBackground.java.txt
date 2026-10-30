import java.awt.Color;

public class CanopyBackground extends Background {

	public CanopyBackground() {
		super("canopy", "The Canopy", "canopyGrid.png", "canopyLevel.txt");
	}

	@Override
	protected void addParticles(double viewX, double viewY, double viewWidth, double viewHeight) {
		if (particles.size() < 35 && Math.random() < 0.06) {
			double x = viewX + Math.random() * viewWidth * 1.2;
			double y = viewY - 5;
			Color leaf = new Color(120, 180, 70, 255);
			if (Math.random() < 0.4) {
				leaf = new Color(210, 150, 60, 255);
			}
			particles.add(new Particle(x, y, -0.15, 0.3 + Math.random() * 0.2, 0.6, 1200, 1.5, leaf));
		}
	}

	@Override
	public double getGravity() {
		return 0.028;
	}

	@Override
	public Color getMapColor() {
		return new Color(214, 236, 246);
	}

}
