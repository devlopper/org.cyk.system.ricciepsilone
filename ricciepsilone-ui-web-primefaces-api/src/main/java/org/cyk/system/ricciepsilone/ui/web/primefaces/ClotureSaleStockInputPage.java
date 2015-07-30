package org.cyk.system.ricciepsilone.ui.web.primefaces;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.ui.web.primefaces.page.product.AbstractSaleStockInputCrudOnePage;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.UIManager;
import org.cyk.ui.api.data.collector.control.Input;
import org.cyk.ui.api.data.collector.form.ControlSet;
import org.cyk.ui.web.primefaces.data.collector.control.ControlSetAdapter;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ClotureSaleStockInputPage extends AbstractSaleStockInputCrudOnePage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private SaleBusiness saleBusiness;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		
		form.setControlSetListener(new ControlSetAdapter<Object>(){
			@Override
			public void input(ControlSet<Object, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> controlSet,
					Input<?, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> input) {
				//input.setReadOnly(!input.getField().getName().equals("commission") && !input.getField().getName().equals("valueAddedTaxable"));
				
				if(input.getField().getName().equals("customer")) input.setReadOnly(Boolean.TRUE);
				else if(input.getField().getName().equals("externalIdentifier")) input.setReadOnly(Boolean.TRUE);
				else if(input.getField().getName().equals("quantity")) input.setReadOnly(Boolean.TRUE);
				//else if(input.getField().getName().equals("price")) input.setDisabled(Boolean.TRUE);
				else if(input.getField().getName().equals("price")) input.setReadOnly(Boolean.TRUE);
				else if(input.getField().getName().equals("comments")) input.setReadOnly(Boolean.TRUE);
				
			}
			
			@Override
			public String fiedLabel(ControlSet<Object, DynaFormModel, DynaFormRow, DynaFormLabel, DynaFormControl, SelectItem> controlSet,Field field) {
				String label = UIManager.getInstance().getLanguageBusiness().findFieldLabelText(field);
				if(field.getName().equals("valueAddedTaxable"))
					label +=  " - "+numberBusiness.format(accountingPeriod.getValueAddedTaxRate().multiply(new BigDecimal("100")))+"%"; 
				else if(field.getName().equals("commission"))
					label +=  "%"; 
				return label;
			}
			
		});
		
		identifiable.getSale().setCompleted(Boolean.TRUE);
		((SaleStockInputFormModel)form.getData()).setTotalCost(numberBusiness.format(identifiable.getSale().getCost()));
		((SaleStockInputFormModel)form.getData()).setValueAddedTax(numberBusiness.format(identifiable.getSale().getValueAddedTax()));
		((SaleStockInputFormModel)form.getData()).setValueAddedTaxable(Boolean.TRUE);
	}
	
	@Override
	protected void update() {
		saleBusiness.complete(identifiable.getSale(),cashRegisterController.getSaleCashRegisterMovement());
	}
	
	@Override
	protected Crud crudFromRequestParameter() {
		return Crud.UPDATE;
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