package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository or;

    public String addOrder(@RequestBody Order order){

      return  or.addOrder(order);

    }
    public String addPartner(@PathVariable String partnerId){
       return or.addPartner(partnerId);
    }
    public String addOrderPartnerPair(@RequestParam String orderId, @RequestParam String partnerId){

       return or.addOrderPartnerPair(orderId,partnerId);

    }
    public Order getOrderById(@PathVariable String orderId){

        Order order= or.getOrderById(orderId);
        //order should be returned with an orderId.
        return order;
    }
    public DeliveryPartner getPartnerById(@PathVariable String partnerId){

        DeliveryPartner deliveryPartner = or.getPartnerById(partnerId);

        return deliveryPartner;
    }
    public int getOrderCountByPartnerId(@PathVariable String partnerId){

        Integer orderCount = or.getOrderCountByPartnerId(partnerId);

        return orderCount;
    }
    public List<String> getOrdersByPartnerId(@PathVariable String partnerId){

        List<String> orders = or.getOrdersByPartnerId(partnerId);

        return orders;
    }
    public List<String> getAllOrders(){
        List<String> orders = or.getAllOrders();

        //Get all orders
        return orders;
    }
    public int getCountOfUnassignedOrders(){

        Integer countOfOrders = or.getCountOfUnassignedOrders();

        return countOfOrders;
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(@PathVariable String time, @PathVariable String partnerId){

        Integer countOfOrders = or.getOrdersLeftAfterGivenTimeByPartnerId(time,partnerId);

        return countOfOrders;
    }
    public String getLastDeliveryTimeByPartnerId(@PathVariable String partnerId){

        String time = or.getLastDeliveryTimeByPartnerId(partnerId);
        return time;
    }
    public String deletePartnerById(@PathVariable String partnerId){


       return or.deletePartnerById(partnerId);

    }
    public String deleteOrderById(@PathVariable String orderId){

       return or.deleteOrderById(orderId);
    }
}
