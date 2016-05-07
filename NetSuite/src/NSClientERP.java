/**
 * Copyright @ NetSuite Inc. 1999-2015. All rights reserved.
 *
 * @author Jan Arendtsz
 * @author Aish Shukla
 * @author Terry Chan
 * @author Scott Liu
 * @author Andrej Hank
 *
 * @version $Revision: 2015.1.1
 * $Date: 04/08/2015
 */


import com.netsuite.webservices.lists.accounting_2015_1.InventoryItem;
import com.netsuite.webservices.lists.accounting_2015_1.ItemSearch;
import com.netsuite.webservices.lists.accounting_2015_1.Price;
import com.netsuite.webservices.lists.accounting_2015_1.PriceList;
import com.netsuite.webservices.lists.accounting_2015_1.Pricing;
import com.netsuite.webservices.lists.accounting_2015_1.PricingMatrix;
import com.netsuite.webservices.lists.accounting_2015_1.types.ItemCostingMethod;
import com.netsuite.webservices.lists.accounting_2015_1.types.ItemType;
import com.netsuite.webservices.lists.relationships_2015_1.Customer;
import com.netsuite.webservices.lists.relationships_2015_1.CustomerSearch;
import com.netsuite.webservices.platform.common_2015_1.CustomerSearchBasic;
import com.netsuite.webservices.platform.common_2015_1.ItemSearchBasic;
import com.netsuite.webservices.platform.common_2015_1.TransactionSearchBasic;
import com.netsuite.webservices.platform.core_2015_1.DataCenterUrls;
import com.netsuite.webservices.platform.core_2015_1.InitializeRecord;
import com.netsuite.webservices.platform.core_2015_1.InitializeRef;
import com.netsuite.webservices.platform.core_2015_1.Passport;
import com.netsuite.webservices.platform.core_2015_1.Record;
import com.netsuite.webservices.platform.core_2015_1.RecordList;
import com.netsuite.webservices.platform.core_2015_1.RecordRef;
import com.netsuite.webservices.platform.core_2015_1.SearchEnumMultiSelectField;
import com.netsuite.webservices.platform.core_2015_1.SearchMultiSelectField;
import com.netsuite.webservices.platform.core_2015_1.SearchResult;
import com.netsuite.webservices.platform.core_2015_1.SearchStringField;
import com.netsuite.webservices.platform.core_2015_1.Status;
import com.netsuite.webservices.platform.core_2015_1.types.InitializeRefType;
import com.netsuite.webservices.platform.core_2015_1.types.InitializeType;
import com.netsuite.webservices.platform.core_2015_1.types.RecordType;
import com.netsuite.webservices.platform.core_2015_1.types.SearchEnumMultiSelectFieldOperator;
import com.netsuite.webservices.platform.core_2015_1.types.SearchMultiSelectFieldOperator;
import com.netsuite.webservices.platform.core_2015_1.types.SearchStringFieldOperator;
import com.netsuite.webservices.platform.faults_2015_1.ExceededRecordCountFault;
import com.netsuite.webservices.platform.faults_2015_1.ExceededUsageLimitFault;
import com.netsuite.webservices.platform.faults_2015_1.InsufficientPermissionFault;
import com.netsuite.webservices.platform.faults_2015_1.InvalidSessionFault;
import com.netsuite.webservices.platform.faults_2015_1.UnexpectedErrorFault;
import com.netsuite.webservices.platform.messages_2015_1.Preferences;
import com.netsuite.webservices.platform.messages_2015_1.ReadResponse;
import com.netsuite.webservices.platform.messages_2015_1.ReadResponseList;
import com.netsuite.webservices.platform.messages_2015_1.SearchPreferences;
import com.netsuite.webservices.platform.messages_2015_1.WriteResponse;
import com.netsuite.webservices.platform_2015_1.NetSuiteBindingStub;
import com.netsuite.webservices.platform_2015_1.NetSuitePortType;
import com.netsuite.webservices.platform_2015_1.NetSuiteServiceLocator;
import com.netsuite.webservices.transactions.sales_2015_1.Estimate;
import com.netsuite.webservices.transactions.sales_2015_1.EstimateItem;
import com.netsuite.webservices.transactions.sales_2015_1.EstimateItemList;
import com.netsuite.webservices.transactions.sales_2015_1.SalesOrder;
import com.netsuite.webservices.transactions.sales_2015_1.SalesOrderItem;
import com.netsuite.webservices.transactions.sales_2015_1.SalesOrderItemList;
import com.netsuite.webservices.transactions.sales_2015_1.TransactionSearch;
import com.netsuite.webservices.transactions.sales_2015_1.types.SalesOrderOrderStatus;
import com.netsuite.webservices.transactions.sales_2015_1.types.TransactionType;
import org.apache.axis.message.SOAPHeaderElement;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;

/**
 * Fully functional, command-line driven application that illustrates how to
 * connect to the NetSuite web services and invoke operations. This application
 * uses the Customer record as an example across all operations.
 *
 * Please see the READEME on how to compile and run. Note that the
 * nsclient.properties file must exist in the installed root directory for
 * this application to run.
 *
 */

/**
 * @author tchan
 *
 */
public class NSClientERP extends Object {
	/**
	 * Proxy class that abstracts the communication with the NetSuite Web
	 * Services. All NetSuite operations are invoked as methods of this class.
	 */
	private NetSuitePortType _port;

	/**
	 * Utility for writing nd logging to console
	 */
	private Console _console;

	/**
	 * Abstraction of the external properties file that contains configuration
	 * parameters and sample data for fields
	 */
	private Properties _properties = null;

	/**
	 * Requested page size for search
	 */
	private int _pageSize;

	/**
	 * Search body fields only
	 */
	private Boolean _bodyFieldsOnly = Boolean.FALSE;

	private String PENDING_APPROVAL_STRING = "_salesOrderPendingApproval";

