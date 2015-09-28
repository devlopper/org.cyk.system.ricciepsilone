package org.cyk.system.ricciepsilone.ui.web.primefaces;

import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.ui.web.primefaces.page.product.AbstractSaleStockInputConsultPage;
import org.cyk.system.ricciepsilone.business.impl.RicciEpsiloneBusinessLayer;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;
import org.cyk.ui.api.AbstractUserSession;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.AbstractMenu;
import org.cyk.ui.api.command.menu.MenuManager;
import org.cyk.ui.api.command.menu.UIMenu;
import org.cyk.ui.api.command.menu.MenuManager.ModuleGroup;
import org.cyk.ui.web.primefaces.AbstractContextListener;
import org.cyk.ui.web.primefaces.page.AbstractPrimefacesPage;
import org.cyk.ui.web.primefaces.page.PrimefacesPageAdapter;
import org.cyk.ui.web.primefaces.page.application.ApplicationInstallationFormModel;
import org.cyk.ui.web.primefaces.page.application.ApplicationInstallationPage;
import org.cyk.ui.web.primefaces.page.application.ApplicationInstallationPage.ApplicationInstallListener;

@WebListener
public class ContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;

	@Override
	protected void initialisation() {
		super.initialisation();
		ApplicationInstallationPage.LISTENERS.add(new ApplicationInstallListener() {
			
			@Override
			public void install(ApplicationInstallationFormModel formModel) {
				
			}
		});
		SaleStockReportTableRow.FIELD_IDENTIFIER_VISIBLE = Boolean.FALSE;
	}
	
	@Override
	protected void addUrls(ServletContextEvent event) {
		super.addUrls(event);
		CompanyReportRepository companyReportRepository = CompanyReportRepository.getInstance();
		String saleStockReportType = companyReportRepository.getParameterSaleStockReportType();
		uniformResourceLocatorBusiness.setFilteringEnabled(Boolean.TRUE);
		addUrl(rootBusinessLayer.getUserRole().getCode(),"/private/index.jsf");
		
		String roleCode = RicciEpsiloneBusinessLayer.getInstance().getRoleInputterCode();
		addUrl(roleCode,"/private/__role__/__inputter__/");
		addCrudUrl(roleCode, Customer.class, Boolean.TRUE, Crud.CREATE,Crud.UPDATE);
		addUrl(roleCode,"/private/__tools__/crud/consultactor.jsf");
		addReportUrl(roleCode,Sale.class,Boolean.FALSE);
		
		roleCode = RicciEpsiloneBusinessLayer.getInstance().getRoleFinaliserCode();
		addUrl(roleCode,"/private/__tools__/event/agenda.jsf");
		addUrl(roleCode,"/private/__tools__/event/notifications.jsf");
		addUrl(roleCode,"/private/__role__/__finaliser__/");
		addUrl(roleCode,"/private/__role__/__inputter__/listregisteredsalestockinput.jsf");
		addUrl(roleCode,"/private/__role__/__inputter__/consultsalestockinput.jsf");
		addUrl(roleCode,"/private/__role__/__salemanager__/salestockinputconsult.jsf");
		addReportUrl(roleCode,Sale.class,Boolean.FALSE);
		addTableUrl(roleCode,SaleStock.class,"/private/__role__/__salemanager__/salestocklist.jsf",saleStockReportType,companyReportRepository.getParameterSaleStockReportInput());
		addTableUrl(roleCode,SaleStock.class,"/private/__role__/__salemanager__/salestocklist.jsf",saleStockReportType,companyReportRepository.getParameterSaleStockReportCustomer());	
		addTableUrl(roleCode,SaleStock.class,"/private/__role__/__salemanager__/salestocklist.jsf",saleStockReportType,companyReportRepository.getParameterSaleStockReportInventory());
		
		roleCode = RicciEpsiloneBusinessLayer.getInstance().getRoleCashierCode();
		addUrl(roleCode,"/private/__role__/__salemanager__/salestockoutputedit.jsf");
		addTableUrl(roleCode,SaleStock.class,"/private/__role__/__salemanager__/salestockoutputlist.jsf",saleStockReportType,companyReportRepository.getParameterSaleStockReportCashRegister());
		addReportUrl(roleCode,Sale.class,Boolean.FALSE);
		addReportUrl(roleCode,SaleCashRegisterMovement.class,Boolean.FALSE);
		
		primefacesManager.getPageListeners().add(new PrimefacesPageAdapter(){
			private static final long serialVersionUID = 8872343935947587912L;
			
			@Override
			public void processContextualMenu(AbstractPrimefacesPage bean,UIMenu contextualMenu) {
				super.processContextualMenu(bean, contextualMenu);
				if(bean instanceof AbstractSaleStockInputConsultPage){
					if(!roleManager.hasRole(RicciEpsiloneBusinessLayer.getInstance().getRoleCashierCode())){
						contextualMenu.remove(AbstractSaleStockInputConsultPage.COMMANDABLE_WITHDRAW_IDENTIFIER);
					}
				}
			}
			
			
		});
	}
	
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		//uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
		uiManager.registerApplicationUImanager(RicciEpsiloneWebManager.getInstance());
	}
	
	@Override
	public void moduleGroupCreated(AbstractUserSession userSession,ModuleGroup group, UICommandable commandable) {
		super.moduleGroupCreated(userSession, group, commandable);
		if(ModuleGroup.USER_ACCOUNT.equals(group)){
			if( !Boolean.TRUE.equals(roleManager.hasRole(RicciEpsiloneBusinessLayer.getInstance().getRoleFinaliserCode())) ){
				AbstractMenu.__remove__(MenuManager.COMMANDABLE_NOTIFICATIONS_IDENTIFIER, (List<UICommandable>) commandable.getChildren());
			}
			AbstractMenu.__remove__(MenuManager.COMMANDABLE_USER_ACCOUNT_IDENTIFIER, (List<UICommandable>) commandable.getChildren());
		}
	}
	
	@Override
	public void sessionContextualMenuCreated(AbstractUserSession userSession,UIMenu menu) {
		super.sessionContextualMenuCreated(userSession, menu);
		System.out.println(userSession.getUserAccount().getRoles());
		if( !Boolean.TRUE.equals(roleManager.hasRole(RicciEpsiloneBusinessLayer.getInstance().getRoleFinaliserCode())) ){
			menu.remove(MenuManager.COMMANDABLE_EVENT_CALENDAR_IDENTIFIER);
			menu.remove(MenuManager.COMMANDABLE_NOTIFICATIONS_IDENTIFIER);
		}
	}
	
}
