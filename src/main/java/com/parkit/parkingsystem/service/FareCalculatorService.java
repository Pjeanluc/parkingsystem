package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double price = 0L;

        double durationTotale = InputReaderUtil.calculateDiffInHours(ticket.getInTime().getTime(),
                ticket.getOutTime().getTime());

        double durationFare = InputReaderUtil.lessThirthyMinutes(durationTotale);

        switch (ticket.getParkingSpot().getParkingType()) {
        case CAR: {
            price = durationFare * Fare.CAR_RATE_PER_HOUR;
            break;
        }
        case BIKE: {
            price = durationFare * Fare.BIKE_RATE_PER_HOUR;
            break;
        }
        default:
            throw new IllegalArgumentException("Unkown Parking Type");
        }

        if (ticket.getNbOfEntry() > 1) {
            price = price * Fare.DISCOUNT_RECURRING_ENTRY;
        }

        ticket.setPrice(InputReaderUtil.arrondir(price, 2));
    }
}