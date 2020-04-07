package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    
    private static final Logger logger = LogManager.getLogger("ParkingDataBaseIT");
   
    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        lenient().when(inputReaderUtil.readSelection()).thenReturn(1,1,1,2);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF","GHIJKL","MNOP");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
    	//GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
        //WHEN
        parkingService.processIncomingVehicle();
        
        //THEN        
		Ticket getTicketTest = ticketDAO.getLastTicket("ABCDEF");
		assertThat(getTicketTest.getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(getTicketTest.getParkingSpot().isAvailable()).isEqualTo(false);
		
    }
    
    @Test
    public void testParkingACarAndABike(){
        //GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
        //WHEN
        parkingService.processIncomingVehicle();
        parkingService.processIncomingVehicle();
        
        //THEN        
        Ticket getTicketTest = ticketDAO.getLastTicket("ABCDEF");
        assertThat(getTicketTest.getVehicleRegNumber()).isEqualTo("ABCDEF");
        assertThat(getTicketTest.getParkingSpot().isAvailable()).isEqualTo(false);
        getTicketTest = ticketDAO.getLastTicket("GHIJKL");
        assertThat(getTicketTest.getVehicleRegNumber()).isEqualTo("GHIJKL");
        assertThat(getTicketTest.getParkingSpot().isAvailable()).isEqualTo(false);
        
    }

    @Test
    public void testParkingLotExitLessThirtyMinutes() throws Exception{
    	//GIVEN       
    	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    	Date inTime = new Date();
    	Date outTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (10*60*1000) ); //10 minutes
        
        //mock incoming vehicle
        processInjectionParkingSpot(false);
        processInjectionTicket(inTime,outTime);
        
        //WHEN
        parkingService.processExitingVehicle();
        
        //THEN        
        Ticket getTicketTest = ticketDAO.getLastTicket("ABCDEF");       
        assertThat(getTicketTest.getPrice()).isEqualTo(0L);
        assertThat(getTicketTest.getOutTime()).isAfter(getTicketTest.getInTime());
      
    }
    
    @Test
    public void testParkingLotExitMoreThirtyminutes() throws Exception{
        //GIVEN
    	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    	Date inTime = new Date();
    	Date outTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (90*60*1000) ); 
        
        //mock incoming vehicle
        processInjectionParkingSpot(false);
        processInjectionTicket(inTime,outTime);
        
        //WHEN
        parkingService.processExitingVehicle();
        
        //THEN
        Ticket getTicketTest = ticketDAO.getLastTicket("ABCDEF");       
        assertThat(getTicketTest.getPrice()).isEqualTo(1.5);
      	
    }
    
    @Test
    public void testParkingLotExitMoreOneDay() throws Exception{
        //GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Date inTime = new Date();
        Date outTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (24*60*60*1000) ); 
        
        //mock incoming vehicle
        processInjectionParkingSpot(false);
        processInjectionTicket(inTime,outTime);
        
        //WHEN
        parkingService.processExitingVehicle();
        
        //THEN
        Ticket getTicketTest = ticketDAO.getLastTicket("ABCDEF");       
        assertThat(getTicketTest.getPrice()).isEqualTo(35.25);
        
    }
    
    
    @Test
    public void testParkingLotExitMoreThirtyminutesWithDiscount() throws Exception{
       
    	//GIVEN
    	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    	    	
        //first Ticket 2h before
    	Date inTime1 = new Date();
    	Date outTime1 = new Date();
        inTime1.setTime( System.currentTimeMillis() - (120*60*1000) ); 
        outTime1.setTime( System.currentTimeMillis() - (110*60*1000) ); 
        processInjectionTicket(inTime1,outTime1);
        
        //Process incoming vehicle       
    	Date inTime2 = new Date();    	
        inTime2.setTime( System.currentTimeMillis() - (90*60*1000) ); 
        processInjectionParkingSpot(false);
        processInjectionTicket(inTime2,null);
           
        //WHEN
        parkingService.processExitingVehicle();
        
        //THEN
        Ticket getTicketTest = ticketDAO.getLastTicket("ABCDEF");       
        assertThat(getTicketTest.getPrice()).isEqualTo(1.42);
        }
    
    
 public void processInjectionTicket(Date inTime, Date outTime) { 
	//To avoid dependencies with other functions, Data injection in database
			Connection con = null;
			PreparedStatement ps = null;
			try {
				con = dataBaseTestConfig.getConnection();
								
				//Ticket Injection
				ps = con.prepareStatement(DBConstants.SAVE_TICKET);
				// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
				String ticketVehicleRegNumber = "ABCDEF";
				double ticketPrice = 0;
				int parkingSpotId = 1;
				ps.setInt(1, parkingSpotId);
				ps.setString(2, ticketVehicleRegNumber);
				ps.setDouble(3, ticketPrice);
				ps.setTimestamp(4,new Timestamp(inTime.getTime()));
				ps.setTimestamp(5,outTime == null ? null : (new Timestamp(outTime.getTime())));
				ps.execute();
				
		
			} catch (Exception ex) {
				logger.error("Error updating parking info during test", ex);
						} finally {
				dataBaseTestConfig.closePreparedStatement(ps);
				dataBaseTestConfig.closeConnection(con);
			}
    }
public void processInjectionParkingSpot(boolean isAvaliable) { 
		
		//To avoid dependencies with other functions, Data injection in database
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseTestConfig.getConnection();
			// ParkingSpot injection
			ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
						
			int parkingSpotId = 1;
			ps.setBoolean(1, isAvaliable);
			ps.setInt(2, parkingSpotId);
			ps.executeUpdate();
			dataBaseTestConfig.closePreparedStatement(ps);
									
	
		} catch (Exception ex) {
			logger.error("Error updating parking info during test", ex);
					} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeConnection(con);
		}
}
}
