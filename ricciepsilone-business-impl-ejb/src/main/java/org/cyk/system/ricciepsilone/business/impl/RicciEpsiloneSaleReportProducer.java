package org.cyk.system.ricciepsilone.business.impl;

import java.io.Serializable;

import org.cyk.system.company.business.api.SaleReportProducer;
import org.cyk.system.company.business.impl.AbstractSaleReportProducer;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.root.model.file.report.LabelValue;

public class RicciEpsiloneSaleReportProducer extends AbstractSaleReportProducer implements SaleReportProducer,Serializable {

	private static final long serialVersionUID = 7126711234011563710L;

	@Override
	protected void valueAddedTaxesPart(SaleReport saleReport, Sale sale) {
		super.valueAddedTaxesPart(saleReport, sale);
		//getLabelValue(LABEL_VAT_RATE).setValue("");
		LabelValue labelValue = currentLabelValueCollection.getCollection().remove(0);
		labelValue.setValue("");
		currentLabelValueCollection.getCollection().add(labelValue);
	}
	
}