	private String PENDING_FULFILLMENT_STRING = "_salesOrderPendingFulfillment";

	private String PRICE_CURRENCY_INTERNAL_ID = "1";

	private String BASE_PRICE_LEVEL_INTERNAL_ID = "1";

	/**
	 * Constructor
	 *
	 * @throws ServiceException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public NSClientERP() throws ServiceException, IOException {
		// Setting pageSize to 20 records per page
		_pageSize = 20;
		// Instantiate console logger
		_console = new Console("info");
		// In order to use SSL forwarding for SOAP messages. Refer to FAQ for
		// details
		System.setProperty("axis.socketSecureFactory",
				"org.apache.axis.components.net.SunFakeTrustSocketFactory");

		// Reference to properties file that contains configuration data as
		// well as sample data. This file is named nsclienterp.properties and
		// is located in the root directory of this installation.
		_properties = new Properties();
		_properties.load(new FileInputStream("nsclient.properties"));

		// Locate the NetSuite web service.
		NetSuiteServiceLocator service = new DataCenterAwareNetSuiteServiceLocator(_properties.getProperty("login.acct"));

		// Enable client cookie management. This is required.
		service.setMaintainSession(true);

		// Get the service port (to the correct datacenter)
		_port = service.getNetSuitePort(new URL(_properties.getProperty("ws.url")));

		// Setting client timeout to 2 hours for long running operations
		((NetSuiteBindingStub) _port).setTimeout(1000 * 60 * 60 * 2);
	}

	/**
	 * Main function that presents the user with the available options and
	 * invokes the methods that encasulate the methods calls to the web services
	 * operations.
	 *
	 * @param args -
	 *            array of command line arguments
	 * @throws SOAPException
	 */
	public static void main(String args[]) throws SOAPException {
		NSClientERP ns = null;

		// Instantiate the NetSuite web services
		try {
			ns = new NSClientERP();
		} catch (ServiceException ex) {
			System.out
					.println("\n\n[Error]: Error in locating the NetSuite web services. "
							+ ex.getMessage());
		} catch (FileNotFoundException ex) {
			System.out
					.println("\n\n[Error]: Cannot find nsclienterp.properties file. Please ensure that "
							+ "this file is in the root directory of this application. ");
		} catch (IOException ex) {
			System.out.println("\n\n[Error]: An IO error has occured. "
					+ ex.getMessage());
		}
		// Process command line arguments
		// if ( !ns.processCmdArgs( args ) )
		// return;

		// Iterate through command options
		while (true) {
			try {
				int myChoice = 0;
				String strMyChoice = null;
				ns._console.writeLn("\nPlease make a selection:");
				ns._console.writeLn("  1) Add an inventory item");
				ns._console.writeLn("  2) Search inventory items by name");
				ns._console.writeLn("  3) Add a sales order");
				ns._console.writeLn("  4) Search sales orders by order status");
				ns._console.writeLn("  5) Search sales orders by customer entity ID");
				ns._console.writeLn("  6) Add a quote/estimate");
				ns._console.writeLn("  7) Create a new sales order based on an existing quote/estimate");
				ns._console.writeLn("  Q) Quit");
				ns._console.write("\nSelection: ");

				// Get user input
				strMyChoice = ns._console.readLn();

				// Process the user response
				ns._console.writeLn("");
				if ("Q".equals(strMyChoice.toUpperCase())) {
					ns._console.write("\nPress any key to quit ... ");
					String response = ns._console.readLn();
					break;
				} else {
					myChoice = Integer.parseInt(strMyChoice);
					// Setting preferences for the web services client
					ns.setPreferences();
					switch (myChoice) {
						case 1:
							ns.addInventoryItem();
							break;
						case 2:
							ns.searchInventoryItemByName();
							break;
						case 3:
							ns.addSalesOrder();
							break;
						case 4:
							ns.searchSalesOrderByStatus();
							break;
						case 5:
							ns.searchSalesOrderByEntityID();
							break;
						case 6:
							ns.addQuote();
							break;
						case 7:
							ns.transfromEstimateToSO();
							break;
					}
				}
			} catch (NumberFormatException ex) {
				ns._console
						.info("\nInvalid choice. Please select once of the following options.");
			} catch (ExceededRecordCountFault ex) {
				ns._console
						.fault("\nExceeded the maximum allowed number of records. "
								+ ex.getMessage());
				ns._console.info("   [Fault Code]: " + ex.getFaultCode());
			} catch (ExceededUsageLimitFault ex) {
				ns._console.fault("\nExceeded rat limit. " + ex.getMessage());
				ns._console.info("   [Fault Code]: " + ex.getFaultCode());
			} catch (InsufficientPermissionFault ex) {
				ns._console
						.fault("\nYou do not have sufficent permission for this request. "
								+ ex.getMessage());
				ns._console.info("   [Fault Code]: " + ex.getFaultCode());
			} catch (InvalidSessionFault ex) {
				ns._console.fault("\nInvalid Session. " + ex.getMessage());
				ns._console.info("   [Fault Code]: " + ex.getFaultCode());
			} catch (SOAPFaultException ex) {
				ns._console
						.fault("There was an error processing this request. "
								+ ex.getMessage());
				ns._console.info("   [Fault Code]: " + ex.getFaultCode());
				ns._console.info("   [Fault String]: " + ex.getFaultString());
				ns._console.info("   [Fault Actor]: " + ex.getFaultActor());
				ns._console.info("   [Fault Detail]: " + ex.getDetail());
			} catch (RemoteException ex) {
				ns._console.fault("\nRemoteException: " + ex.getMessage());
			} finally {
			}
		}
	}

