package com.vehicle.customer.model;
import androidx.annotation.Nullable;

import java.util.List;

public class Trip {
    String id;
    Customer customer;
    Driver driver;
    Vehicle vehicle;
    long createdAtMills;
    String loadingUpazilaThana;
    String loadingFullAddress;
    String loadingLandmark;
    String loadingDate;
    String loadingTime;
    String unloadingUpazilaThana;
    String unloadingFullAddress;
    String unloadingLandmark;
    String description;
    int upDownTrip;
    int containAnimal;
    int fragile;
    int perishable;
    int laborNeeded;
    int rentalPrice;
    String finalPayMethod;
    int advancePay;

    String status;
    List<Bid> bids;
    String loadingArea;
    String loadingAlternative_person_number;
    String unloadingArea;
    String unloading_personName;
    String unloading_mobileNumber;
    String productCategory;
    String stopAddress;
    String stopPointPersonName;
    String stopPointPersonPhoneNumber;
    String returnAddress;
    String returnDate;
    String returnTime;
    int lengthAlert;
    int weightAlert;

    public Trip() {

    }

    public Trip(@Nullable String id, Customer customer, @Nullable  Driver driver, @Nullable  Vehicle vehicle,
                long createdAtMills, String loadingUpazilaThana, String loadingFullAddress, String loadingArea,
                String loadingLandmark, String loadingDate, String loadingTime, String loadingAlternative_person_number,
                String unloadingUpazilaThana, String unloadingFullAddress, String unloadingArea,
                String unloadingLandmark, String unloading_personName, String unloading_mobileNumber, String description,
                String productCategory, int upDownTrip, int containAnimal, int fragile, int perishable, int laborNeeded,
                @Nullable int rentalPrice, @Nullable String finalPayMethod, int lengthAlert, int weightAlert, String status, List<Bid> bids,
                String stopAddress, String stopPointPersonName, int advancePay,
                String stopPointPersonPhoneNumber, String returnAddress, String returnDate, String returnTime) {
        this.id = id;
        this.customer = customer;
        this.driver = driver;
        this.vehicle = vehicle;
        this.createdAtMills = createdAtMills;
        this.loadingUpazilaThana = loadingUpazilaThana;
        this.loadingFullAddress = loadingFullAddress;
        this.loadingLandmark = loadingLandmark;
        this.loadingDate = loadingDate;
        this.loadingTime = loadingTime;
        this.unloadingUpazilaThana = unloadingUpazilaThana;
        this.unloadingFullAddress = unloadingFullAddress;
        this.unloadingLandmark = unloadingLandmark;
        this.description = description;
        this.upDownTrip = upDownTrip;
        this.containAnimal = containAnimal;
        this.fragile = fragile;
        this.perishable = perishable;
        this.laborNeeded = laborNeeded;
        this.rentalPrice = rentalPrice;
        this.finalPayMethod = finalPayMethod;
        this.advancePay = advancePay;
        this.status = status;
        this.bids = bids;
        this.loadingArea = loadingArea;
        this.loadingAlternative_person_number = loadingAlternative_person_number;
        this.unloadingArea = unloadingArea;
        this.unloading_personName = unloading_personName;
        this.unloading_mobileNumber = unloading_mobileNumber;
        this.productCategory = productCategory;
        this.stopAddress = stopAddress;
        this.stopPointPersonName = stopPointPersonName;
        this.stopPointPersonPhoneNumber = stopPointPersonPhoneNumber;
        this.returnAddress = returnAddress;
        this.returnDate = returnDate;
        this.lengthAlert = lengthAlert;
        this.weightAlert = weightAlert;
        this.returnTime = returnTime;
    }

    public String getLoadingArea() {
        return loadingArea;
    }

    public void setLoadingArea(String loadingArea) {
        this.loadingArea = loadingArea;
    }

    public String getLoadingAlternative_person_number() {
        return loadingAlternative_person_number;
    }

    public void setLoadingAlternative_person_number(String loadingAlternative_person_number) {
        this.loadingAlternative_person_number = loadingAlternative_person_number;
    }

    public String getUnloadingArea() {
        return unloadingArea;
    }

    public void setUnloadingArea(String unloadingArea) {
        this.unloadingArea = unloadingArea;
    }

    public String getUnloading_personName() {
        return unloading_personName;
    }

    public void setUnloading_personName(String unloading_personName) {
        this.unloading_personName = unloading_personName;
    }

    public String getUnloading_mobileNumber() {
        return unloading_mobileNumber;
    }

