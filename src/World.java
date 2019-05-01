import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

/* This class contains all the objects in the game */

public class World {
	// We set the maximum objects in this world is 50
	public static final int maximumObjects = 50;
	
	public static final String mapLocation = "assets/main.tmx";
	public static final String csvLocation = "assets/objects.csv";
	
	public static final String SOLID_PROPERTY = "solid";
	public static final String OCCUPIED_PROPERTY = "occupied";
	
	// Set the local variables for the map, player and camera
	private final TiledMap map;
	private final Camera camera;
	
	// Create a list contains all the objects in the world
	private Objects[] objectList = new Objects[maximumObjects];
	// Keep track the number of the objects
	private int numberOfObjects = 0;
	
	// The destination position and store it into a vector
	// Update when right-click anywhere, initilised at (-1, -1)
	private Vector2f destPos = new Vector2f(-1, -1);
	
	// The position the player choose, update when left-click anywhere
	private Vector2f selectPos = new Vector2f(0, 0);

	
	boolean isAnythingSelected = false;
	int selectedIndex = -1;

	// Construct the World
	public World() throws SlickException {
		map = new TiledMap(mapLocation);
		camera = new Camera(map, map.getWidth() * map.getTileWidth(), map.getHeight() * map.getTileHeight());
		
		// Get the initial objects
		try (BufferedReader br =
				new BufferedReader(new FileReader(csvLocation))) {
				String text;
				while ((text = br.readLine()) != null) {
					
					String cells[] = text.split(",");
					String name = cells[0];
					int x = Integer.parseInt(cells[1]);
					int y = Integer.parseInt(cells[2]);
					
					if(name.equals("command_centre")) {
						objectList[numberOfObjects++] = new Commandcentre(x, y, map);
					} else if (name.equals("metal_mine")) {
						objectList[numberOfObjects++] = new Metal(x, y, map);
					} else if (name.equals("unobtainium_mine")) {
						objectList[numberOfObjects++] = new Unobtainium(x, y, map);
					} else if (name.equals("pylon")) {
						objectList[numberOfObjects++] = new Pylon(x, y, map);
					} else if (name.equals("engineer")) {
						objectList[numberOfObjects++] = new Engineer(x, y, map);
					}
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(Input input, int delta) {
		// Read the mouse
		if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			// Calculate the right button position respect to the world using the function in the Camera class
			destPos.x = camera.calcWorldX(input.getMouseX());
			destPos.y = camera.calcWorldY(input.getMouseY());

		} else if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			
			// Reset the destination position
			destPos.x = -1; destPos.y = -1;
			
			boolean ifNewPosIsEmpty = true;
			// Calculate the left button position respect to the world
			selectPos.x = camera.calcWorldX(input.getMouseX());
			selectPos.y = camera.calcWorldY(input.getMouseY());
			
			for(int i=0;i<numberOfObjects;i++) {
				if((objectList[i].getPos().distance(selectPos)<=App.SELECT_DISTANCE)) {
					if(objectList[i] instanceof Units) {
						isAnythingSelected = true;
						selectedIndex = i;
						ifNewPosIsEmpty = false;
						break;
					}
					else if(objectList[i] instanceof Buildings) {
						isAnythingSelected = true;
						selectedIndex = i;
						ifNewPosIsEmpty = false;
					}
				}
			}
			if(ifNewPosIsEmpty==true) {
				// If nothing to be selected in the new mouse position, discard the selection
				isAnythingSelected = false;
				selectedIndex = -1;
			}
		}
		// Check if the destination has updated by the right-click (x or y > 0)
		if(isAnythingSelected && destPos.x>=0) {
			objectList[selectedIndex].update(delta, destPos);
		}
	}
	
	public void render(Graphics g) {
		// Firstly translate the camera based on the unit selected
		if(isAnythingSelected && objectList[selectedIndex] instanceof Units) {
			camera.translate(g, objectList[selectedIndex]);
		}
			
		// Display the map onto the screen
		map.render(0, 0);
		
		// Loop to render all the objects
		for(int i=0;i<numberOfObjects;i++) {
			objectList[i].render(g);
		}
	}
}
