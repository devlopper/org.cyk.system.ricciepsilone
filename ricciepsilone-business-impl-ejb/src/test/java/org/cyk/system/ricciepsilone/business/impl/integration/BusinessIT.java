package org.cyk.system.ricciepsilone.business.impl.integration;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;

public class BusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Deployment
    public static Archive<?> createDeployment() {
    	return createRootDeployment();
    } 
    
    @Inject private CustomerBusiness customerBusiness;
    
    @Override
    protected void businesses() {
    	installApplication();
    	registerCustomer("ABCDEF", "IJKL", "41110ABCI");
    	registerCustomer("ABCDEF", "JKL", "41110ABCJ");
    	registerCustomer("ABCDEF", "IJKL", "41110ABCIA1");
    	registerCustomer("ABCDEF", "IJKL", "41110ABCIA2");
    }
    
    private Customer registerCustomer(String firstName,String lastName,String expectedRegistrationCode){
    	Customer customer = new Customer();
    	customer.setPerson(RootRandomDataProvider.getInstance().person());
    	customer.getPerson().setName(firstName);
    	customer.getPerson().setLastName(lastName);
    	customerBusiness.create(customer); 
    	Assert.assertEquals("Registration code",expectedRegistrationCode, customer.getRegistration().getCode());
    	return customer;
    }
    

}
