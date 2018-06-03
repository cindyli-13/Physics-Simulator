package textRender;

import java.util.ArrayList;

import org.joml.Vector3f;

import objects.Entity;
import objects.Loader;
import objects.Model;
import widgets.Button;
import widgets.GUIComponent;

public class Text {

	private String str;
	private float x;
	private float y;
	private float height;
	private float width;
	private Loader loader;
	private float z;
	private ArrayList<GUIComponent> GUIlist = new ArrayList<GUIComponent>();
	public Text(String str, float x, float y, float z, float height, float width, Loader loader)
	{
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
	
	private ArrayList<String> changeText()
	{
		int sizeS = this.str.length();
       
		ArrayList<String> New = new ArrayList<String>();
		for( int i=0;i<sizeS; i++)
		{
			New.add(charToString(this.str.charAt(i)));

		}

		return New;	
	}



	private ArrayList<String> changeText(String str)
	{
		int sizeS = str.length();

		ArrayList<String> New = new ArrayList<String>();
		for( int i=0;i<sizeS; i++)
		{
			New.add(charToString(str.charAt(i)));

		}

		return New;	
	}
	public void setPositionOfLine(float x, float y)
	{
		this.x = x;
		this.y=y;
	}

	public float getPositionOfLineX()
	{
		return x;
	}

	public float getPositionOfLineY()
	{
		return y;
	}

	public void setSize(int size)
	{
		this.height = this.height*size;
		this.width = this.width*size;
	}
	public float getHeight()
	{
		return height;

	}

	public float getWidth()
	{
		return width;
	}

	public void getList(){
		ArrayList<String> list = new ArrayList<String>();
		list = changeText();
		int size = list.size();


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
			
			
			 int textureID = loader.loadTexture("./text/"+list.get(i)+".png");
			
			Model nButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);

			Button letter = new Button(nButtonModel, Pos, rotation, scale, width, height);

			GUIlist.add(letter);
		}


	}

	public void addStr(int index)
	{
		float height = this.height;
		float width = this.width;
		float x = this.x +width/2 +0.5f*width*index;
		float y = this.y+height/2;
		float z=this.z;

		float[] vertices = Entity.getVertices(width, height, z);
		float[] texCoords = Entity.getTexCoords();
		int[] indices = Entity.getIndices();

		Vector3f rotation = new Vector3f(0,0,0);
		float scale = 1f;

		Vector3f Pos = new Vector3f(x, y, z);

		int textureID = loader.loadTexture("./text/1.png");
		Model nButtonModel = loader.loadToVAO(vertices, texCoords, indices, textureID);

		Button letter = new Button(nButtonModel, Pos, rotation, scale, width, height);

		GUIlist.add(letter);
	}





	public void changeStr(String str)
	{

		int length = this.str.length();
		str.replaceAll(" ","space");
		ArrayList<String> intList = changeText(str);
		if(length==str.length())
		{
			for(int i = 0; i<length;i++)
			{
				GUIlist.get(i).getModel().setTextureID("./text/"+intList.get(i)+".png", loader);
			}

		}
		else if(length<str.length())
		{
			for(int i = 0; i<length;i++)
			{
				GUIlist.get(i).getModel().setTextureID("./text/"+intList.get(i)+".png", loader);
			}
			for(int n= length;n<str.length();n++)
			{
				addStr(n);
				GUIlist.get(n).getModel().setTextureID("./text/"+intList.get(n)+".png", loader);

			}
		}
		else if(length>str.length())
		{
			for(int i = 0; i<str.length();i++)
			{
				GUIlist.get(i).getModel().setTextureID("./text/"+intList.get(i)+".png", loader);
			}
			for(int n=length-1;n>=str.length();n--)
			{

				GUIlist.remove(n);

			}
		}
		this.str = str;
	}

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
