# Aerodrome for Jet
## Jet.com API integration library

[![License](https://img.shields.io/badge/license-Apache_2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

[![Authentication](https://img.shields.io/badge/Authentication-working-yellowgreen.svg)](https://github.com/SixArmDonkey/aerodrome-for-jet#quick-start-guide)
[![ProductAPI](https://img.shields.io/badge/Product%20API-working-yellowgreen.svg)](https://github.com/SixArmDonkey/aerodrome-for-jet#product-api)
[![BulkProductAPI](https://img.shields.io/badge/Bulk%20Product%20API-working-yellowgreen.svg)](https://github.com/SixArmDonkey/aerodrome-for-jet#bulk-product-upload-api)
[![OrdersAPI](https://img.shields.io/badge/Orders%20API-working-yellowgreen.svg)](https://github.com/SixArmDonkey/aerodrome-for-jet#1-check-for-orders)
[![ReturnsAPI](https://img.shields.io/badge/Returns%20API-working-yellowgreen.svg)](https://github.com/SixArmDonkey/aerodrome-for-jet#returns-api)
[![RefundssAPI](https://img.shields.io/badge/Refunds%20API-working-yellowgreen.svg)](https://github.com/SixArmDonkey/aerodrome-for-jet#refund-api)
[![TaxonomyAPI](https://img.shields.io/badge/Taxonomy%20API-working-yellowgreen.svg)](https://github.com/SixArmDonkey/aerodrome-for-jet#taxonomy-api)
[![SettlementAPI](https://img.shields.io/badge/Settlement%20API-working-yellowgreen.svg)](https://github.com/SixArmDonkey/aerodrome-for-jet#settlement-api)

---
### Don't want to code it yourself?  Try Aerodrome Pro! 

Aerodrome Professional is a complete graphical implementation of every feature on Jet.  
[Get it today!](http://www.buffalokiwi.com)


[![Aerodrome Professional](http://www.buffalokiwi.com/public/images/aerodrome-logo.png)](http://www.buffalokiwi.com)

---

The source is completely documented, check out the JavaDoc here: https://sixarmdonkey.github.io/aerodrome-for-jet/ .

Some of the documentation below is slightly out of date.

For questions, please feel free to email johnquinn3@gmail.com.

# Quick Start Guide

### 1: Create a JetConfig object 

At minimum, you need to add your merchant id, username, password

[JetConfig JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/DefaultJetConfig.html)

```java
JetConfig config = new DefaultJetConfig.Builder();
  .setMerchantId( "your merchant id" )
  .setUser( "your user id" )
  .setPass( "your password" )
  .build();
```

### 2: Create a shared Http Client to use 

[APIHttpClient JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/api/APIHttpClient.html)

```java
IAPIHttpClient client = new APIHttpClient.Builder().build();    
```

### 3: Authenticate with Jet 

Authentication is thread safe, and a single config object is designed to be 
shared between threads.  Aerodrome keeps track of your authentication status, 
and will automatically authenticate.  There is no need to manually call login().

[IJetAPIAuth JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/IJetAPIAuth.html)

The JetConfig object keeps track of the shared authentication data and is 
automatically updated.


# Product API

Follow the steps in the Quick Start Guide prior to using the Product API.
Jet API Authentication is required.


### 1: Initialize the product API

[JetAPIProduct JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/JetAPIProduct.html)

```java
IJetAPIProduct productApi = new JetAPIProduct( client, jetConfig );
```


### 2: Add a single product to Jet 

Each request and response is encapsulated in a unique object.
In this instance we will use an instance of ProductRec, which represents
a product.

First we create a product.  The minimum required properties are shown.


[ProductRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/ProductRec.html)

```java
  ProductRec prod = new ProductRec().Builder()
  .setMerchantSku( "Your unique local sku" )
  .setTitle( "Product title" )
  .setProductDescription( "Product description" )
  .setMultipackQuantity( 1 )
  .setMsrp( new Money( "44.99" ))
  .setPrice( new Money( "44.99" ))
  .setMainImageUrl( "https://www.example.com/image.jpg" )
  .setSwatchImageUrl( "https://www.example.com/thumbnail.jpg" )
  .setBrand( "Manufacturer Name" )
  //..Your fulfillment node id's are unique to your account, and are found in your
  //  Jet Partner Portal 
  .setfNodeInventory( new FNodeInventoryRec( "Fulfillment Node Id", 1 ))
  .setProductCode(new ProductCodeRec( "111111111111", ProductCodeType.UPC ))
  .build();
```

The next step is to send this product to Jet.  Please note, adding and editing
sku's are done with the same operation (addProduct)

addProduct() will upload: sku, image, price and inventory in a single call.
You can also call each of those individual methods separately.

```java
productApi.addProduct( prod );
```

#### Send a variation group for an existing sku

The variation request is used to create a variation-type relationship between 
several SKUs. To use this request, one must have already uploaded all the 
SKUs in question ; they should then choose one "parent" SKU and make the 
variation request to that SKU, adding as "children" any SKUs they want 
considered part of the relationship.
 
To denote the particular variation refinements, one must have uploaded one 
or more attributes in the product call for all the SKUs in question; finally, 
they are expected to list these attributes in the variation request.

[Jet Taxonomy Spreadsheet](https://www.dropbox.com/s/wh2ud1q2ujucdt2/Jet_Taxonomy_8.28.2015.xlsx?dl=0)

[ProductVariationGroupRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/ProductVariationGroupRec.html)

```java
//..This is a list of jet-defined node attribute id's.
//  You must use the taxonomy api or the jet node spreadsheet to 
//  retrieve these values
List<Integer> refinementAttrNodes = new ArrayList<>();
refinementAttrNodes.add( 12345 );

//..A list of YOUR merchant sku's that are considered variations of the 
//  specified parent sku
List<String> childSkus = new ArrayList<>();
childSkus.add( "Some other merchant sku" );

//..Send the variation group
product.sendPutProductVariation( new ProductVariationGroupRec(
  "YOUR parent sku id (variation group product)", 
  ProductVariationGroupRec.Relationship.VARIATION, 
  refinementAttrNodes, 
  childSkus, 
  "A custom variation group heading"
));

```


### 3: Send shipping exceptions for fullfillment nodes

The shipping exceptions call is used to set up specific methods and costs for 
individual SKUs that will override your default settings, with the ability to 
drill down to the fulfillment node level.

Shipping Exceptions are configured per fulfillment node, and are sent in a 
batch using FNodeShippingRec 

[FNodeShippingRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/FNodeShippingRec.html)

Shipping exceptions are configured individually and are added to an 
FNodeShippingRec instance 

[ShippingExceptionRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/ShippingExceptionRec.html)

```java
//..A shipping exception must be added to a fulfillment node
//..More than 1 node can be configured in a single request
List<FNodeShippingRec> nodes = new ArrayList<>();


//..Each node has a list of shipping exceptions/rules
// These can be configured with a ShippingExceptionRec and are added
//  to the fulfillment node object
List<ShippingExceptionRec> exceptions = new ArrayList<>();


//..The rule 
exceptions.add( new ShippingExceptionRec(
  ShippingServiceLevel.SCHEDULED, 
  ShippingMethod.UPS_GROUND, 
  ShipOverrideType.OVERRIDE, 
  new Money( "5.00" ), 
  ShipExceptionType.INCLUDE )
);

//..Add the node exceptions to the list 
nodes.add( new FNodeShippingRec( "Your jet-defined fulfillment node id", exceptions ));

//..Send the exceptions to jet 
product.sendPutProductShippingExceptions( "YOUR merchant sku", exceptions );
```


### 4: Send Returns Exceptions 

The returns exceptions call is used to set up specific methods that will 
overwrite your default settings on a fulfillment node level for returns. 
This exception will be used to determine how and to where a product is 
returned unless the merchant specifies otherwise in the Ship Order message.

Returns exceptions are configured within your partner portal on jet.

For a given product sku, we echo a list of return location id's from the 
jet portal.  This overrides the default settings in the portal for the single
sku.

```java
//..Make a list of return id's from the jet portal
List<String> returnIds = new ArrayList<>();
returnIds.add( "some return hash" );

//..Send them back with a sku to enable the override
product.sendPutReturnsException( "YOUR merchant sku", returnIds );
```


### 5: Archive a Sku

Sku's on Jet can't ever be deleted, but fortunately we can archive them.
As an added bonus, sku's can also be reactivated down the road if you want.

```java
//..To archive
product.sendPutArchiveSku( "YOUR merchant sku", true );

//..To unarchive
product.sendPutArchiveSku( "YOUR merchant sku", false );
```


### 6: Get Product Prices

At Jet, the price the retailer sets is not the same as the price the customer 
pays. The price set for a SKU will be the price the retailer gets paid for
selling the products. However, the price that is set will influence how 
competitive your product offer matches up compared to other product offers 
for the same SKU.

You can retrieve just pricing data for a sku

[ProductPriceRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/ProductPriceRec.html)

[FNodePriceRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/FNodePriceRec.html)

```java
ProductPriceRec priceData = product.getProductPrice( "YOUR merchant sku" );

Money price = priceData.getPrice();

//..or for a specific fulfillment Node
List<FNodePriceRec> fulfillmentNodes = priceData.getFulfillmentNodes();
Money nodePrice = fulfillmentNodes.get( 0 ).getPrice();
```


### 7: Get product inventory 

The inventory returned from this endpoint represents the number in the feed, 
not the quantity that is currently sellable on Jet.com

If you want to find the quantity listed of a given sku within any fulfillment
node, use getProductInventory()

[ProductInventoryRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/ProductInventoryRec.html)

[FNodeInventoryRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/FNodeInventoryRec.html)

```java
//..Retrieve the inventory for a product sku 
ProductInventoryRec inventory = product.getProductInventory( "YOUR merchant sku" );

//..Inventory is grouped by fulfillment node 
for( FNodeInventoryRec node : inventory.getNodes())
{
  //..The quantity within the fulfillment Node 
  int quantity = node.getQuantity()
}
```
 

### 8: Get Product Variations

You can retrieve a variation group for a given sku.

[ProductVariationGroupRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/ProductVariationGroupRec.html)

```java
//..Get the list of variations
ProductVariationGroupRec variations = product.getProductVariations( "YOUR merchant sku" );

//..Your list of child sku's that can be queried individually 
List<String> childSkus = variations.getChildSkus();
```


### 9 Get Product Shipping Exceptions 

The shipping exceptions call is used to set up specific methods and costs for 
individual SKUs that will override your default settings, with the ability to 
drill down to the fulfillment node level.

This is the same as adding an exception, just backwards.

[FNodeShippingRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/FNodeShippingRec.html)
[ShippingExceptionRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/ShippingExceptionRec.html)


```java
List<FNodeShippingRec> exceptions = product.getShippingExceptions( "YOUR merchant sku" );
List<ShippingExceptionRec> shippingExceptions = exceptions.get( 0 ).getItemData();
```


### 10: Retrieve an exception that you have configured 

[ReturnsExceptionRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/ReturnsExceptionRec.html)

```java
ReturnsExceptionRec returnsException = product.getReturnsExceptions( "YOUR merchant sku" );
```


### 11: Retrieve all of the merchant sku data

Any information about the SKU that was previously uploaded (price, inventory, 
shipping exception) will show up here

[ProductRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/ProductRec.html)

```java
//..It's that easy
ProductRec pRec = product.getProduct( "YOUR Merchant Sku" );

//..Want to save this as json somewhere? (Hint: this works with every object)
String json = pRec.toJSON().toString();

//..And back again
ProductRec fromJson = ProductRec.fromJSON( json );
```


### 12: Retrieve a list of sku's on Jet 

This call allows you visibility into the total number of SKUs you have uploaded. 
Alternatively, the Partner Portal allows you to download a CSV file of all SKUs.


```java
for( String sku : product.getSkuList( 0, 100 ))
{
  //..Then you can do whatever with the sku, like get details about it.
  ProductRec pRec = product.getProduct( sku );
}
```


### 13: Retrieving sales/performance data about a single sku

Analyze how your individual product price (item and shipping price) compares to 
the lowest individual product prices from the marketplace. These prices are only 
provided for SKUs that have the status “Available for Sale”. If a best price 
does not change, then the last_update time also will not change. If your 
inventory is zero, then these prices will not continue to be updated and will 
be stale. Note: It may take up to 24 hours to reflect any price updates from 
you and the marketplace.

Product pricing is one factor that Jet uses to determine which retailer wins a 
basket order. Jet determines what orders retailers will win based on the the 
product prices of all products in the order, base commission on those items as 
well as commission adjustments set via the Rules Engine. Commission adjustments 
set via the Rules Engine can be very effective in optimizing your win rate and 
profitability at the order level without having to have the absolute lowest 
item and shipping prices.


You can retrieve stats about each sku individually using this command

[ProductSalesDataRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/ProductSalesDataRec.html)

[OfferRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/OfferRec.html)

```java
ProductSalesDataRec data = product.getSkuSalesData( "YOUR merchant sku" );

//..Then you can grab stats 
for ( OfferRec offer : data.getBestOffers())
{
  //..Do something with the price 
  Money price = offer.getItemPrice();
}
```


# Bulk Product Upload API

Bulk uploads can be done by creating an in-memory list of ProductRec instances
or simply streaming some json file through the library.

[JetAPIBulkProductUpload JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/JetAPIBulkProductUpload.html)

[BulkUploadAuthRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/BulkUploadAuthRec.html)

[PostFile JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/api/PostFile.html)


#### This example is for merchant sku's, but the technique is similar for all file types

```java

    final JetAPIBulkProductUpload up = new JetAPIBulkProductUpload( client, config );
    
    List<ProductRec> products = new ArrayList<>();
    products.add( getTestProduct());
    
    //..The local filename to write the bulk product data to
    final File file = new File( "jetproducttext.json.gz" );
    
    try ( final BulkProductFileGenerator gen = new BulkProductFileGenerator( file )) 
    {      
      //..Write the product json to a gzip file
      for ( final ProductRec pRec : products )
      {
        gen.writeLine( pRec );
      }
    } catch( IOException e ) {
      fail( "Failed to open output file", 0, e );
    }
     
    try {
      //..Get authorization to upload a file
      final BulkUploadAuthRec uploadToken = up.getUploadToken();
      
      //..Sends the authorized gzip file to the url specified in the uploadToken response.
      up.sendAuthorizedFile( uploadToken.getUrl(), new PostFile( file, ContentType.create( "application/x-gzip" ), "gzip", uploadToken.getJetFileId()));
      
      //..If you want to add an additional file to an existing authorization token/processing batch on jet, create a new PostFile instance for the new file
      final PostFile pf = new PostFile( file, ContentType.DEFAULT_BINARY, "gzip", file.getName());
      
      //..Post the request for a file addition to 
      JsonObject addRes = up.sendPostUploadedFiles( uploadToken.getUrl(), pf.getFilename(), BulkUploadFileType.MERCHANT_SKUS ).getJsonObject();
      
      //..Send the next file up to the batch 
      up.sendAuthorizedFile( addRes.getString( "url" ), pf );
      
      //..Get some stats for an uploaded file 
      up.getJetFileId( uploadToken.getJetFileId());
      
      //..And get some stats for the other uploaded file.
      up.getJetFileId( addRes.getString( "jet_file_id" ));
      
    } catch( Exception e ) {
      fail( "Failed to bulk", 0, e );
}

```java

PostFile pf = new PostFile( file, ContentType.DEFAULT_BINARY, "gzip", file.getName());

//..Post the request for a file addition to 
JsonObject addRes = up.sendPostUploadedFiles( 
  uploadToken.getUrl(), pf, BulkUploadFileType.MERCHANT_SKUS ).getJsonObject();

//..Send the next file up to the batch 
up.sendAuthorizedFile( addRes.getString( "url" ), pf );
```

Then you can retrieve the status for any of the uploads

[FileIdRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/products/FileIdRec.html)


```java
//..Get some stats for an uploaded file 
FileIdRec file1Stats = up.getJetFileId( uploadToken.getJetFileId());

//..And get some stats for the other uploaded file.
FileIdRec file2Stats = up.getJetFileId( addRes.getString( "jet_file_id" ));
```


# Orders API 


### 1: Check for Orders

#### Create an IJetAPIOrder instance

[JetOrderAPI JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/orders/JetAPIOrder.html)

```java
IJetAPIOrder orderApi = new JetAPIOrder( client, config );
```

Using this endpoint you can access the first 1000 orders in a certain status.

```java
//..Poll READY status to acknowledge orders
List<String> orderIds = orderApi.getOrderStatusTokens( OrderStatus.READY );
```



### 2: Directed Cancel 
#### This is deprecated 2/17, [Use This](https://github.com/SixArmDonkey/aerodrome-for-jet#6-cancel-order) instead


### 3: Get Order Details

This endpoint will provide you with requisite fulfillment information for the 
order denoted by the Jet Defined Order ID.

[OrderRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/orders/OrderRec.html)

```java
OrderRec order = orderApi.getOrderDetail( jetOrderId );
```


### 4: Acknowledge Order

The order acknowledge call is utilized to allow a retailer to accept or 
reject an order. If there are any skus in the order that cannot be 
fulfilled then you will reject the order.

[AckRequestItemRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/orders/AckRequestItemRec.html)

[AckRequestRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/orders/AckRequestRec.html)

```java
//..Poll for READY orders 
for ( String jetOrderId : orderApi.getOrderStatusTokens( OrderStatus.READY ))
{
  //..Get the order detail 
  OrderRec order = orderApi.getOrderDetail( jetOrderId );

  //..A list of order items to reply about 
  List<AckRequestItemRec> items = new ArrayList<>();

  //..Turn those into ack order records
  for ( OrderItemRec item : order.getOrderItems())
  {
    //..Try to acknowledge the order
    //..Can add a custom status here if you want 
    items.add( AckRequestItemRec.fromOrderItem( 
      item, AckRequestItemRec.Status.FULFILLABLE ));
  }

  //..Build the acknowledgement request to send back to jet 
  AckRequestRec ackRequest = new AckRequestRec( 
    AckStatus.ACCEPTED, jetOrderId, items );

  //..Tell jet that you acknowledge the order 
  orderApi.sendPutAckOrder( jetOrderId, ackRequest );
}
```

### 5: Ship Order

The order shipped call is utilized to provide Jet with the SKUs that have been 
shipped or cancelled in an order, the tracking information, carrier information 
and any additional returns information for the order.

[ShipmentRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/orders/ShipmentRec.html)

[ShipmentItemRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/orders/ShipmentItemRec.html)

[AddressRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/AddressRec.html) 

[OrderItemRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/orders/OrderItemRec.html)

[ShipRequestRec JavaDoc](https://sixarmdonkey.github.io/aerodrome-for-jet/com/buffalokiwi/aerodrome/jet/orders/ShipRequestRec.html)

```java
for ( String jetOrderId : orderApi.getOrderStatusTokens( OrderStatus.ACK, false ))
{
  //..Get the order detail 
  OrderRec order = orderApi.getOrderDetail( jetOrderId );

  //..A list of shipments 
  //..You can split the order up into however many shipments you want.
  List<ShipmentRec> shipments = new ArrayList<>();

  //..a list of items in a shipment 
  List<ShipmentItemRec> shipmentItems = new ArrayList<>();

  //..Create a return address object 
  AddressRec returnAddress = new AddressRec(
    "123 Sesame Street",
    "Suite 100",
    "Sesame",
    "AK",
    "38473"
  );

  //..Create an rma number (can be custom for each item if you want)
  String rmaNumber = "1234RMA";

  //..Turn those into ack order records
  for ( OrderItemRec item : order.getOrderItems())
  {
    //..Create a builder for a new ShipmentItem by converting the retrieved
    //  order item to a Shipment Item
    ShipmentItemRec.Builder builder = ShipmentItemRec.fromOrderItem( item );

    //..You can modify the quantity shipped or cancelled here, or just fulfill as it was ordered
    //..like this:
    //builder.setQuantity( 1 );
    //builder.setCancelQuantity( 1 );

    //...Set the return address
    builder.setReturnTo( returnAddress );

    //..Set the rma number if desired
    builder.setRmaNumber( rmaNumber );

    //..Build the item and add it to the items list 
    shipmentItems.add( builder.build());
  }


  //..Build a shipment for the items in the order
  //..You can create multiple shipments, and also mix cancellations 
  //  with shipments.
  ShipmentRec shipment = new ShipmentRec.Builder()
    .setCarrier( order.getOrderDetail().getRequestShippingCarrier())
    .setTrackingNumber( "Z123456780123456" )
    .setShipmentDate(new ISO801Date())
    .setExpectedDeliveryDate(new ISO801Date( new Date( new Date().getTime() + ( 86400L * 2L ))))
    .setShipFromZip( "38473" )
    .setPickupDate(new ISO801Date())
    .setItems( shipmentItems )
    .build();

  //..Add it to the list of shipments you're sending out.
  shipments.add( shipment );

  //..Create the final request object to tell jet about the shipment
  ShipRequestRec shipmentRequest = new ShipRequestRec( "", shipments );

  //..Send the shipment to jet
  orderApi.sendPutShipOrder( jetOrderId, shipmentRequest );
}       

```

### 6: Cancel Order

You cancel orders using the put shipment command 


```java
//..Get acknowledged orders 
for ( final String jetOrderId : orderApi.getOrderStatusTokens( OrderStatus.ACK ))
{
  //..Get the order detail 
  OrderRec order = orderApi.getOrderDetail( jetOrderId );

  //..A list of shipments 
  //..You can split the order up into however many shipments you want.
  List<ShipmentRec> shipments = new ArrayList<>();

  //..a list of items in a shipment 
  List<ShipmentItemRec> shipmentItems = new ArrayList<>();

  //..Turn those into ack order records
  for ( OrderItemRec item : order.getOrderItems())
  {
    //..Create a builder for a new ShipmentItem by converting the retrieved
    //  order item to a Shipment Item
    ShipmentItemRec.Builder builder = ShipmentItemRec.fromOrderItem( item );

    //..You can modify the quantity shipped or cancelled here, or just fulfill as it was ordered
    //..like this:        
    builder.setQuantity( 0 );
    builder.setCancelQuantity( item.getRequestOrderQty());

    //..Build the item and add it to the items list 
    shipmentItems.add( builder.build());

    //..All of the above code can be chained into a 1-liner.
  }


  //..Build a shipment for the items in the order
  //..You can create multiple shipments, and also mix cancellations 
  //  with shipments.      
  final ShipmentRec shipment = new ShipmentRec.Builder()
    .setItems( shipmentItems )
    //..Shipments with only cancelled items must have an alt shipment id
    .setAltShipmentId( "Alt ship id test" ) 
    .build();

  //..Add it to the list of shipments you're sending out.
  shipments.add( shipment );

  //..Create the final request object to tell jet about the shipment
  final ShipRequestRec shipmentRequest = new ShipRequestRec( "", shipments );

  //..Send the shipment to jet
  orderApi.sendPutShipOrder( jetOrderId, shipmentRequest );
}      
``` 

#Returns API

```java
IJetAPIReturn returnsApi = new JetAPIReturn( client, config );
      
//..Find any returns waiting to be approved 
for ( final String id : returnsApi.getReturnsStatusTokens( ReturnStatus.CREATED ))
{
  //..Get the return detail
  final ReturnRec ret = returnsApi.getReturnDetail( id );

  //..List of items for the return        
  final List<ReturnItemRec> returnItems = new ArrayList<>();

  //..Convert merchant sku items from the detail response into items for 
  //  the put return complete command
  for ( final ReturnMerchantSkuRec m : ret.getReturnMerchantSkus())
  {
    returnItems.add( ReturnItemRec.fromReturnMerchantSkuRec( m )
      //..Set some custom attributes for jet 
      //..Any property can be overridden
      .setFeedback( RefundFeedback.OPENED )
      .setNotes( "Some of my notes about this return" )
      .build() //..Build the item and add it to the list 
    );
  }

  //..approve/complete the return 
  returnsApi.putCompleteReturn( id, new CompleteReturnRequestRec( 
    ret.getMerchantOrderId(), "", true, ChargeFeedback.FRAUD, returnItems ));
}

```

#Refund API

```java
//..Need the order api to use the refund api.
final IJetAPIOrder orderApi = new JetAPIOrder( client, jetConfig );      
final IJetAPIRefund refundApi = new JetAPIRefund( client, jetConfig );

//..Poll for a list of jet order id's to play with 
List<String> orderTokens = orderApi.getOrderStatusTokens( OrderStatus.COMPLETE );

final String orderId = orderTokens.get( 0 );
//..Get the order detail so we can generate a refund
final OrderRec order = orderApi.getOrderDetail( orderId );


//..Items for the refund 
final List<RefundItemRec> refundItems = new ArrayList<>();

//..This is annoying, but each item requires custom attributes to be added
//..So, get a list of ReturnItemRec builders from the order and loop em
//..Each one will have properties added and then be built and added to a list
for ( final RefundItemRec.Builder b : order.generateItemsForRefund())
{
  refundItems.add( 
   b.setNotes( "Some notes about the item" )
   .setRefundReason( ReturnReason.DAMAGED_ITEM )
   .build());
}      

//..Post a new refund to jet      
//..Your order id is in your jet console 
//refundApi.postCreateRefund( orderId, "alt-refund-id", refundItems );


//..If NOT posting a new refund, you can poll for refund id's by status.
for( final String refundId : refundApi.pollRefunds( RefundStatus.CREATED ))
{
  //..Get the detail for the report id
  refundApi.getRefundDetail( refundId );
}

```


#Taxonomy API 

Create a Taxonomy API library instance 

```java
IJetAPITaxonomy taxApi = new JetAPITaxonomy( client, config );
```

Retrieve a list of taxonomy nodes and then fetch details about the node
itself and then fetch any attributes.


```java

//..Poll for nodes
for ( String id : taxApi.pollNodes( 0, 100 ))
{
  try {
    //..Get the node detail
    taxApi.getNodeDetail( id );

    //..Get any node attribute details 
    taxApi.getAttrDetail( id );
  } catch( JetException e ) {
    IAPIResponse r = e.getResponse();
    if ( r == null )
      throw e;                    

    //..otherwise it was a successful api response and we can process
    // the result further here or just continue on.
    //..This will be a 404 for attribute not found in this instance.
  }
}
```


#Settlement API

Create a settlement API instance

```java
IJetAPISettlement settlementApi = new JetAPISettlement( client, config );
```

Poll for settlement id's, then fetch the report for each id.

```java      
//..Retrieve a list of settlement id's for the last 7 days
for ( final String id : settlementApi.getSettlementDays( 7 ))
{
  //..Retrieve the report for each id 
  settlementApi.getSettlementReport( id );
}

```
