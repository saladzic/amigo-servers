package com.amigoservers.backend.payment;

import com.amigoservers.backend.util.main.Config;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PayPal extends Transaction {
    private Transaction transaction;
    /**
     *Set up the PayPal Java SDK environment with PayPal access credentials.
     *This sample uses SandboxEnvironment. In production, use LiveEnvironment.
     */
    private PayPalEnvironment environment;

    /**
     *PayPal HTTP client instance with environment that has access
     *credentials context. Use to invoke PayPal APIs.
     */
    private PayPalHttpClient client;

    public PayPal() {
        build();
    }

    public PayPal(Transaction transaction) {
        this.transaction = transaction;
        build();
    }

    private void build() {
        Config config = new Config();
        if (config.getMode().equals("dev")) {
            environment = new PayPalEnvironment.Sandbox(
                    config.getPaypalSandboxClientId(),
                    config.getPaypalSandboxSecret());
        } else {
            environment = new PayPalEnvironment.Live(
                    config.getPaypalLiveClientId(),
                    config.getPaypalLiveSecret());
        }
        client = new PayPalHttpClient(environment);
    }

    /**
     *Method to get client object
     *
     *@return PayPalHttpClient client
     */
    public PayPalHttpClient client() {
        return this.client;
    }

    public String getPaymentUrl() {
        Config config = new Config();
        Order order = null;
        // Construct a request object and set desired parameters
        // Here, OrdersCreateRequest() creates a POST request to /v2/checkout/orders
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        orderRequest.applicationContext(new ApplicationContext()
                .returnUrl(config.getPaypalSuccess())
                .cancelUrl(config.getPaypalCancel())
                .brandName(config.getBrand()));
        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits
                .add(new PurchaseUnitRequest().amountWithBreakdown(new AmountWithBreakdown().currencyCode("EUR").value(transaction.getTotal().toString())));
        orderRequest.purchaseUnits(purchaseUnits);
        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            // Call API with your client and get a response for your call
            HttpResponse<Order> response = client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            System.out.println("Order ID: " + order.id());
            transaction.setPaypalOrderId(order.id());
            transaction.insert();
            order.links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
            for (LinkDescription link : order.links())
                if (link.rel().equals("approve"))
                    return link.href();
        } catch (IOException ioe) {
            try {
                if (ioe instanceof HttpException) {
                    // Something went wrong server-side
                    HttpException he = (HttpException) ioe;
                    System.out.println(he.getMessage());
                    he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
                } else {
                    // Something went wrong client-side
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public boolean capture(String orderId) {
        System.out.println("orderid: " + orderId);
        Order order = null;
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);

        try {
            // Call API with your client and get a response for your call
            HttpResponse<Order> response = client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            System.out.println("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());
            order.purchaseUnits().get(0).payments().captures().get(0).links()
                    .forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
            return true;
        } catch (IOException ioe) {
            try {
                if (ioe instanceof HttpException) {
                    // Something went wrong server-side
                    HttpException he = (HttpException) ioe;
                    System.out.println(he.getMessage());
                    he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
                } else {
                    // Something went wrong client-side

                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }
}
