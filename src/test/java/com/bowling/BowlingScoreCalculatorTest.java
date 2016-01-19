package com.bowling;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.bowling.exception.BowlingScoreException;

public class BowlingScoreCalculatorTest {

	private static final String[] invalidInputs = {"ABC","1 2 3.0","1 2 3 4 5 X","1|2|3"};

	private static final String[] invalidBonusRollInputs = {"10 10 10 10 10 10 10 10 10 5 2 3",
										"10 10 10 10 10 10 10 10 10 5 5 2 3"};
	
	BowlingScoreCalculator bowlingScoreCalculator;

	@Before
	public void setUp() throws Exception {
		bowlingScoreCalculator = new BowlingScoreCalculator();
	}

	@Test
	public void calculateScoreTest() {
		try {
			int score = bowlingScoreCalculator.calculateScore("1 2 3 4");
			assertEquals(10,score);
		} catch (BowlingScoreException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void spareScoreTest() {
		try {
			int score = bowlingScoreCalculator.calculateScore("9 1 9 1");
			assertEquals(29,score);
		} catch (BowlingScoreException e) {
			fail(e.getMessage());
		}

		try {
			int score = bowlingScoreCalculator.calculateScore("8 2 3 4 5 2 4 6 7 2 1 6 7 2 4 2 4 2 7 2");
			assertEquals(90,score);
		} catch (BowlingScoreException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void strikeScoreTest() {

		//Test 1
		try {
			int score = bowlingScoreCalculator.calculateScore("9 0 1 4 10 6 3 6 2 10");
			assertEquals(60,score);
		} catch (BowlingScoreException e) {
			fail(e.getMessage());
		}

		//Test 2
		try {
			int score = bowlingScoreCalculator.calculateScore("1 1 1 1 10 1 1");
			assertEquals(18,score);
		} catch (BowlingScoreException e) {
			fail(e.getMessage());
		}

		//Test 3 - Perfect Score
		try {
			int score = bowlingScoreCalculator.calculateScore("10 10 10 10 10 10 10 10 10 10 10 10");
			assertEquals(300,score);
		} catch (BowlingScoreException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void bonusScoreTest() {

		//Test 1
		try {
			int score = bowlingScoreCalculator.calculateScore("2 2 2 2 4 6 6 2 2 2 2 2 2 2 2 2 2 2 2 8 4");
			assertEquals(66,score);
		} catch (BowlingScoreException e) {
			fail(e.getMessage());
		}

		//Test 2
		try {
			int score = bowlingScoreCalculator.calculateScore("1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 10 2 3");
			assertEquals(33,score);
		} catch (BowlingScoreException e) {
			fail(e.getMessage());
		}

		//Test 3
		try {
			int score = bowlingScoreCalculator.calculateScore("1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 9 10 2 3");
			assertEquals(51,score);
		} catch (BowlingScoreException e) {
			fail(e.getMessage());
		}
	}


	/* Below are all invalid input tests*/

	@Test
	public void invalidInputFomatTest() {
		String errorMessage = null;
		for (String invalidInput : invalidInputs) {
			errorMessage = null;
			try {
				bowlingScoreCalculator.calculateScore(invalidInput);
			} catch (BowlingScoreException e) {
				errorMessage = e.getMessage();
			}
			assertEquals(BowlingScoreCalculator.MSG_INVALID_INPUT,errorMessage);
		}
	}

	@Test
	public void nullInputTest() {
		String errorMessage = null;
		try {
			bowlingScoreCalculator.calculateScore(null);
		} catch (BowlingScoreException e) {
			errorMessage = e.getMessage();
		}
		assertEquals(BowlingScoreCalculator.MSG_EMPTY_INPUT,errorMessage);
	}

	@Test
	public void invalidRollScoreTest() {
		String errorMessage = null;
		try {
			bowlingScoreCalculator.calculateScore("1 2 4 12");
		} catch (BowlingScoreException e) {
			errorMessage = e.getMessage();
		}
		assertEquals(BowlingScoreCalculator.MSG_INVALID_ROLL_SCORE,errorMessage);
	}

	@Test
	public void invalidFrameScoreTest() {
		String errorMessage = null;
		try {
			bowlingScoreCalculator.calculateScore("1 2 4 10");
		} catch (BowlingScoreException e) {
			errorMessage = e.getMessage();
		}
		assertEquals(BowlingScoreCalculator.MSG_INVALID_FRAME_SCORE,errorMessage);
	}

	@Test
	public void invalidBonusRollsTest() {
		String errorMessage = null;
		for (String invalidInput : invalidBonusRollInputs) {
			errorMessage = null;
			try {
				bowlingScoreCalculator.calculateScore(invalidInput);
			} catch (BowlingScoreException e) {
				errorMessage = e.getMessage();
			}
			assertEquals(BowlingScoreCalculator.MSG_BONUS_ROLL_NOT_ELIGIBLE,errorMessage);
		}
	}	

}
