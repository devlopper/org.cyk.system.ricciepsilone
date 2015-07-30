package org.cyk.system.ricciepsilone.business.impl.integration;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.cyk.system.company.business.api.product.AbstractSaleStockBusiness;
import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.product.SaleStockReportTableRow;
import org.cyk.system.company.model.product.AbstractSaleStockSearchCriteria;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.SaleStock;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStockOutputSearchCriteria;
import org.cyk.system.company.model.product.SaleStockSearchCriteria;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.system.root.model.file.report.ReportBasedOnDynamicBuilderParameters;
import org.cyk.system.root.model.party.person.Person;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;

public class BusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Deployment
    public static Archive<?> createDeployment() {
    	return createRootDeployment();
    } 
    
    @Inject private CustomerBusiness customerBusiness;
    private Customer customer1;
    
    @Override
    protected void businesses() {
    	installApplication();
    	registerCustomers();
    	
    	Person person = companyBusinessTestHelper.cashierPerson();
    	
    	dropAndTakeInOne(person);
    	dropAndTakeInMany(person);
    	dropAndTakeInManyWithZeroPayment(person);
    	dropAndTakeInManyWithZeroQuantity(person);
    	dropManyAndTakeInMany(person);
    	
    	searchByCriterias();
    	
    	printReports();
    }
    
    private void registerCustomers(){
    	customer1 = registerCustomer("ABCDEF", "IJKL", "41110ABCI");
    	registerCustomer("ABCDEF", "JKL", "41110ABCJ");
    	registerCustomer("ABCDEF", "IJKL", "41110ABCIA1");
    	registerCustomer("ABCDEF", "IJKL", "41110ABCIA2");
    }
    
    private Customer registerCustomer(String firstName,String lastName,String expectedRegistrationCode){
    	Customer customer = new Customer();
    	customer.setPerson(RootRandomDataProvider.getInstance().person());
    	customer.getPerson().setName(firstName);
    	customer.getPerson().setLastName(lastName);
    	customerBusiness.create(customer); 
    	Assert.assertEquals("Registration code",expectedRegistrationCode, customer.getRegistration().getCode());
    	return customer;
    }
    
    private void dropAndTakeInOne(Person person){
    	SaleStockInput saleStockInput = companyBusinessTestHelper
        		.drop(date(2015, 1, 1),person, customer1,"A", "1000", "100", "3", "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 2),person, saleStockInput, "3", "1100", "0", "0","0");
    }
    
    private void dropAndTakeInMany(Person person){
    	SaleStockInput saleStockInput = companyBusinessTestHelper.drop(date(2015, 1, 3),person, customer1,"B", "1000", "100", "3", "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 4),person, saleStockInput, "2", "800", "1", "300","300");
    	companyBusinessTestHelper.taking(date(2015, 1, 5),person, saleStockInput, "1", "300", "0", "0","0");
    }
    
    private void dropAndTakeInManyWithZeroQuantity(Person person){
    	SaleStockInput saleStockInput = companyBusinessTestHelper.drop(date(2015, 1, 6),person, customer1,"C", "1000", "100", "3", "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 7),person, saleStockInput, "0", "800", "3", "300","300");
    	companyBusinessTestHelper.taking(date(2015, 1, 8),person, saleStockInput, "2", "100", "1", "200","200");
    	companyBusinessTestHelper.taking(date(2015, 1, 9),person, saleStockInput, "1", "200", "0", "0","0");
    }
    
    private void dropAndTakeInManyWithZeroPayment(Person person){
    	SaleStockInput saleStockInput = companyBusinessTestHelper.drop(date(2015, 1, 10),person, customer1,"D", "1000", "100", "3", "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 11),person, saleStockInput, "1", "0", "2", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 1, 12),person, saleStockInput, "1", "1000", "1", "100","100");
    	companyBusinessTestHelper.taking(date(2015, 1, 13),person, saleStockInput, "1", "100", "0", "0","0");
    }
    
    private void dropManyAndTakeInMany(Person person){
    	SaleStockInput saleStockInput1 = companyBusinessTestHelper.drop(date(2015, 2, 1),person, customer1,"E", "1000", "100", "3", "1100", "168", "1100","1100");
    	companyBusinessTestHelper.taking(date(2015, 2, 2),person, saleStockInput1, "1", "400", "2", "700","700");
    	
    	SaleStockInput saleStockInput2 = companyBusinessTestHelper.drop(date(2015, 2, 3),person, customer1,"F", "1500", "100", "5", "1600", "245", "1600","2300");
    	companyBusinessTestHelper.taking(date(2015, 2, 4),person, saleStockInput2, "2", "1000", "3", "600","1300");
    	
    	companyBusinessTestHelper.taking(date(2015, 2, 5),person, saleStockInput1, "1", "300", "1", "400","1000");
    }
    
    private void searchByCriterias(){
    	//SaleStock
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,null, date(2015,1,1), date(2015,12,31), 18l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,null, null, null, 18l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,null, date(2015,1,1), date(2015,1,31), 13l);
    	
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", date(2015,1,1), date(2015,12,31), 2l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", date(2015,1,2), date(2015,1,3), 1l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"A", null, null, 2l);
    	
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", date(2015,1,1), date(2015,12,31), 3l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", date(2015,1,3), date(2015,1,3), 1l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStock.class,SaleStockSearchCriteria.class,saleStockBusiness,"B", null, null, 3l);
    	
    	//Sale Stock Input
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,null, date(2015,1,1), date(2015,12,31), 6l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,null, null, null, 6l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,null, date(2015,1,1), date(2015,1,31), 4l);
    	
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", date(2015,1,1), date(2015,12,31), 1l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", date(2015,1,2), date(2015,1,3), 0l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"A", null, null, 1l);
    	
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", date(2015,1,1), date(2015,12,31), 1l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", date(2015,1,3), date(2015,1,3), 1l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockInput.class,SaleStockInputSearchCriteria.class,saleStockInputBusiness,"B", null, null, 1l);
    	
    	//Sale Stock Output
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,null, date(2015,1,1), date(2015,12,31), 12l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,null, null, null, 12l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,null, date(2015,1,1), date(2015,1,31), 9l);
    	
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", date(2015,1,1), date(2015,12,31), 1l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", date(2015,1,2), date(2015,1,3), 1l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"A", null, null, 1l);
    	
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", date(2015,1,1), date(2015,12,31), 2l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", date(2015,1,3), date(2015,1,3), 0l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", date(2016,1,1), date(2016,12,31), 0l);
    	searchByCriteria(SaleStockOutput.class,SaleStockOutputSearchCriteria.class,saleStockOutputBusiness,"B", null, null, 2l);
    }
    
    private <ENTITY extends SaleStock,SEARCH_CRITERIA extends AbstractSaleStockSearchCriteria> void searchByCriteria(Class<ENTITY> saleStockClass,
    		Class<SEARCH_CRITERIA> searchCriteriaClass,AbstractSaleStockBusiness<ENTITY, SEARCH_CRITERIA> business,String externalIdentifier,Date fromDate,Date toDate,Long expectedCount){
    	SEARCH_CRITERIA searchCriteria = null;
		try {
			searchCriteria = searchCriteriaClass.newInstance();
			if(searchCriteria instanceof SaleStockOutputSearchCriteria)
				((SaleStockOutputSearchCriteria)searchCriteria).setMinimumPaid(BigDecimal.ZERO);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	searchCriteria.getFromDateSearchCriteria().setValue(fromDate); 
    	searchCriteria.getToDateSearchCriteria().setValue(toDate);
    	searchCriteria.getExternalIdentifierStringSearchCriteria().setValue(externalIdentifier);
    	Assert.assertEquals("Count by search criteria", expectedCount, business.countByCriteria(searchCriteria));
    	if(StringUtils.isNotBlank(externalIdentifier))
	    	for(ENTITY saleStock : business.findByCriteria(searchCriteria)){
	    		SaleStockInput saleStockInput = saleStock instanceof SaleStockInput?((SaleStockInput)saleStock):
	    			((SaleStockOutput)saleStock).getSaleStockInput();
	    		Assert.assertEquals("External identifier",externalIdentifier, saleStockInput.getExternalIdentifier());
	    	}
    		
    }
    
    private void printReports(){
    	ReportBasedOnDynamicBuilderParameters<SaleStockReportTableRow> saleStockOutputCashRegisterParameters = new ReportBasedOnDynamicBuilderParameters<>();
        saleStockOutputCashRegisterParameters.setIdentifiableClass(SaleStock.class);
        saleStockOutputCashRegisterParameters.setModelClass(SaleStockReportTableRow.class);
        saleStockOutputCashRegisterParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterSaleStockReportType(), CompanyBusinessLayer.getInstance().getParameterSaleStockReportCashRegister());
        rootTestHelper.addReportParameterFromDate(saleStockOutputCashRegisterParameters, DateUtils.addDays(new Date(), -1000));
        rootTestHelper.addReportParameterToDate(saleStockOutputCashRegisterParameters, DateUtils.addDays(new Date(), 1000));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(saleStockOutputCashRegisterParameters);
        
        ReportBasedOnDynamicBuilderParameters<SaleStockReportTableRow> saleStockOutputInventoryParameters = new ReportBasedOnDynamicBuilderParameters<>();
        saleStockOutputInventoryParameters.setIdentifiableClass(SaleStock.class);
        saleStockOutputInventoryParameters.setModelClass(SaleStockReportTableRow.class);
        saleStockOutputInventoryParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterSaleStockReportType(), CompanyBusinessLayer.getInstance().getParameterSaleStockReportInventory());
        rootTestHelper.addReportParameterFromDate(saleStockOutputInventoryParameters, DateUtils.addDays(new Date(), -1000));
        rootTestHelper.addReportParameterToDate(saleStockOutputInventoryParameters, DateUtils.addDays(new Date(), 1000));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(saleStockOutputInventoryParameters);
        
        ReportBasedOnDynamicBuilderParameters<SaleStockReportTableRow> saleStockOutputCustomerParameters = new ReportBasedOnDynamicBuilderParameters<>();
        saleStockOutputCustomerParameters.setIdentifiableClass(SaleStock.class);
        saleStockOutputCustomerParameters.setModelClass(SaleStockReportTableRow.class);
        saleStockOutputCustomerParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterSaleStockReportType(), 
        		CompanyBusinessLayer.getInstance().getParameterSaleStockReportCustomer());
        rootTestHelper.addReportParameterFromDate(saleStockOutputCustomerParameters, DateUtils.addDays(new Date(), -1000));
        rootTestHelper.addReportParameterToDate(saleStockOutputCustomerParameters, DateUtils.addDays(new Date(), 1000));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(saleStockOutputCustomerParameters);
    }
    

}
