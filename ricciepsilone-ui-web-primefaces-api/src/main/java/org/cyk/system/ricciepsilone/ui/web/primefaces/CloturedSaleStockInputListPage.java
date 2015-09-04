package org.cyk.system.ricciepsilone.ui.web.primefaces;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.company.ui.web.primefaces.page.product.AbstractSaleStockInputListPage;
import org.cyk.ui.web.primefaces.Commandable;

@Named @ViewScoped @Getter @Setter
public class CloturedSaleStockInputListPage extends AbstractSaleStockInputListPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected void initialisation() {
		super.initialisation();
		contentTitle = text("ui.listcloturedsalestockinput.page.title");
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		((Commandable)table.getAddRowCommandable()).getButton().setRendered(Boolean.FALSE);
	}
	
	@Override
	protected void __afterFindByCriteria__(SaleStockInputSearchCriteria criteria,Collection<SaleStockInput> results) {
		super.__afterFindByCriteria__(criteria, results);
	}
}