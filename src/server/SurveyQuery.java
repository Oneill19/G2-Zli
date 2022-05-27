package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import common.ReturnCommand;
import entity.Survey;
import entity.SurveyReport;

/**
 * @AUTHOR ONEILL PANKER
 *
 */
public class SurveyQuery {
	public static ReturnCommand getAllSurveys(Connection con) {
		Statement stmt;
		String sqlQuery = "SELECT * FROM zli.survey";
		ArrayList<Survey> surveys = new ArrayList<>();
		
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {
				int surveyId = rs.getInt(1);
				String surveyName = rs.getString(2);
				String[] questions = new String[6];
				questions[0] = rs.getString(3);
				questions[1] = rs.getString(4);
				questions[2] = rs.getString(5);
				questions[3] = rs.getString(6);
				questions[4] = rs.getString(7);
				questions[5] = rs.getString(8);
				surveys.add(new Survey(surveyId, surveyName, questions));
			}
			return new ReturnCommand("GetAllSurveys", surveys);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ReturnCommand getSurveyById(Connection con, int surveyId) {
		Statement stmt;
		String sqlQuery = "SELECT * FROM zli.survey WHERE SurveyID=" + surveyId + ";";
		Survey survey = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlQuery);
			if (rs.next()) {
				String surveyName = rs.getString(2);
				String[] questions = new String[6];
				questions[0] = rs.getString(3);
				questions[1] = rs.getString(4);
				questions[2] = rs.getString(5);
				questions[3] = rs.getString(6);
				questions[4] = rs.getString(7);
				questions[5] = rs.getString(8);
				survey = new Survey(surveyId, surveyName, questions);
			}
			return new ReturnCommand("GetSurveyById", survey);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ReturnCommand getSurveysWithReports(Connection con) {
		Statement stmt;
		String sqlQuery = "SELECT * FROM zli.survey WHERE zli.survey.SurveyID IN (SELECT SurveyID FROM zli.survey_reports);";
		ArrayList<Survey> surveys = new ArrayList<>();
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {
				int surveyId = rs.getInt(1);
				String surveyName = rs.getString(2);
				String[] questions = new String[6];
				questions[0] = rs.getString(3);
				questions[1] = rs.getString(4);
				questions[2] = rs.getString(5);
				questions[3] = rs.getString(6);
				questions[4] = rs.getString(7);
				questions[5] = rs.getString(8);
				surveys.add(new Survey(surveyId, surveyName, questions));
			}
			return new ReturnCommand("GetSurveysWithReports", surveys);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ReturnCommand getSurveyReport(Connection con, int surveyId) {
		Statement stmt;
		String sqlQuery = "SELECT * FROM zli.survey_reports WHERE SurveyID=" + surveyId + ";";
		SurveyReport surveyReport = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlQuery);
			if (rs.next()) {
				Survey survey = (Survey) getSurveyById(con, surveyId).getReturnValue();
				String fileName = rs.getString(2);
				int[][] answers = (int[][]) getSurveyAnswers(con, surveyId).getReturnValue();
				surveyReport = new SurveyReport(survey, fileName, answers);
			}
			return new ReturnCommand("GetSurveyReport", surveyReport);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ReturnCommand getSurveyAnswers(Connection con, int surveyId) {
		Statement stmt;
		String sqlQuery = "SELECT * FROM zli.survey_answers WHERE SurveyID=" + surveyId + ";";
		int[][] surveyAnswers = new int[6][10];
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {
				surveyAnswers[0][rs.getInt(3) - 1]++;
				surveyAnswers[1][rs.getInt(4) - 1]++;
				surveyAnswers[2][rs.getInt(5) - 1]++;
				surveyAnswers[3][rs.getInt(6) - 1]++;
				surveyAnswers[4][rs.getInt(7) - 1]++;
				surveyAnswers[5][rs.getInt(8) - 1]++;
			}
			return new ReturnCommand("GetSurveyAnswers", surveyAnswers);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
