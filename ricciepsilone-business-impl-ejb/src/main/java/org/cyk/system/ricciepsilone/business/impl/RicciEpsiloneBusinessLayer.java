package org.cyk.system.ricciepsilone.business.impl;

import java.io.Serializable;
import java.util.Map;

import javax.inject.Singleton;

import org.cyk.system.root.business.api.TypedBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=RicciEpsiloneBusinessLayer.DEPLOYMENT_ORDER)
public class RicciEpsiloneBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = RootBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

	private static RicciEpsiloneBusinessLayer INSTANCE;
		
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
		registerResourceBundle("org.cyk.system.ricciepsilone.model.resources.entity", getClass().getClassLoader());
		registerResourceBundle("org.cyk.system.ricciepsilone.model.resources.message", getClass().getClassLoader());
		registerResourceBundle("org.cyk.system.ricciepsilone.business.impl.resources.message", getClass().getClassLoader());
		
	}
	
	
	@Override
	protected void persistData() {
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void registerTypedBusinessBean(Map<Class<AbstractIdentifiable>, TypedBusiness<AbstractIdentifiable>> beansMap) {
        
    }
	
	/**/
	
	@Override
	protected void setConstants(){
    	
    }
	
	
	public static RicciEpsiloneBusinessLayer getInstance() {
		return INSTANCE;
	}
	
	
	/**/
	
	protected void fakeTransactions(){
		
	}    
    
}
