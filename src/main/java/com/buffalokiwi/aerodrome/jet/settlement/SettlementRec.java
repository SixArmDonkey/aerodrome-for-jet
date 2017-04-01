/**
 * This file is part of the Aerodrome package, and is subject to the
 * terms and conditions defined in file 'LICENSE', which is part
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.buffalokiwi.aerodrome.jet.settlement;

import com.buffalokiwi.aerodrome.jet.JetDate;
import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.utils.Money;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


/**
 * Represents a response from the settlement report api call.
 * 
 * Date properties in this record use instances of ISO8601UTCDate().
 * 
 * @author John Quinn
 */
public class SettlementRec implements Jsonable
{
  /**
   * SettlementRec builder 
   */
  public static class Builder
  {
    /**
     * The unique ID for this settlement report. This ID should be referenced 
     * when creating a dispute with Jet.com
     */
    private String reportId = "";

    /**
     * The status of the current settlement
     */
    private SettlementState state = SettlementState.NONE;

    /**
     * The currency that all monetary values are represented in, for the 
     * retrieved settlement report.
     */
    private String currency = "";

    /**
     * The total value of all sales and returns that aren't settled yet. This 
     * value will remain unavailable until settlement occur. This value is 
     * provided for estimation purposes and is not considered final.
     */
    private Money unavailableBalance = new Money();

    /**
     * The start date/time of the settlement report.
     */
    private JetDate periodStart = new JetDate();

    /**
     * The end date/time of the settlement report
     */
    private JetDate periodEnd = new JetDate();

    /**
     * Order balance details 
     */
    private OrderDetailRec orderDetails = null;

    /**
     * The total value of orders that is settled in this settlement report
     */
    private Money orderBalance = new Money();

    /**
     * Return balance details
     */
    private ReturnDetailRec returnDetails = null;

    /**
     * The total value of returns that is settled in this settlement report
     */
    private Money returnBalance = new Money();

    /**
     * A credit given to a merchant or fee taken from the merchant
     */
    private Money jetAdjustment = new Money();

    /**
     * The total value of orders that is settled in this settlement report
     */
    private Money settlementValue = new Money();

    
    /**
     * The unique ID for this settlement report. This ID should be referenced when creating a dispute with Jet.com
     * @param reportId the reportId to set
     * @return this
     */
    public Builder setReportId( final String reportId )
    {
      Utils.checkNull( reportId, "reportId" );
      this.reportId = reportId;
      return this;
    }

    
    /**
     * The status of the current settlement
     * @param state the state to set
     * @return this
     */
    public Builder setState( final SettlementState state )
    {
      Utils.checkNull( state, "state" );
      this.state = state;
      return this;
    }

    
    /**
     * The currency that all monetary values are represented in, for the retrieved settlement report.
     * @param currency the currency to set
     * @return this
     */
    public Builder setCurrency( final String currency )
    {
      Utils.checkNull( currency, "currency" );
      this.currency = currency;
      return this;
    }
    

