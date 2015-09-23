package org.cyk.system.ricciepsilone.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.business.impl.CompanyReportRepository;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.ricciepsilone.business.impl.RicciEpsiloneBusinessLayer;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.ui.web.primefaces.api.RootWebManager;
import org.cyk.ui.web.primefaces.AbstractContextListener;
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
		uniformResourceLocatorBusiness.setFilteringEnabled(Boolean.TRUE);
		addUrl(rootBusinessLayer.getUserRole().getCode(),"/private/index.jsf");
		
		String roleCode = RicciEpsiloneBusinessLayer.getInstance().getRoleInputterCode();
		addUrl(roleCode,"/private/__role__/__inputter__/");
		addCrudUrl(roleCode, Customer.class, Boolean.TRUE, Crud.CREATE,Crud.UPDATE);
		addUrl(roleCode,"/private/__tools__/crud/consultactor.jsf");
		addReportUrl(roleCode,Sale.class);
		
		roleCode = RicciEpsiloneBusinessLayer.getInstance().getRoleFinaliserCode();
		addUrl(roleCode,"/private/__role__/__finaliser__/");
		addUrl(roleCode,"/private/__role__/__inputter__/listregisteredsalestockinput.jsf");
		addUrl(roleCode,"/private/__role__/__inputter__/consultsalestockinput.jsf");
		addUrl(roleCode,"/private/__role__/__salemanager__/salestockinputconsult.jsf");
		
		addReportUrl(roleCode,Sale.class,Boolean.FALSE);
		
		
		addTableUrl(roleCode,SaleStock.class,"/private/__role__/__salemanager__/salestocklist.jsf",CompanyReportRepository.getInstance().getParameterSaleStockReportType(),
				CompanyReportRepository.getInstance().getParameterSaleStockReportCustomer());
		
		//addReportUrl(roleCode,SaleStock.class,CompanyReportRepository.getInstance().getParameterSaleStockReportType(),
		//		CompanyReportRepository.getInstance().getParameterSaleStockReportInput());
		
		addTableUrl(roleCode,SaleStock.class,"/private/__role__/__salemanager__/salestocklist.jsf",CompanyReportRepository.getInstance().getParameterSaleStockReportType(),
				CompanyReportRepository.getInstance().getParameterSaleStockReportInventory());
		
		roleCode = RicciEpsiloneBusinessLayer.getInstance().getRoleCashierCode();
		addUrl(roleCode,"/private/__role__/__salemanager__/salestockoutputedit.jsf");
		addReportUrl(roleCode,Sale.class,Boolean.FALSE);
		addReportUrl(roleCode,SaleCashRegisterMovement.class,Boolean.FALSE);
		addReportUrl(roleCode,SaleStock.class,CompanyReportRepository.getInstance().getParameterSaleStockReportType(),
				CompanyReportRepository.getInstance().getParameterSaleStockReportCashRegister());
		
		addTableUrl(roleCode,SaleStock.class,"/private/__role__/__salemanager__/salestockoutputlist.jsf",CompanyReportRepository.getInstance().getParameterSaleStockReportType(),
				CompanyReportRepository.getInstance().getParameterSaleStockReportCashRegister());
	}
	
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		//uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
		uiManager.registerApplicationUImanager(RicciEpsiloneWebManager.getInstance());
	}
	
	
	
}
