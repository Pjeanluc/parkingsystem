package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class InputReaderUtil {

    //private static Scanner scan = new Scanner(System.in,StandardCharsets.UTF_8.name());
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");
        

    public int readSelection() {
    	Scanner scan = new Scanner(System.in,StandardCharsets.UTF_8.name());
        try {
        	String in = scan.nextLine();
            int input = Integer.parseInt(in);
            scan.close();
            return input;
            
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            scan.close();
            return -1;
          
        }
    }

    public String readVehicleRegistrationNumber() throws Exception {
    	Scanner scan = new Scanner(System.in,StandardCharsets.UTF_8.name());
        try {
            String vehicleRegNumber= scan.nextLine();
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            scan.close();
            return vehicleRegNumber;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            scan.close();
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
            
        }
    }
    
   

    public static double calculateDiffInHours(long inHour, long outHour) {
    	    	
        return arrondir((double) (outHour - inHour)/(60*60*1000),2);
}

	public static double lessThirthyMinutes(double durationReel) {
		double durationCalculate = 0;
		if (durationReel > 0.5) {
			durationCalculate = (durationReel - 0.5d);
		}
		
		return arrondir(durationCalculate,2);
	}
	
	public static double arrondir(double nombre,double nbApVirg)
	{
		return(double)((int)(nombre * Math.pow(10,nbApVirg) + .5)) / Math.pow(10,nbApVirg);
	}
}
