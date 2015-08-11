package org.cyk.system.ricciepsilone.business.impl;

import java.io.Serializable;

import javax.inject.Singleton;

import org.cyk.system.root.business.impl.file.report.AbstractReportRepository;
import org.cyk.utility.common.annotation.Deployment;
import org.cyk.utility.common.annotation.Deployment.InitialisationType;

@Singleton @Deployment(initialisationType=InitialisationType.EAGER,order=RicciEpsiloneBusinessLayer.DEPLOYMENT_ORDER+1)
public class RicciEpsiloneReportRepository extends AbstractReportRepository implements Serializable {

	private static final long serialVersionUID = 6917567891985885124L;

	@Override
	public void build() {
		
	}
	
	/**/
	
	private static RicciEpsiloneReportRepository INSTANCE;
	public static RicciEpsiloneReportRepository getInstance() {
		return INSTANCE;
	}
}
