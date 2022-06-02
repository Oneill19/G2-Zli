//package gui.client;
//
//import java.io.IOException;
//
//import client.ChatClient;
//import client.ClientUI;
//import entity.AbstractProduct;
//import entity.Item;
//import entity.Order;
//import entity.Product;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.Button;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//
///**
// * @author DORIN BEERY
// *
// */
//public class PaymentController {
//
//    @FXML private PasswordField cvv, first4, fourth4, second4, third4; 
//    @FXML private TextField day, fullName, month; 
//    @FXML private Button exit, logoutBtn, nextBtn, onBack, userOptBtn;
//    
//    private CommonController cc = new CommonController();
//    
//    /**
//     * Triggered by pressing the back button, executes CommonController.changeFXML()
//     * Replaces screen to PersonalDetails.fxml
//     * @param event
//     * @throws IOException
//     * @see CommonController#changeFXML
//     */
//    @FXML
//	void onBack(ActionEvent event) throws IOException {		
//		cc.changeFXML(event, "PersonalDetails.fxml", "Zer-Li Personal Details",null);
//	}
//
//    /**
//     * Triggered by pressing the exit button, executes CommonController.OnExit()
//     * @see CommonController#OnExit() 
//     * @param event
//     * @throws Exception
//     */
//    @FXML
//	void onExit(ActionEvent event) throws Exception {
//		cc.OnExit();
//	}
//
//    /**
//     * Triggred by pressing the logout button, executes CommonController.onLogout()
//     * @param event
//     * @throws IOException
//     * @see {@link CommonController#onLogout(ActionEvent)}
//     */
//	@FXML
//	void onLogout(ActionEvent event) throws Exception {
//		cc.onLogout(event);
//	}
//
//    /**
//     * Triggred by pressing the next button
//     * Responsible for adding the products and items to the db.
//     * also, send data to ChatClient.accept for managing of reports every 30 days.
//     * @param event
//     * @throws IOException
//     */
//    @FXML
//    void onNext(ActionEvent event) throws IOException {
//    	//TODO - does payment method is always credit card?
//    	ChatClient.cartOrder.setPaymentMethod("Credit Card");
//    	
//    	//Add Order to DB
//    	try {
//    		ClientUI.chat.accept("AddOrderToDB\t"+ChatClient.cartOrder.DBToString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	
//    	//Send data for managing reports every 30 days.
//    	StringBuilder sbItems = new StringBuilder();
//    	Integer itemCounter= 0;
//    	StringBuilder sbProducts=new StringBuilder();
//    	Integer productCounter=0;
//    	double productPriceSum = 0;
//    	double itemPriceSum = 0;
//    	for(AbstractProduct ap : ChatClient.cart) {
//    		if (ap instanceof Product) {
//    			productCounter++;
//    			productPriceSum += ap.getPrice();
//    			sbProducts.append("'").append(ap.getSerialNumber()).append("',");
//    		}
//    		if(ap instanceof Item) {
//    			itemCounter++;
//    			itemPriceSum += ap.getPrice();
//    			sbItems.append("'").append(ap.getSerialNumber()).append("',");
//    		}
//    	}
//    	if (sbItems.length() > 0)
//    		sbItems.delete(sbItems.length()-1, sbItems.length());
//    	if (sbProducts.length() > 0)
//    		sbProducts.delete(sbProducts.length()-1, sbProducts.length());
//    	try {
//			ClientUI.chat.accept("numberOfItemsInOrder\t"+productCounter.toString()+"\t"+itemCounter.toString()+
//					"\t"+ productPriceSum+"\t"+ itemPriceSum + '\t' + ChatClient.cartOrder.DBToString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	
//    	
//    	//Add products and items in the cart to the DB
//    	Integer orderNumber = ChatClient.cartOrder.getOrderNumber();
//    	try {
//    		ClientUI.chat.accept("addProductsAndItemsInOrderToDB\t"+orderNumber.toString()+"\t"
//    							+sbItems.toString()+"\t"+sbProducts.toString());
//    	}catch(Exception ex) {
//    		ex.printStackTrace();
//    	}
//    	
//    	//show success message and go back to catalog
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle("Order reception completed SUCCESSFULY!");
//		alert.setHeaderText("You're order " + ChatClient.cartOrder.getOrderNumber() + " was set and is waiting"
//				+ " for confirmation within the next 3 hours.\nYou can check its' status at the Watch Orders option.");
//			alert.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("orderReception.png"))));
//		alert.showAndWait();
//    	
//		ChatClient.cart.clear();
//		ChatClient.cartOrder = new Order();
//		cc.changeFXML(event, "Catalog.fxml", "Catalog", null);
//		
//    }
//    
//    /**
//     * Initials the comboBox
//     */
//    public void initialize() {
//    	fullName.setText(ChatClient.user.getFirstName()+" "+ChatClient.user.getLastName());
//    	String[] creditCard = new String[4]; 
//    	try {
//    	creditCard[0] = ChatClient.user.getCreditCard().subSequence(0, 4).toString();
//    	creditCard[1] = ChatClient.user.getCreditCard().subSequence(4, 8).toString();
//    	creditCard[2] = ChatClient.user.getCreditCard().subSequence(8, 12).toString();
//    	creditCard[3] = ChatClient.user.getCreditCard().subSequence(12, 16).toString();
//    	}catch(StringIndexOutOfBoundsException e) {
//    		System.out.println("no valid credit card is saved in DB");
//    	}
//    	first4.setText(creditCard[0]);
//    	second4.setText(creditCard[1]);
//    	third4.setText(creditCard[2]);
//    	fourth4.setText(creditCard[3]);
//    }
//
//}
//
