package entity;

import java.util.ArrayList;

import javafx.scene.image.ImageView;

/**
 * @AUTHOR ONEILL PANKER
 *
 */
public class Product extends AbstractProduct {
	
	private String other;
	private ArrayList<Item> madeFrom;

	/**
	 * @param serialNumber
	 * @param name
	 * @param price
	 * @param type
	 * @param image
	 * @param other
	 * @param madeFrom
	 */
	public Product(String serialNumber, 
					String name, 
					double price, 
					String type, 
					ImageView image, 
					String other,
					ArrayList<Item> madeFrom) {
		super(serialNumber, name, price, image, false, type);
		this.other = other;
		this.madeFrom = madeFrom;
	}
	
	@Override
	public double getPrice() {
		double sum=0;
		for(Item item : madeFrom)
			sum+=item.getPrice();
		return sum;
	}
	
	/**
	 * @return the other
	 */
	public String getOther() {
		return other;
	}

	/**
	 * @param other the other to set
	 */
	public void setOther(String other) {
		this.other = other;
	}
	
	/**
	 * @return the madeFrom
	 */
	public ArrayList<Item> getMadeFrom() {
		return madeFrom;
	}

	/**
	 * @param madeFrom the madeFrom to set
	 */
	public void setMadeFrom(ArrayList<Item> madeFrom) {
		this.madeFrom = madeFrom;
	}

}
