package com.jaoafa.MyMaid2.Lib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class MyMaidVariable extends MyMaid2Premise {
	public static boolean set(String key, String value){
		boolean exist = exist(key);
		try {
			PreparedStatement statement;
			if(exist){
				statement = MySQL.getNewPreparedStatement("INSERT INTO variable (var_key, var_value) VALUES (?, ?);");
				statement.setString(1, key);
				statement.setString(2, value);
			}else{
				statement = MySQL.getNewPreparedStatement("UPDATE variable SET var_value = ? WHERE var_key = ?;");
				statement.setString(1, value);
				statement.setString(2, key);
			}
			if(statement.execute()){
				return true;
			}else{
				return false;
			}
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
		}
		return false;
	}
	public static boolean exist(String key){
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM variable WHERE var_key = ?;");
			statement.setString(1, key);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
		}
		return false;
	}
	public static String getString(String key){
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM variable WHERE var_key = ?;");
			statement.setString(1, key);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return res.getString("var_value");
			}
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
		}
		return null;
	}
	public static Map<String, String> list(int page){
		page = page - 1;
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM variable LIMIT ?, 10");
			statement.setInt(1, page * 10);
			ResultSet res = statement.executeQuery();
			Map<String, String> map = new HashMap<String, String>();
			while(res.next()){
				map.put(
						res.getString("var_key"),
						res.getString("var_value")
						);
			}
			return map;
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
		}
		return null;
	}
	public static void remove(String key){
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("DELETE FROM variable WHERE var_key = ?;");
			statement.setString(1, key);
			statement.execute();
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
		}
	}
	public static int getCount(){
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT COUNT(*) FROM variable;");
			ResultSet res = statement.executeQuery();
			int count = res.getInt("COUNT(*)");
			return count;
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
		}
		return -1;
	}
}
