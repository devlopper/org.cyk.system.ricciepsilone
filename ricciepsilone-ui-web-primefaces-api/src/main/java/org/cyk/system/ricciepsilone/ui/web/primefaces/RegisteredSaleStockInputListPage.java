package org.cyk.system.ricciepsilone.ui.web.primefaces;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.page.product.AbstractSaleStockInputListPage;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.ui.api.command.CommandAdapter;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.api.model.table.ColumnAdapter;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.api.WebManager;
import org.cyk.ui.web.api.WebNavigationManager;
import org.cyk.ui.web.primefaces.Commandable;

@Named @ViewScoped @Getter @Setter
public class RegisteredSaleStockInputListPage extends AbstractSaleStockInputListPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("ui.listregisteredsalestockinput.page.title");
		table.getOpenRowCommandable().getCommand().getCommandListeners().add(new CommandAdapter(){
			private static final long serialVersionUID = 1120566504648934547L;
			@SuppressWarnings("unchecked")
			@Override
			public void serve(UICommand command, Object parameter) {
				AbstractIdentifiable identifiable = ((Row<SaleStockReportTableRow>)parameter).getData().getSaleStock();
				WebNavigationManager.getInstance().redirectTo(RicciEpsiloneWebManager.getInstance().getOutcomeConsultSaleStockInput(), 
						new Object[]{WebManager.getInstance().getRequestParameterIdentifiable(),identifiable.getIdentifier()});
			}
		});
		
		table.getColumnListeners().add(new ColumnAdapter(){
			@Override
			public Boolean isColumn(Field field) {
				return field.getName().equals("date") || field.getName().equals("customer") || field.getName().equals("identifier") 
						||field.getName().equals("saleStockInputExternalIdentifier") || field.getName().equals("numberOfGoods") || field.getName().equals("amount");
			}
		});
		
		((Commandable)table.getAddRowCommandable()).getButton().setValue(text("ui.registersalestockinput.command.label"));
		table.getAddRowCommandable().setViewId("registersalestockinput");
		table.getPrintCommandable().setRendered(Boolean.FALSE);
		((Commandable)table.getPrintCommandable()).getButton().setRendered(Boolean.FALSE);
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		table.getAddRowCommandable().setViewId("registersalestockinput");
	}
	
	@Override
	protected SaleStockInputSearchCriteria searchCriteria() {
		SaleStockInputSearchCriteria searchCriteria = super.searchCriteria();
		searchCriteria.getSaleSearchCriteria().setDone(Boolean.FALSE);
		return searchCriteria;
	}
	
}