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