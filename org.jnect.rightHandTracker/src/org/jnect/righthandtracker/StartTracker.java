package org.jnect.righthandtracker;

import org.jnect.righthandtracker.RightHandTracker;
import org.jnect.righthandtracker.LeftHandTracker;


public class StartTracker {
	
        public static void main(String[] args) {
        	
        	RightHandTracker.INSTANCE.useRightHandPosition();
        	LeftHandTracker.INSTANCE.printLeftHandPosition();
        	
        	}
     }