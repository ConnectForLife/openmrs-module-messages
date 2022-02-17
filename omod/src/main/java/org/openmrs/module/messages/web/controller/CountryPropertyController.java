package org.openmrs.module.messages.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.openmrs.module.messages.web.model.CountryPropertyValueList;
import org.openmrs.module.messages.web.model.PatientTemplatesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.HttpURLConnection;

@Api(
    value = "Country Property Actions",
    tags = {"REST API for Country Property Actions"})
@Controller
@RequestMapping(value = "/messages/countryProperty")
public class CountryPropertyController extends BaseRestController {
  @Autowired private CountryPropertyService countryPropertyService;

  @ApiOperation(
      value = "None",
      notes = "Set the values for Country Property. Creates properties if needed.",
      response = PatientTemplatesWrapper.class)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = HttpURLConnection.HTTP_OK,
            message = "Country Property Values have been set."),
        @ApiResponse(
            code = HttpURLConnection.HTTP_INTERNAL_ERROR,
            message = "Failed to set Country Property value.")
      })
  @RequestMapping(value = "/setValues", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public void setValue(@RequestBody CountryPropertyValueList propertyValues) {
    countryPropertyService.setCountryPropertyValues(propertyValues.getValues());
  }
}
