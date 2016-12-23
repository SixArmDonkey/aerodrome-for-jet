# Aerodrome for Jet
## Jet.com API library

[![Aerodrome](https://img.shields.io/badge/Aerodrome-Not%20Tested; In Development-red.svg)]()
[![License](https://img.shields.io/badge/license-Apache_2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

[![Authentication](https://img.shields.io/badge/Authentication-working-yellowgreen.svg)]()
[![ProductAPI](https://img.shields.io/badge/Product%20API-working-yellowgreen.svg)]()
[![BulkProductAPI](https://img.shields.io/badge/Bulk%20Product%20API-working-yellowgreen.svg)]()
[![OrdersAPI](https://img.shields.io/badge/Orders%20API-working-yellowgreen.svg)]()
[![ReturnsAPI](https://img.shields.io/badge/Returns%20API-working-yellowgreen.svg)]()
[![RefundssAPI](https://img.shields.io/badge/Refunds%20API-working-yellowgreen.svg)]()
[![TaxonomyAPI](https://img.shields.io/badge/Taxonomy%20API-working-yellowgreen.svg)]()



The source is completely documented, check out the JavaDoc here: https://sheepguru.github.io/aerodrome-for-jet/ .

There is a makeshift test set up in the Main class, see that for usage for now.  

Yes, I will be writing tests for everything...

For questions, please feel free to email johnquinn3@gmail.com.

# Quick Start Guide

### 1: Create a JetConfig object 

At minimum, you need to add your merchant id, username, password

[JetConfig JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/DefaultJetConfig.html)

```java
JetConfig config = new DefaultJetConfig.Builder();
  .setMerchantId( "your merchant id" )
  .setUser( "your user id" )
  .setPass( "your password" )
  .build();
```

### 2: Create a shared Http Client to use 

[APIHttpClient JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/api/APIHttpClient.html)

```java
IAPIHttpClient client = new APIHttpClient.Builder().build();    
```

### 3: Authenticate with Jet 

[JetAPIAuth JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/JetAPIAuth.html)

```java
  JetAPIAuth auth = new JetAPIAuth( client, config );

  //..Perform the login and retrieve a token
  //  This token is stored in the jet config and will automatically be 
  //  added to any requests that use the config object.
  auth.login();
```

If authentication succeeded, then the config object will contain the proper tokens
to send to Jet.  This process is thread safe.


# Product API

Follow the steps in the Quick Start Guide prior to using the Product API.
Jet API Authentication is required.

### 1: Initialize the product API

[JetAPIProduct JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/JetAPIProduct.html)

```java
IJetAPIProduct productApi = new JetAPIProduct( client, jetConfig );
```

### 2: Add a single product to Jet 

Each request and response is encapsulated in a unique object.
In this instance we will use an instance of ProductRec, which represents
a product.

First we create a product.  The minimum required properties are shown.

Note: This needs a builder class, and will be immutable in the near future.

[ProductRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/ProductRec.html)

```java
  ProductRec prod = new ProductRec();
  prod.setMerchantSku( "Your unique local sku" );
  prod.setTitle( "Product title" );
  prod.setProductDescription( "Product description" );
  prod.setMultipackQuantity( 1 );
  prod.setMsrp( new Money( "44.99" ));
  prod.setPrice( new Money( "44.99" ));
  prod.setMainImageUrl( "https://www.example.com/image.jpg" );
  prod.setSwatchImageUrl( "https://www.example.com/thumbnail.jpg" );
  prod.setBrand( "Manufacturer Name" );
  //..Your fulfillment node id's are unique to your account, and are found in your
  //  Jet Partner Portal 
  prod.setfNodeInventory( new FNodeInventoryRec( "Fulfillment Node Id", 1 ));
  prod.setProductCode(new ProductCodeRec( "111111111111", ProductCodeType.UPC ));
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

[ProductVariationGroupRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/ProductVariationGroupRec.html)

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

Shipping Exceptions are configured per fulfillment node, and are sent in a 
batch using FNodeShippingRec 

[FNodeShippingRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/FNodeShippingRec.html)

Shipping exceptions are configured individually and are added to an 
FNodeShippingRec instance 

[ShippingExceptionRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/ShippingExceptionRec.html)

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

You can retrieve just pricing data for a sku

[ProductPriceRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/ProductPriceRec.html)

[FNodePriceRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/FNodePriceRec.html)

```java
ProductPriceRec priceData = product.getProductPrice( "YOUR merchant sku" );

Money price = priceData.getPrice();

//..or for a specific fulfillment Node
List<FNodePriceRec> fulfillmentNodes = priceData.getFulfillmentNodes();
Money nodePrice = fulfillmentNodes.get( 0 ).getPrice();
```

### 7: Get product inventory 

If you want to find the quantity listed of a given sku within any fulfillment
node, use getProductInventory()

[ProductInventoryRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/ProductInventoryRec.html)

[FNodeInventoryRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/FNodeInventoryRec.html)

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

[ProductVariationGroupRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/ProductVariationGroupRec.html)

```java
//..Get the list of variations
ProductVariationGroupRec variations = product.getProductVariations( "YOUR merchant sku" );

//..Your list of child sku's that can be queried individually 
List<String> childSkus = variations.getChildSkus();
```


### 9 Get Product Shipping Exceptions 

This is the same as adding an exception, just backwards.

[FNodeShippingRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/FNodeShippingRec.html)
[ShippingExceptionRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/ShippingExceptionRec.html)


```java
List<FNodeShippingRec> exceptions = product.getShippingExceptions( "YOUR merchant sku" );
List<ShippingExceptionRec> shippingExceptions = exceptions.get( 0 ).getItemData();
```

### 10:

Retrieve an exception that you have configured 

[ReturnsExceptionRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/ReturnsExceptionRec.html)

```java
ReturnsExceptionRec returnsException = product.getReturnsExceptions( "YOUR merchant sku" );
```

### 11: 

Retrieve all of the merchant sku data

[ProductRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/ProductRec.html)

```java
//..It's that easy
ProductRec pRec = product.getProduct( "YOUR Merchant Sku" );

//..Want to save this as json somewhere? (Hint: this works with every object)
String json = pRec.toJSON().toString();

//..And back again
ProductRec fromJson = ProductRec.fromJSON( json );
```

### 12: Retrieve a list of sku's on Jet 

If you want to see what sku's you have uploaded, you use this:

```java
for( String sku : product.getSkuList( 0, 100 ))
{
  //..Then you can do whatever with the sku, like get details about it.
  ProductRec pRec = product.getProduct( sku );
}
```

### 12: Retrieving sales/performance data about a single sku

You can retrieve stats about each sku individually using this command

[ProductSalesDataRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/ProductSalesDataRec.html)

[OfferRec JavaDoc](https://sheepguru.github.io/aerodrome-for-jet/com/sheepguru/aerodrome/jet/products/OfferRec.html)

```java
ProductSalesDataRec data = product.getSkuSalesData( "YOUR merchant sku" );

//..Then you can grab stats 
for ( OfferRec offer : data.getBestOffers())
{
  //..Do something with the price 
  Money price = offer.getItemPrice();
}
```