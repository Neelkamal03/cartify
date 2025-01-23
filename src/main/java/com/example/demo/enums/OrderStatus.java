package com.example.demo.enums;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
//A enum is a special type of class used to define set/collection of constants.
//For example if we want a variable that can have 7 values, like a day variable,
//The day can have any of the 7 day(Mon, tues, wed)etc.
//Here,Each constant (e.g., PENDING, PROCESSING) represents a
// distinct state or value of OrderStatus.
//By default, Java assigns each enum constant an ordinal value,
// starting from 0 for the first constant. For example:
//PENDING has ordinal value 0
//PROCESSING has ordinal value 1
//SHIPPED has ordinal value 2
//DELIVERED has ordinal value 3
//CANCELLED has ordinal value 4
//You can use enums as types for variables,
// method parameters, and return values.
// For example, you can create an OrderStatus variable and assign it one of the constants:
//OrderStatus status = OrderStatus.PENDING;

//Why enum is useful?
//Enum gives us type safety you cannot assign any other value other than constants of enum.
//For ex:-A List<String> can store any string,
// which means you can easily add invalid or unexpected values,
// leading to potential runtime issues.
//List<String> orderStatuses = Arrays.asList("PENDING", "PROCESSING");
//orderStatuses.add("INVALID"); // No compile-time error, but this can be problematic at runtime

