package org.openmrs.module.stockmanagement.advice;

import org.openmrs.Drug;
import org.openmrs.MedicationDispense;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.DispenseRequest;
import org.openmrs.module.stockmanagement.api.model.StockBatch;
import org.openmrs.module.stockmanagement.api.model.StockItem;
import org.openmrs.module.stockmanagement.api.model.StockItemPackagingUOM;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MedicationDispenseAfterReturningAdvice implements AfterReturningAdvice {
	
	/**
	 * This is called immediately medication dispense is saved
	 * The use case is to immediately reduce the quantities of stock items from a dispense action
	 */
	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		// This advice will be executed after the saveMedicationDispense method is successfully called
		try {
			// Extract the Order object from the arguments
			if (method.getName().equals("saveMedicationDispense") && args.length > 0
			        && args[0] instanceof MedicationDispense) {
				MedicationDispense medicationDispense = (MedicationDispense) args[0];
				Drug drug = medicationDispense.getDrug();
				StockItem stockItem = Context.getService(StockManagementService.class).getStockItemByDrug(drug.getDrugId());

				// TODO: explore a concrete way to get batch and packaging UOM details for the dispensed medication
				List<StockBatch> batchList = new ArrayList<StockBatch>(stockItem.getStockBatches());
				List<StockItemPackagingUOM> stockItemPackagingUOMList = new ArrayList<>(stockItem.getStockItemPackagingUOMs());

				DispenseRequest dispenseRequest = new DispenseRequest();
				dispenseRequest.setOrderId(medicationDispense.getDrugOrder().getOrderId());
				dispenseRequest.setQuantity(BigDecimal.valueOf(medicationDispense.getQuantity()));
				dispenseRequest.setStockBatchUuid(batchList.get(0).getUuid());// defaulting to the first batch. TODO: how do we get active batches?
				dispenseRequest.setStockItemUuid(stockItem.getUuid());
				dispenseRequest.setStockItemPackagingUOMUuid(stockItemPackagingUOMList.get(0).getUuid());
				dispenseRequest.setEncounterId(medicationDispense.getEncounter().getEncounterId());
				dispenseRequest.setLocationUuid("");//TODO: configure a default dispensing location and assign
				dispenseRequest.setPatientId(medicationDispense.getPatient().getPatientId());
				Context.getService(StockManagementService.class).dispenseStockItems(Arrays.asList(dispenseRequest));

			}
		}
		catch (Exception e) {
			System.err.println("Error intercepting medicationDispense after creation: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
