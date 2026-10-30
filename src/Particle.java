import java.awt.Color;

public class Particle {

	private double x;
	private double y;
	private double moveX;
	private double moveY;
	private double sway;
	private int age = 0;
	private int life;
	private double size;
	private Color color;

	public Particle(double x, double y, double moveX, double moveY, double sway, int life, double size, Color color) {
		this.x = x;
		this.y = y;
		this.moveX = moveX;
		this.moveY = moveY;
		this.sway = sway;
		this.life = life;
		this.size = size;
		this.color = color;
	}

	public void update() {
		age++;
		x = x + moveX + Math.sin(age * 0.03) * sway;
		y = y + moveY;
	}

	public boolean isDead() {
		return age >= life;
	}

	public double getFade() {
		double fade = 1.0;
		if (age < 60) {
			fade = age / 60.0;
		}
		if (life - age < 60) {
			fade = (life - age) / 60.0;
		}
		return fade;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getSize() {
		return size;
	}

	public Color getColor() {
		return color;
	}

}
