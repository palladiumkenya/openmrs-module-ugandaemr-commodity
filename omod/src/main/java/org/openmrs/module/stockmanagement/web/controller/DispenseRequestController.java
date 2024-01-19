package org.openmrs.module.stockmanagement.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.DispenseRequest;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/inventory-dispense")
public class DispenseRequestController extends BaseRestController {
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Object get(@RequestBody DispenseRequestMapper request) {
		DispenseRequest dispenseRequest = request.dispenseRequestConverter(request);
		System.out.println("Before Processing dispense");
		Context.getService(StockManagementService.class).dispenseStockItems(Arrays.asList(dispenseRequest));
		System.out.println("Processing dispense");
		return true;
	}
}
