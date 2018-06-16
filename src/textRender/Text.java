package textRender;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;
import widgets.Button;
import widgets.GUIComponent;

public class Text {

	//instance variables
	private String str;
	private float x;
	private float y;
	private float height;
	private float width;
	private Loader loader;
	private float z;
	private ArrayList<GUIComponent> GUIlist = new ArrayList<GUIComponent>();
	
	/**
	 * Creates a text object.
	 * 
	 * @param str			the string to be displayed
	 * @param x				the x-coordinate of the bottom-left corner of the text
	 * @param y				the y-coordinate of the bottom-left corner of the text
	 * @param z				the z-coordinate of the text
	 * @param height		the height of a single letter
	 * @param width			the width of a single letter
	 * @param loader		the loader object
	 */
	public Text(String str, float x, float y, float z, float height, float width, Loader loader) {
		
		this.str = str;
		this.x = x;
		this.y = y;
		this.z = z;
		this.height = height;
		this.width = width;
		this.loader = loader;
		getList();
	}
	
	/**
	 * Updates the position of the text.
	 * 
	 * @param offsetX  offset in horizontal direction 
	 * from current horizontal position
	 * @param offsetY  offset in vertical direction 
	 * from current vertical position
	 */
	public void updatePosition(float offsetX, float offsetY) {
		
		for (GUIComponent guiComponent: GUIlist) {
			
			guiComponent.getPosition().x += offsetX;
			guiComponent.getPosition().y += offsetY;
		}
	}
	
	/**
	 * Changes text into an array of strings, with each index holding a character
	 * 
	 * @return New array list of strings
	 */
	private ArrayList<String> changeText()
	{
		int sizeS = this.str.length();
       
		ArrayList<String> New = new ArrayList<String>();
		
		//goes through all characters of the string
		for( int i=0;i<sizeS; i++)
		{
			New.add(charToString(this.str.charAt(i)));

		}

		return New;	
	}

	/**
	 * Changes text into an array of strings, with each index holding a character
	 * 
	 * @param str string that needs to be changed
	 * @return New array list of strings
	 */
	private ArrayList<String> changeText(String str)
	{
		int sizeS = str.length();

		ArrayList<String> New = new ArrayList<String>();
		
		//goes through all characters of the string
		for( int i=0;i<sizeS; i++)
		{
			New.add(charToString(str.charAt(i)));

		}

		return New;	
	}
	
	/** 
	 * Changes the position of the string
	 * 
	 * @param x new x position
	 * @param y new y position
	 */
	public void setPositionOfLine(float x, float y)
	{
		this.x = x;
		this.y=y;
	}

	/** 
	 * returns the  x position of the string
	 * 
	 * @return x 
	 */
	public float getPositionOfLineX()
	{
		return x;
	}

	/** 
	 * returns the  y position of the string
	 * 
	 * @return y 
	 */
	public float getPositionOfLineY()
	{
		return y;
	}

	/** 
	 * sets the size of the string
	 * 
	 * @param size 
	 */
	public void setSize(int size)
	{
		this.height = this.height*size;
		this.width = this.width*size;
	}
	
	/**
	 * returns the height of the string
	 * 
	 * @return height 
	 */
	public float getHeight()
	{
		return height;

	}

	/** 
	 * returns the width of the string
	 * 
	 * @return width 
	 */
	public float getWidth()
	{
		return width;
	}

