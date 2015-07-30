package org.cyk.system.ricciepsilone.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.CompanyBusinessLayerAdapter;
import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.root.business.api.RootBusinessLayerAdapter;
import org.cyk.system.root.business.api.party.person.AbstractActorBusiness;
import org.cyk.system.root.business.impl.AbstractBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.party.person.AbstractActor;
import org.cyk.system.root.model.security.Role;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

import lombok.Getter;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=RicciEpsiloneBusinessLayer.DEPLOYMENT_ORDER)
public class RicciEpsiloneBusinessLayer extends AbstractBusinessLayer implements Serializable {

	public static final int DEPLOYMENT_ORDER = CompanyBusinessLayer.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = -462780912429013933L;

	private static RicciEpsiloneBusinessLayer INSTANCE;

	private static final String CUSTOMER_BASE_REGISTRATION_CODE = "41110";
	private static final Integer CUSTOMER_REGISTRATION_CODE_NAMES_LENGHT = 4;
	
	@Getter private final String roleRicciSuperCashierCode = "RICCISUPERCASHIER",roleRicciCashierCode = "RICCICASHIER";
	@Getter private Role roleRicciSuperCashier,roleRicciCashier;
	
	@Inject private RootBusinessLayer rootBusinessLayer;
	@Inject private CompanyBusinessLayer companyBusinessLayer;
	@Inject private CustomerBusiness customerBusiness;
	
	@Override
	protected void initialisation() {
		INSTANCE = this; 
		super.initialisation();
		registerResourceBundle("org.cyk.system.ricciepsilone.model.resources.entity", getClass().getClassLoader());
		registerResourceBundle("org.cyk.system.ricciepsilone.model.resources.message", getClass().getClassLoader());
		registerResourceBundle("org.cyk.system.ricciepsilone.business.impl.resources.message", getClass().getClassLoader());
		
		rootBusinessLayer.getRootBusinessLayerListeners().add(new RootBusinessLayerAdapter(){
			private static final long serialVersionUID = 3873929111679088940L;

			@Override
			public String generateActorRegistrationCode(AbstractActor actor, String previousCode) {
				if(actor instanceof Customer){
					if(previousCode==null){
						String baseCode = StringUtils.substring(actor.getPerson().getName(),0,3)+StringUtils.substring(actor.getPerson().getLastName(),0,1);
						baseCode = CUSTOMER_BASE_REGISTRATION_CODE+StringUtils.upperCase(baseCode);
						return baseCode;
					}else{
						String base = previousCode.substring(0,CUSTOMER_BASE_REGISTRATION_CODE.length()+CUSTOMER_REGISTRATION_CODE_NAMES_LENGHT);
						if(!base.equals(previousCode)){
							base = base + previousCode.charAt(base.length());//to include discrimination letter
						}
						String orderString = StringUtils.substringAfter(previousCode, base);
						Integer order = null;
						if(StringUtils.isNotBlank(orderString))
							order = Integer.parseInt(orderString)+1;
						else{
							base = base + "A";
							order = 1;
						}
						return base+order;
					}
				}else
					return null;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public <ACTOR extends AbstractActor> AbstractActorBusiness<ACTOR> findActorBusiness(ACTOR actor) {
				if(actor instanceof Customer)
					return (AbstractActorBusiness<ACTOR>) customerBusiness;
				return super.findActorBusiness(actor);
			}
		});
		
		companyBusinessLayer.getCompanyBusinessLayerListeners().add(new CompanyBusinessLayerAdapter() {
			
			private static final long serialVersionUID = 5179809445850168706L;

			@Override
			public String getCompanyName() {
				return "RicciEpsilone";
			}
			
			@Override
			public byte[] getCompanyLogoBytes() {
				return getResourceAsBytes("image/logo.gif");
			}
			
			@Override
			public byte[] getCompanyPointOfSaleBytes() {
				return getResourceAsBytes("report/payment/pos1.jrxml");
			}
			
			@Override
			public void handleAccountingPeriodToInstall(AccountingPeriod accountingPeriod) {
				accountingPeriod.setValueAddedTaxIncludedInCost(Boolean.TRUE);
				accountingPeriod.setValueAddedTaxRate(new BigDecimal("0.18"));
				accountingPeriod.getStockConfiguration().setZeroQuantityAllowed(Boolean.TRUE);
			}
		});
	}
	
	@Override
	protected void persistData() {
		security();
	}
	
	private void security(){ 
    	createRole(roleRicciSuperCashierCode, "Ricci Super Cashier");
    	createRole(roleRicciCashierCode, "Ricci Cashier");
    }
	
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
