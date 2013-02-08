package org.jnect.righthandtracker;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.jnect.bodymodel.LeftHand;
import org.jnect.core.KinectManager;
import org.jnect.righthandtracker.LeftHandTracker;

public class LeftHandTracker {

    public static LeftHandTracker INSTANCE = new LeftHandTracker();

    private LeftHandTracker() {
    };

    public void printLeftHandPosition() {
        
        final LeftHand leftHand = KinectManager.INSTANCE.getSkeletonModel()
                            .getLeftHand();
        
            leftHand.eAdapters().add(new Adapter() {
                    @Override
                    public void notifyChanged(Notification notification) {
                            System.out.println("LeftHand x: " + leftHand.getX() + "| y: "
                                            + leftHand.getY() + "| z: " + leftHand.getZ());
                    }

                    @Override
                    public Notifier getTarget() {
                            return leftHand;
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
}