	/** 
	 * uses the information of the arraylist and creates a quad for each character
	 *  
	 */
	public void getList(){
		ArrayList<String> list = new ArrayList<String>();
		list = changeText();
		int size = list.size();

		//goes through the array list of characters and creates a quad
		for(int i = 0; i<size;i++ )
		{
			float height = this.height;
			float width = this.width;
			float x = this.x +width/2 +0.5f*width*i;
			float y = this.y+height/2;
			float z=this.z;

			float[] vertices = Entity.getVertices(width, height, z);
			float[] texCoords = Entity.getTexCoords();
			int[] indices = Entity.getIndices();

			Vector3f rotation = new Vector3f(0,0,0);
			float scale = 1f;

			Vector3f Pos = new Vector3f(x, y, z);
			
			//gets the specified character image
			int textureID = loader.loadTexture("./text/"+list.get(i)+".png");
			
			Model nButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);

			Button letter = new Button(nButtonModel, Pos, rotation, scale, width, height);

			GUIlist.add(letter);
		}


	}

	/**
	 * allows characters to be added to the string
	 * 
	 * @param index index where the new character has to be added to
	 *  
	 */
	public void addStr(int index)
	{
		float height = this.height;
		float width = this.width;
		float x = this.x +width/2 +0.5f*width*index;
		float y = this.y+height/2;
		float z=this.z;

		//creates another quad
		float[] vertices = Entity.getVertices(width, height, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();

		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;

		Vector3f Pos = new Vector3f(x, y, z);

		//gives the quad a default image, it will be changed to the correct one later
		int textureID = loader.loadTexture("./text/1.png");
		Model nButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);

		Button letter = new Button(nButtonModel, Pos, rotation, scale, width, height);

		GUIlist.add(letter);
	}

	/**
	 * changes the text of each quad that is rendered
	 * also gives the command of 
	 * 
	 * @param index index where the new character has to be added to
	 *  
	 */
	public void changeStr(String str)
	{

		int length = this.str.length();
		
		//changes the " " to "space" for the program to understand which image to get
		str.replaceAll(" ","space");
		ArrayList<String> intList = changeText(str);
		
		//if the new string is equal in length to the previous string
		if(length==str.length())
		{
			//loops through all the characters in the new string
			for(int i = 0; i<length;i++)
			{
				GUIlist.get(i).getModel().setTextureID("./text/"+intList.get(i)+".png", loader);
			}

		}
		
		//if the new string is longer than the previous string
		else if(length<str.length())
		{
			for(int i = 0; i<length;i++)
			{
				GUIlist.get(i).getModel().setTextureID("./text/"+intList.get(i)+".png", loader);
			}
			
			//adds the new quads 
			for(int n= length;n<str.length();n++)
			{
				addStr(n);
				GUIlist.get(n).getModel().setTextureID("./text/"+intList.get(n)+".png", loader);

			}
		}
		
		//if the new string is shorter than the previous string
		else if(length>str.length())
		{
			for(int i = 0; i<str.length();i++)
			{
				GUIlist.get(i).getModel().setTextureID("./text/"+intList.get(i)+".png", loader);
			}
			
			//delete the unneeded characters
			for(int n=length-1;n>=str.length();n--)
			{

				GUIlist.remove(n);

			}
		}
		this.str = str;
	}

	/**
	 * 
	 * @return GUIlist arraylist of GUI components containing the characters to render text
	 */
	public ArrayList<GUIComponent> getGUIlist()
	{
		return GUIlist;
	}
	
	/**
	 * Returns the string.
	 * 
	 * @return str
	 */
	public String getString() {
		return str;
	}
	
	/**
	 * Converts a given character to the string 
	 * that represents the file name containing that 
	 * character.
	 * 
	 * @return a string
	 */
	private String charToString(char c) {
		
		switch (c) {
			case 'A':
				return "aUpper";
			case 'B':
				return "bUpper";
			case 'C':
				return "cUpper";
			case 'D':
				return "dUpper";
			case 'E':
				return "eUpper";
			case 'F':
				return "fUpper";
			case 'G':
				return "gUpper";
			case 'H':
				return "hUpper";
			case 'I':
				return "iUpper";
			case 'J':
				return "jUpper";
			case 'K':
				return "kUpper";
			case 'L':
				return "lUpper";
			case 'M':
				return "mUpper";
			case 'N':
				return "nUpper";
			case 'O':
				return "oUpper";
			case 'P':
				return "pUpper";
			case 'Q':
				return "qUpper";
			case 'R':
				return "rUpper";
			case 'S':
				return "sUpper";
			case 'T':
				return "tUpper";
			case 'U':
				return "uUpper";
			case 'V':
				return "vUpper";
			case 'W':
				return "wUpper";
			case 'X':
				return "xUpper";
			case 'Y':
				return "yUpper";
			case 'Z':
				return "zUpper";
			case ' ':
				return "space";
			case '.':
				return "period";
			case '*':
				return "asterick";
			case '\\':
				return "backwardSlash";
			case ':':
				return "colon";
			case '/':
				return "forwardSlash";
			case '<':
				return "lessThan";
			case '>':
				return "greaterThan";
			case '?':
				return "questionMark";
			case '"':
				return "quotationMark";
			case ';':
				return "semicolon";
			case '~':
				return "tilde";
			case '|':
				return "verticalLine";
			default:
				return Character.toString(c);
		}
		
	}
}
