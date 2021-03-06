package org.cyk.system.ricciepsilone.ui.web.primefaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.ui.web.primefaces.page.product.AbstractSaleStockInputConsultPage;
import org.cyk.system.ricciepsilone.business.impl.RicciEpsiloneBusinessLayer;
import org.cyk.ui.api.UIProvider;
import org.cyk.ui.api.command.UICommandable;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ConsultSaleStockInputPage extends AbstractSaleStockInputConsultPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected Collection<UICommandable> contextualCommandables() {
		Collection<UICommandable> commandables = new ArrayList<UICommandable>(super.contextualCommandables());
		if(Boolean.FALSE.equals(identifiable.getSale().getDone())
				&& Boolean.TRUE.equals(roleManager.hasRole(RicciEpsiloneBusinessLayer.getInstance().getRoleFinaliserCode()))
				&& !Boolean.TRUE.equals(roleManager.hasRole(RicciEpsiloneBusinessLayer.getInstance().getRoleCashierCode()))){
			UICommandable commandable = UIProvider.getInstance().createCommandable("ricciepsilone.command.cloture.salestockinput", null,"cloturesalestockinput");
			commandable.addParameter(uiManager.getIdentifiableParameter(), identifiable.getIdentifier());
			commandable.addParameter(webManager.getRequestParameterPreviousUrl(), url);
			commandables.add(commandable);
		}
		return commandables;
	}
	
}