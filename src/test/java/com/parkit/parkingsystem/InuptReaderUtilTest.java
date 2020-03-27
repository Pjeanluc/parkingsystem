package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

class InuptReaderUtilTest {
	
		 
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
