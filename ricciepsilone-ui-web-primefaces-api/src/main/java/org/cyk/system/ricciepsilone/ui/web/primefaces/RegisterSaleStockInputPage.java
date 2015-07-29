package org.cyk.system.ricciepsilone.ui.web.primefaces;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.ui.web.primefaces.page.product.AbstractSaleStockInputCrudOnePage;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RegisterSaleStockInputPage extends AbstractSaleStockInputCrudOnePage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("ui.registersalestockinput.page.title");
		form.setControlSetListener(new ControlSetAdapter<Object>(){
			@Override
			public Boolean build(Field field) {
				return !field.getName().equals("commission") && !field.getName().equals("valueAddedTaxable")
						&& !field.getName().equals("totalCost") && !field.getName().equals("valueAddedTax");
			}
		});
		identifiable.getSale().getSaleProducts().iterator().next().setCommission(BigDecimal.ZERO);
		identifiable.getSale().setValueAddedTax(BigDecimal.ZERO);
		identifiable.getSale().setCompleted(Boolean.FALSE);
		
		//((SaleStockInputFormModel)form.getData()).getv
	}
	
	@Override
	protected void ajax() {
		
	}
	
	@Override
	protected void create() {
		super.create();
		previousUrl = WebNavigationManager.getInstance().url(previousUrlOutcome(),previousUrlParameters(),Boolean.FALSE,Boolean.FALSE);
	}
	
	@Override
	protected String previousUrlOutcome() {
		return RicciEpsiloneWebManager.getInstance().getOutcomeConsultSaleStockInput();
	}
	
	@Override
	protected Object[] previousUrlParameters() {
		return new Object[]{uiManager.getIdentifiableParameter(),identifiable.getIdentifier()};
	}
		
}