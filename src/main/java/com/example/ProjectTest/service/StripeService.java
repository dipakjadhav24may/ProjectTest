package com.example.ProjectTest.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.ProjectTest.entity.ChargeRequest.Currency;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

@Component
@Service
public class StripeService {

	@Value("${STRIPE_SECRET_KEY}")
	private String secretKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = secretKey;
	}

	public Charge charge() {
		Charge charge = null;
		Map<String, Object> chargeParams;
		try {
			chargeParams = new HashMap<>();
			chargeParams.put("amount", 50*100);
			chargeParams.put("currency", Currency.INR);
			chargeParams.put("description", "test desc");
			chargeParams.put("source", "tok_bypassPending");
			charge = Charge.create(chargeParams);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return charge;

	}
}
