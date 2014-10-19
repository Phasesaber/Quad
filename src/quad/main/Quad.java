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
	
	float x = Mouse.getX(), y = Mouse.getY();
	float rotation = 0;
	long lastFrame;
	int fps;
	long lastFPS;
	final int WIDTH = 1280;
	final int HEIGHT = 720;
	float speed = 0.12f;
	
	public void start() {
		try {
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

		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			
			update(delta);
			renderGL();

			Display.update();
			Display.sync(60);
		}

		Display.destroy();
	}
	
	public void update(int delta) {
		// rotate quad
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
		
		if (x < 0) x = 0;
		if (x > WIDTH) x = WIDTH;
		if (y < 0) y = 0;
		if (y > HEIGHT) y = HEIGHT;
		
		if(rotation >= 360) rotation = 0;
		
		updateFPS();
	}
	
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
	    return delta;
	}
	
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
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
	
	public void initGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, 0, HEIGHT, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public void registerEvents(){
		if(Keyboard.isKeyDown(Keyboard.KEY_R))
			GL11.glColor3f((new Random()).nextFloat(), (new Random()).nextFloat(), (new Random()).nextFloat());
		else if(Mouse.isButtonDown(0) && Mouse.isButtonDown(1))
			GL11.glColor3f(0.5f, 1.0f, 1.0f);
		else if(Mouse.isButtonDown(0))
			GL11.glColor3f(0.5f, 0.5f, 1.0f);
		else if(Mouse.isButtonDown(1))
			GL11.glColor3f(0.5f, 1.0f, 0.5f);
		else
			GL11.glColor3f(10, 10, 10);
		
		//Gradient for rotation
		//GL11.glColor3f(-(rotation-180)/100, (rotation-90)/100, rotation/100);
	}
	
	public void renderGL() {
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		registerEvents();
		
		GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 1);
			GL11.glRotatef(rotation, 0f, 0f, 1f);
			GL11.glTranslatef(-x, -y, 0);
			if(Keyboard.isKeyDown(Keyboard.KEY_T))
				GL11.glBegin(GL11.GL_TRIANGLES);
			else
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex2f(x - 150, y - 150);
					GL11.glVertex2f(x + 150, y - 150);
					GL11.glVertex2f(x + 150, y + 150);
					GL11.glVertex2f(x - 150, y + 150);
			GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public static void main(String[] arg) {
		Quad quad = new Quad();
		quad.start();
	}
}
