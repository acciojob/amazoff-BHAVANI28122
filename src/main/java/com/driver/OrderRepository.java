package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

@Repository
public class OrderRepository {
    HashMap<String, Order> order_db = new HashMap<>();
    HashMap<String, DeliveryPartner> partner_db = new HashMap<>();
    HashMap<String, List<String>> order_pair_db = new HashMap<>();
    HashMap<String, String> orders_to_deliveryPartner = new HashMap<>(); // <orderId, partnerId>

    public String addOrder(Order order) {
        order_db.put(order.getId(), order);
        return "Order Added Successfully";
    }

    public String addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partner_db.put(partnerId, partner);
        return "Partner Added Successfully";
    }

    public String addOrderPartnerPair(String orderId, String partnerId) {
        // This is basically assigning that order to that partnerId
        List<String> list = order_pair_db.getOrDefault(partnerId, new ArrayList<>());
        list.add(orderId);
        order_pair_db.put(partnerId, list);
        orders_to_deliveryPartner.put(orderId, partnerId);
        DeliveryPartner partner = partner_db.get(partnerId);
        partner.setNumberOfOrders(list.size());
        return "Order-Partner pair Added Successfully";

    }

    public Order getOrderById(String orderId) {
        // order should be returned with an orderId.
        for (String s : order_db.keySet()) {
            if (s.equals(orderId)) {
                return order_db.get(s);
            }
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        // deliveryPartner should contain the value given by partnerId

        if (partner_db.containsKey(partnerId)) {
            return partner_db.get(partnerId);
        }
        return null;

    }

    public int getOrderCountByPartnerId(String partnerId) {
        // orderCount should denote the orders given by a partner-id
        int orders = order_pair_db.getOrDefault(partnerId, new ArrayList<>()).size();
        return orders;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        // orders should contain a list of orders by PartnerId

        List<String> orders = order_pair_db.getOrDefault(partnerId, new ArrayList<>());
        return orders;
    }

    public List<String> getAllOrders() {
        // Get all orders
        List<String> orders = new ArrayList<>();
        for (String s : order_db.keySet()) {
            orders.add(s);
        }
        return orders;

    }

    public int getCountOfUnassignedOrders() {
        // Count of orders that have not been assigned to any DeliveryPartner
        int countOfOrders = order_db.size() - orders_to_deliveryPartner.size();
        return countOfOrders;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        // countOfOrders that are left after a particular time of a DeliveryPartner
        int countOfOrders = 0;
        List<String> list = order_pair_db.get(partnerId);
        int deliveryTime = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
        for (String s : list) {
            Order order = order_db.get(s);
            if (order.getDeliveryTime() > deliveryTime) {
                countOfOrders++;
            }
        }
        return countOfOrders;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        // Return the time when that partnerId will deliver his last delivery order.
        String time = "";
        List<String> list = order_pair_db.get(partnerId);
        int deliveryTime = 0;
        for (String s : list) {
            Order order = order_db.get(s);
            deliveryTime = Math.max(deliveryTime, order.getDeliveryTime());
        }
        int hour = deliveryTime / 60;
        String sHour = "";
        if (hour < 10) {
            sHour = "0" + String.valueOf(hour);
        } else {
            sHour = String.valueOf(hour);
        }

        int min = deliveryTime % 60;
        String sMin = "";
        if (min < 10) {
            sMin = "0" + String.valueOf(min);
        } else {
            sMin = String.valueOf(min);
        }

        time = sHour + ":" + sMin;

        return time;

    }

    public String deletePartnerById(String partnerId) {
        // Delete the partnerId
        // And push all his assigned orders to unassigned orders.
        partner_db.remove(partnerId);

        List<String> list = order_pair_db.getOrDefault(partnerId, new ArrayList<>());
        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            orders_to_deliveryPartner.remove(s);
        }
        order_pair_db.remove(partnerId);
        return "partner Deleted Successfully";
    }

    public String deleteOrderById(String orderId) {

        // Delete an order and also
        // remove it from the assigned order of that partnerId
        order_db.remove(orderId);
        String partnerId = orders_to_deliveryPartner.get(orderId);
        orders_to_deliveryPartner.remove(orderId);
        List<String> list = order_pair_db.get(partnerId);

        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            if (s.equals(orderId)) {
                itr.remove();
            }
        }
        order_pair_db.put(partnerId, list);

        return "Order Deleted Successfully";

    }
}
