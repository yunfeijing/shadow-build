import java.util.ArrayList;

import org.newdawn.slick.Graphics;

// Class contains all the information needed to be display on the screen
public class Info {
	
	public static final int DRAW_STRING_A = 32;
	public static final int DRAW_STRING_B = 100;
	
	
	public Info() {}
	
	public void renderInfo(World world, Graphics g, ArrayList<Objects> list) {
		
		g.drawString("Metal:  "+world.getCurrMetal()+"\nUnobtainium:  "+world.getCurrUnobtain(), world.getCamera().calcWorldX(DRAW_STRING_A), world.getCamera().calcWorldY(DRAW_STRING_A));
		
		
		for(int i=0;i<list.size();i++) {
			
			Objects currObject = list.get(i);
		
			if(currObject.isSelected()) {
				if(currObject instanceof Builder) {
					g.drawString("1- Build Factory\n", world.getCamera().calcWorldX(DRAW_STRING_A), world.getCamera().calcWorldY(DRAW_STRING_B));
				}
				else if(currObject instanceof Commandcentre) {
					g.drawString("1- Create Scout\n2- Create Builder\n3- Create Engineer\n", world.getCamera().calcWorldX(DRAW_STRING_A), world.getCamera().calcWorldY(DRAW_STRING_B));
				}
				else if(currObject instanceof Factory) {
					g.drawString("1- Train Truck\n", world.getCamera().calcWorldX(DRAW_STRING_A), world.getCamera().calcWorldY(DRAW_STRING_B));
				}
			}
		}
		
	}
	
}
