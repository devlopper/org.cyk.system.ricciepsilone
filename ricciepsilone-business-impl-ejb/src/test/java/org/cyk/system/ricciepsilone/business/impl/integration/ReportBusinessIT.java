package org.cyk.system.ricciepsilone.business.impl.integration;

import javax.inject.Inject;

import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.business.api.product.TangibleProductInventoryBusiness;
import org.cyk.system.company.business.impl.CompanyRandomDataProvider;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.utility.common.generator.RandomDataProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class ReportBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Deployment
    public static Archive<?> createDeployment() {
    	return createRootDeployment();
    } 
    
    @Inject private RandomDataProvider randomDataProvider;
    @Inject private RootRandomDataProvider rootRandomDataProvider;
    @Inject private CompanyRandomDataProvider companyRandomDataProvider;
    @Inject private TangibleProductInventoryBusiness tangibleProductInventoryBusiness;
    @Inject private SaleStockOutputBusiness saleStockOutputBusiness;
    
    @Override
    protected void businesses() {
    	installApplication();
    	rootRandomDataProvider = RootRandomDataProvider.getInstance();
    	rootRandomDataProvider.createActor(Customer.class, 10);
    	companyRandomDataProvider.createSale(50);
    	companyRandomDataProvider.createSaleStockInput(10);
    	companyRandomDataProvider.createTangibleProductStockMovement(0);
        companyRandomDataProvider.createTangibleProductInventory(0);
        
        /*
        ReportBasedOnDynamicBuilderParameters<TangibleProductStockMovementLineReport> tpsmlrParameters = new ReportBasedOnDynamicBuilderParameters<>();
        tpsmlrParameters.setIdentifiableClass(TangibleProductStockMovement.class);
        tpsmlrParameters.setModelClass(TangibleProductStockMovementLineReport.class);
        rootTestHelper.addReportParameterFromDate(tpsmlrParameters, DateUtils.addDays(new Date(), -randomDataProvider.randomInt(1, 10)));
        rootTestHelper.addReportParameterToDate(tpsmlrParameters, DateUtils.addDays(new Date(), randomDataProvider.randomInt(1, 10)));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(tpsmlrParameters);
        
        ReportBasedOnDynamicBuilderParameters<SaleReportTableDetail> sParameters = new ReportBasedOnDynamicBuilderParameters<>();
        sParameters.setIdentifiableClass(Sale.class);
        sParameters.setModelClass(SaleReportTableDetail.class);
        rootTestHelper.addReportParameterFromDate(sParameters, DateUtils.addDays(new Date(), -randomDataProvider.randomInt(1, 10)));
        rootTestHelper.addReportParameterToDate(sParameters, DateUtils.addDays(new Date(), randomDataProvider.randomInt(1, 10)));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(sParameters);
        
        for(TangibleProductInventory tangibleProductInventory : tangibleProductInventoryBusiness.findAll()){
	        ReportBasedOnDynamicBuilderParameters<TangibleProductInventoryReportTableDetails> tpiParameters = new ReportBasedOnDynamicBuilderParameters<>();
	        tpiParameters.setIdentifiableClass(TangibleProductInventory.class);
	        tpiParameters.setModelClass(TangibleProductInventoryReportTableDetails.class);
	        BusinessEntityInfos tangibleProductInventoryEntityInfos = applicationBusiness.findBusinessEntityInfos(TangibleProductInventory.class);
	        tpiParameters.addParameter(tangibleProductInventoryEntityInfos.getIdentifier(), tangibleProductInventory.getIdentifier());
	        rootTestHelper.reportBasedOnDynamicBuilderParameters(tpiParameters);
        }
        
        
        ReportBasedOnDynamicBuilderParameters<CustomerBalanceReportTableDetails> customerBalanceParameters = new ReportBasedOnDynamicBuilderParameters<>();
        customerBalanceParameters.setIdentifiableClass(Customer.class);
        customerBalanceParameters.setModelClass(CustomerBalanceReportTableDetails.class);
        customerBalanceParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerReportType(), 
        		CompanyBusinessLayer.getInstance().getParameterCustomerReportBalance());
        customerBalanceParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerBalanceType(), 
        		CompanyBusinessLayer.getInstance().getParameterCustomerBalanceAll());
        rootTestHelper.reportBasedOnDynamicBuilderParameters(customerBalanceParameters);
        
        ReportBasedOnDynamicBuilderParameters<CustomerBalanceReportTableDetails> credenceParameters = new ReportBasedOnDynamicBuilderParameters<>();
        credenceParameters.setIdentifiableClass(Customer.class);
        credenceParameters.setModelClass(CustomerBalanceReportTableDetails.class);
        credenceParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerReportType(), 
        		CompanyBusinessLayer.getInstance().getParameterCustomerReportBalance());
        credenceParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerBalanceType(), 
        		CompanyBusinessLayer.getInstance().getParameterCustomerBalanceCredence());
        rootTestHelper.reportBasedOnDynamicBuilderParameters(credenceParameters);
        
        ReportBasedOnDynamicBuilderParameters<CustomerBalanceReportTableDetails> customerSaleStockParameters = new ReportBasedOnDynamicBuilderParameters<>();
        customerSaleStockParameters.setIdentifiableClass(Customer.class);
        customerSaleStockParameters.setModelClass(CustomerBalanceReportTableDetails.class);
        customerSaleStockParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterCustomerReportType(), 
        		CompanyBusinessLayer.getInstance().getParameterCustomerReportSaleStock());
        rootTestHelper.reportBasedOnDynamicBuilderParameters(customerSaleStockParameters);
        
        ReportBasedOnDynamicBuilderParameters<SaleStockInputReportTableDetail> saleStockInputParameters = new ReportBasedOnDynamicBuilderParameters<>();
        saleStockInputParameters.setIdentifiableClass(SaleStockInput.class);
        saleStockInputParameters.setModelClass(SaleStockInputReportTableDetail.class);
        saleStockInputParameters.addParameter(CompanyBusinessLayer.getInstance().getParameterMinimumRemainingNumberOfGoods(), "0");
        rootTestHelper.addReportParameterFromDate(saleStockInputParameters, DateUtils.addDays(new Date(), -1000));
        rootTestHelper.addReportParameterToDate(saleStockInputParameters, DateUtils.addDays(new Date(), 1000));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(saleStockInputParameters);
        */
        /*
        ReportBasedOnDynamicBuilderParameters<SaleStockOutputReportTableDetail> saleStockOutputParameters = new ReportBasedOnDynamicBuilderParameters<>();
        saleStockOutputParameters.setIdentifiableClass(SaleStockOutput.class);
        saleStockOutputParameters.setModelClass(SaleStockOutputReportTableDetail.class);
        rootTestHelper.addReportParameterFromDate(saleStockOutputParameters, DateUtils.addDays(new Date(), -1000));
        rootTestHelper.addReportParameterToDate(saleStockOutputParameters, DateUtils.addDays(new Date(), 1000));
        rootTestHelper.reportBasedOnDynamicBuilderParameters(saleStockOutputParameters);
        */
        
    }
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
}