	/**********************************************************************************
	 * This method creates a SalesOrder record by referencing an existing
	 * estimate record in the system.  This utilizes the WS "initialize" method.
	 *
	 * This process is generally a 2-step process.  Step 1 is calling initialize by
	 * referencing an existing record (in this case an estimate) to obtain a copy of the
	 * initialized record (in this case a sales order) with all fields pre-populated.  At this
	 * point the sales order has not been committed to the NetSuite database yet. Step 2
	 * is calling the add method to save the sales order record.  Note that the
	 * initialize-add tandem should be use in conjunction with the WS preference
	 * "ignoreReadOnlyFields".
	 *
	 * Please refer to InitializeType and InitializeRefType for other uses cases
	 * of the initialize method.
	 *
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	public void transfromEstimateToSO() throws RemoteException,
	ExceededUsageLimitFault, UnexpectedErrorFault, InvalidSessionFault,
	ExceededRecordCountFault
	{
		InitializeRecord initRec = new InitializeRecord();
		boolean estIDProvided = false;
		while(!estIDProvided)
		{
			_console.writeLn("Please enter the internal ID of the estimate/quote record on which the new sales order is based");
			String estID = _console.readLn();
			if(!estID.equalsIgnoreCase(""))
			{
				InitializeRef initRef = new InitializeRef();
				initRef.setInternalId(estID);
				initRef.setType(InitializeRefType.estimate);

				initRec.setType(InitializeType.salesOrder);
				initRec.setReference(initRef);

				estIDProvided = true;
			}
		}

		Record addSO = getInitializedRecord(initRec);
		if(addSO != null)
		{
			addInitializedRecord(addSO);
		}

	}

	/*******************************************************
	 * Calling the initialize method to obtain a copy of the
	 * initialized record with all the fields pre-populated.
	 *
	 * @param _initRec
	 * @return
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	private Record getInitializedRecord(InitializeRecord _initRec)throws RemoteException,
	ExceededUsageLimitFault, UnexpectedErrorFault, InvalidSessionFault,
	ExceededRecordCountFault
	{
		if(_initRec.getType()!=null && _initRec.getReference()!=null)
		{
			ReadResponse initRes = _port.initialize(_initRec);
			if(initRes.getStatus().isIsSuccess())
			{
				return initRes.getRecord();
			}
			else
			{
				_console.error(getStatusDetails(initRes.getStatus()));
				return null;
			}
		}
		else
		{
			_console.error("InitializeRecord is missing either the type or reference");
			return null;
		}
	}

	/**************************************************************
	 * Committing the initialized record to the NetSuite account
	 *
	 * @param rec
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	private void addInitializedRecord(Record rec)throws RemoteException,
	ExceededUsageLimitFault, UnexpectedErrorFault, InvalidSessionFault,
	ExceededRecordCountFault
	{
		if(rec != null)
		{
			WriteResponse res = _port.add(rec);
			if(res.getStatus().isIsSuccess())
			{
				_console.writeLn("The record was added successfully.");
				RecordRef ref = (RecordRef)(res.getBaseRef());
				_console.writeLn("Internal ID: " + ref.getInternalId());
				_console.writeLn("Record Type: " + ref.getType());
			}
			else
			{
				_console.error(getStatusDetails(res.getStatus()));
			}
		}
		else
		{
			_console.error("Error: null was given to add when a Record is expected");
		}
	}

	/***********************************************************************
	 * Adding an Estimate record.  This works similarly to the addSalesOrder
	 * method in this application.
	 *
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	public void addQuote() throws RemoteException,
			ExceededUsageLimitFault, UnexpectedErrorFault, InvalidSessionFault,
			ExceededRecordCountFault {

		Estimate est = new Estimate();
		// Set customer entity

		_console.writeLn("\nPlease enter the following customer information. "
				+ "Note that some fields have already been populated. ");
		_console.write("  Customer entity name: ");

		CustomerSearch custSearch = new CustomerSearch();
		SearchStringField customerEntityID = new SearchStringField();
		customerEntityID.setOperator(SearchStringFieldOperator.is);
		customerEntityID.setSearchValue(_console.readLn());

		CustomerSearchBasic custBasic = new CustomerSearchBasic();
		custBasic.setEntityId(customerEntityID);

		custSearch.setBasic(custBasic);

		// Search for the customer entity
		SearchResult res = _port.search(custSearch);

		if (res.getStatus().isIsSuccess()) {
			if (res.getRecordList().getRecord() != null
					&& res.getRecordList().getRecord().length == 1) {
				RecordRef customer = new RecordRef();
				customer.setType(RecordType.customer);
				String entID = ((Customer) (res.getRecordList().getRecord(0)))
						.getEntityId();
				customer.setName(entID);
				customer.setInternalId(((Customer) (res.getRecordList()
						.getRecord(0))).getInternalId());
				est.setEntity(customer);

				// set the transaction date
				est.setTranDate(Calendar.getInstance());

				// Enter the internal ID for inventory items to be added to the SO
				_console
						.writeLn("\nPlease enter the internal ID values for INVENTORY ITEMS seperated by commas (do not enter discount or subtotal items).");
				String itemKeys = _console.readLn();
				String[] itemKeysArray = itemKeys.split(",");

				ReadResponse[] readRes = getInventoryItemList(itemKeysArray);

				// Determine the number of valid inventory item internal IDs entered
				Vector vec = new Vector();
				for (int a = 0; a < readRes.length; a++) {
					if (readRes[a].getRecord() != null) {
						vec.add(readRes[a].getRecord());
					}
				}
				EstimateItem[] estItemArray = new EstimateItem[vec
						.size()];

				// Create the correct sales order items and populate the
				// quantity
				for (int i = 0; i < vec.size(); i++) {
					if (vec.get(i) instanceof InventoryItem) {
						RecordRef item = new RecordRef();
						item.setType(RecordType.inventoryItem);
						item.setInternalId(((InventoryItem) (vec.get(i)))
								.getInternalId());
						estItemArray[i] = new EstimateItem();
						estItemArray[i].setItem(item);

						_console.writeLn("\nPlease enter quantity for "
								+ ((InventoryItem) (vec.get(i))).getItemId());
						Double quantity = Double.valueOf(_console.readLn());
						estItemArray[i].setQuantity(quantity);
					}
				}
				EstimateItemList estimateItemList = new EstimateItemList(
						estItemArray, true);
				est.setItemList(estimateItemList);

				WriteResponse writeRes = _port.add(est);
				if (writeRes.getStatus().isIsSuccess()) {
					_console.writeLn("\nQuote created successfully.  Internal ID is " + ((RecordRef)(writeRes.getBaseRef())).getInternalId() + ".");
				} else {
					_console.error(getStatusDetails(writeRes.getStatus()));
				}
			} else {
				_console
						.writeLn("\nEstimate is not created because 0 or more than 1 customer records found for the entityID given");
			}
		} else {
			_console.error(getStatusDetails(res.getStatus()));
		}

	}

	/**
	 * Prepare loing passport for request level user login.
	 *
	 *
	 */
	public Passport prepareLoginPassport() {

			// Populate Passport object with all login information
			Passport passport = new Passport();
			RecordRef role = new RecordRef();

			// Determine whether to get login information from config
			// file or prompt for it
			if ("true".equals(_properties.getProperty("promptForLogin"))) {
				_console.writeLn("\nPlease enter your login information: ");
				System.out.print("  E-mail: ");
				passport.setEmail(_console.readLn());
				System.out.print("  Password: ");
				passport.setPassword(_console.readLn());
				System.out
						.print("  Role internal ID/nsKey (press enter for default administrator role): ");
				role.setInternalId(_console.readLn());
				passport.setRole(role);
				System.out.print("  Account: ");
				passport.setAccount(_console.readLn());
			} else {
				passport.setEmail(_properties.getProperty("login.email"));
				passport.setPassword(_properties.getProperty("login.password"));
				role.setInternalId(_properties.getProperty("login.roleNSkey"));
				passport.setRole(role);
				passport.setAccount(_properties.getProperty("login.acct"));
			}

			return passport;
	}


