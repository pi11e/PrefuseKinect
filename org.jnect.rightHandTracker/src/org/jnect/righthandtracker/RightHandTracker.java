package org.jnect.righthandtracker;

import java.awt.Point;
import java.awt.Robot;
import java.util.Vector;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.jnect.bodymodel.RightHand;
import org.jnect.core.KinectManager;


public class RightHandTracker {

        public static RightHandTracker INSTANCE = new RightHandTracker();
		private Robot robot;

        private RightHandTracker() {
        };

        public void printRightHandPosition() {
        	
        	KinectManager.INSTANCE.startKinect();
            
            KinectManager.INSTANCE.startSkeletonTracking();
            
            final RightHand rightHand = KinectManager.INSTANCE.getSkeletonModel()
                                .getRightHand();
    
            
                rightHand.eAdapters().add(new Adapter() {
                        @Override
                        public void notifyChanged(Notification notification) 
                        {
                        	// original code:
                        	
                                //System.out.println("RightHand x: " + rightHand.getX() + "| y: "
                                 //               + rightHand.getY() + "| z: " + rightHand.getZ());
                                                
                        	
                                float rightHandX = rightHand.getX();
                                float rightHandY = rightHand.getY();
                                
                                float rightShoulderX = KinectManager.INSTANCE.getSkeletonModel().getRightShoulder().getX();
                                float rightShoulderY = KinectManager.INSTANCE.getSkeletonModel().getRightShoulder().getY();
                                
                                float handvectorX = rightHandX - rightShoulderX;
                                float handvectorY = rightShoulderY - rightHandY;
                                
                                // distance between those points is equal to:
                                // sqrt((y2-y1)^2 + (x2-x1)^2)
                                double distance = Math.sqrt(Math.pow(rightShoulderY-rightHandY,2) + Math.pow(rightShoulderX-rightHandX, 2));
                                                                
                                System.out.println("Handvector with respect to shoulder is = (" + handvectorX + "," + handvectorY + ")");
                                //System.out.println("Distance rightHand (" + rightHandX + "," + rightHandY + ") to rightShoulder (" + rightShoulderX + "," + rightShoulderY + ") = " + distance);
                                
                        	// new notification handler: make robot set mouse position to right hand
                        	// position
                        	if(INSTANCE.robot != null)
                        	{
                        		INSTANCE.moveMouse(handvectorX, handvectorY);
                        		//INSTANCE.moveMouse(rightHand.getX(), rightHand.getY());
                        	}
                        		
                        }

                        @Override
                        public Notifier getTarget() {
                                return rightHand;
                        }

                        @Override
                        public void setTarget(Notifier newTarget) {
                                // TODO Auto-generated method stub
                        }

                        @Override
                        public boolean isAdapterForType(Object type) {
                                // TODO Auto-generated method stub
                                return false;
                                
                        }
                });
                
        }

        protected void moveMouse(float x, float y) 
        {
			// in: float coordinates of kinect coordinate space
			// x, y values typically from 0.001 to 0.5
        	
        	// screen coordinates e.g. 1440x900
        	// conversion function for x values:
        	// f(x) = screenWidth * x + 0.5*screenWidth
        	// f(y) = screenHeight * y + 0.5*screenHeight
        	int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        	int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        	
        	int convertedX = (int)(screenWidth * x + 0.5f*screenWidth);
        	int convertedY = (int)(screenHeight * y + 0.5f*screenHeight);
        	
        	
        	
        	// mapping coordinates to each other
        	if(INSTANCE.robot != null)
        	{
        		INSTANCE.robot.mouseMove(convertedX, convertedY);
        	}
		}

		public void setRobot(java.awt.Robot robot)
        {
        	INSTANCE.robot = robot;
        }
        
        public void stop() {
                KinectManager.INSTANCE.stopKinect();
        }
}
