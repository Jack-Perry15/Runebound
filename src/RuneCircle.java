import java.util.ArrayList;
import java.util.Collections;

//press C
public class RuneCircle {

	private ArrayList<Rune> runes = new ArrayList<Rune>();
	private boolean open = false;

	private static final int spinTime = 70;
	private int spinTimer = 0;

	//where each rune was (in degrees) when the spin started, so it can slide to its new spot
	private ArrayList<Double> startAngles = new ArrayList<Double>();

	public synchronized void open(ArrayList<Rune> ring) {
		runes.clear();
		startAngles.clear();
		for (int i = 0; i < ring.size(); i++) {
			runes.add(ring.get(i));
			startAngles.add(-90.0);
		}
		Collections.shuffle(runes);
		spinTimer = spinTime;
		open = true;
	}

	public synchronized void close() {
		open = false;
		runes.clear();
	}

	public synchronized boolean isOpen() {
		return open;
	}

	public synchronized boolean isSpinning() {
		return spinTimer > 0;
	}

	//takes the rune in spot number (1, 2, 3...) out of the circle and spins the rest
	//returns null if there's no rune in that spot or the circle is still spinning
	public synchronized Rune pick(int number) {
		if (spinTimer > 0) {
			return null;
		}
		int index = number - 1;
		if (index < 0 || index >= runes.size()) {
			return null;
		}

		Rune chosen = runes.get(index);

		//remember where every rune is right now, then take the chosen one out
		ArrayList<Double> angles = new ArrayList<Double>();
		for (int i = 0; i < runes.size(); i++) {
			angles.add(getSlotAngle(i, runes.size()));
		}
		runes.remove(index);
		angles.remove(index);

		//mix up the runes that are left, keeping each rune's old angle with it
		for (int i = runes.size() - 1; i > 0; i--) {
			int j = (int) (Math.random() * (i + 1));
			Rune tempRune = runes.get(i);
			runes.set(i, runes.get(j));
			runes.set(j, tempRune);
			double tempAngle = angles.get(i);
			angles.set(i, angles.get(j));
			angles.set(j, tempAngle);
		}

		startAngles = angles;
		spinTimer = spinTime;
		return chosen;
	}

	//runs every tick of the game
	public synchronized void update() {
		if (spinTimer > 0) {
			spinTimer--;
		}
	}

	public synchronized int getRuneCount() {
		return runes.size();
	}

	public synchronized Rune getRune(int index) {
		return runes.get(index);
	}

	//the angle (in degrees) of spot i when there are "count" runes: spot 0 is straight up
	private double getSlotAngle(int i, int count) {
		return -90 + i * 360.0 / count;
	}

	//where rune i should be drawn right now, in degrees
	//while spinning it slides from where it was to its new spot, going around the circle twice
	public synchronized double getAngle(int index) {
		double target = getSlotAngle(index, runes.size());
		if (spinTimer == 0) {
			return target;
		}

		//progress goes from 0 to 1, and slows down at the end like a real spinning wheel
		double progress = 1.0 - (double) spinTimer / spinTime;
		double slowDown = 1.0 - (1.0 - progress) * (1.0 - progress);

		double start = startAngles.get(index);
		double end = target + 720;
		return start + (end - start) * slowDown;
	}

}