    public void setUnloading_mobileNumber(String unloading_mobileNumber) {
        this.unloading_mobileNumber = unloading_mobileNumber;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getStopAddress() {
        return stopAddress;
    }

    public void setStopAddress(String stopAddress) {
        this.stopAddress = stopAddress;
    }

    public String getStopPointPersonName() {
        return stopPointPersonName;
    }

    public void setStopPointPersonName(String stopPointPersonName) {
        this.stopPointPersonName = stopPointPersonName;
    }

    public String getStopPointPersonPhoneNumber() {
        return stopPointPersonPhoneNumber;
    }

    public void setStopPointPersonPhoneNumber(String stopPointPersonPhoneNumber) {
        this.stopPointPersonPhoneNumber = stopPointPersonPhoneNumber;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public int getLengthAlert() {
        return lengthAlert;
    }

    public void setLengthAlert(int lengthAlert) {
        this.lengthAlert = lengthAlert;
    }

    public int getWeightAlert() {
        return weightAlert;
    }

    public void setWeightAlert(int weightAlert) {
        this.weightAlert = weightAlert;
    }

    public int getAdvancePay() {
        return advancePay;
    }

    public void setAdvancePay(int advancePay) {
        this.advancePay = advancePay;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public long getCreatedAtMills() {
        return createdAtMills;
    }

    public void setCreatedAtMills(long createdAtMills) {
        this.createdAtMills = createdAtMills;
    }

    public String getLoadingUpazilaThana() {
        return loadingUpazilaThana;
    }

    public void setLoadingUpazilaThana(String loadingUpazilaThana) {
        this.loadingUpazilaThana = loadingUpazilaThana;
    }

    public String getLoadingFullAddress() {
        return loadingFullAddress;
    }

    public void setLoadingFullAddress(String loadingFullAddress) {
        this.loadingFullAddress = loadingFullAddress;
    }

    public String getLoadingLandmark() {
        return loadingLandmark;
    }

    public void setLoadingLandmark(String loadingLandmark) {
        this.loadingLandmark = loadingLandmark;
    }

    public String getLoadingDate() {
        return loadingDate;
    }

    public void setLoadingDate(String loadingDate) {
        this.loadingDate = loadingDate;
    }

    public String getLoadingTime() {
        return loadingTime;
    }

    public void setLoadingTime(String loadingTime) {
        this.loadingTime = loadingTime;
    }

    public String getUnloadingUpazilaThana() {
        return unloadingUpazilaThana;
    }

    public void setUnloadingUpazilaThana(String unloadingUpazilaThana) {
        this.unloadingUpazilaThana = unloadingUpazilaThana;
    }

    public String getUnloadingFullAddress() {
        return unloadingFullAddress;
    }

    public void setUnloadingFullAddress(String unloadingFullAddress) {
        this.unloadingFullAddress = unloadingFullAddress;
    }

    public String getUnloadingLandmark() {
        return unloadingLandmark;
    }

    public void setUnloadingLandmark(String unloadingLandmark) {
        this.unloadingLandmark = unloadingLandmark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUpDownTrip() {
        return upDownTrip;
    }

    public void setUpDownTrip(int upDownTrip) {
        this.upDownTrip = upDownTrip;
    }

    public int getContainAnimal() {
        return containAnimal;
    }

    public void setContainAnimal(int containAnimal) {
        this.containAnimal = containAnimal;
    }

    public int getFragile() {
        return fragile;
    }

    public void setFragile(int fragile) {
        this.fragile = fragile;
    }

    public int getPerishable() {
        return perishable;
    }

    public void setPerishable(int perishable) {
        this.perishable = perishable;
    }

    public int getLaborNeeded() {
        return laborNeeded;
    }

    public void setLaborNeeded(int laborNeeded) {
        this.laborNeeded = laborNeeded;
    }

    public int getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(int rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public String getFinalPayMethod() {
        return finalPayMethod;
    }

    public void setFinalPayMethod(String finalPayMethod) {
        this.finalPayMethod = finalPayMethod;
    }

    public static class Bid{
        String id;
        int bidPrice;
        Driver driver;
        Vehicle vehicle;
        String reqPayMethod;
        int advance;
        String description;

        public Bid() {
        }

        public Bid(@Nullable String id, int bidPrice, Driver driver, Vehicle vehicle, String reqPayMethod, int advance, String description) {
            this.id = id;
            this.bidPrice = bidPrice;
            this.driver = driver;
            this.vehicle = vehicle;
            this.reqPayMethod = reqPayMethod;
            this.advance = advance;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getBidPrice() {
            return bidPrice;
        }

        public void setBidPrice(int bidPrice) {
            this.bidPrice = bidPrice;
        }

        public Driver getDriver() {
            return driver;
        }

        public void setDriver(Driver driver) {
            this.driver = driver;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public void setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
        }

        public String getReqPayMethod() {
            return reqPayMethod;
        }

        public void setReqPayMethod(String reqPayMethod) {
            this.reqPayMethod = reqPayMethod;
        }

        public int getAdvance() {
            return advance;
        }

        public void setAdvance(int advance) {
            this.advance = advance;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
