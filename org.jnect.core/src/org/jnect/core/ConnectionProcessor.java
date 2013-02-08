/*******************************************************************************
 * Copyright (c) 2012 jnect.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eugen Neufeld - initial API and implementation
 *******************************************************************************/
package org.jnect.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.jnect.core.impl.ConnectionDataHandler;
import org.osgi.framework.Bundle;

import net.sf.jni4net.Bridge;

//no longer needed:
//import org.eclipse.core.runtime.FileLocator;
//import org.eclipse.core.runtime.Path;
//import org.eclipse.core.runtime.Platform;
//import org.osgi.framework.Bundle;


public abstract class ConnectionProcessor implements Runnable {

	private final Logger logger = Logger.getLogger(this.getClass().getName());
	protected ConnectionDataHandler connectionDataHandler;
	
	public void setConnectionDataHandler(ConnectionDataHandler dataHandler) {
		this.connectionDataHandler = dataHandler;
	}
	public void init() {
		URL url;
		
		try {
			
			//original code
			/*
			Bundle bundle = Platform.getBundle("org.jnect.core");
			URL locationUrl = FileLocator.find(bundle, new Path("/"), null);
			URL fileUrl = FileLocator.toFileURL(locationUrl);
			String parentPath = fileUrl.getFile();
			System.out.println("#### parent path = " + parentPath);
			*/
				String temp = "file:../PrefuseKinect/org.jnect.core";
			
			    //url = new URL("file:../org.jnect.core/");
				url = new URL(temp);
			    InputStream inputStream = url.openConnection().getInputStream();
			    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			    String inputLine;
			    		 
			    while ((inputLine = in.readLine()) != null) {
			        System.out.println(inputLine);	        
			    }
				
			    String parentPath = url.getFile();
				
				Bridge.init(new File(parentPath, "lib"));
				Bridge.LoadAndRegisterAssemblyFrom(new File(parentPath, "lib/jni4net.n-0.8.6.0.dll"));
				File path = new File(parentPath, "lib/MicrosoftKinectWrapper.j4n.dll").getCanonicalFile();
				Bridge.LoadAndRegisterAssemblyFrom(path);

			    in.close();
			 
			} catch (IOException e) {
			    e.printStackTrace();
			}

			
			//Bundle bundle = Platform.getBundle("org.jnect.core");
			//URL fileUrl = bundle.getEntry("files/text.txt");
			//File file = null;
			//try {
				//file = new File(FileLocator.resolve(fileUrl).toURI());
			//}  catch (URISyntaxException e1) {
				//e1.printStackTrace();
			//}  catch (IOException e1) {
				//e1.printStackTrace();
			//}
			
			//System.out.println("bundle = ");
			//System.out.println(bundle);
		
	}
	public abstract void stop();
}