	/**
	 * Adds an InventoryItem. Asks the user for the Item Name/Number and the
	 * costing method (Average, FIFO or LIFO).
	 *
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	public void addInventoryItem() throws RemoteException,
			ExceededUsageLimitFault, UnexpectedErrorFault, InvalidSessionFault,
			ExceededRecordCountFault {

		InventoryItem item = new InventoryItem();

		_console.writeLn("Please enter the Item Name/Number: ");
		String itemName = _console.readLn();
		item.setItemId(itemName);

		boolean needValidInput = true;
		while (needValidInput) {
			_console.writeLn("\nEnter the costing method (optional).  Enter:");
			_console.writeLn("\n1 for AVERAGE, 2 for FIFO, 3 for LIFO");

			String sCostingMethod = _console.readLn();
			if (sCostingMethod == null || sCostingMethod.trim().equals(""))
				sCostingMethod = "1"; // default
			int intCostingMethod = Integer.parseInt(sCostingMethod);
			switch (intCostingMethod) {
			case 1:
				item.setCostingMethod(ItemCostingMethod._average);
				needValidInput = false;
				break;

			case 2:

				item.setCostingMethod(ItemCostingMethod._fifo);
				needValidInput = false;
				break;

			case 3:
				item.setCostingMethod(ItemCostingMethod._lifo);
				needValidInput = false;
				break;
			}
		}

		_console.writeLn("Please enter the external ID.  Note: the external ID is generally used for external system data synchronization.  It is OPTIONAL but must be UNIQUE.");
		String extID = _console.readLn();
		if(!extID.equalsIgnoreCase(""))
		{
			item.setExternalId(extID);
		}

		createPricingMatrix(item);

		// Because there is a little bug in item price matrix, temporarily switch ignoreReadOnlyFields preference to false.
		// Otherwise, the base price is not saved.
		// Remove once the issue is fixed.
		setIgnoreReadOnlyFieldsPreference(false);
		WriteResponse writeRes = _port.add(item);
		setIgnoreReadOnlyFieldsPreference(true);
		if (writeRes.getStatus().isIsSuccess()) {
			_console.writeLn("\nThe item " + itemName + " has been added successfully.");
			RecordRef _ref = (RecordRef)(writeRes.getBaseRef());
			_console.writeLn("Internal ID: " + _ref.getInternalId());
			_console.writeLn("External ID: " + _ref.getExternalId());
		} else {
			_console.error(getStatusDetails(writeRes.getStatus()));
		}

	}

	/**
	 * This method takes an InventoryItem and sets its pricing matrix. It only
	 * sets the base price at quantity 0. If an invalid price was entered (a
	 * non-numeric value), it does not set the pricing matrix.
	 *
	 * @param item
	 */
	private void createPricingMatrix(InventoryItem item) {
		_console.writeLn("\nPlease enter the base price, e.g. 25:");
		String priceString = _console.readLn();
		Price[] prices = new Price[1];
		prices[0] = new Price();
		try {
			prices[0].setValue(Double.valueOf(priceString));
			prices[0].setQuantity(0.0);

			PriceList priceList = new PriceList();
			priceList.setPrice(prices);

			Pricing[] pricing = new Pricing[1];
			pricing[0] = new Pricing();
			RecordRef currencyRef = new RecordRef();
			currencyRef.setInternalId(PRICE_CURRENCY_INTERNAL_ID);
			pricing[0].setCurrency(currencyRef);
			pricing[0].setPriceList(priceList);
			pricing[0].setDiscount(null);
			RecordRef priceLevel = new RecordRef();
			priceLevel.setInternalId(BASE_PRICE_LEVEL_INTERNAL_ID);
			priceLevel.setType(RecordType.priceLevel);
			pricing[0].setPriceLevel(priceLevel);

			PricingMatrix pricingMatrix = new PricingMatrix();
			pricingMatrix.setPricing(pricing);
			pricingMatrix.setReplaceAll(false);

			item.setPricingMatrix(pricingMatrix);
		} catch (NumberFormatException e) {
			_console
					.error("\nInvalid base price entered: "
							+ priceString
							+ ".  Proceed creating item without setting pricing matrix.");
		}
	}

