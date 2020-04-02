package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
        ticket.setNbOfEntry(0);
    }

    @Test
    public void calculateFareCar(){
    	//GIVEN
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  90 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);        
        
        //WHEN 
        fareCalculatorService.calculateFare(ticket);        
      
      	//THEN
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike(){
    	//GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  90 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
      
         //WHEN 
        fareCalculatorService.calculateFare(ticket);
        
        //THEN
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){
    	//GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  90 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        //WHEN
        //THEN
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
    	//GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  90 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        //WHEN
        //THEN
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
    	//GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  75 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        
        //THEN
        assertEquals((0.75), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
    	//GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  75 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        //WVHEN
        fareCalculatorService.calculateFare(ticket);
        
        //THEN
        assertEquals( (1.13) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareBikeWithLessThanHalfOneHourParkingTime(){
    	//GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        
        //THEN
        assertEquals(0, ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanHalfOneHourParkingTime(){
    	//GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        
        //THEN
        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
    	//GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000 + 30 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        
        //THEN
        assertEquals( (36) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareWithReduction(){
    	//GIVEN
    	 Date inTime = new Date();
         inTime.setTime( System.currentTimeMillis() - (  90 * 60 * 1000) );
         Date outTime = new Date();
         ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

         ticket.setInTime(inTime);
         ticket.setOutTime(outTime);
         ticket.setParkingSpot(parkingSpot);
         ticket.setNbOfEntry(2);
         
         //WHEN
         fareCalculatorService.calculateFare(ticket);
         
         //THEN
         assertEquals(ticket.getPrice(), 1.42);
    }
    @Test
	public void calculateFare_bikeWithFutureInTime_sendException() {
		//GIVEN
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		//WHEN 
		//THEN
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}
}
