import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Game extends JPanel  implements Runnable, KeyListener, MouseWheelListener, MouseListener, MouseMotionListener{


	private BufferedImage back;

	private static final int charSelectScreen = 0;
	private static final int spellbookScreen = 1;
	private static final int exploreScreen = 2;

	private int currentScreen = charSelectScreen;

	//ai written code to pull images from the sprite_grid
	private BufferedImage spriteSheet;
	private static final int frameSize = 64;
	private static final int characterFrameSize = 128;
	private static final int framesPerCharacter = 18;
	private static final int spriteMargin = 4;
	private static final int spritePadding = 4;

	//the columns of the sprite grid
	private static final int dirFront = 0;
	private static final int dirRight = 1;
	private static final int dirLeft = 2;
	private static final int dirJumpFront = 3;
	private static final int dirJumpRight = 4;
	private static final int dirJumpLeft = 5;
	private static final int dirFallFront = 6;
	private static final int dirFallRight = 7;
	private static final int dirFallLeft = 8;
	private BufferedImage[][] frames;

	private Character[] roster = new Character[] {
		new Mossback(),
		new Croakfang(),
		new Glimmerswarm(),
		new Cindertail()
	};

	private int selectedIndex = 0;

	//spin animation
	private static final int spinTime = 50;
	private static final int frontPauseTime = 200;
	private int spinCounter = 0;
	private int spinDirection = dirFront;
	private static final int spinStepTime = 40;
	private int spinStepCounter = 0;
	private int spinStep = 0;

	//ai written code to pull images from the 4 spell grids
	//aim directions match the order of the spell grids: down, up, left, right
	private static final int aimDown = 0;
	private static final int aimUp = 1;
	private static final int aimLeft = 2;
	private static final int aimRight = 3;
	private BufferedImage[][][] spellFrames;

	private SpellBook book = new SpellBook();

	//rune pictures: [which rune][animation frame]
	private BufferedImage[][] runeFrames;
	private int runeFrame = 0;
	private int runeFrameTimer = 0;

	private int spellFrame = 0;
	private int spellFrameTimer = 0;

	private int spellbookScroll = 0;
	private int spellbookMaxScroll = 0;
	private boolean upHeld = false;
	private boolean downHeld = false;

	private static final int viewHeight = 288;
	private static final int imageScale = 2;
	private static final int hitWidth = 17;
	private static final int hitHeight = 40;
	private static final double jumpSpeed = 2.5;
	private static final double gravity = 0.035;
	private static final double maxFallSpeed = 3.0;
	private int animStep = 0;
	private int animTimer = 0;

	private Platform[] platforms;
	private int explorerRow = 0;
	private double walkSpeed = 1.0;
	private double playerX = 0;
	private double playerY = 0;
	private double velocityY = 0;
	private boolean onGround = false;
	private int facing = dirFront;
	private double moveAmount = 0;
	private boolean leftHeld = false;
	private boolean rightHeld = false;
	private boolean jumpHeld = false;
	private boolean jumpQueued = false;
	private boolean dropHeld = false;
	private int downTapTimer = 0;
	private int dropTimer = 0;

	private static final int spellTile = 40;
	private static final int spellPicture = 64;
	private static final int worldRuneSize = 24;
	private static final int maxChain = 3;
	private Character hero;
	private ArrayList<Rune> worldRunes = new ArrayList<Rune>();
	private ArrayList<Rune> chain = new ArrayList<Rune>();
	private ArrayList<SpellEffect> effects = new ArrayList<SpellEffect>();
	private RuneCircle circle = new RuneCircle();
	private boolean faceRight = true;
	private int manaTimer = 0;

	private static final int enemyCount = 12;
	private static final int enemyColumns = 25;
	private static final int enemyStillColumn = 0;
	private static final int enemyRightColumn = 1;
	private static final int enemyLeftColumn = 9;
	private static final int enemyFadeTime = 80;
	private static final int enemyRespawnTime = 400;
	private static final int enemyAttackTime = 60;
	private static final int enemyHitTime = 40;
	private static final int enemyRecoverTime = 20;
	private static final int enemyCooldownTime = 300;
	private static final int enemyAttackColumn = 4;
	private static final int enemyUpRightColumn = 17;
	private static final int enemyUpLeftColumn = 20;
	private static final int enemyRecoverRightColumn = 23;
	private static final int enemyRecoverLeftColumn = 24;
	private static final int enemyHitWidth = 30;
	private static final int enemyHitHeight = 40;
	private BufferedImage[][] enemyFrames;
	private String[] enemyNames = new String[] {
		"Bog Leech", "Mire Lurker", "Mire Wraith",
		"Cinder Gnat", "Magma Toad", "Charred Stalker",
		"Glowcap Crawler", "Drowned Angler", "Bonewater Crab",
		"Thorn Beetle", "Vine Snapper", "Dusk Moth"
	};
	private Enemy enemy;
	private int enemyType = 0;
	private int enemyStep = 0;
	private int enemyStepTimer = 0;
	private int enemyAttackTimer = 0;
	private int enemyCooldown = 0;
	private boolean enemyAttackUp = false;


	public Game() {

		loadSprites();
		loadSpellSprites();
		loadRuneSprites();
		loadEnemySprites();

		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		new Thread(this).start();

	}

	private void loadSprites() {
		try {
			spriteSheet = ImageIO.read(new File("spriteGrid.png"));
		} catch (IOException e) {
			System.out.println("Could not load spriteGrid.png: " + e);
			spriteSheet = null;
		}

		frames = new BufferedImage[roster.length][framesPerCharacter];
		if (spriteSheet == null) {
			return;
		}

		for (int row = 0; row < roster.length; row++) {
			for (int col = 0; col < framesPerCharacter; col++) {
				int sx = spriteMargin + col * (characterFrameSize + spritePadding);
				int sy = spriteMargin + row * (characterFrameSize + spritePadding);
				frames[row][col] = spriteSheet.getSubimage(sx, sy, characterFrameSize, characterFrameSize);
			}
		}
	}

	private void loadSpellSprites() {
		spellFrames = new BufferedImage[4][book.getSpellCount()][3];
		loadSpellGrid(aimDown, "spellGridDown.png");
		loadSpellGrid(aimUp, "spellGridUp.png");
		loadSpellGrid(aimLeft, "spellGridLeft.png");
		loadSpellGrid(aimRight, "spellGridRight.png");
	}

	private void loadSpellGrid(int direction, String fileName) {
		BufferedImage sheet;
		try {
			sheet = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			System.out.println("Could not load " + fileName + ": " + e);
			return;
		}

		for (int row = 0; row < book.getSpellCount(); row++) {
			for (int col = 0; col < 3; col++) {
				int sx = spriteMargin + col * (frameSize + spritePadding);
				int sy = spriteMargin + row * (frameSize + spritePadding);
				spellFrames[direction][row][col] = sheet.getSubimage(sx, sy, frameSize, frameSize);
			}
		}
	}

	//runeGrid.png has one row per rune and 4 columns of it floating and glowing
	private void loadRuneSprites() {
		runeFrames = new BufferedImage[12][4];
		BufferedImage sheet;
		try {
			sheet = ImageIO.read(new File("runeGrid.png"));
		} catch (IOException e) {
			System.out.println("Could not load runeGrid.png: " + e);
			return;
		}

		for (int row = 0; row < 12; row++) {
			for (int col = 0; col < 4; col++) {
				int sx = spriteMargin + col * (frameSize + spritePadding);
				int sy = spriteMargin + row * (frameSize + spritePadding);
				runeFrames[row][col] = sheet.getSubimage(sx, sy, frameSize, frameSize);
			}
		}
	}

	//ai written code to pull images from the enemy grid
	private void loadEnemySprites() {
		enemyFrames = new BufferedImage[enemyCount][enemyColumns];
		BufferedImage sheet;
		try {
			sheet = ImageIO.read(new File("enemyGrid.png"));
		} catch (IOException e) {
			System.out.println("Could not load enemyGrid.png: " + e);
			return;
		}

		for (int row = 0; row < enemyCount; row++) {
			for (int col = 0; col < enemyColumns; col++) {
				int sx = spriteMargin + col * (characterFrameSize + spritePadding);
				int sy = spriteMargin + row * (characterFrameSize + spritePadding);
				enemyFrames[row][col] = sheet.getSubimage(sx, sy, characterFrameSize, characterFrameSize);
			}
		}
	}

	  public void run()
	   {
	   	try
	   	{
	   		repaint();
	   		while(true)
	   		{
	   		   Thread.currentThread().sleep(5);
	   		   updateSpin();
	   		   if (currentScreen == spellbookScreen) {
	   			   updateSpellbook();
	   		   }
	   		   if (currentScreen == exploreScreen) {
	   			   updateExploring();
	   		   }
	            repaint();
	         }
	      }catch(Exception e)
	      {
	      }
	  	}


	  public  void paint(Graphics g){

		  Graphics2D  twoDgraph= (Graphics2D)g;

		if(back==null)
		   back = (BufferedImage)(createImage(getWidth(),getHeight()));


		Graphics g2d = back.createGraphics();


		g2d.clearRect(0,0,getSize().width, getSize().height);
		//start coding here

		switch (currentScreen) {
			case charSelectScreen:
				drawCharacterSelect((Graphics2D) g2d);
				break;
			case spellbookScreen:
				drawSpellbook((Graphics2D) g2d);
				break;
			case exploreScreen:
				drawExploring((Graphics2D) g2d);
				break;
		}



		//do not delte this line of code, it draws everything above
		twoDgraph.drawImage(back, null, 0, 0);





	}

	private void drawCharacterSelect(Graphics2D g2d) {
		int w = getWidth();
		int h = getHeight();

		g2d.setColor(new Color(238, 233, 214));
		g2d.fillRect(0, 0, w, h);

		g2d.setColor(new Color(30, 22, 15));
		g2d.setFont(new Font("Arial", Font.BOLD, h / 20));
		drawCentered(g2d, "SELECT YOUR CHARACTER", w / 2, h / 8);

		int statBoxWidth = w / 4;
		int statBoxMargin = w / 32;
		int statBoxHeight = getStatLineHeight() * 11;

		int characterAreaWidth = w - statBoxWidth - statBoxMargin * 2;
		int slotSpacing = characterAreaWidth / (roster.length + 1);
		int centerY = h / 2;

		int maxSpriteSize = h / 3;
		int spriteSize = Math.min(maxSpriteSize, (int) (slotSpacing * 0.85));

		//keeps the pixel art sharp instead of blurry when it gets scaled up
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		for (int i = 0; i < roster.length; i++) {
			boolean isSelected = (i == selectedIndex);

			int drawSize = spriteSize * 3 / 4;
			int nameSize = h / 45;
			int dir = dirFront;

			if (isSelected) {
				drawSize = spriteSize;
				nameSize = h / 33;
				dir = spinDirection;
			}

			int slotX = slotSpacing * (i + 1);
			int drawX = slotX - drawSize / 2;
			int drawY = centerY - drawSize / 2;

			if (isSelected) {
				g2d.setColor(new Color(222, 214, 188));
				g2d.fillRect(drawX, drawY, drawSize, drawSize);
			}

			int step = 0;
			if (isSelected) {
				step = spinStep;
			}
			int bigSize = drawSize * 4 / 3;
			g2d.drawImage(frames[i][dir * 2 + step], slotX - bigSize / 2, drawY + drawSize - bigSize, bigSize, bigSize, null);

			g2d.setColor(new Color(30, 22, 15));
			g2d.setFont(new Font("Arial", Font.BOLD, nameSize));
			drawCentered(g2d, roster[i].getName(), slotX, drawY + drawSize + h / 12);
		}

		drawStatBox(g2d, roster[selectedIndex], w - statBoxWidth - statBoxMargin, centerY - statBoxHeight / 2, statBoxWidth, statBoxHeight);

		g2d.setColor(new Color(90, 80, 65));
		g2d.setFont(new Font("Arial", Font.PLAIN, h / 45));
		drawCentered(g2d, "Use ARROW KEYS or WASD to choose", w / 2, h - h / 12);
		drawCentered(g2d, "Press B to open the spellbook          Press ENTER to explore", w / 2, h - h / 30);
	}

	//one line of text in the stat box, used for the box height too
	private int getStatLineHeight() {
		return getHeight() / 26;
	}

	private void updateSpin() {
		spinCounter++;

		spinStepCounter++;
		if (spinStepCounter >= spinStepTime) {
			spinStepCounter = 0;
			if (spinStep == 0) {
				spinStep = 1;
			} else {
				spinStep = 0;
			}
		}

		//idle, right, left, jump, fall, then a pause on idle before it starts over
		if (spinCounter < spinTime) {
			spinDirection = dirFront;
		} else if (spinCounter < spinTime * 2) {
			spinDirection = dirRight;
		} else if (spinCounter < spinTime * 3) {
			spinDirection = dirLeft;
		} else if (spinCounter < spinTime * 4) {
			spinDirection = dirJumpFront;
		} else if (spinCounter < spinTime * 5) {
			spinDirection = dirFallFront;
		} else if (spinCounter < spinTime * 5 + frontPauseTime) {
			spinDirection = dirFront;
		} else {
			spinCounter = 0;
		}
	}

	private void drawStatBox(Graphics2D g2d, Character c, int x, int y, int boxWidth, int boxHeight) {
		int lineHeight = getStatLineHeight();
		int corner = lineHeight / 2;

		g2d.setColor(new Color(250, 246, 235));
		g2d.fillRoundRect(x, y, boxWidth, boxHeight, corner, corner);
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(getHeight() / 250 + 1));
		g2d.drawRoundRect(x, y, boxWidth, boxHeight, corner, corner);

		int textX = x + lineHeight;
		int textY = y + lineHeight + lineHeight / 2;

		g2d.setFont(new Font("Arial", Font.BOLD, lineHeight * 3 / 4));
		g2d.setColor(new Color(30, 22, 15));
		g2d.drawString(c.getName(), textX, textY);
		textY += lineHeight + lineHeight / 4;

		g2d.setFont(new Font("Arial", Font.PLAIN, lineHeight * 3 / 5));
		g2d.setColor(new Color(90, 80, 65));

		g2d.drawString("HP: " + c.getHp(), textX, textY);
		textY += lineHeight;
		g2d.drawString("Stamina: " + c.getStamina(), textX, textY);
		textY += lineHeight;
		g2d.drawString("Mana: " + c.getMana(), textX, textY);
		textY += lineHeight;
		g2d.drawString("Speed: " + c.getSpeed(), textX, textY);
		textY += lineHeight;
		g2d.drawString("Defense: " + c.getDefense(), textX, textY);
		textY += lineHeight;

		textY += lineHeight / 4;
		g2d.setColor(new Color(30, 22, 15));
		g2d.setFont(new Font("Arial", Font.BOLD, lineHeight * 3 / 5));
		g2d.drawString("Element: " + c.getElement(), textX, textY);
		textY += lineHeight;
		g2d.drawString("Special Rune: " + c.getSpecialRune().getName(), textX, textY);
	}

	private void drawCentered(Graphics2D g2d, String text, int centerX, int y) {
		FontMetrics fm = g2d.getFontMetrics();
		int textWidth = fm.stringWidth(text);
		g2d.drawString(text, centerX - textWidth / 2, y);
	}

	private void openSpellbook() {
		currentScreen = spellbookScreen;
		spellbookScroll = 0;
		upHeld = false;
		downHeld = false;
	}

	private void updateSpellbook() {
		int scrollSpeed = getHeight() / 300 + 1;
		if (upHeld) {
			spellbookScroll = spellbookScroll - scrollSpeed;
		}
		if (downHeld) {
			spellbookScroll = spellbookScroll + scrollSpeed;
		}
		if (spellbookScroll > spellbookMaxScroll) {
			spellbookScroll = spellbookMaxScroll;
		}
		if (spellbookScroll < 0) {
			spellbookScroll = 0;
		}

		runeFrameTimer++;
		if (runeFrameTimer >= 25) {
			runeFrameTimer = 0;
			runeFrame++;
			if (runeFrame >= 4) {
				runeFrame = 0;
			}
		}

		spellFrameTimer++;
		if (spellFrameTimer >= 40) {
			spellFrameTimer = 0;
			spellFrame++;
			if (spellFrame >= 3) {
				spellFrame = 0;
			}
		}
	}

	private void drawSpellbook(Graphics2D g2d) {
		int w = getWidth();
		int h = getHeight();

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		g2d.setColor(new Color(238, 233, 214));
		g2d.fillRect(0, 0, w, h);

		int margin = w / 32;
		int gap = h / 50;
		int cardWidth = (w - margin * 2 - gap) / 2;
		int rightX = margin + cardWidth + gap;
		int runeCardHeight = h / 8;
		int spellCardHeight = h / 6;
		int headerHeight = h / 12;
		int titleHeight = h / 7;
		int footerHeight = h / 14;

		int scroll = spellbookScroll;
		int y = titleHeight + gap - scroll;

		drawSectionHeader(g2d, "RUNES  (" + book.getRuneCount() + ")", margin, y + headerHeight * 2 / 3);
		y = y + headerHeight;

		for (int i = 0; i < book.getRuneCount(); i = i + 2) {
			drawRuneCard(g2d, book.getRune(i), margin, y, cardWidth, runeCardHeight);
			if (i + 1 < book.getRuneCount()) {
				drawRuneCard(g2d, book.getRune(i + 1), rightX, y, cardWidth, runeCardHeight);
			}
			y = y + runeCardHeight + gap;
		}

		y = y + gap;
		drawSectionHeader(g2d, "SPELLS  (" + book.getSpellCount() + ")", margin, y + headerHeight * 2 / 3);
		y = y + headerHeight;

		for (int i = 0; i < book.getSpellCount(); i = i + 2) {
			drawSpellCard(g2d, book.getSpell(i), i, margin, y, cardWidth, spellCardHeight);
			if (i + 1 < book.getSpellCount()) {
				drawSpellCard(g2d, book.getSpell(i + 1), i + 1, rightX, y, cardWidth, spellCardHeight);
			}
			y = y + spellCardHeight + gap;
		}

		spellbookMaxScroll = y + scroll - (h - footerHeight);

		g2d.setColor(new Color(238, 233, 214));
		g2d.fillRect(0, 0, w, titleHeight);
		g2d.fillRect(0, h - footerHeight, w, footerHeight);

		g2d.setColor(new Color(30, 22, 15));
		g2d.fillRect(margin, titleHeight - 3, w - margin * 2, 3);
		g2d.fillRect(margin, h - footerHeight, w - margin * 2, 3);
		g2d.setFont(new Font("Arial", Font.BOLD, h / 20));
		drawCentered(g2d, "SPELLBOOK", w / 2, h / 11);

		g2d.setColor(new Color(90, 80, 65));
		g2d.setFont(new Font("Arial", Font.PLAIN, h / 45));
		drawCentered(g2d, "SCROLL WHEEL, W / S or UP / DOWN to scroll          B to go back", w / 2, h - h / 45);
	}

	private void drawSectionHeader(Graphics2D g2d, String text, int x, int y) {
		g2d.setColor(new Color(30, 22, 15));
		g2d.setFont(new Font("Arial", Font.BOLD, getHeight() / 28));
		g2d.drawString(text, x, y);
	}

	private int drawCard(Graphics2D g2d, int x, int y, int cardWidth, int cardHeight) {
		int h = getHeight();
		int corner = h / 50;
		int pad = cardHeight / 10;
		int pictureSize = cardHeight - pad * 2;

		g2d.setColor(new Color(250, 246, 235));
		g2d.fillRoundRect(x, y, cardWidth, cardHeight, corner, corner);
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(h / 400 + 1));
		g2d.drawRoundRect(x, y, cardWidth, cardHeight, corner, corner);

		g2d.setColor(new Color(28, 38, 40));
		g2d.fillRoundRect(x + pad, y + pad, pictureSize, pictureSize, corner, corner);
		return pictureSize;
	}

	private void drawRuneCard(Graphics2D g2d, Rune r, int x, int y, int cardWidth, int cardHeight) {
		int h = getHeight();
		int pad = cardHeight / 10;
		int pictureSize = drawCard(g2d, x, y, cardWidth, cardHeight);

		g2d.drawImage(runeFrames[r.getSpriteRow()][runeFrame], x + pad, y + pad, pictureSize, pictureSize, null);

		int textX = x + pad * 2 + pictureSize;
		int lineHeight = cardHeight / 4;
		int textY = y + pad + lineHeight * 3 / 4;

		g2d.setColor(new Color(30, 22, 15));
		g2d.setFont(new Font("Arial", Font.BOLD, h / 38));
		g2d.drawString(r.getName(), textX, textY);
		textY = textY + lineHeight;

		g2d.setColor(new Color(90, 80, 65));
		g2d.setFont(new Font("Arial", Font.PLAIN, h / 52));
		g2d.drawString("Element: " + r.getElement() + "     Used in " + book.countSpellsWith(r.getName()) + " spells", textX, textY);
		textY = textY + lineHeight;
		g2d.drawString(getRuneSource(r), textX, textY);
	}

	private String getRuneSource(Rune r) {
		if (r.getSpriteRow() <= 3) {
			return "Every character starts with this rune";
		}

		for (int i = 0; i < roster.length; i++) {
			if (roster[i].getSpecialRune().getName().equals(r.getName())) {
				return roster[i].getName() + "'s special rune";
			}
		}

		return "Found out in the swamp";
	}

	private void drawSpellCard(Graphics2D g2d, Spell spell, int row, int x, int y, int cardWidth, int cardHeight) {
		int h = getHeight();
		int pad = cardHeight / 10;
		int pictureSize = drawCard(g2d, x, y, cardWidth, cardHeight);

		BufferedImage spellPicture = spellFrames[aimRight][row][spellFrame];
		if (spellPicture != null) {
			g2d.drawImage(spellPicture, x + pad, y + pad, pictureSize, pictureSize, null);
		}

		int textX = x + pad * 2 + pictureSize;
		int lineHeight = cardHeight / 5;
		int textY = y + pad + lineHeight * 3 / 4;

		g2d.setColor(new Color(30, 22, 15));
		g2d.setFont(new Font("Arial", Font.BOLD, h / 38));
		g2d.drawString(spell.getName(), textX, textY);
		textY = textY + lineHeight;

		g2d.setColor(new Color(90, 80, 65));
		g2d.setFont(new Font("Arial", Font.PLAIN, h / 52));
		g2d.drawString(spell.getType() + " spell     " + spell.getElement() + "     Power " + spell.getPower(), textX, textY);
		textY = textY + lineHeight;

		String extra = "";
		if (spell instanceof RangedSpell) {
			RangedSpell ranged = (RangedSpell) spell;
			extra = "Range: " + ranged.getRange() + " tiles";
		} else if (spell instanceof MeleeSpell) {
			MeleeSpell melee = (MeleeSpell) spell;
			extra = "+" + melee.getBonusDamage() + " damage up close";
		} else if (spell instanceof HealingSpell) {
			extra = "Heals you instead of hurting an enemy";
		}
		String[] recipe = spell.getRecipe();
		extra = extra + "     Costs " + book.getManaCost(recipe.length) + " mana";
		g2d.drawString(extra, textX, textY);
		textY = textY + lineHeight / 3;

		int iconSize = lineHeight * 5 / 4;
		int iconX = textX;
		g2d.setFont(new Font("Arial", Font.BOLD, h / 55));
		FontMetrics fm = g2d.getFontMetrics();
		for (int i = 0; i < recipe.length; i++) {
			if (i > 0) {
				g2d.setColor(new Color(30, 22, 15));
				g2d.drawString("+", iconX, textY + iconSize * 2 / 3);
				iconX = iconX + fm.stringWidth("+ ") + 2;
			}

			Rune r = book.findRune(recipe[i]);
			if (r != null) {
				g2d.drawImage(runeFrames[r.getSpriteRow()][1], iconX, textY, iconSize, iconSize, null);
			}
			iconX = iconX + iconSize;

			g2d.setColor(new Color(30, 22, 15));
			g2d.drawString(recipe[i], iconX, textY + iconSize * 2 / 3);
			iconX = iconX + fm.stringWidth(recipe[i] + " ") + 4;
		}
	}



	private double getZoom() {
		return getHeight() / (double) viewHeight;
	}

	private double getViewWidth() {
		return getWidth() / getZoom();
	}

	private void startExploring() {
		explorerRow = selectedIndex;
		hero = makeHero(selectedIndex);
		walkSpeed = 0.8 + hero.getSpeed() / 200.0;

		int viewWidth = (int) getViewWidth();
		platforms = new Platform[2];
		platforms[0] = new Platform(0, viewHeight - 40, viewWidth, 40);
		platforms[1] = new Platform(viewWidth * 11 / 20, viewHeight - 110, 110, 12);

		worldRunes.clear();
		addWorldRune("Lightning Rune", "Lightning", 8, viewWidth / 10, platforms[0].getY());
		addWorldRune("Ice Rune", "Ice", 9, viewWidth * 2 / 5, platforms[0].getY());
		addWorldRune("Venom Rune", "Venom", 10, platforms[1].getX() + platforms[1].getWidth() / 2, platforms[1].getY());
		addWorldRune("Shadow Rune", "Shadow", 11, viewWidth * 9 / 10, platforms[0].getY());

		synchronized (effects) {
			effects.clear();
		}
		chain.clear();
		circle.close();
		faceRight = true;
		manaTimer = 0;
		upHeld = false;

		enemyType = 0;
		makeEnemy();

		placePlayer();
		leftHeld = false;
		rightHeld = false;
		jumpHeld = false;
		jumpQueued = false;
		currentScreen = exploreScreen;
	}

	private void placePlayer() {
		playerX = getViewWidth() / 4;
		playerY = platforms[0].getY() - hitHeight;
		velocityY = 0;
		onGround = true;
	}

	private void updateExploring() {
		circle.update();
		updateEffects();

		updateEnemy();

		manaTimer++;
		if (manaTimer >= 40) {
			manaTimer = 0;
			hero.restoreMana(1);
		}

		runeFrameTimer++;
		if (runeFrameTimer >= 25) {
			runeFrameTimer = 0;
			runeFrame++;
			if (runeFrame >= 4) {
				runeFrame = 0;
			}
		}

		if (downTapTimer > 0) {
			downTapTimer--;
		}
		if (dropTimer > 0) {
			dropTimer--;
			if (dropHeld) {
				dropTimer = 20;
			}
		}

		moveAmount = 0;
		if (leftHeld) {
			moveAmount = moveAmount - walkSpeed;
			facing = dirLeft;
			faceRight = false;
		}
		if (rightHeld) {
			moveAmount = moveAmount + walkSpeed;
			facing = dirRight;
			faceRight = true;
		}
		if (!leftHeld && !rightHeld) {
			facing = dirFront;
		}

		if (jumpQueued && onGround) {
			velocityY = -jumpSpeed;
		}
		jumpQueued = false;
		if (!jumpHeld && velocityY < -1.0) {
			velocityY = -1.0;
		}

		velocityY = velocityY + gravity;
		if (velocityY > maxFallSpeed) {
			velocityY = maxFallSpeed;
		}

		playerX = playerX + moveAmount;
		if (playerX < 0) {
			playerX = 0;
		}
		if (playerX > getViewWidth() - hitWidth) {
			playerX = getViewWidth() - hitWidth;
		}

		onGround = false;
		moveUpOrDown(velocityY);

		if (playerY > viewHeight + 40) {
			placePlayer();
		}

		int animSpeed = 90;
		if (!onGround && velocityY < 0) {
			animSpeed = 16;
		} else if (!onGround) {
			animSpeed = 50;
		} else if (moveAmount != 0) {
			animSpeed = 24;
		}
		animTimer++;
		if (animTimer >= animSpeed) {
			animTimer = 0;
			if (animStep == 0) {
				animStep = 1;
			} else {
				animStep = 0;
			}
		}
	}

	private void moveUpOrDown(double amount) {
		double oldBottom = playerY + hitHeight;
		playerY = playerY + amount;

		if (amount > 0) {
			double newBottom = playerY + hitHeight;
			for (int i = 0; i < platforms.length; i++) {
				Platform p = platforms[i];
				boolean overlapsSideways = playerX + hitWidth > p.getX() && playerX < p.getX() + p.getWidth();
				if (overlapsSideways && dropTimer == 0 && oldBottom <= p.getY() + 0.01 && newBottom >= p.getY()) {
					playerY = p.getY() - hitHeight;
					velocityY = 0;
					onGround = true;
					return;
				}
			}
		}
	}

	private void makeEnemy() {
		if (enemyType == 0) {
			enemy = new BogLeech();
		} else if (enemyType == 1) {
			enemy = new SwampLurker();
		} else if (enemyType == 2) {
			enemy = new MireWraith();
		} else if (enemyType == 3) {
			enemy = new CinderGnat();
		} else if (enemyType == 4) {
			enemy = new MagmaToad();
		} else if (enemyType == 5) {
			enemy = new CharredStalker();
		} else if (enemyType == 6) {
			enemy = new GlowcapCrawler();
		} else if (enemyType == 7) {
			enemy = new DrownedAngler();
		} else if (enemyType == 8) {
			enemy = new BonewaterCrab();
		} else if (enemyType == 9) {
			enemy = new ThornBeetle();
		} else if (enemyType == 10) {
			enemy = new VineSnapper();
		} else if (enemyType == 11) {
			enemy = new DuskMoth();
		} else {
			enemy = new Enemy(20, 5);
		}
		enemy.setPosition((int) (getViewWidth() * 3 / 4), platforms[0].getY());
		enemyStep = 0;
		enemyStepTimer = 0;
		enemyAttackTimer = 0;
		enemyCooldown = enemyCooldownTime;
	}

	private void nextEnemy() {
		enemyType = enemyType + 1;
		if (enemyType >= enemyCount) {
			enemyType = 0;
		}
		makeEnemy();
	}

	private void killEnemy() {
		if (enemy != null && enemy.isAlive()) {
			enemy.takeDamage(enemy.getHp());
		}
	}

	private void updateEnemy() {
		if (enemy == null) {
			return;
		}

		if (enemy.isAlive()) {
			enemyStepTimer++;
			if (enemyStepTimer >= 60) {
				enemyStepTimer = 0;
				if (enemyStep == 0) {
					enemyStep = 1;
				} else {
					enemyStep = 0;
				}
			}

			if (enemyAttackTimer > 0) {
				enemyAttackTimer--;
				if (enemyAttackTimer == enemyHitTime && canEnemyHit()) {
					enemy.attack(hero);
				}
			} else if (enemyCooldown > 0) {
				enemyCooldown--;
			} else if (canEnemyHit()) {
				enemyAttackTimer = enemyAttackTime;
				enemyCooldown = enemyCooldownTime;
				enemyAttackUp = !isPlayerLevelWithEnemy();
			}
		} else {
			enemyAttackTimer = 0;
			enemy.addDeadTime();
			if (enemy.getDeadTime() >= enemyRespawnTime) {
				enemy.revive();
			}
		}
	}

	private boolean isPlayerLevelWithEnemy() {
		double enemyTop = enemy.getY() - enemyHitHeight;
		return enemy.getY() > playerY && enemyTop < playerY + hitHeight;
	}

	//the enemy can hit you on its left, its right and above it, but never below it
	private boolean canEnemyHit() {
		if (!hero.isAlive()) {
			return false;
		}

		double enemyLeft = enemy.getX() - enemyHitWidth / 2.0;
		double enemyRight = enemy.getX() + enemyHitWidth / 2.0;
		double enemyTop = enemy.getY() - enemyHitHeight;
		double enemyCenterY = enemy.getY() - enemyHitHeight / 2.0;
		double playerCenterY = playerY + hitHeight / 2.0;
		double reach = enemy.getReach();

		boolean sameColumn = enemyRight > playerX && enemyLeft < playerX + hitWidth;

		if (isPlayerLevelWithEnemy()) {
			double gapX = 0;
			if (playerX + hitWidth < enemyLeft) {
				gapX = enemyLeft - (playerX + hitWidth);
			} else if (playerX > enemyRight) {
				gapX = playerX - enemyRight;
			}
			return gapX <= reach;
		}

		if (sameColumn && playerCenterY < enemyCenterY) {
			double gapY = enemyTop - (playerY + hitHeight);
			return gapY <= reach;
		}

		return false;
	}

	private Character makeHero(int index) {
		if (index == 0) {
			return new Mossback();
		} else if (index == 1) {
			return new Croakfang();
		} else if (index == 2) {
			return new Glimmerswarm();
		} else {
			return new Cindertail();
		}
	}

	private void addWorldRune(String name, String element, int spriteRow, int centerX, int platformTop) {
		Rune rune = new Rune(name, element, spriteRow);
		rune.setPosition(centerX - worldRuneSize / 2, platformTop - worldRuneSize);
		worldRunes.add(rune);
	}

	private int getAim() {
		if (upHeld) {
			return aimUp;
		} else if (dropHeld) {
			return aimDown;
		} else if (faceRight) {
			return aimRight;
		} else {
			return aimLeft;
		}
	}

	private Rune getNearbyRune() {
		double centerX = playerX + hitWidth / 2.0;
		for (int i = 0; i < worldRunes.size(); i++) {
			Rune r = worldRunes.get(i);
			double runeCenterX = r.getX() + worldRuneSize / 2.0;
			boolean closeSideways = Math.abs(runeCenterX - centerX) < 24;
			boolean closeUpDown = r.getY() + worldRuneSize > playerY && r.getY() < playerY + hitHeight;
			if (closeSideways && closeUpDown) {
				return r;
			}
		}
		return null;
	}

	private void pickUpRune() {
		Rune r = getNearbyRune();
		if (r == null) {
			return;
		}
		if (hero.addRune(r)) {
			worldRunes.remove(r);
		}
	}

	private void pickFromCircle(int number) {
		if (!circle.isOpen()) {
			return;
		}
		if (chain.size() >= maxChain) {
			return;
		}
		Rune picked = circle.pick(number);
		if (picked != null) {
			chain.add(picked);
		}
	}

	private void useCircleKey() {
		if (!circle.isOpen()) {
			chain.clear();
			circle.open(hero.getRuneRing());
		} else {
			castChain();
			circle.close();
			chain.clear();
		}
	}

	private void castChain() {
		if (chain.size() == 0) {
			return;
		}

		int aim = getAim();
		double gap = getEnemyGap(aim);
		Enemy target = null;
		int distance = 999;
		if (gap >= 0) {
			target = enemy;
			distance = (int) Math.ceil(gap / spellTile);
			if (distance < 1) {
				distance = 1;
			}
		}

		boolean enoughMana = hero.getMana() >= book.getManaCost(chain.size());
		Spell spell = book.findSpell(chain);
		hero.castSpell(chain, book, target, distance);

		if (enoughMana && spell != null) {
			startEffect(spell, aim, gap);
		}
	}

	//how many pixels away the enemy is in the aim direction, measured from the middle of the player
	//returns -1 if the enemy is dead or isn't in that direction
	private double getEnemyGap(int aim) {
		if (enemy == null || !enemy.isAlive()) {
			return -1;
		}

		double playerCenterX = playerX + hitWidth / 2.0;
		double playerCenterY = playerY + hitHeight / 2.0;
		double enemyLeft = enemy.getX() - enemyHitWidth / 2.0;
		double enemyRight = enemy.getX() + enemyHitWidth / 2.0;
		double enemyTop = enemy.getY() - enemyHitHeight;
		double enemyBottom = enemy.getY();
		double enemyCenterX = enemy.getX();
		double enemyCenterY = enemy.getY() - enemyHitHeight / 2.0;

		boolean sameHeight = enemyBottom > playerY && enemyTop < playerY + hitHeight;
		boolean sameColumn = enemyRight > playerX && enemyLeft < playerX + hitWidth;

		double gap = -1;
		if (aim == aimRight && sameHeight && enemyCenterX > playerCenterX) {
			gap = Math.max(0, enemyLeft - playerCenterX);
		} else if (aim == aimLeft && sameHeight && enemyCenterX < playerCenterX) {
			gap = Math.max(0, playerCenterX - enemyRight);
		} else if (aim == aimUp && sameColumn && enemyCenterY < playerCenterY) {
			gap = Math.max(0, playerCenterY - enemyBottom);
		} else if (aim == aimDown && sameColumn && enemyCenterY > playerCenterY) {
			gap = Math.max(0, enemyTop - playerCenterY);
		}
		return gap;
	}

	private void addEffect(SpellEffect effect) {
		synchronized (effects) {
			effects.add(effect);
		}
	}

	//gap is how far away the enemy is in the aim direction (-1 if there is no enemy there)
	private void startEffect(Spell spell, int aim, double gap) {
		int row = book.getSpellIndex(spell);
		double playerCenterX = playerX + hitWidth / 2.0;
		double feetY = playerY + hitHeight;
		double bodyY = feetY - hitHeight / 2.0;
		double viewWidth = getViewWidth();
		double half = spellPicture / 2.0;

		double dirX = 0;
		double dirY = 0;
		if (aim == aimLeft) {
			dirX = -1;
		} else if (aim == aimRight) {
			dirX = 1;
		} else if (aim == aimUp) {
			dirY = -1;
		} else {
			dirY = 1;
		}

		if (spell instanceof HealingSpell) {
			addEffect(new SpellEffect(row, aimRight, playerCenterX - half, feetY - spellPicture, 0, 0, 0));
			return;
		}

		double tiles = 1;
		if (spell instanceof RangedSpell) {
			RangedSpell ranged = (RangedSpell) spell;
			tiles = ranged.getRange();
		}

		if (spell.travels()) {
			double startDistance = spellTile / 2.0;
			double travelDistance = tiles * spellTile - startDistance;
			if (spell instanceof MeleeSpell) {
				travelDistance = spellTile / 2.0;
			}
			double startX = playerCenterX + dirX * startDistance;
			double startY = bodyY + dirY * startDistance;

			double room = 0;
			if (aim == aimLeft) {
				room = startX;
			} else if (aim == aimRight) {
				room = viewWidth - startX;
			} else if (aim == aimUp) {
				room = startY;
			} else {
				room = viewHeight - startY;
			}
			if (travelDistance > room) {
				travelDistance = room;
			}
			if (gap >= 0 && spell instanceof RangedSpell && gap - startDistance < travelDistance) {
				travelDistance = gap - startDistance;
			}
			if (travelDistance < 0) {
				travelDistance = 0;
			}

			double speed = spellTile / 12.0;
			int travelTime = (int) (travelDistance / speed);
			addEffect(new SpellEffect(row, aim, startX - half, startY - half, dirX * speed, dirY * speed, travelTime));
		} else {
			double distance = tiles * spellTile;
			if (gap >= 0 && spell instanceof RangedSpell && gap < distance) {
				if (dirY == 0) {
					distance = gap + enemyHitWidth / 2.0;
				} else {
					distance = gap + enemyHitHeight / 2.0;
				}
			}
			double centerX = playerCenterX + dirX * distance;
			double centerY = feetY - half;
			if (dirY != 0) {
				centerY = bodyY + dirY * distance;
			}
			if (centerX < half) {
				centerX = half;
			}
			if (centerX > viewWidth - half) {
				centerX = viewWidth - half;
			}
			if (centerY < half) {
				centerY = half;
			}
			if (centerY > viewHeight - half) {
				centerY = viewHeight - half;
			}
			addEffect(new SpellEffect(row, aim, centerX - half, centerY - half, 0, 0, 0));
		}
	}

	private void updateEffects() {
		synchronized (effects) {
			for (int i = effects.size() - 1; i >= 0; i--) {
				effects.get(i).update();
				if (effects.get(i).isFinished()) {
					effects.remove(i);
				}
			}
		}
	}

	private void drawEffects(Graphics2D g2d, double zoom) {
		int size = (int) Math.round(spellPicture * zoom);
		synchronized (effects) {
			for (int i = 0; i < effects.size(); i++) {
				SpellEffect e = effects.get(i);
				BufferedImage picture = spellFrames[e.getDirection()][e.getRow()][e.getFrame()];
				if (picture != null) {
					int sx = (int) Math.round(e.getX() * zoom);
					int sy = (int) Math.round(e.getY() * zoom);
					g2d.drawImage(picture, sx, sy, size, size, null);
				}
			}
		}
	}

	private void drawWorldRunes(Graphics2D g2d, double zoom) {
		Rune nearby = getNearbyRune();
		int size = (int) Math.round(worldRuneSize * zoom);
		for (int i = 0; i < worldRunes.size(); i++) {
			Rune r = worldRunes.get(i);
			int sx = (int) Math.round(r.getX() * zoom);
			int sy = (int) Math.round(r.getY() * zoom);
			g2d.drawImage(runeFrames[r.getSpriteRow()][runeFrame], sx, sy, size, size, null);
			if (r == nearby) {
				g2d.setColor(new Color(245, 238, 215));
				g2d.setFont(new Font("Arial", Font.BOLD, size / 2));
				drawCentered(g2d, "E", sx + size / 2, sy - size / 4);
			}
		}
	}

	private void drawEnemy(Graphics2D g2d, double zoom) {
		if (enemy == null) {
			return;
		}

		int spriteSize = (int) Math.round(characterFrameSize * zoom / imageScale);
		int screenX = (int) Math.round(enemy.getX() * zoom);
		int feetY = (int) Math.round(enemy.getY() * zoom);

		int column = enemyLeftColumn + enemyStep;
		if (playerX + hitWidth / 2.0 > enemy.getX()) {
			column = enemyRightColumn + enemyStep;
		}
		if (enemy.isAlive() && enemyAttackTimer > 0) {
			int swing = 0;
			if (enemyAttackTimer <= enemyHitTime) {
				swing = 1;
			}
			if (enemyAttackTimer <= enemyRecoverTime) {
				swing = 2;
			}
			boolean playerOnRight = playerX + hitWidth / 2.0 > enemy.getX();
			if (enemyAttackUp && playerOnRight) {
				column = enemyUpRightColumn + swing;
			} else if (enemyAttackUp) {
				column = enemyUpLeftColumn + swing;
			} else if (swing == 2 && playerOnRight) {
				column = enemyRecoverRightColumn;
			} else if (swing == 2) {
				column = enemyRecoverLeftColumn;
			} else if (playerOnRight) {
				column = enemyRightColumn + enemyAttackColumn + swing;
			} else {
				column = enemyLeftColumn + enemyAttackColumn + swing;
			}
		}

		Composite oldComposite = g2d.getComposite();
		if (!enemy.isAlive()) {
			int dead = enemy.getDeadTime();
			if (dead >= enemyFadeTime) {
				return;
			}
			float alpha = 1.0f - dead / (float) enemyFadeTime;
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			feetY = feetY + (int) Math.round(dead * zoom / 8);
		}

		BufferedImage picture = enemyFrames[enemyType][column];
		if (picture != null) {
			g2d.drawImage(picture, screenX - spriteSize / 2, feetY - spriteSize, spriteSize, spriteSize, null);
		}
		g2d.setComposite(oldComposite);

		if (enemy.isAlive()) {
			g2d.setFont(new Font("Arial", Font.BOLD, getHeight() / 50));
			g2d.setColor(new Color(0, 0, 0, 140));
			drawCentered(g2d, enemyNames[enemyType], screenX + 2, feetY - spriteSize + 2);
			g2d.setColor(new Color(245, 238, 215));
			drawCentered(g2d, enemyNames[enemyType], screenX, feetY - spriteSize);

			if (enemy.getHp() < enemy.getMaxHp()) {
				int barWidth = spriteSize / 2;
				int barHeight = Math.max(4, getHeight() / 120);
				int barX = screenX - barWidth / 2;
				int barY = feetY - spriteSize + getHeight() / 40;
				g2d.setColor(new Color(0, 0, 0, 160));
				g2d.fillRect(barX - 1, barY - 1, barWidth + 2, barHeight + 2);
				g2d.setColor(new Color(190, 60, 50));
				g2d.fillRect(barX, barY, barWidth * enemy.getHp() / enemy.getMaxHp(), barHeight);
			}
		}
	}

	private void drawHud(Graphics2D g2d) {
		int w = getWidth();
		int h = getHeight();
		int barWidth = w / 5;
		int barHeight = h / 34;
		int x = w / 40;
		int y = h / 30;

		g2d.setFont(new Font("Arial", Font.BOLD, barHeight * 3 / 4));
		drawBar(g2d, x, y, barWidth, barHeight, hero.getHp(), hero.getMaxHp(), new Color(190, 60, 50), "HP");
		drawBar(g2d, x, y + barHeight * 3 / 2, barWidth, barHeight, hero.getMana(), hero.getMaxMana(), new Color(70, 120, 220), "Mana");

		ArrayList<Rune> ring = hero.getRuneRing();
		int icon = h / 18;
		int gap = icon / 6;
		for (int i = 0; i < ring.size(); i++) {
			int iconX = w - w / 40 - (i + 1) * (icon + gap);
			g2d.setColor(new Color(0, 0, 0, 120));
			g2d.fillRect(iconX, y, icon, icon);
			g2d.drawImage(runeFrames[ring.get(i).getSpriteRow()][runeFrame], iconX, y, icon, icon, null);
		}
	}

	private void drawBar(Graphics2D g2d, int x, int y, int barWidth, int barHeight, int value, int maxValue, Color color, String label) {
		g2d.setColor(new Color(0, 0, 0, 160));
		g2d.fillRect(x - 2, y - 2, barWidth + 4, barHeight + 4);
		g2d.setColor(color);
		g2d.fillRect(x, y, barWidth * value / maxValue, barHeight);
		g2d.setColor(Color.WHITE);
		g2d.drawString(label + " " + value + "/" + maxValue, x + barHeight / 3, y + barHeight * 3 / 4);
	}

	private void drawRuneCircle(Graphics2D g2d, int centerX, int centerY, int spriteSize) {
		int radius = spriteSize;
		int icon = spriteSize * 2 / 5;
		int margin = radius + icon;
		if (centerX < margin) {
			centerX = margin;
		}
		if (centerX > getWidth() - margin) {
			centerX = getWidth() - margin;
		}
		if (centerY < margin) {
			centerY = margin;
		}
		if (centerY > getHeight() - margin) {
			centerY = getHeight() - margin;
		}

		g2d.setColor(new Color(20, 18, 30, 150));
		g2d.fillOval(centerX - radius - icon / 2, centerY - radius - icon / 2, (radius + icon / 2) * 2, (radius + icon / 2) * 2);
		g2d.setColor(new Color(150, 130, 96, 200));
		g2d.setStroke(new BasicStroke(2));
		g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

		g2d.setFont(new Font("Arial", Font.BOLD, icon / 3));
		for (int i = 0; i < circle.getRuneCount(); i++) {
			double angle = Math.toRadians(circle.getAngle(i));
			int x = centerX + (int) (Math.cos(angle) * radius);
			int y = centerY + (int) (Math.sin(angle) * radius);
			Rune r = circle.getRune(i);
			g2d.drawImage(runeFrames[r.getSpriteRow()][runeFrame], x - icon / 2, y - icon / 2, icon, icon, null);
			if (!circle.isSpinning()) {
				g2d.setColor(new Color(245, 238, 215));
				drawCentered(g2d, "" + (i + 1), x, y + icon / 2 + icon / 3);
			}
		}

		int small = icon * 3 / 4;
		int startX = centerX - chain.size() * small / 2;
		for (int i = 0; i < chain.size(); i++) {
			g2d.drawImage(runeFrames[chain.get(i).getSpriteRow()][runeFrame], startX + i * small, centerY - small / 2, small, small, null);
		}
	}

	private void drawExploring(Graphics2D g2d) {
		int w = getWidth();
		int h = getHeight();

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, w, h);
		if (platforms == null) {
			return;
		}

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		double zoom = getZoom();

		for (int i = 0; i < platforms.length; i++) {
			Platform p = platforms[i];
			int px = (int) Math.round(p.getX() * zoom);
			int py = (int) Math.round(p.getY() * zoom);
			int pw = (int) Math.round(p.getWidth() * zoom);
			int ph = (int) Math.round(p.getHeight() * zoom);
			g2d.setColor(new Color(70, 62, 52));
			g2d.fillRect(px, py, pw, ph);
			g2d.setColor(new Color(150, 130, 96));
			g2d.fillRect(px, py, pw, Math.max(3, (int) Math.round(2 * zoom)));
		}

		drawWorldRunes(g2d, zoom);
		drawEnemy(g2d, zoom);

		int dir = facing;
		if (!onGround) {
			if (velocityY < 0) {
				if (facing == dirRight) {
					dir = dirJumpRight;
				} else if (facing == dirLeft) {
					dir = dirJumpLeft;
				} else {
					dir = dirJumpFront;
				}
			} else {
				if (facing == dirRight) {
					dir = dirFallRight;
				} else if (facing == dirLeft) {
					dir = dirFallLeft;
				} else {
					dir = dirFallFront;
				}
			}
		}

		int spriteSize = (int) Math.round(characterFrameSize * zoom / imageScale);
		int playerScreenX = (int) Math.round((playerX + hitWidth / 2.0) * zoom);
		int feetScreenY = (int) Math.round((playerY + hitHeight) * zoom);
		g2d.drawImage(frames[explorerRow][dir * 2 + animStep], playerScreenX - spriteSize / 2, feetScreenY - spriteSize, spriteSize, spriteSize, null);

		drawEffects(g2d, zoom);
		drawHud(g2d);
		if (circle.isOpen()) {
			drawRuneCircle(g2d, playerScreenX, feetScreenY - spriteSize / 2, spriteSize);
		}

		g2d.setColor(new Color(245, 238, 215, 170));
		g2d.setFont(new Font("Arial", Font.PLAIN, h / 55));
		drawCentered(g2d, "A/D walk     SPACE jump     C rune circle (then 1-9 to pick, C to cast)     E pick up rune     hold W or S to aim     Q character select", w / 2, h - h / 40);
	}

	  private void handleExploreInput(KeyEvent e) {
	  	int key = e.getKeyCode();

	  	if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
	  		leftHeld = true;
	  	} else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
	  		rightHeld = true;
	  	} else if (key == KeyEvent.VK_SPACE) {
	  		if (!jumpHeld) {
	  			jumpQueued = true;
	  		}
	  		jumpHeld = true;
	  	} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
	  		if (!dropHeld) {
	  			if (downTapTimer > 0) {
	  				dropTimer = 20;
	  				downTapTimer = 0;
	  			} else {
	  				downTapTimer = 60;
	  			}
	  		}
	  		dropHeld = true;
	  	} else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
	  		upHeld = true;
	  	} else if (key == KeyEvent.VK_C) {
	  		useCircleKey();
	  	} else if (key == KeyEvent.VK_E) {
	  		pickUpRune();
	  	} else if (key == KeyEvent.VK_BACK_SPACE) {
	  		circle.close();
	  		chain.clear();
	  	} else if (key >= KeyEvent.VK_1 && key <= KeyEvent.VK_9) {
	  		pickFromCircle(key - KeyEvent.VK_1 + 1);
	  	} else if (key == KeyEvent.VK_Q) {
	  		circle.close();
	  		chain.clear();
	  		currentScreen = charSelectScreen;
	  	}
	  }

	  @Override
	  public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	//	throw new UnsupportedOperationException("Unimplemented method 'mouseDragged'");
		//System.out.println("mouse dragged");
	  
}

	  @Override
	  public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Unimplemented method 'mouseMoved'");
		//System.out.println("mouse moved");
	  }

	  @Override
	  public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
		//System.out.println("mouse clicked");

	  }

	  @Override
	  public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
		//System.out.println("mouse pressed");
		if (currentScreen == exploreScreen) {
			nextEnemy();
		}
	  }

	  @Override
	  public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
	  }

	  @Override
	  public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		////throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
	  }

	  @Override
	  public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
	  }

	  @Override
	  public void mouseWheelMoved(MouseWheelEvent e) {
		//System.out.println("mouse wheel scroll amt" +e.getScrollAmount());
		//System.out.println("mouse wheel scroll rotation" +e.getWheelRotation());
		//System.out.println("mouse wheel scroll rotation perc" +e.getPreciseWheelRotation());

		if (currentScreen == spellbookScreen) {
			spellbookScroll = spellbookScroll + (int) (e.getPreciseWheelRotation() * getHeight() / 12);
		}
	  }

	  @Override
	  public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
	  }

	  @Override
	  public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}

		switch (currentScreen) {
			case charSelectScreen:
				handleCharacterSelectInput(e);
				break;
			case spellbookScreen:
				handleSpellbookInput(e);
				break;
			case exploreScreen:
				handleExploreInput(e);
				break;
		}
	  }

	  private void handleCharacterSelectInput(KeyEvent e) {
	  	int key = e.getKeyCode();

	  	if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A || key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
	  		selectedIndex = (selectedIndex - 1 + roster.length) % roster.length;
	  		spinCounter = 0;
	  	} else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
	  		selectedIndex = (selectedIndex + 1) % roster.length;
	  		spinCounter = 0;
	  	} else if (key == KeyEvent.VK_B) {
	  		openSpellbook();
	  	} else if (key == KeyEvent.VK_ENTER) {
	  		startExploring();
	  	}
	  }

	  private void handleSpellbookInput(KeyEvent e) {
	  	int key = e.getKeyCode();

	  	if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
	  		upHeld = true;
	  	} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
	  		downHeld = true;
	  	} else if (key == KeyEvent.VK_B || key == KeyEvent.VK_BACK_SPACE) {
	  		currentScreen = charSelectScreen;
	  	}
	  }

	  @Override
	  public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			upHeld = false;
		} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			downHeld = false;
		}

		if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			leftHeld = false;
		} else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			rightHeld = false;
		} else if (key == KeyEvent.VK_SPACE) {
			jumpHeld = false;
		}
		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			dropHeld = false;
		}
	  }

}
