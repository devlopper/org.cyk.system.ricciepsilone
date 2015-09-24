package org.cyk.system.ricciepsilone.ui.web.primefaces;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.ricciepsilone.business.impl.RicciEpsiloneBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.ui.api.AbstractUserSession;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.api.security.shiro.WebEnvironmentAdapter;
import org.cyk.ui.web.api.security.shiro.WebEnvironmentAdapter.SecuredUrlProvider;
import org.cyk.ui.web.primefaces.AbstractPrimefacesManager;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

import lombok.Getter;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=RicciEpsiloneWebManager.DEPLOYMENT_ORDER) @Getter
public class RicciEpsiloneWebManager extends AbstractPrimefacesManager implements Serializable {

	public static final int DEPLOYMENT_ORDER = CompanyWebManager.DEPLOYMENT_ORDER+1;
	private static final long serialVersionUID = 7231721191071228908L;

	@Getter private final String outcomeConsultSaleStockInput = "consultsalestockinput";
	
	private static RicciEpsiloneWebManager INSTANCE;
	
	@Inject private CashierBusiness cashierBusiness;
	@Inject private CompanyWebManager companyWebManager;
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		identifier = "ricciepsilone";
		super.initialisation();  
		
		WebEnvironmentAdapter.SECURED_URL_PROVIDERS.add(new SecuredUrlProvider() {
			@Override
			public void provide() {
				roleFolder("__inputter__", RicciEpsiloneBusinessLayer.getInstance().getRoleInputterCode());
				roleFolder("__finaliser__", RicciEpsiloneBusinessLayer.getInstance().getRoleFinaliserCode());
			}
		});
		
		//languageBusiness.registerResourceBundle("org.cyk.system."+identifier+".ui.web.primefaces.api.resources.i18n",getClass().getClassLoader());
		//logoFileInfos.setExtension("gif");
	}
			
	public static RicciEpsiloneWebManager getInstance() {
		return INSTANCE;
	}
	
	@Override
	public SystemMenu systemMenu(AbstractUserSession userSession) {
		SystemMenu systemMenu = new SystemMenu();
		if(userSession.hasRole(RootBusinessLayer.getInstance().getManagerRole().getCode())){
			addBusinessMenu(systemMenu,companyWebManager.humanResourcesManagerCommandables(userSession,systemMenu.getMobileBusinesses())); 
		}
		addBusinessMenu(systemMenu,companyWebManager.customerManagerCommandables(userSession,systemMenu.getMobileBusinesses())); 
		
		Cashier cashier = null;
		if(userSession.getUser() instanceof Person){
			cashier = cashierBusiness.findByPerson((Person) userSession.getUser());
		}
		UICommandable sale = uiProvider.createCommandable("parcel", null);
		saleCommandables(userSession, sale.getChildren(),systemMenu.getMobileBusinesses(), cashier);	
		systemMenu.getBusinesses().add(sale);
		
		//addBusinessMenu(systemMenu,companyWebManager.stockCommandables(userSession));
		
		return systemMenu;
	}
	
	public void saleCommandables(AbstractUserSession userSession,Collection<UICommandable> commandables,Collection<UICommandable> mobileCommandables,Cashier cashier){
		if(userSession.hasRole(RootBusinessLayer.getInstance().getManagerRole().getCode())){
			managerSaleCommandables(userSession, commandables, mobileCommandables);
		}else if(userSession.hasRole(RicciEpsiloneBusinessLayer.getInstance().getRoleCashierCode())){
			cashierSaleCommandables(userSession, commandables, mobileCommandables);
		}else if(userSession.hasRole(RicciEpsiloneBusinessLayer.getInstance().getRoleFinaliserCode())){
			finaliserSaleCommandables(userSession, commandables, mobileCommandables);
		}else if(userSession.hasRole(RicciEpsiloneBusinessLayer.getInstance().getRoleInputterCode())){
			inputterSaleCommandables(userSession, commandables, mobileCommandables);
		}
	}
	
	public void inputterSaleCommandables(AbstractUserSession userSession,Collection<UICommandable> commandables,Collection<UICommandable> mobileCommandables){
		commandables.add(uiProvider.createCommandable("ui.listregisteredsalestockinput.command.label", null, "listregisteredsalestockinput"));
	}
	
	public void finaliserSaleCommandables(AbstractUserSession userSession,Collection<UICommandable> commandables,Collection<UICommandable> mobileCommandables){
		commandables.add(uiProvider.createCommandable("ui.listregisteredsalestockinput.command.label", null, "listregisteredsalestockinput"));
		commandables.add(uiProvider.createCommandable("ui.listcloturedsalestockinput.command.label", null, "listcloturedsalestockinput"));
		saleReportCommandables(userSession, commandables, mobileCommandables);
	}
	
	public void cashierSaleCommandables(AbstractUserSession userSession,Collection<UICommandable> commandables,Collection<UICommandable> mobileCommandables){
		commandables.add(uiProvider.createCommandable("ui.listcloturedsalestockinput.command.label", null, "listcloturedsalestockinput"));
		saleReportCommandables(userSession, commandables, mobileCommandables);
	}
	
	public void managerSaleCommandables(AbstractUserSession userSession,Collection<UICommandable> commandables,Collection<UICommandable> mobileCommandables){
		commandables.add(uiProvider.createCommandable("ui.listregisteredsalestockinput.command.label", null, "listregisteredsalestockinput"));
		commandables.add(uiProvider.createCommandable("ui.listcloturedsalestockinput.command.label", null, "listcloturedsalestockinput"));
		saleReportCommandables(userSession, commandables, mobileCommandables);
	}
	
	public void saleReportCommandables(AbstractUserSession userSession,Collection<UICommandable> commandables,Collection<UICommandable> mobileCommandables){
		CompanyReportRepository companyReportRepository = CompanyReportRepository.getInstance();
		CompanyWebManager companyWebManager = CompanyWebManager.getInstance();
		UICommandable c;
		
		if(roleManager.isManager(null) || userSession.hasRole(RicciEpsiloneBusinessLayer.getInstance().getRoleCashierCode())){
			commandables.add(c = uiProvider.createCommandable("company.report.salestockoutput.cashregister.title", null, companyWebManager.getOutcomeSaleStockOutputList()));
			c.addParameter(companyReportRepository.getParameterSaleStockReportType(), companyReportRepository.getParameterSaleStockReportCashRegister());
		}
		
		if(roleManager.isManager(null) || userSession.hasRole(RicciEpsiloneBusinessLayer.getInstance().getRoleFinaliserCode())){
			commandables.add(c = uiProvider.createCommandable("company.report.salestock.inventory.title", null, companyWebManager.getOutcomeSaleStockList()));
			c.addParameter(companyReportRepository.getParameterSaleStockReportType(), companyReportRepository.getParameterSaleStockReportInventory());
			
			commandables.add(c = uiProvider.createCommandable("company.report.salestock.customer.title", null, companyWebManager.getOutcomeSaleStockList()));
			c.addParameter(companyReportRepository.getParameterSaleStockReportType(), companyReportRepository.getParameterSaleStockReportCustomer());
		}
		
		
	}
	
}
