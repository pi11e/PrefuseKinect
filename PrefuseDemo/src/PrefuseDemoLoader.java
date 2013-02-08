
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.jnect.righthandtracker.RightHandTracker;

import Visualization.VisualizationComponent;

public class PrefuseDemoLoader 
{
	public JFrame myFrame;
	
	public PrefuseDemoLoader()
	{
	    myFrame = new JFrame();
	    
	    //make sure the program exits when the frame closes
	    myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    myFrame.setTitle("Example GUI");
	    myFrame.setSize(300,250);
	    
        //This will center the JFrame in the middle of the screen
        myFrame.setLocationRelativeTo(null);
        
        // get prefuse panel:
        //JPanel rgvw =  RadialGraphView.demo(GraphFactory.makeGraph(), "name");
        //JPanel rgvw = RadialGraphView.demo("C:/Users/Philipp/Downloads/prefuse-beta-20071021/prefuse-beta/data/socialnet.xml", "name");
        
        
        
        JPanel radialGraphView = VisualizationComponent.createPanel(); 
        
        myFrame.add(radialGraphView);
        
        this.setupKinect();
        
        // maximize frame
        myFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);  
        
        // and go undecorated
        myFrame.setUndecorated(true);
        
        // show frame
        myFrame.setVisible(true);
        
        //VisualizationComponent.getInstance().updateSize();
        
        // add ESC key listener
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        @SuppressWarnings("serial")
		Action escapeAction = new AbstractAction() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		 //close the frame
        		myFrame.dispose();
        		System.exit(0);
        	}
        };
        
        myFrame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        myFrame.getRootPane().getActionMap().put("ESCAPE", escapeAction);
    
	}
	
	private void setupKinect()
	{

    	
    	try {
			Robot robo = new Robot();
			
			RightHandTracker.INSTANCE.setRobot(robo);
			RightHandTracker.INSTANCE.printRightHandPosition();
	    	//LeftHandTracker.INSTANCE.printLeftHandPosition();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			
			// note: http://docs.oracle.com/javase/1.4.2/docs/api/java/awt/Robot.html#Robot()
			// AWTException thrown if the platform configuration does not allow 
			// low-level input control. This exception is always thrown when 
			// GraphicsEnvironment.isHeadless() returns true
			e.printStackTrace();
		}
	}
	
	

  
	public static void main(String[] args) 
	{
		@SuppressWarnings("unused")
		PrefuseDemoLoader pdl = new PrefuseDemoLoader();
		//System.out.println("frame dimensions = " + pdl.myFrame.getWidth() + " x " + pdl.myFrame.getHeight());
	}
}
