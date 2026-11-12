import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Canopy extends Level {

	public Canopy() {
		super(setEs(), new CanopyBackground(), setPlats());
	}

	public static Queue<Enemy> setEs() {
		Queue<Enemy> enemies = new LinkedList<Enemy>();
		enemies.add(new ThornBeetle());
		enemies.add(new VineSnapper());
		enemies.add(new DuskMoth());
		return enemies;
	}

	public static Platform[][] setPlats() {
		Platform[][] plats = new Platform[1][1];
		try {
			Scanner in = new Scanner(new File("canopyLevel.txt"));

			String word = in.next();
			while (!word.equals("map")) {
				word = in.next();
			}
			int cols = in.nextInt();
			int rows = in.nextInt();
			in.nextLine();

			plats = new Platform[rows][cols];
			for (int row = 0; row < rows; row++) {
				String line = in.nextLine();
				for (int col = 0; col < cols; col++) {
					if (line.charAt(col) == '#') {
						plats[row][col] = new Platform(col * 16, row * 16, 16, 16, true);
					} else if (line.charAt(col) == '=') {
						plats[row][col] = new Platform(col * 16, row * 16, 16, 16, false);
					}
				}
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Could not load canopyLevel.txt: " + e);
		}
		return plats;
	}

}
