package quad.main;

import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * Quad:Quad.java
 * 
 * @author Phasesaber on Oct 18, 2014
 */
public class Quad {
	
	//Variables
	float x = Mouse.getX(), y = Mouse.getY();
	float rotation = 0;
	long lastFrame;
	int fps;
	long lastFPS;
	final int WIDTH = 1080;
	final int HEIGHT = 720;
	float speed = 0.12f;
	
	/**
	 * Starts Game
	 */
	public void start() {
		try {
			//Setup Display
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();
			Display.setTitle("Quad");
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		initGL();
		getDelta();
		lastFPS = getTime();
		
		//Main Game Loop
		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			
			update(delta);
			renderGL();
			
			Display.update();
			Display.sync(60); //Keeps FPS at 60
		}
		
		//Removes Display after while loop ends
		Display.destroy();
	}
	
	/**
	 * Updates Game
	 * @param delta
	 */
	public void update(int delta) {
		//Combos for different speeds
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			rotation += speed * delta;
			if(Keyboard.isKeyDown(Keyboard.KEY_UP))
				speed += 0.001;
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
				speed -= 0.001;
			if(speed > 20)
				speed = 0.12f;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_O))
			rotation -= 0.12f * delta;
		else	
			rotation += 0.08f * delta;
		if(!Keyboard.isKeyDown(Keyboard.KEY_S))
			speed = 0.12f;
	
		x = Mouse.getX();
		y = Mouse.getY();
		
		//Makes sure square is on screen
		if (x < 0) x = 0;
		if (x > WIDTH) x = WIDTH;
		if (y < 0) y = 0;
		if (y > HEIGHT) y = HEIGHT;
		
		//Resets rotation to be used for things such as the gradient
		if(rotation >= 360) rotation = 0;
		
		updateFPS();
	}
	
	/**
	 * Gets Delta
	 * @return delta
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
	    return delta;
	}
	
	/**
	 * Gets System Time
	 * @return System Time
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * Updates the FPS on the Title
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			if(Keyboard.isKeyDown(Keyboard.KEY_T))
				Display.setTitle("Tri | FPS: " + fps + " |");
			else
				Display.setTitle("Quad | FPS: " + fps + " |");
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	/**
	 * Initializes OpenGL
	 */
	public void initGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, 0, HEIGHT, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	/**
	 * Registers the "Events" used as combos for secrets
	 */
	public void registerEvents() {
		// Checks for Key held down and changes color based on that
		if (Keyboard.isKeyDown(Keyboard.KEY_R))
			GL11.glColor3f((new Random()).nextFloat(),
					(new Random()).nextFloat(), (new Random()).nextFloat());
		else if (Keyboard.isKeyDown(Keyboard.KEY_G))
			GL11.glColor3f(-(rotation - 360) / 100, (rotation - 180) / 100,
					rotation / 100);
		else if (Mouse.isButtonDown(0) && Mouse.isButtonDown(1)
				&& Mouse.isButtonDown(2)) {
			GL11.glColor3f(0.7f,0.7f,0.7f);
			Display.setTitle("SPONGE IS THE NEW BUCKET");
		}else if(Mouse.isButtonDown(0)&&Mouse.isButtonDown(2))
			GL11.glColor3f(1.0f, 0.5f, 1.0f);
		else if(Mouse.isButtonDown(1)&&Mouse.isButtonDown(2))
			GL11.glColor3f(1.0f, 1.0f, 0.5f);
		else if (Mouse.isButtonDown(2))
			GL11.glColor3f(1.0f, 0.5f, 0.5f);
		else if (Mouse.isButtonDown(0) && Mouse.isButtonDown(1))
			GL11.glColor3f(0.5f, 1.0f, 1.0f);
		else if (Mouse.isButtonDown(0))
			GL11.glColor3f(0.5f, 0.5f, 1.0f);
		else if (Mouse.isButtonDown(1))
			GL11.glColor3f(0.5f, 1.0f, 0.5f);
		else
			GL11.glColor3f(10, 10, 10);
	}
	
	/**
	 * Renders OpenGL
	 */
	public void renderGL() {
		//Clears 
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		registerEvents();
		
		//Renders Shapes
		GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 1);
			GL11.glRotatef(rotation, 0f, 0f, 1f);
			GL11.glTranslatef(-x, -y, 0);
			//House Mode
			if(Keyboard.isKeyDown(Keyboard.KEY_H)){
				GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
					GL11.glVertex2f(x, y+300); // top of the roof
					GL11.glVertex2f(x - 150, y + 150); // left corner of the roof
					GL11.glVertex2f(x + 150, y + 150); // right corner of the roof
					GL11.glVertex2f(x - 150, y - 150); // bottom left corner of the house
					GL11.glVertex2f(x + 150, y - 150); //bottom right corner of the house
				GL11.glEnd();
				
			}else if(Keyboard.isKeyDown(Keyboard.KEY_F)){	
				GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
					GL11.glVertex2f(x, y);
					GL11.glVertex2f(x+150, y+130);
					GL11.glVertex2f(x+150, y-130);
					GL11.glVertex2f(x, y);
					GL11.glVertex2f(x-150, y+130);
					GL11.glVertex2f(x-150, y-130);
					GL11.glVertex2f(x, y);
					GL11.glVertex2f(x+130, y+150);
					GL11.glVertex2f(x-130, y+150);
					GL11.glVertex2f(x, y);
					GL11.glVertex2f(x+130, y-150);
					GL11.glVertex2f(x-130, y-150);
					if(Keyboard.isKeyDown(Keyboard.KEY_D)){
						GL11.glVertex2f(x+150, y+130);
						GL11.glVertex2f(x+300, y);
						GL11.glVertex2f(x+150, y-130);
						GL11.glVertex2f(x-150, y+130);
						GL11.glVertex2f(x-300, y);
						GL11.glVertex2f(x-150, y-130);
						GL11.glVertex2f(x, y+300);
						GL11.glVertex2f(x-130, y+150);
						GL11.glVertex2f(x+130, y+150);
						GL11.glVertex2f(x, y-300);
						GL11.glVertex2f(x-130, y-150);
						GL11.glVertex2f(x+130, y-150);
					}
				GL11.glEnd();
			}else{
				//Triangle Mode
				if(Keyboard.isKeyDown(Keyboard.KEY_T))
					GL11.glBegin(GL11.GL_TRIANGLES);
				else
					GL11.glBegin(GL11.GL_QUADS);
						GL11.glVertex2f(x - 150, y - 150);
						GL11.glVertex2f(x + 150, y - 150);
						GL11.glVertex2f(x + 150, y + 150);
						GL11.glVertex2f(x - 150, y + 150);
					GL11.glEnd();
					//Copy of Square
					if(Keyboard.isKeyDown(Keyboard.KEY_C)){
						GL11.glRotatef(0, 0f, 0f, 0f);
						GL11.glTranslatef(x, y, 0);
						if(Keyboard.isKeyDown(Keyboard.KEY_T))
							GL11.glBegin(GL11.GL_TRIANGLES);
						else
							GL11.glBegin(GL11.GL_QUADS);
							GL11.glVertex2f(x - 150, y - 150);
							GL11.glVertex2f(x + 150, y - 150);
							GL11.glVertex2f(x + 150, y + 150);
							GL11.glVertex2f(x - 150, y + 150);
						GL11.glEnd();
					}
			}
		GL11.glPopMatrix();
	}
	
	public static void main(String[] arg) {
		Quad quad = new Quad();
		quad.start();
	}
}
