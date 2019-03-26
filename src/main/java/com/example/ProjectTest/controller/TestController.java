package com.example.ProjectTest.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProjectTest.entity.ChargeRequest;
import com.example.ProjectTest.service.StripeService;
import com.example.ProjectTest.service.TestService;
import com.example.ProjectTest.util.ConfigurationPropertiesFile;
import com.example.ProjectTest.util.UserCredentials;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;


@RestController
@CrossOrigin
public class TestController {

	@Autowired
	TestService testService;

	@Value("${STRIPE_PUBLIC_KEY}")
	private String stripePublicKey;
	
	@Autowired
	ConfigurationPropertiesFile configurationPropertiesFile;

	@Autowired
	private StripeService paymentsService;

	@GetMapping("/charge")
	public String charge() throws StripeException {
		System.out.println("111111111111");
		
		Charge charge = paymentsService.charge();		
		System.out.println("Charge Response===="+charge);
		return "Success";
	}

	@ExceptionHandler(StripeException.class)
	public String handleError(Model model, StripeException ex) {
		model.addAttribute("error", ex.getMessage());
		return "result";
	}

	@RequestMapping("/checkout")
	public String checkout(Model model) {
		model.addAttribute("amount", 50 * 100); // in cents
		model.addAttribute("stripePublicKey", stripePublicKey);
		model.addAttribute("currency", ChargeRequest.Currency.EUR);
		return "checkout";
	}

	@RequestMapping("/")
	public String hello() {
		//String str=configurationPropertiesFile.getText();
		//System.out.println("Properties Text=="+str);
		return "hello world123";
	}
	
	
	@RequestMapping("/url")
	public String testPostURL() {
		
		ObjectMapper objectMapper = new ObjectMapper();
		UserCredentials credentials = new UserCredentials();
		credentials.setUserName("livef-full");
		credentials.setPassword("Full@1234");

		String payLoad = "";
		try {
			payLoad = objectMapper.writeValueAsString(credentials);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String response = testService.httpPostRequest("", payLoad);
		
		System.out.println("Response==="+response.toString());
		return response.toString();
	}
	

	@RequestMapping("/write")
	public void WriteReport() {
		testService.writeCsvFile("/opt/test.csv");
	}
	
	@RequestMapping("/writehomework")
	public void WriteReportHomeWork() {
		testService.writeCsvFileHomeWork("/opt/test.csv");
	}

	@RequestMapping("/readcsv")
	public void ReadReport() {
		testService.readCsvFile("/opt/test.csv");
	}
	
	@RequestMapping("/createxls")
	public void WriteXLSFile() {
		testService.writeXLSFile();
	}
	
	@RequestMapping("/readxls")
	public void ReadReportXLS() {
		testService.readXLSFile("/opt/TestData.xls");
	}
	
	@RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> send() throws JSONException {

		JSONObject body = new JSONObject();
		//add device token 
		body.put("to", "c9moGd9bMIk:APA91bFUG5o4eIEfxka-hBVjzaUMtZ5Lh4pFWQGwukyrKtnvBEc7QcSzwa1zsnXWesnqADZLTYaX6aXErVyOyeFUBjuerkQtrNMS1gkY-afKgwzUnwkaV7r_IwXonpr1MCD94_FNowHV");
		body.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", "Test Notification");
		notification.put("body", "Happy Message!");
		
		JSONObject data = new JSONObject();
		data.put("Key-1", "JSA Data 1");
		data.put("Key-2", "JSA Data 2");

		body.put("notification", notification);
		body.put("data", data);

/**
		{
		   "notification": {
		      "title": "JSA Notification",
		      "body": "Happy Message!"
		   },
		   "data": {
		      "Key-1": "JSA Data 1",
		      "Key-2": "JSA Data 2"
		   },
		   "to": "/topics/JavaSampleApproach",
		   "priority": "high"
		}
*/

		HttpEntity<String> request = new HttpEntity<>(body.toString());

		CompletableFuture<String> pushNotification = testService.send(request);
		CompletableFuture.allOf(pushNotification).join();

		try {
			String firebaseResponse = pushNotification.get();
			
			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
	}

}
