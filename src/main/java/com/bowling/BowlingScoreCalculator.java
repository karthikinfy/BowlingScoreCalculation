package com.bowling;

import java.util.ArrayList;
import java.util.List;

import com.bowling.exception.BowlingScoreException;

/**
 * The BowlingScoreCalculator program calculates the score of bowling game. 
 * @author Siva_Ravi
 *
 */
public class BowlingScoreCalculator {

	public final static String SCORE_SEPERATOR = " ";
	
	public final static String SCORE_VALIDATOR_REG_EX = "^([0-9]*\\s+)*[0-9]*$";
	
	public final static String MSG_EMPTY_INPUT = "Input is empty";
	
	public final static String MSG_INVALID_INPUT = "Input is not in a valid format";
	
	public final static String MSG_BONUS_ROLL_NOT_ELIGIBLE = "Invalid score: Not eligible for bonus rolls";
	
	public final static String MSG_INVALID_FRAME_SCORE = "A frame score cant be greater than 10";
	
	public final static String MSG_INVALID_ROLL_SCORE = "Only numbers between 0 and 10 are allowed";
	
	/**
	 * This method takes a string of space separated numbers from 0 to 10 and calculates the score.
	 * @param scoreString space separated string.
	 * @return score.
	 * @throws BowlingScoreException On invalid input.
	 */
	public int calculateScore(String scoreString) throws BowlingScoreException{
		int score = 0;
		int previousFrame = 0;
		int prevPrevRollScore = 0;
		int prevRollScore = 0;
		int ballInFrame = 0;
		boolean isLastRollSpare=false;
		boolean isBonusRoll = false;
		int numberOfBonusRolls = 0;
		int currentBonusRoll = 0;

		List<Integer> rollScoresList = parseScore(scoreString);
		for (Integer currentRollScore : rollScoresList) {

			//Calculate the score for the current roll
			if(isBonusRoll){
				currentBonusRoll++;
				if(currentBonusRoll>numberOfBonusRolls){
					throw new BowlingScoreException(MSG_BONUS_ROLL_NOT_ELIGIBLE);
				}
				score+=currentRollScore;
				
				//If the ninth frame is strike then add the first bonus roll again to total score
				if(currentBonusRoll == 1 && prevPrevRollScore == 10){
					score+=currentRollScore;
				}
			}else if(previousFrame==10){
				throw new BowlingScoreException(MSG_BONUS_ROLL_NOT_ELIGIBLE);
			}else{
				score+=currentRollScore;

				//If the previous roll is spare or strike add the current roll score to total score
				if(prevRollScore==10 || isLastRollSpare){
					score+=currentRollScore;
					isLastRollSpare=false;
				}

				//If roll previous to the previous roll is strike add the current roll score to total score
				if(prevPrevRollScore==10){
					score+=currentRollScore;
				}
			}


			//Calculations to update the frames details
			ballInFrame++;
			//Increase the previousFrame if it is second ball
			if(ballInFrame==2 && !isBonusRoll){
				if(prevRollScore+currentRollScore>10){
					throw new BowlingScoreException(MSG_INVALID_FRAME_SCORE);
				}
				previousFrame++;
				ballInFrame=0;
				if(prevRollScore+currentRollScore==10){
					isLastRollSpare = true;
					if(previousFrame==10){
						numberOfBonusRolls = 1;
						isBonusRoll = true;
					}
				}
			}

			//Increase the previousFrame if current the score is a strike
			if(currentRollScore==10 && !isBonusRoll){
				previousFrame++;
				ballInFrame=0;
				//If current score is 10 then eligible for two bonus scores
				if(previousFrame==10){
					numberOfBonusRolls = 2;
					isBonusRoll = true;
				}
			}
			prevPrevRollScore = prevRollScore;
			prevRollScore=currentRollScore;
		}

		return score;
	}

	/**
	 * This method parses a string of space separated numbers and returns a list of integers.
	 * @param scoreString string of space separated numbers.
	 * @return List of integers.
	 * @throws BowlingScoreException On invalid input.
	 */
	protected List<Integer> parseScore(String scoreString) throws BowlingScoreException{
		List<Integer> rollScoresList = null;
		String[] scoreStrings = null;
		int rollScore;

		//Check if input is not empty
		if(scoreString==null || scoreString.isEmpty()){
			throw new BowlingScoreException(MSG_EMPTY_INPUT);
		}

		if(!scoreString.matches(SCORE_VALIDATOR_REG_EX)){
			throw new BowlingScoreException(MSG_INVALID_INPUT);
		}

		//Split the string
		scoreStrings = scoreString.split(SCORE_SEPERATOR);

		//Populate the rollScores
		rollScoresList = new ArrayList<Integer>();
		for (String rollScoreString : scoreStrings) {
			rollScore=Integer.parseInt(rollScoreString);
			if(rollScore<0 || rollScore>10){
				throw new BowlingScoreException(MSG_INVALID_ROLL_SCORE);
			}
			rollScoresList.add(rollScore);
		}

		return rollScoresList;
	}

}