	/**
	 * Searches for InventoryItem records based on a keyword entered by the
	 * user. All inventory items, including parent items, with the keyword found
	 * in their name field will be returned.
	 *
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	public void searchInventoryItemByName() throws RemoteException,
			ExceededUsageLimitFault, UnexpectedErrorFault, InvalidSessionFault,
			ExceededRecordCountFault {

		_console.writeLn("Enter a keyword of the item name to be searched");
		String keyWordString = _console.readLn();

		ItemSearch itemSearch = new ItemSearch();

		SearchStringField name = new SearchStringField();
		name.setOperator(SearchStringFieldOperator.contains);
		name.setSearchValue(keyWordString);

		SearchEnumMultiSelectField type = new SearchEnumMultiSelectField();
		String[] inv = new String[1];
		inv[0] = ItemType.__inventoryItem;
		type.setOperator(SearchEnumMultiSelectFieldOperator.anyOf);
		type.setSearchValue(inv);

		ItemSearchBasic itemBasic = new ItemSearchBasic();
		itemBasic.setType(type);
		itemBasic.setItemId(name);

		// itemSearch.setType(type);
		// itemSearch.setName(name);
		itemSearch.setBasic(itemBasic);

		SearchResult res = _port.search(itemSearch);
		if (res.getStatus().isIsSuccess()) {
			RecordList recordList;
			_console.writeLn("\nNumber of items found with the keyword "
					+ keyWordString + ": " + res.getTotalRecords());
			if (res.getTotalRecords().intValue() > 0) {
				_console.writeLn("\nPage size: " + res.getPageSize());

				for (int i = 1; res.getTotalPages() != null && i <= res.getTotalPages().intValue(); i++) {
					recordList = res.getRecordList();
					_console.writeLn("Page " + res.getPageIndex());
					_console.writeLn("------");

					for (int j = 0; j < recordList.getRecord().length; j++) {
						if (recordList.getRecord(j) instanceof InventoryItem) {
							InventoryItem item = (InventoryItem) (recordList
									.getRecord(j));
							/*
							 * _console.writeLn(" Name: " +
							 * item.getDisplayName()); _console.writeLn("
							 * Description: " + item.getPurchaseDescription());
							 * _console.writeLn(" Price: " + item.getCost() +
							 * "\n");
							 */
							displayInventoryItemFields(item);
						}
					}

					if (res.getPageIndex().intValue() < res.getTotalPages()
							.intValue()) {
						res = searchMore(res);
					}
				}
			}
		} else {
			_console.error(getStatusDetails(res.getStatus()));
		}

	}

	private SearchResult searchMore(SearchResult res)  throws RemoteException,
		ExceededUsageLimitFault, UnexpectedErrorFault, InvalidSessionFault,
		ExceededRecordCountFault
	{
		return _port.searchMoreWithId(res.getSearchId(), res.getPageIndex()+1);
	}

	/**
	 * A helper method that takes an InventoryItem and prints out its Display
	 * Name, Item ID and Cost fields.
	 *
	 * @param item
	 */
	private void displayInventoryItemFields(InventoryItem item) {
		if (item != null) {
			if (item.getDisplayName() != null) {
				_console.writeLn("     Display Name:                "
						+ item.getDisplayName());
			} else {
				_console.writeLn("     Display Name:                none");
			}

			if (item.getItemId() != null) {
				_console.writeLn("     Item ID:                     "
						+ item.getItemId());
			} else {
				_console.writeLn("     Item ID:                     none");
			}

			if (item.getPurchaseDescription() != null) {
				_console.writeLn("     Purchase Description:        "
						+ item.getPurchaseDescription());
			} else {
				_console.writeLn("     Purchase Description:        none");
			}

			if (item.getCost() != null) {
				_console.writeLn("     Cost:                        "
						+ item.getCost() + "\n");
			} else {
				_console.writeLn("     Cost:                        none\n");
			}
		}
	}

	/***************************************************************************
	 * Search for SalesOrder based on status of either pendingApproval or
	 * pendingFulfillment.
	 *
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	public void searchSalesOrderByStatus() throws RemoteException,
			ExceededUsageLimitFault, UnexpectedErrorFault, InvalidSessionFault,
			ExceededRecordCountFault {

		boolean needValidInput = true;
		while (needValidInput) {
			_console
					.writeLn("\nEnter status of sales orders to search.  Enter:");
			_console
					.writeLn("\n1 for PENDING APPROVAL, or 2 for PENDING FULFILLMENT");
			String strMyChoice = _console.readLn();
			int myChoice = Integer.parseInt(strMyChoice);
			switch (myChoice) {
			case 1:
				// searchSOByStatusAllValues(SalesOrderOrderStatus.__pendingApproval);
				searchSOByStatusAllValues(PENDING_APPROVAL_STRING);
				needValidInput = false;
				break;
			case 2:
				// searchSOByStatusAllValues(SalesOrderOrderStatus.__pendingFulfillment);
				searchSOByStatusAllValues(PENDING_FULFILLMENT_STRING);
				needValidInput = false;
				break;
			}
		}
	}

	/**
	 * Helper method that searches for SalesOrder records with status of the
	 * given String parameter value.
	 *
	 * @param parmStatus
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	private void searchSOByStatusAllValues(String parmStatus)
			throws RemoteException, ExceededUsageLimitFault,
			UnexpectedErrorFault, InvalidSessionFault, ExceededRecordCountFault {
		TransactionSearch xactionSearch = new TransactionSearch();

		SearchEnumMultiSelectField soField = new SearchEnumMultiSelectField();
		soField.setOperator(SearchEnumMultiSelectFieldOperator.anyOf);
		String[] searchValueStringArray = new String[1];
		searchValueStringArray[0] = TransactionType.__salesOrder;
		soField.setSearchValue(searchValueStringArray);

		SearchEnumMultiSelectField statusField = new SearchEnumMultiSelectField();
		statusField.setOperator(SearchEnumMultiSelectFieldOperator.anyOf);
		String[] searchStatusStringArray = new String[1];
		searchStatusStringArray[0] = parmStatus;
		statusField.setSearchValue(searchStatusStringArray);

		TransactionSearchBasic xactionBasic = new TransactionSearchBasic();
		xactionBasic.setType(soField);
		xactionBasic.setStatus(statusField);

		// xactionSearch.setType(soField);
		// xactionSearch.setStatus(statusField);

		xactionSearch.setBasic(xactionBasic);

		SearchResult res = _port.search(xactionSearch);

		if (res.getStatus().isIsSuccess()) {
			RecordList recordList;

			if (res.getTotalRecords() == 0) {
				_console.writeLn("\nNo record found.");
				return;
			}

			_console.writeLn("\nNumber of records found: "
					+ res.getTotalRecords());
			_console.writeLn("\nPage size: " + res.getPageSize());

			for (int i = 1; res.getTotalPages() != null && i <= res.getTotalPages().intValue(); i++) {
				recordList = res.getRecordList();
				_console.writeLn("Page " + res.getPageIndex());
				_console.writeLn("------");

				for (int j = 0; j < recordList.getRecord().length; j++) {
					if (recordList.getRecord(j) instanceof SalesOrder) {
						SalesOrder so = (SalesOrder) (recordList.getRecord(j));
						_console.writeLn("\n     Order Number/TranID: " + so.getTranId());
						_console.writeLn("     Internal ID: " + so.getInternalId());
						_console.writeLn("     Customer entity ID: "
								+ so.getEntity().getName());
						_console.writeLn("     Number of items: "
							+ (so.getItemList() != null && so.getItemList().getItem() != null? String.valueOf(so.getItemList().getItem().length) : ""));
						processSalesOrderItemList(so);

					}
				}

				if (res.getPageIndex().intValue() < res.getTotalPages()
						.intValue()) {
					res = searchMore(res);
				}
			}

		} else {
			_console.error(getStatusDetails(res.getStatus()));
		}
	}

	/**
	 * Searches for SalesOrder records for a given customer. The customer's
	 * entity ID needs to be entered by the user. This method mainly performs
	 * the customer search, then delegates the actual transaction search to
	 * searchSOByEntityID.
	 *
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	public void searchSalesOrderByEntityID() throws RemoteException,
			ExceededUsageLimitFault, UnexpectedErrorFault, InvalidSessionFault,
			ExceededRecordCountFault {

		_console.writeLn("\nEnter the entity ID for a customer:");
		String entityIDString = _console.readLn();

		CustomerSearch custSearch = new CustomerSearch();
		SearchStringField customerEntityID = new SearchStringField();
		customerEntityID.setOperator(SearchStringFieldOperator.contains);
		customerEntityID.setSearchValue(entityIDString);

		CustomerSearchBasic custBasic = new CustomerSearchBasic();
		custBasic.setEntityId(customerEntityID);

		// custSearch.setEntityId(customerEntityID);
		custSearch.setBasic(custBasic);

		SearchResult res = _port.search(custSearch);

		if (res.getStatus().isIsSuccess()) {
			if (res.getTotalRecords().intValue() > 0) {
				RecordList recordList;
				for (int i = 0; res.getTotalPages() != null && i <= res.getTotalPages().intValue() - 1; i++) {
					recordList = res.getRecordList();
					for (int j = 0; j <= recordList.getRecord().length - 1; j++) {
						Customer cust = (Customer) recordList.getRecord(j);
						_console.writeLn("\n\nSales orders for customer "
								+ cust.getEntityId() + ":");
						searchSOByEntityID(cust);
					}

					if (res.getPageIndex().intValue() < res.getTotalPages()
							.intValue()) {
						res = searchMore(res);
					}
				}
			} else {
				_console.writeLn("\nNo customer records found with entityID "
						+ entityIDString);
			}
		} else {
			_console.error(getStatusDetails(res.getStatus()));
		}
	}

	/**
	 * This method searches for all the SalesOrders associated with the Customer
	 * record parameter.
	 *
	 * @param customerRecord
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	private void searchSOByEntityID(Customer customerRecord)
			throws RemoteException, ExceededUsageLimitFault,
			UnexpectedErrorFault, InvalidSessionFault, ExceededRecordCountFault {

		TransactionSearch xactionSearch = new TransactionSearch();

		// Set SO customer search filter
		SearchMultiSelectField entity = new SearchMultiSelectField();
		entity.setOperator(SearchMultiSelectFieldOperator.anyOf);
		RecordRef custRecordRef = new RecordRef();
		custRecordRef.setInternalId(customerRecord.getInternalId());
		custRecordRef.setType(RecordType.customer);
		RecordRef[] custRecordRefArray = new RecordRef[1];
		custRecordRefArray[0] = custRecordRef;
		entity.setSearchValue(custRecordRefArray);

		SearchEnumMultiSelectField searchSalesOrderField = new SearchEnumMultiSelectField();
		searchSalesOrderField
				.setOperator(SearchEnumMultiSelectFieldOperator.anyOf);
		String[] soStringArray = new String[1];
		soStringArray[0] = TransactionType.__salesOrder;
		searchSalesOrderField.setSearchValue(soStringArray);

		TransactionSearchBasic xactionBasic = new TransactionSearchBasic();
		xactionBasic.setType(searchSalesOrderField);
		xactionBasic.setEntity(entity);

		xactionSearch.setBasic(xactionBasic);

		SearchResult res = _port.search(xactionSearch);
		if (res.getStatus().isIsSuccess()) {
			RecordList recordList;
			for (int i = 1; res.getTotalPages() != null && i <= res.getTotalPages().intValue(); i++)
			{
				if(res.getRecordList() != null && res.getRecordList().getRecord() != null)
				{
					recordList = res.getRecordList();

					for (int j = 0; j < recordList.getRecord().length; j++) {
						if (recordList.getRecord(j) instanceof SalesOrder) {
							SalesOrder so = (SalesOrder) (recordList.getRecord(j));
							processSalesOrderItemList(so);
						}
					}
				}
				if (res.getPageIndex().intValue() < res.getTotalPages()
						.intValue()) {
					res = searchMore(res);
				}
			}
		} else {
			_console.error(getStatusDetails(res.getStatus()));
		}
	}

	/**
	 * This method prints out the line items, quantity, and total amount for a
	 * SalesOrder parameter.
	 *
	 * @param so
	 */
	private void processSalesOrderItemList(SalesOrder so) {
		_console.writeLn("\nTranID: " + so.getTranId());
		_console.writeLn("Internal ID: " + so.getInternalId());
		_console.writeLn("        Sales Order line item list:");
		if (so.getItemList() != null) {
			for (int i = 0; i < so.getItemList().getItem().length; i++) {
				if (so.getItemList().getItem(i) instanceof SalesOrderItem) {
					SalesOrderItem item = (SalesOrderItem) (so.getItemList()
							.getItem(i));
					_console.writeLn("        " + item.getItem().getName()
							+ ", quantity: " + (item.getQuantity() != null? String.valueOf(item.getQuantity()) : ""));
				}
			}
			_console.writeLn("                                    "
					+ "Total amount: " + so.getTotal());
		}
	}

	/**
	 * Adds a SalesOrder record into NetSuite using the add() operation. Users
	 * are required to enter the entityID for the customer, as well as the
	 * internal ID for the line items to be added to the sales order.
	 *
	 * Note: do NOT add internal ID's for non-inventory items such as service items,
	 * discounts and subtotals.
	 *
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	public void addSalesOrder() throws RemoteException,
			ExceededUsageLimitFault, UnexpectedErrorFault, InvalidSessionFault,
			ExceededRecordCountFault {

		SalesOrder so = new SalesOrder();
		// Set customer entity

		_console.writeLn("\nPlease enter the following customer information. "
				+ "Note that some fields have already been populated. ");
		_console.write("  Customer entity name: ");

		CustomerSearch custSearch = new CustomerSearch();
		SearchStringField customerEntityID = new SearchStringField();
		customerEntityID.setOperator(SearchStringFieldOperator.is);
		customerEntityID.setSearchValue(_console.readLn());

		CustomerSearchBasic custBasic = new CustomerSearchBasic();
		custBasic.setEntityId(customerEntityID);

		// custSearch.setEntityId(customerEntityID);

		custSearch.setBasic(custBasic);

		// Search for the customer entity
		SearchResult res = _port.search(custSearch);

		if (res.getStatus().isIsSuccess()) {
			if (res.getRecordList().getRecord() != null
					&& res.getRecordList().getRecord().length == 1) {
				RecordRef customer = new RecordRef();
				customer.setType(RecordType.customer);
				String entID = ((Customer) (res.getRecordList().getRecord(0)))
						.getEntityId();
				customer.setName(entID);
				customer.setInternalId(((Customer) (res.getRecordList()
						.getRecord(0))).getInternalId());
				so.setEntity(customer);

				// set the transaction date and status
				so.setTranDate(Calendar.getInstance());
				so.setOrderStatus(SalesOrderOrderStatus._pendingApproval);

				_console.writeLn("\nPlease enter the external ID.  Note: the external ID is generally used for external system data synchronization.  It is OPTIONAL but must be UNIQUE.");
				String extID = _console.readLn();
				if(!extID.equalsIgnoreCase(""))
				{
					so.setExternalId(extID);
				}
				// Enter the internal ID for inventory items to be added to the SO
				_console
						.writeLn("\nPlease enter the internal ID values for INVENTORY ITEMS seperated by commas (do not enter discount or subtotal items).");
				String itemKeys = _console.readLn();
				String[] itemKeysArray = itemKeys.split(",");

				ReadResponse[] readRes = getInventoryItemList(itemKeysArray);

				// Determine the number of valid inventory item internal IDs entered
				Vector vec = new Vector();
				for (int a = 0; a < readRes.length; a++) {
					if (readRes[a].getRecord() != null) {
						vec.add(readRes[a].getRecord());
					}
				}
				SalesOrderItem[] salesOrderItemArray = new SalesOrderItem[vec
						.size()];

				// Create the correct sales order items and populate the
				// quantity
				for (int i = 0; i < vec.size(); i++) {
					if (vec.get(i) instanceof InventoryItem) {
						RecordRef item = new RecordRef();
						item.setType(RecordType.inventoryItem);
						item.setInternalId(((InventoryItem) (vec.get(i)))
								.getInternalId());
						salesOrderItemArray[i] = new SalesOrderItem();
						salesOrderItemArray[i].setItem(item);

						_console.writeLn("\nPlease enter quantity for "
								+ ((InventoryItem) (vec.get(i))).getItemId());
						Double quantity = Double.valueOf(_console.readLn());
						salesOrderItemArray[i].setQuantity(quantity);
					}
				}
				SalesOrderItemList salesOrderItemList = new SalesOrderItemList(
						salesOrderItemArray, true);
				so.setItemList(salesOrderItemList);

				WriteResponse writeRes = _port.add(so);
				if (writeRes.getStatus().isIsSuccess()) {
					_console.writeLn("\nSales order created successfully.");
					RecordRef _ref = (RecordRef)(writeRes.getBaseRef());
					_console.writeLn("Internal ID: " + _ref.getInternalId());
					_console.writeLn("External ID: " + _ref.getExternalId());
				} else {
					_console.error(getStatusDetails(writeRes.getStatus()));
				}
			} else {
				_console
						.writeLn("\nSales order is not created because 0 or more than 1 customer records found for the entityID given");
			}
		} else {
			_console.error(getStatusDetails(res.getStatus()));
		}

	}

	/**
	 * A helper method for adding a sales order. The parameter nsKeys is a
	 * String array of nsKeys entered by the user. This method returns the list
	 * of items found.
	 *
	 * @param nsKeys
	 * @return
	 * @throws RemoteException
	 * @throws ExceededUsageLimitFault
	 * @throws UnexpectedErrorFault
	 * @throws InvalidSessionFault
	 * @throws ExceededRecordCountFault
	 */
	private ReadResponse[] getInventoryItemList(String[] nsKeys)
			throws RemoteException, ExceededUsageLimitFault,
			UnexpectedErrorFault, InvalidSessionFault, ExceededRecordCountFault {
		// Build an array of RecordRef objects and invoke the getList()
		// operation to retrieve these records
		RecordRef[] recordRefs = new RecordRef[nsKeys.length];
		for (int i = 0; i < nsKeys.length; i++) {
			RecordRef recordRef = new RecordRef();
			recordRef.setInternalId(nsKeys[i]);
			recordRefs[i] = recordRef;
			recordRefs[i].setType(RecordType.inventoryItem);
		}

		ReadResponseList resList = _port.getList(recordRefs);
		return resList.getReadResponse();
	}


	/**
	 * Sets the Preferences and search preferences for an operation by adding
	 * the preferences to the SOAP header
	 *
	 * @throws SOAPException
	 */
	private void setPreferences() throws SOAPException {
		// Cast your login NetSuitePortType variable to a NetSuiteBindingStub
		NetSuiteBindingStub stub = (NetSuiteBindingStub) _port;

		// Clear the headers to make sure you know exactly what you are sending.
		// Headers do not overwrite when you are using Axis/Java
		stub.clearHeaders();

		//Create request level login user passport header
		SOAPHeaderElement userPassportHeader = new SOAPHeaderElement("urn:messages.platform.webservices.netsuite.com", "passport");
		userPassportHeader.setObjectValue(prepareLoginPassport());

		// Create a new SOAPHeaderElement, this is what the NetSuiteBindingStub
		// will accept
		// This is the same command for all preference elements, ie you might
		// substitute "useDefaults" for "searchPreferences"
		SOAPHeaderElement searchPrefHeader = new SOAPHeaderElement(
				"urn:messages.platform.webservices.netsuite.com",
				"searchPreferences");

		// Create your Actual SearchPreference Object, this contains the
		// elements you are allowed to set.
		// In this case it is PageSize (for pagination of searches) and
		// BodyFieldsOnly (reserved)
		SearchPreferences searchPrefs = new SearchPreferences();
		searchPrefs.setPageSize(new Integer(_pageSize));
		searchPrefs.setBodyFieldsOnly(_bodyFieldsOnly);

		// setObjectValue applies search preference object to the HeaderElement
		searchPrefHeader.setObjectValue(searchPrefs);

		//Create another SOAPHeaderElement to store the preference ignoreReadOnlyFields
		//This preference is used for the initialize method call.  See the
		// comments for the transformEstimateToSO() method for details.
		SOAPHeaderElement platformPrefHeader = new SOAPHeaderElement("urn:messages.platform.webservices.netsuite.com", "preferences");
		Preferences pref = new Preferences();
		pref.setIgnoreReadOnlyFields(Boolean.TRUE);
		platformPrefHeader.setObjectValue(pref);

		// setHeader applies the Header Element to the stub
		// Again, note that if you reuse your NetSuitePort object (vs logging in
		// before every request)
		// that headers are sticky, so if in doubt, call clearHeaders() first.
		stub.setHeader(userPassportHeader);
		stub.setHeader(searchPrefHeader);
		stub.setHeader(platformPrefHeader);
	}

	/**
	 * Quickly change ignoreReadOnlyFields preference
	 */
	private void setIgnoreReadOnlyFieldsPreference(boolean ignoreReadOnlyFields)
	{
		((Preferences)(((NetSuiteBindingStub) _port).getHeader("urn:messages.platform.webservices.netsuite.com", "preferences").getObjectValue())).setIgnoreReadOnlyFields(ignoreReadOnlyFields);
	}

	/**
	 * Processes the status object and prints the status details
	 *
	 * @param status -
	 *            the status object from a response
	 * @return string containing details about the status
	 */
	private String getStatusDetails(Status status) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < status.getStatusDetail().length; i++) {
			sb.append("[Code=" + status.getStatusDetail()[i].getCode() + "] "
					+ status.getStatusDetail()[i].getMessage() + "\n");
		}
		return sb.toString();
	}

	/**
	 * Since 12.2 endpoint accounts are located in multiple datacenters with different domain names.
	 * In order to use the correct one, wrap the Locator and get the correct domain first.
	 *
	 * See getDataCenterUrls WSDL method documentation in the Help Center.
	 */
	private static class DataCenterAwareNetSuiteServiceLocator extends NetSuiteServiceLocator
	{
		private String account;

		public DataCenterAwareNetSuiteServiceLocator(String account)
		{
			this.account = account;
		}

		@Override
		public NetSuitePortType getNetSuitePort(URL defaultWsDomainURL)
		{
			try
			{
				NetSuitePortType _port = super.getNetSuitePort(defaultWsDomainURL);
				// Get the webservices domain for your account
				DataCenterUrls urls = _port.getDataCenterUrls(account).getDataCenterUrls();
				String wsDomain = urls.getWebservicesDomain();

				// Return URL appropriate for the specific account
				return super.getNetSuitePort(new URL(wsDomain.concat(defaultWsDomainURL.getPath())));
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
