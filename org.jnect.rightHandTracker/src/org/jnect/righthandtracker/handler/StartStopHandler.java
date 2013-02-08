package org.jnect.righthandtracker.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.jnect.righthandtracker.RightHandTracker;

public class StartStopHandler extends AbstractHandler {

        private static boolean STARTED = false;

        @Override
        public Object execute(ExecutionEvent event) throws ExecutionException {
                if (!STARTED) {
                        RightHandTracker.INSTANCE.printRightHandPosition();
                        STARTED = true;
                } else {
                        RightHandTracker.INSTANCE.stop();
                        STARTED = false;
                }

                return null;
        }
}
        
