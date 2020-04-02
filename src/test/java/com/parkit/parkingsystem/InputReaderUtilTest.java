package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.util.InputReaderUtil;

class InputReaderUtilTest {
	
	
	
	@Test
	public void readVehicleNumberTest() throws Exception {
		//ARRANGE
		InputStream sysInBackup = System.in;
	    String input = "ABCDEF";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
	    InputReaderUtil inputSelection= new InputReaderUtil();
	    
	    //ASSERT
	    assertEquals("ABCDEF", inputSelection.readVehicleRegistrationNumber());
	    System.setIn(sysInBackup);
	  
	}
	
	
	
	@Test
	public void readSelectionTest() throws IOException {
		//ARRANGE
		InputStream sysInBackup = System.in;
	    String input = "3";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
	    InputReaderUtil inputSelection= new InputReaderUtil();
	    
	    //ASSERT
	    assertEquals(3, inputSelection.readSelection());
	    System.setIn(sysInBackup);
	    
	    
	}
	
	
	@Test
	public void readVehicleRegistrationNumberExceptionTest() {
		//ARRANGE
		String input = "";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
	    InputReaderUtil inputSelection= new InputReaderUtil();
	    
	    //ASSERT
	    assertThrows(NoSuchElementException.class, () -> {inputSelection.readVehicleRegistrationNumber();});
	}
	
	
	@Test
	public void readSelectionExceptionTest() {
		//ARRANGE
		String input = "";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
	    InputReaderUtil inputSelection= new InputReaderUtil();
	    
	    //ASSERT
	    assertEquals(-1, inputSelection.readSelection());
	}
	
	
	@Test
	void caculateDiffInHoursMoreOneHours() {
		//ARRANGE
		Date outTime = new Date();
		outTime.setTime(System.currentTimeMillis());
						       
		//ACT
		double duration = InputReaderUtil.calculateDiffInHours((outTime.getTime()-( 90 * 60 * 1000)), outTime.getTime());
		
		//ASSERT
		assertEquals(1.5, duration);
		
	}
	@Test
	void caculateDiffInHoursLessOneHours() {
		//ARRANGE
		Date outTime = new Date();
		outTime.setTime(System.currentTimeMillis());
		
		       
		//ACT
		double duration = InputReaderUtil.calculateDiffInHours((outTime.getTime()-( 45 * 60 * 1000)), outTime.getTime());
		
		//ASSERT
		assertEquals(0.75, duration);
		
	}
	
	@Test
	void caculateDiffInHoursMoreOneDay() {
		//ARRANGE
		Date outTime = new Date();
		outTime.setTime(System.currentTimeMillis());
		
		       
		//ACT
		double duration = InputReaderUtil.calculateDiffInHours((outTime.getTime()-( 25 * 60 * 60 * 1000)), outTime.getTime());
		
		//ASSERT
		assertEquals(25.0, duration);
		
	}
	
	@Test
	void durationlessThirtyMinute() {
		//ARRANGE
		double durationReel = 0.4;
				
		//ACT
				
		//ASSERT
		assertEquals(0, InputReaderUtil.lessThirthyMinutes (durationReel));
	}
	
	@Test
	void durationMoreThirtyMinute() {
		//ARRANGE
		double durationReel = 1.4;
				
		//ACT
				
		//ASSERT
		assertEquals(0.9, InputReaderUtil.lessThirthyMinutes (durationReel));
	}
	
}
