package org.openmrs.module.stockmanagement.web.controller;

import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.dto.DispenseRequest;

import java.math.BigDecimal;

public class DispenseRequestMapper {
	
	private String locationUuid;
	
	private String patientUuid;
	
	private String orderUuid;
	
	private String encounterUuid;
	
	private String stockItemUuid;
	
	private String stockBatchUuid;
	
	private BigDecimal quantity;
	
	private String stockItemPackagingUOMUuid;
	
	public String getLocationUuid() {
		return locationUuid;
	}
	
	public void setLocationUuid(String locationUuid) {
		this.locationUuid = locationUuid;
	}
	
	public String getPatientUuid() {
		return patientUuid;
	}
	
	public String getOrderUuid() {
		return orderUuid;
	}
	
	public String getEncounterUuid() {
		return encounterUuid;
	}
	
	public String getStockItemUuid() {
		return stockItemUuid;
	}
	
	public void setStockItemUuid(String stockItemUuid) {
		this.stockItemUuid = stockItemUuid;
	}
	
	public String getStockBatchUuid() {
		return stockBatchUuid;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public String getStockItemPackagingUOMUuid() {
		return stockItemPackagingUOMUuid;
	}
	
	public DispenseRequest dispenseRequestConverter(DispenseRequestMapper requestMapper) {
		DispenseRequest request = new DispenseRequest();
		request.setLocationUuid(requestMapper.getLocationUuid());
		request.setPatientId(Context.getPatientService().getPatientByUuid(requestMapper.getPatientUuid()).getPatientId());
		Order order = Context.getOrderService().getOrderByUuid(requestMapper.getOrderUuid());
		request.setOrderId(order.getOrderId());
		if (order.getEncounter() != null) {
			request.setEncounterId(order.getEncounter().getEncounterId());
		}
		request.setStockItemUuid(requestMapper.stockItemUuid);
		request.setStockBatchUuid(requestMapper.getStockBatchUuid());
		request.setQuantity(requestMapper.getQuantity());
		request.setStockItemPackagingUOMUuid(requestMapper.getStockItemPackagingUOMUuid());
		return request;
	}
}
