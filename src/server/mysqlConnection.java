package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import entity.Order;

public class mysqlConnection {
	private static final int ORDERNUMBER=1;
	private static final int TOTALPRICE = 2;
	private static final int GREETINGCARD = 3;
	private static final int COLOR = 4;
	private static final int ORDERDESC = 5;
	private static final int FROMSTORE=6;
	private static final int ORDERTIMEDATE = 7;
	private static final int CUSTOMERID = 8;
	private static final int PAYMENTMETHOD =9;
	private static final int ORDERSTATUS =10;
	private static final int CONFIRMEDDATE=11;
	private static final int COMPLETEDATE=12;
	private static final int DELIVERYMETHOD=13;
	private static final int EXPECTEDDATETIMEINSTORE=14;
	

	//Details to show to table
	//Triggred by clicking on "Show Orders" button
	public static String showOrder(Connection con, String orderNumber) {
		Statement stmt;
		try {
			String sql = "SELECT * FROM zli.orders WHERE orderNumber=" + Integer.parseInt(orderNumber) + ";";
			String order_detalis = null;
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				order_detalis = (rs.getInt(ORDERNUMBER) + " " + rs.getString(TOTALPRICE) + " " + rs.getString(GREETINGCARD) + " "
								+ rs.getString(COLOR) + " " + rs.getString(ORDERDESC) + " " + rs.getString(FROMSTORE) + " " + 
								rs.getString(ORDERTIMEDATE) + " " + rs.getString(CUSTOMERID) + " " + rs.getString(PAYMENTMETHOD)
								 + " " + rs.getString(ORDERSTATUS) + " " + rs.getString(CONFIRMEDDATE) + " " + rs.getString(COMPLETEDATE)
								 + " " + rs.getString(DELIVERYMETHOD) + " " + rs.getString(EXPECTEDDATETIMEINSTORE));
			return order_detalis;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		/*
		 * finally{ rs.close(); }
		 */

	}
	
	public static ArrayList<Order> loadOrders(Connection con) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM zli.orders");
			ArrayList<Order> orders = new ArrayList<>();
			while(rs.next()) {
				int orderNumber = rs.getInt(1);
				double price = rs.getDouble(2);
				String greetingCard = rs.getString(3);
				String color = rs.getString(4);
				String dorder = rs.getString(5);
				String shop = rs.getString(6);
				LocalDate supplyDate = LocalDate.parse(rs.getString(7));
				LocalTime supplyTime = LocalTime.parse(rs.getString(8));
				LocalDate orderDate = LocalDate.parse(rs.getString(9));
				LocalTime orderTime = LocalTime.parse(rs.getString(10));
				Order order = new Order(orderNumber, price, greetingCard,
										color, dorder, shop, supplyDate,
										supplyTime, orderDate, orderTime);
				orders.add(order);
			}
			return orders;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean cellUpdate(Connection con, String orderNumber, Object newValue, int column) {
		PreparedStatement ps;
		String sql;
		try {
			switch (column) {// start SWITCH
			case COLOR:
				sql = "UPDATE zli.orders SET color=? WHERE orderNumber=" + Integer.parseInt(orderNumber) + ";";
				ps = con.prepareStatement(sql);
				ps.setString(1, newValue.toString());
				ps.executeUpdate();
				ps.close();
				break;
			case SUPPLYDATE:
				sql = "UPDATE zli.orders SET supplyDate=? WHERE orderNumber=" + Integer.parseInt(orderNumber) + ";";
				ps = con.prepareStatement(sql);
				ps.setString(1, newValue.toString());
				ps.executeUpdate();
				ps.close();
				break;
			case SUPPLYTIME:
				sql = "UPDATE zli.orders SET supplyTime=? WHERE orderNumber=" + Integer.parseInt(orderNumber) + ";";
				ps = con.prepareStatement(sql);
				ps.setString(1, newValue.toString());
				ps.executeUpdate();
				ps.close();
				break;
			default://debugging
				System.out.println("Command doesn't exist");
			}// end SWITCH
			System.out.println("Order Updated");
			return true;
		} catch (SQLException e) {
			System.out.println("CellUpdateQuery failed");
			e.printStackTrace();
			return false;
		}
	}

}