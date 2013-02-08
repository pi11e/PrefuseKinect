/*******************************************************************************
 * Copyright (c) 2012 jnect.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 *******************************************************************************/
package org.jnect.core.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
//import org.eclipse.core.runtime.Platform;
import org.jnect.bodymodel.Body;
import org.jnect.core.IBodyProvider;
import org.jnect.core.KinectManager;
import org.jnect.core.SpeechListener;
import org.jnect.core.impl.connection.jni.ProxyConnectionManager;
//import org.jnect.core.impl.connection.socket.SocketConnectionManager;
import org.w3c.dom.Document;
import org.eclipse.equinox.nonosgi.registry.RegistryFactoryHelper;
import system.ArgumentException;

public class KinectManagerImpl implements KinectManager, KinectDataHandler {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	private static final KinectManager INSTANCE = new KinectManagerImpl();
	
	public static KinectManager getInstance() {				
		return INSTANCE;
	}

	private ConnectionManager connectionManager;

	private SkeletonParser skeletonParser;
	private Body body;
	private Map<SpeechListener, Set<String>> speechWords = new HashMap<SpeechListener, Set<String>>();
	private Map<String, Set<SpeechListener>> filteredSpeechListeners = new HashMap<String, Set<SpeechListener>>();
	private Set<SpeechListener> unfilteredSpeechListeners = new HashSet<SpeechListener>();

	private IBodyProvider bodyProvider;

	public KinectManagerImpl() {
		
		//this.connectionManager = new SocketConnectionManager();
		
		this.connectionManager = new ProxyConnectionManager();
		
		this.connectionManager.setDataHandler(this);
		setUpBodyProvider();
		
		body = bodyProvider.getBody();
		// body=BodymodelFactory.eINSTANCE.createBody();
		// fillBody();
		
		this.skeletonParser = new SkeletonParser(body);
	}

	private void setUpBodyProvider() {
		//original code
		//IConfigurationElement[] bodyConfElements = Platform.getExtensionRegistry().getConfigurationElementsFor(
		//	"org.jnect.core.bodyprovider");
		
	
		IConfigurationElement[] bodyConfElements = RegistryFactoryHelper.getRegistry().getConfigurationElementsFor(
				"org.jnect.core.bodyprovider");
		
		/*
		// trying to eliminate RegistryFactoryHelper
		if (Platform.getExtensionRegistry() != null) {
			IConfigurationElement[] bodyConfElements = Platform.getExtensionRegistry().getConfigurationElementsFor(
				"org.jnect.core.bodyprovider");
		} else {
			try {
				bodyProvider = (IBodyProvider) bodyConfElements[0].createExecutableExtension("class");
				System.out.println("bodyProvider written here");
				System.out.println(" ");
			} catch (CoreException e) {
				e.printStackTrace();
				bodyProvider = new BodyProviderDefaultImpl();
			}
		*/		
				

		if (bodyConfElements.length > 1) {
			throw new IllegalStateException("Only one extension for the body provider allowed currently!");
		} else if (bodyConfElements.length == 0) {
			bodyProvider = new BodyProviderDefaultImpl();
			
		} else {
			try {
				bodyProvider = (IBodyProvider) bodyConfElements[0].createExecutableExtension("class");
				System.out.println("bodyProvider written here");
				System.out.println(" ");
			} catch (CoreException e) {
				e.printStackTrace();
				bodyProvider = new BodyProviderDefaultImpl();
			}
			
			System.out.println("In KinectManagerImpl setUpBodyProvider...");
			System.out.println("bodyProvider = ");
			System.out.println(bodyProvider);
			System.out.println(" ");
		}
	}

	@Override
	public void startKinect() {
		try {
			this.connectionManager.openConnection();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception a)
		{
			logger.log(Level.SEVERE, "an error occurred when setting up Kinect sensor. Check if it is physically connected.", a);
		}
		
	}

	@Override
	public void stopKinect() {
		try {
			this.connectionManager.closeConnection();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	@Override
	public Body getSkeletonModel() {
		return body;
	}

	@Override
	public void startSkeletonTracking() {
		this.skeletonParser.reset();
		this.connectionManager.startSkeletonTracking();
	}

	@Override
	public void stopSkeletonTracking() {
		this.connectionManager.stopSkeletonTracking();
	}

	@Override
	public void addSpeechListener(SpeechListener listener) {
		this.speechWords.put(listener, listener.getWords());
		if (listener.isFiltered()) {
			for (String word : listener.getWords()) {
				if (!this.filteredSpeechListeners.containsKey(word)) {
					this.filteredSpeechListeners.put(word, new HashSet<SpeechListener>());
				}
				this.filteredSpeechListeners.get(word).add(listener);
			}
		} else {
			this.unfilteredSpeechListeners.add(listener);
		}
	}

	@Override
	public void removeSpeechListener(SpeechListener listener) {
		this.speechWords.remove(listener);
		for (String word : listener.getWords()) {
			this.filteredSpeechListeners.get(word).remove(listener);
		}
		this.unfilteredSpeechListeners.remove(listener);
	}

	@Override
	public void startSpeechRecognition() {
		Set<String> words = new HashSet<String>();
		for (Set<String> listenerWords : this.speechWords.values()) {
			words.addAll(listenerWords);
		}

		String[] keywords = words.toArray(new String[words.size()]);
		this.connectionManager.startSpeechRecognition(keywords);
	}

	@Override
	public void stopSpeechRecognition() {
		this.connectionManager.stopSpeechRecognition();
	}

	@Override
	public void handleSkeletonData(Document doc) {
		this.skeletonParser.parseSkeleton(doc);
		// emfStorage.updateBody();
	}

	@Override
	public void handleSpeechData(String word) {
		Set<SpeechListener> listeners = new HashSet<SpeechListener>();

		// Add all listeners that want to get notified on every recognized word
		listeners.addAll(this.unfilteredSpeechListeners);

		// Add all listeners that only want to be notified on specific words
		if (this.filteredSpeechListeners.containsKey(word)) {
			listeners.addAll(this.filteredSpeechListeners.get(word));
		}

		// Notify speech listeners
		for (SpeechListener listener : listeners) {
			listener.notifySpeech(word);
		}
	}

	@Override
	public boolean isStarted() {
		return this.connectionManager.isConnected();
	}

	@Override
	public boolean isSkeletonTrackingStarted() {
		return this.connectionManager.isSkeletonTrackingStarted();
	}

	@Override
	public boolean isSpeechRecognitionStarted() {
		return this.connectionManager.isSpeechRecognitionStarted();
	}

}