    /**
     * The total value of all sales and returns that aren't settled yet. This value will remain unavailable until settlement occur. This value is provided for estimation purposes and is not considered final.
     * @param unavailableBalance the unavailableBalance to set
     * @return this
     */
    public Builder setUnavailableBalance( final Money unavailableBalance )
    {
      Utils.checkNull( unavailableBalance, "unavailableBalance" );
      this.unavailableBalance = unavailableBalance;
      return this;
    }

    
    /**
     * The start date/time of the settlement report
     * @param periodStart the periodStart to set
     * @return this
     */
    public Builder setPeriodStart( final JetDate periodStart )
    {
      Utils.checkNull( periodStart, "periodStart" );
      this.periodStart = periodStart;
      return this;
    }

    
    /**
     * The end date/time of the settlement report
     * @param periodEnd the periodEnd to set
     * @return this
     */
    public Builder setPeriodEnd( final JetDate periodEnd )
    {
      Utils.checkNull( periodEnd, "periodEnd" );
      this.periodEnd = periodEnd;
      return this;
    }

    
    /**
     * Set Order balance details.
     * @param orderDetails the orderDetails to set
     * @return this
     */
    public Builder setOrderDetails( final OrderDetailRec orderDetails )
    {
      this.orderDetails = orderDetails;
      return this;
    }

    
    /**
     * The total value of orders that is settled in this settlement report
     * @param orderBalance the orderBalance to set
     * @return this
     */
    public Builder setOrderBalance( final Money orderBalance )
    {
      Utils.checkNull( orderBalance, "orderBalance" );
      this.orderBalance = orderBalance;
      return this;
    }

    
    /**
     * Set return balance details.
     * @param returnDetails the returnDetails to set
     * @return this
     */
    public Builder setReturnDetails( final ReturnDetailRec returnDetails )
    {      
      this.returnDetails = returnDetails;
      return this;
    }

    
    /**
     * The total value of returns that is settled in this settlement report
     * @param returnBalance the returnBalance to set
     * @return this
     */
    public Builder setReturnBalance( final Money returnBalance )
    {
      Utils.checkNull( returnBalance, "returnBalance" );
      this.returnBalance = returnBalance;
      return this;
    }

    
    /**
     * A credit given to a merchant or fee taken from the merchant
     * @param jetAdjustment the jetAdjustment to set
     * @return this
     */
    public Builder setJetAdjustment( final Money jetAdjustment )
    {
      Utils.checkNull( jetAdjustment, "jetAdjustment" );
      this.jetAdjustment = jetAdjustment;
      return this;
    }

    
    /**
     * The total value of orders that is settled in this settlement report
     * @param settlementValue the settlementValue to set
     * @return this
     */
    public Builder setSettlementValue( final Money settlementValue )
    {
      Utils.checkNull( settlementValue, "settlementValue" );
      this.settlementValue = settlementValue;
      return this;
    }
    
    
    /**
     * Build a new SettlementRec instance.
     * @return object 
     */
    public SettlementRec build()
    {
      return new SettlementRec( this );
    }
  }
  
  
  
  /**
   * The unique ID for this settlement report. This ID should be referenced 
   * when creating a dispute with Jet.com
   */
  private final String reportId;
  
  /**
   * The status of the current settlement
   */
  private final SettlementState state;
  
  /**
   * The currency that all monetary values are represented in, for the 
   * retrieved settlement report.
   */
  private final String currency;
  
  /**
   * The total value of all sales and returns that aren't settled yet. This 
   * value will remain unavailable until settlement occur. This value is 
   * provided for estimation purposes and is not considered final.
   */
  private final Money unavailableBalance;
  
  /**
   * The start date/time of the settlement report
   */
  private final JetDate periodStart;
  
  /**
   * The end date/time of the settlement report
   */
  private final JetDate periodEnd;
  
  /**
   * 
   */
  private final OrderDetailRec orderDetails;
  
  /**
   * The total value of orders that is settled in this settlement report
   */
  private final Money orderBalance;
  
  /**
   * 
   */
  private final ReturnDetailRec returnDetails;
  
  /**
   * The total value of returns that is settled in this settlement report
   */
  private final Money returnBalance;
  
  /**
   * A credit given to a merchant or fee taken from the merchant
   */
  private final Money jetAdjustment;
  
