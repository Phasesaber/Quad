package quad.event;

import org.lwjgl.opengl.GL11;

public class ColorEvent extends Event {
	
	public ColorEvent(boolean combo, final float r, final float g, final float b){
		super(combo, new Runnable(){public void run(){GL11.glColor3f(r,g,b);}});
	}
	
}
