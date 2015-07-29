package org.cyk.system.ricciepsilone.business.impl.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class ApplicationSetupBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Deployment
    public static Archive<?> createDeployment() {
        return createRootDeployment();
    } 
    
    @Override
    protected void businesses() {
    	installApplication(Boolean.TRUE);
    	System.exit(0);
    }

    
    

}
