/**
 * This file is part of the Aerodrome package, and is subject to the 
 * terms and conditions defined in file 'LICENSE', which is part 
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

package com.buffalokiwi.aerodrome.jet;

/**
 * Product Tax Code values for Jet 
 * @author John Quinn
 */
public enum ProductTaxCode
{
  NO_VALUE( "" ),  
  ADULT_DIAPERS( "Adult Diapers" ),
	ATHLETIC_CLOTHING( "Athletic Clothing" ),
	BABY_SUPPLIES( "Baby Supplies" ),
	BANDAGES_AND_FIRST_AID_KITS( "Bandages and First Aid Kits" ),
	BATHING_SUITS( "Bathing Suits" ),
	JUICE_50_99( "Beverages with 51 to 99 Percent Juice" ),
	BOTTLED_WATER( "Bottled Water Plain" ),
	BRACES_AND_SUPPORTS( "Braces and Supports" ),
	BREAST_PUMPS( "Breast Pumps" ),
  CANDY( "Candy" ),
	CANDY_FLOUR( "Candy with Flour" ),
	CAR_SEATS( "Car Seats" ),
	CARB_SOFT_DRINK( "Carbonated Soft Drinks" ),
	CONTACT_LENS_SOLUTION( "Contact Lens Solution" ),
	CONTRACEPTIVES( "Contraceptives" ),
	COSTUMES( "Costumes" ),
	DIABETIC_SUPPLIES( "Diabetic Supplies" ),
	DIETARY_SUPPLEMENTS( "Dietary Supplements" ),
	DISPOSABLE_DIAPERS( "Disposable Infant Diapers" ),
	DISPOSABLE_WIPES( "Disposable Wipes" ),
	JUICE_0_50( "Drinks under 50 Percent Juice" ),
	MEDICAL_EQUIP( "Durable Medical Equipment" ),
	FEM_HYGIENE( "Feminine Hygiene Products" ),
	FLUORIDE( "Fluoride Toothpaste" ),
	CLOTHING( "General Clothing" ),
	GROCERY( "General Grocery Items" ),
	GENERIC_TAXABLE( "Generic Taxable Product" ),
	HANDKERCHIEF( "Handkerchiefs" ),
	HELMET( "Helmets" ),
  INACTIVE( "N/A - Inactive" ),
	INFANT_CLOTHING( "Infant Clothing" ),
  MAGAZINES( "Magazines" ),
	MEDICATED_PERSONAL( "Medicated Personal Care Items" ),
  MOBILITY_EQUIP( "Mobility Equipment" ),
	NOT_TAXABLE( "Non Taxable Product" ),
	NON_MOTOR_BOATS( "Non-Motorized Boats" ),
	ORAL_CARE( "Oral Care Products" ),
	OTC_MEDICATION( "OTC Medication" ),
	OTC_PET_MEDICATION( "OTC Pet Meds" ),
	PAPER( "Paper Products" ),
	PET_FOOD( "Pet Foods" ),
  PRODUCTIVITY_SOFTWARE( "Productivity Software" ),
	SAFETY_CLOTHING( "Safety Clothing" ),
  SHOE_INSOLES( "Shoe Insoles" ),
	SMOKING_CESSATION( "Smoking Cessation" ),
	SPARKLING_WATER( "Sparkling Water" ),
	SPF_SUNCARE( "SPF Suncare Products" ),
	SWEATBANDS( "Sweatbands" ),
	THERMOMETER( "Thermometers" ),
	TOILET_PAPER( "Toilet Paper" );


  private final String text;

  private static final ProductTaxCode[] values = values();
  
  /**
   * Attempt to retrieve a ProductTaxCode by text value 
   * @param text value 
   * @return tax code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ProductTaxCode fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ProductTaxCode c : values )
    {
      if ( c.getText().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }
  
  
  /**
   * Create a new ProductTaxCode instance 
   * @param text Text for jet  
   */
  ProductTaxCode ( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Retrieve the jet text 
   * @return jet value 
   */
  public String getText()
  {
    return text;
  }
  
  
  /**
   * Returns jet api text 
   * @return 
   */
  @Override
  public String toString()
  {
    return text;
  }
}