  /**
   * The total value of orders that is settled in this settlement report
   */
  private final Money settlementValue;
  
  
  /**
   * Build a SettlementRec instance form Jet json
   * @param json jet json 
   * @return instance
   */
  public static SettlementRec fromJson( final JsonObject json )
  {
    if ( json == null )
      throw new IllegalArgumentException( "json cannot be null" );
    
    final Builder b = new Builder()
      .setReportId( json.getString(  "settlement_report_id", "" ))
      .setState( SettlementState.fromText( 
        json.getString(  "settlement_state", "" )))
      .setCurrency( json.getString( "currency", "" ))
      .setUnavailableBalance( Utils.jsonNumberToMoney( json.getJsonNumber( "unavailable_balance" )))
      .setPeriodStart( JetDate.fromJetValueOrNull( 
        json.getString( "settlement_period_start", "" )))
      .setPeriodEnd( JetDate.fromJetValueOrNull( 
        json.getString( "settlement_period_end", "" )))
      .setOrderBalance( Utils.jsonNumberToMoney( json.getJsonNumber( "order_balanace" )))
      .setReturnBalance( Utils.jsonNumberToMoney( json.getJsonNumber( "return_balance" )))
      .setJetAdjustment( Utils.jsonNumberToMoney( json.getJsonNumber( "jet_adjustment" )))
      .setSettlementValue( Utils.jsonNumberToMoney( json.getJsonNumber( "settlement_value" )));
    
    final JsonObject od = json.getJsonObject( "order_balance_details" );
    if ( od != null )
      b.setOrderDetails( OrderDetailRec.fromJson( od ));
    
    final JsonObject rd = json.getJsonObject( "return_balance_details" );
    if ( rd != null )
      b.setReturnDetails( ReturnDetailRec.fromJson( rd ));
    
    return b.build();
  }
  
  
  /**
   * Build a new SettlementRec instance
   * @param b builder 
   */
  protected SettlementRec( final Builder b )
  {
    this.reportId = b.reportId;
    this.state = b.state;
    this.currency = b.currency;
    this.unavailableBalance = b.unavailableBalance;
    this.periodStart = b.periodStart;
    this.periodEnd = b.periodEnd;
    this.orderDetails = b.orderDetails;
    this.orderBalance = b.orderBalance;
    this.returnDetails = b.returnDetails;
    this.returnBalance = b.returnBalance;
    this.jetAdjustment = b.jetAdjustment;
    this.settlementValue = b.settlementValue;
  }
  
  
  /**
   * The unique ID for this settlement report. This ID should be referenced when creating a dispute with Jet.com
   * @return the reportId
   */
  public String getReportId()
  {
    return reportId;
  }

  
  /**
   * The status of the current settlement
   * @return the state
   */
  public SettlementState getState()
  {
    return state;
  }

  
  /**
   * The currency that all monetary values are represented in, for the retrieved settlement report.
   * @return the currency
   */
  public String getCurrency()
  {
    return currency;
  }

  
  /**
   * The total value of all sales and returns that aren't settled yet. This value will remain unavailable until settlement occur. This value is provided for estimation purposes and is not considered final.
   * @return the unavailableBalance
   */
  public Money getUnavailableBalance()
  {
    return unavailableBalance;
  }

  
  /**
   * The start date/time of the settlement report.
   * @return the periodStart
   */
  public JetDate getPeriodStart()
  {
    return periodStart;
  }

  
  /**
   * The end date/time of the settlement report
   * @return the periodEnd
   */
  public JetDate getPeriodEnd()
  {
    return periodEnd;
  }

  
  /**
   * Retrieve the order balance details - this can return null.
   * @return the orderDetails
   */
  public OrderDetailRec getOrderDetails()
  {
    return orderDetails;
  }

  
  /**
   * The total value of orders that is settled in this settlement report
   * @return the orderBalance
   */
  public Money getOrderBalance()
  {
    return orderBalance;
  }

  
  /**
   * Get the return balance details - this can be null.
   * @return the returnDetails
   */
  public ReturnDetailRec getReturnDetails()
  {
    return returnDetails;
  }

  
  /**
   * The total value of returns that is settled in this settlement report
   * @return the returnBalance
   */
  public Money getReturnBalance()
  {
    return returnBalance;
  }

  
  /**
   * A credit given to a merchant or fee taken from the merchant
   * @return the jetAdjustment
   */
  public Money getJetAdjustment()
  {
    return jetAdjustment;
  }

  
  /**
   * The total value of orders that is settled in this settlement report
   * @return the settlementValue
   */
  public Money getSettlementValue()
  {
    return settlementValue;
  }  
  
  
  /**
   * Turn this into Jet json.
   * @return jet json 
   */
  @Override
  public JsonObject toJSON()
  {
    final JsonObjectBuilder b = Json.createObjectBuilder()
      .add( "settlement_report_id", reportId )
      .add( "settlement_state", state.getText())
      .add( "currency", currency )
      .add( "unavailable_balance", unavailableBalance.toString())
      .add( "settlement_period_start", periodStart.getDateString( JetDate.FMT_ZULU_MICRO ))
      .add( "settlement_period_end", periodEnd.getDateString( JetDate.FMT_ZULU_MICRO ))
      .add( "order_balance", orderBalance.toString())
      .add( "return_balance", returnBalance.toString())
      .add( "jet_adjustment", jetAdjustment.toString())
      .add( "settlement_value", settlementValue.toString());
    
    if ( orderDetails != null )
      b.add( "order_balance_details", orderDetails.toJSON());
    
    if ( returnDetails != null )
      b.add( "return_balance_details", returnDetails.toJSON());
    
    return b.build();
  }
}
