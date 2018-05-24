package screens;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class implements the user guide pop-up of the 
 * physics simulator.
 * 
 * @author Cindy Li
 * @author Larissa Jin
 * @since Thursday, April 19th, 2018
 */
public class UserGuideScreen {

	/**
	 * Shows the user guide pop-up.
	 */
	public static void showUserGuide() {
		
		String pt1 = "<html><body width='";
        String pt2 =
           "'><h1>Instructions</h1>" +
           "<p>Features " +
           "<pre>            1. The top bar : " +
           "<pre>               allows you to drag and drop the selected objects over to your simulation" +
           "<pre>            2. Once an object is in your simulation, you will be allowed to click on it " +
           "<pre>               to change the dimensions" +
           "<pre>            3. To change the position drag the object over to the desired position" +
           "<pre>            4. "+
           "<pre>               The next too bar allows you to : play the simulation, watch it in slow " +
           "<pre>               motion, watch it in fast forward motion, or reset your simulation <br>" +
           "<pre>Game Mode " +
           "<pre>            Objective : using the objects try to get the ball to the target " +
           "<pre>Lesson Mode " +
           "<pre>            Click on the different lessons to learn about the basic laws of motion" +
           "<pre>Customized System Mode " +
           "<pre>            Create your own simulations and save it for later " ;


       JPanel p = new JPanel( new BorderLayout() );

       int width = 700;

       String s = pt1 + width + pt2 ;

       JFrame frame = new JFrame("User Guide");
       
       frame.getContentPane().add(p);
       JOptionPane.showMessageDialog(null, s);

	}
}
