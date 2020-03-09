package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        double durationTotale = (double) (outHour - inHour)/(60*60*1000);
        
        double durationFirstPeriod = 0d;
    	double durationSecondPeriod = 0d;
        
        if (durationTotale > Fare.DURATION_FIRST_PERIOD) {
        	
        	durationFirstPeriod = (double) Fare.DURATION_FIRST_PERIOD ;
        	durationSecondPeriod = (double) (durationTotale- Fare.DURATION_FIRST_PERIOD);
			
		} else {
			
			durationFirstPeriod = durationTotale;
        	
		}
                
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(durationFirstPeriod* Fare.CAR_RATE_FOR_FIRST_PERIOD + durationSecondPeriod * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(durationFirstPeriod* Fare.BIKE_RATE_FOR_FIRST_PERIOD + durationSecondPeriod * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}