package com.vehicle.customer.model;

import androidx.annotation.Nullable;

public class Trip {
    String id;
    Customer customer;
    Driver driver;
    String selectedCar;
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
    int paymentMethod;


    public Trip() {
    }

    public Trip(@Nullable String id, Customer customer, @Nullable  Driver driver, @Nullable  String selectedCar,
                long createdAtMills, String loadingUpazilaThana, String loadingFullAddress, String loadingLandmark, String loadingDate,
                String loadingTime, String unloadingUpazilaThana, String unloadingFullAddress, String unloadingLandmark, String description,
                int upDownTrip, int containAnimal, int fragile, int perishable, int laborNeeded,
                @Nullable int rentalPrice, @Nullable int paymentMethod) {
        this.id = id;
        this.customer = customer;
        this.driver = driver;
        this.selectedCar = selectedCar;
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
        this.paymentMethod = paymentMethod;
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

    public String getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(String selectedCar) {
        this.selectedCar = selectedCar;
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

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    class Bid{
        String id;
        int bidPrice;
        Driver driver;
        int selectedCar;
        int paymentMethod;
        String description;

        public Bid() {
        }

        public Bid(@Nullable String id, int bidPrice, Driver driver, int selectedCar, int paymentMethod, String description) {
            this.id = id;
            this.bidPrice = bidPrice;
            this.driver = driver;
            this.selectedCar = selectedCar;
            this.paymentMethod = paymentMethod;
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

        public int getSelectedCar() {
            return selectedCar;
        }

        public void setSelectedCar(int selectedCar) {
            this.selectedCar = selectedCar;
        }

        public int getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(int paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}