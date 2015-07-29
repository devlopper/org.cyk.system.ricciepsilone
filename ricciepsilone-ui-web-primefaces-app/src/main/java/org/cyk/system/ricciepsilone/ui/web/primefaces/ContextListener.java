package org.cyk.system.ricciepsilone.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

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
	}
	
	@Override
	protected void identifiableConfiguration(ServletContextEvent event) {
		super.identifiableConfiguration(event);
		uiManager.registerApplicationUImanager(RootWebManager.getInstance());
		//uiManager.registerApplicationUImanager(CompanyWebManager.getInstance());
		uiManager.registerApplicationUImanager(RicciEpsiloneWebManager.getInstance());
	}
	
	
	
}
