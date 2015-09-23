package org.cyk.system.ricciepsilone.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Sale;
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
		addCrudUrl(RicciEpsiloneBusinessLayer.getInstance().getRoleInputterCode(), Customer.class, Boolean.TRUE, Crud.CREATE,Crud.UPDATE);
		addUrl(roleCode,"/private/__tools__/crud/consultactor.jsf");
		addUrl(roleCode,"/private/__tools__/export/report.jsf",uiManager.getClassParameter(),uiManager.businessEntityInfos(Sale.class).getIdentifier());
		
		roleCode = RicciEpsiloneBusinessLayer.getInstance().getRoleFinaliserCode();
		addUrl(roleCode,"/private/__role__/__finaliser__/");
	}
	
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		//uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
		uiManager.registerApplicationUImanager(RicciEpsiloneWebManager.getInstance());
	}
	
	
	
}
