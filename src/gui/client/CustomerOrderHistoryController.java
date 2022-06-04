package gui.client;

import java.io.IOException;
import java.util.ArrayList;

import client.ChatClient;
import client.ClientUI;
import entity.AbstractProduct;
import entity.Item;
import entity.Order;
import entity.Product;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CustomerOrderHistoryController {

    @FXML private Button onBack, logoutBtn, userOptBtn, exit;
    @FXML private ScrollPane itemScrollPane;
    @FXML private GridPane grid;
    @FXML private Tooltip tableToolTip;
    @FXML private Text textCreationDate,textSupplyDate,textConfirmedDate,textCompleteDate;
    @FXML private Text textCreationTime,textSupplyTime;
    @FXML private Text receiverNameText,receiverPhoneText, receiverAddressText,textOrderNumber,storeNameText,statusText,deliveryStatusText;
    @FXML private HBox deliveryHbox;
    @FXML private VBox pickupVbox;
    
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> colOrderNumber;
    @FXML private TableColumn<Order, Double> colPrice;
    @FXML private TableColumn<Order, String> colGreetingCard,colDeliveryMethod,colCancelOrder;

    private ArrayList<Order> userOrdersDataFromDB = new ArrayList<Order>();
    private ArrayList<Item> orderHistoryItmes = new ArrayList<Item>();
    private ArrayList<Product> orderHistoryProducts= new ArrayList<Product>();
    private ArrayList<AbstractProduct> orderHistoryAll = new ArrayList<AbstractProduct>();
    private Order selectedOrder = null;
    private CommonController cc = new CommonController();

	@FXML
	void onBack(ActionEvent event) throws IOException {		
		cc.changeFXML(event, "ApprvoedCustomerOptions.fxml", "Zer-Li Customer Options");
	}

	@FXML
	void onExit(ActionEvent event) throws Exception {
		cc.OnExit();
	}

	@FXML
	void onLogout(ActionEvent event) throws Exception {
		cc.onLogout(event);
	}
    
	/**
	 * initials userOrdersDataFromDB with all orders of the user from the DB,
	 * and sets the orderTable to select the first order from this list.
	 */
	private void setOrdersFromDB() {

//		get orders from DB
		try {
			ClientUI.chat.accept("getUserOrders\t"+Integer.toString(ChatClient.user.getUserID()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
//		initial classs' order array
		userOrdersDataFromDB.clear();
    	userOrdersDataFromDB = ChatClient.userOrdersHistory;

//    	set the first order in the side bar
    	if (ChatClient.userOrdersHistory.size() > 0) {
    		try { setChosenOrder(userOrdersDataFromDB.get(0)); } catch (IOException e) { e.printStackTrace(); }
    	}
	}
	
    public void initialize() {
    	setOrdersFromDB();
    	
    	Rectangle rect = new Rectangle(0, 0, 100, 100);
        Tooltip.install(rect, tableToolTip);
    	
//    	set the user button to show the name
    	userOptBtn.setText("Hello, " + ChatClient.user.getFirstName());
    	
    	deliveryHbox.setVisible(false);
    	pickupVbox.setVisible(false);
    	
		colOrderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
		colPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
		colGreetingCard.setCellValueFactory(new PropertyValueFactory<>("greetingCard"));
        colDeliveryMethod.setCellValueFactory(new PropertyValueFactory<>("deliveryMethod"));
//        TODO
//        colCancelOrder.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));

        ordersTable.setId("my-table");
        ordersTable.getItems().clear();
        ordersTable.autosize();
        ordersTable.setItems(FXCollections.observableArrayList(ChatClient.userOrdersHistory));
        
        ordersTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {
			@Override
			public void changed(ObservableValue<? extends Order> observable, Order oldValue, Order newValue ) {
				try { setChosenOrder(newValue); } catch (IOException e) { e.printStackTrace(); }
			}
        });
    }
    
    
	
	
	
	
    /**
     * @param order
     * method to set the image, name, price and description in the side bar
     * @throws IOException 
     */
    private void setChosenOrder(Order order) throws IOException {
    	grid.getChildren().clear();
    	orderHistoryAll.clear();
    	
//    	set the selected product
    	selectedOrder = order;
    	textOrderNumber.setText("Order #"+ Integer.toString(order.getOrderNumber()));
    	
    	//get items and products of selected order
    	ClientUI.chat.accept("getOrderItems\t"+order.getOrderNumber());
    	ClientUI.chat.accept("getOrderProducts\t"+order.getOrderNumber());
    	orderHistoryAll.addAll(ChatClient.orderHistoryItems);
    	orderHistoryAll.addAll(ChatClient.orderHistoryProducts);
    	
//    	set selected order header info
    	textCreationDate.setText(order.getOrderCreationDate().toString());
    	textSupplyDate.setText(order.getSupplyDate().toString());
    	if(order.getConfirmedDate().equals("null"))
    		textConfirmedDate.setText(("Not Yet"));
    	else
    		textConfirmedDate.setText(order.getConfirmedDate());    	
    	if(order.getCompleteDate().equals("null"))
    		textCompleteDate.setText(("Not Yet"));
    	else
    		textCompleteDate.setText(order.getCompleteDate().toString());
    	textCreationTime.setText(order.getOrderCreationTime().toString());
    	textSupplyTime.setText(order.getSupplyTime().toString());
    	
    	statusText.setFont(Font.font(null, FontWeight.BLACK, FontPosture.REGULAR, 20));
    	deliveryStatusText.setFont(Font.font(null, FontWeight.BLACK, FontPosture.REGULAR, 20));
   	
    	if (order.getDeliveryMethod().equals("Delivery")) {
    		pickupVbox.setVisible(false);
    		ClientUI.chat.accept("getOrderDeliveryData\t"+order.getOrderNumber());
    		String s = ChatClient.orderHistoryDeliveryData;
    		String[] receiverData = s.split("\t");
    		receiverNameText.setText(receiverData[0]);
    		receiverPhoneText.setText(receiverData[1].toString());
    		receiverAddressText.setText(receiverData[2].toString());
    		deliveryHbox.setVisible(true);
    		switch(order.getOrderStatus()) {
	        	case "CONFIREMED":
	        		deliveryStatusText.setText("Confirmed");
	        		deliveryStatusText.setFill(Color.GREEN);
	        		break;
	        	case "CANCELED":
	        		deliveryStatusText.setText("Canceled");
	        		deliveryStatusText.setFill(Color.GREEN);
	        		break;
	        	case "WAITING_FOR_CANCELATION":
	        		deliveryStatusText.setText("Waiting for cancelation");
	        		deliveryStatusText.setFill(Color.ORANGE);
	        		break;
	        	case "WAITING_FOR_CONFIRMATION":
	        		deliveryStatusText.setText("Waiting for confirmation");
	        		deliveryStatusText.setFill(Color.ORANGE);
	        		break;
        	}
    	}
    	else {
    		deliveryHbox.setVisible(false);
    		pickupVbox.setVisible(true);
    		storeNameText.setText(order.getFromStore());
    		storeNameText.setFont(Font.font(null, FontWeight.BLACK, FontPosture.REGULAR, 20));
    		storeNameText.setFill(Color.GREEN);
        	switch(order.getOrderStatus()) {
	        	case "CONFIREMED":
	        		statusText.setText("Confirmed");
	        		statusText.setFill(Color.GREEN);
	        		break;
	        	case "CANCELED":
	        		statusText.setText("Canceled");
	        		statusText.setFill(Color.GREEN);
	        		break;
	        	case "WAITING_FOR_CANCELATION":
	        		statusText.setText("Waiting for cancelation");
	        		statusText.setFill(Color.ORANGE);
	        		break;
	        	case "WAITING_FOR_CONFIRMATION":
	        		statusText.setText("Waiting for confirmation");
	        		statusText.setFill(Color.ORANGE);
	        		break;
        	}
    	}
    	
//    	show items and products of selected order
    	int i=0;
    	for (AbstractProduct ap : orderHistoryAll) {
    		Text name = new Text(ap.getName());
    		ImageView image = new ImageView(new Image(getClass().getResourceAsStream(ap.getImagePath()),100,100,false,false));
    		VBox vbox1 = new VBox(name,image);
    		VBox vbox2=null;
    		Text itemOrProduct=null, type=null,price=null,color=null,amount = null;
    		if (ap instanceof Product) {
    			itemOrProduct = new Text("Product");
    			vbox2 = new VBox(itemOrProduct);
    		}
    		else {
    			Item item = (Item)ap;
    			itemOrProduct = new Text("A Custom Item");
    			type = new Text("Type: "+item.getType());
    			price = new Text("Price: "+ Double.toString(item.getPrice())+"$");
    			amount = new Text("Amount: " + Integer.toString(item.getAmount()));
    			color = new Text("Color: " + item.getColor());
    			vbox2 = new VBox(itemOrProduct, type, price,amount,color);
    		}
    		HBox hbox = new HBox(vbox1, vbox2);
    		hbox.setSpacing(15.0);
    		vbox1.setAlignment(Pos.CENTER);
    		vbox1.setPadding(new Insets(20));
    		vbox1.setSpacing(5);
    		vbox2.setAlignment(Pos.CENTER);
    		
    		hbox.setStyle("-fx-border-color: #E5E4E2; -fx-border-radius: 10px");
    		hbox.autosize();
    		
    		name.autosize();
    		name.setFont(Font.font(null,FontWeight.EXTRA_BOLD,FontPosture.REGULAR,20));
    		if(ap instanceof Item) {
	    		itemOrProduct.autosize();
	    		type.autosize();
	    		price.autosize();
	    		color.autosize();
	    		amount.autosize();
	    		
	    		itemOrProduct.setFont(Font.font(null,FontWeight.BOLD,FontPosture.REGULAR,15));
	    		type.setFont(Font.font(null,FontWeight.BOLD,FontPosture.REGULAR,15));
	    		price.setFont(Font.font(null,FontWeight.BOLD,FontPosture.REGULAR,15));
	    		color.setFont(Font.font(null,FontWeight.BOLD,FontPosture.REGULAR,15));
	    		amount.setFont(Font.font(null,FontWeight.BOLD,FontPosture.REGULAR,15));
    		}
    		grid.add(hbox, 0, i++);
    	}
		grid.setHgap(10);
		grid.setVgap(10);
    }
}
