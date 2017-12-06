/**
 * This file is part of the Aerodrome package, and is subject to the 
 * terms and conditions defined in file 'LICENSE', which is part 
 * of this source code package.
 *
 * Copyright (c) 2017 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */


package com.buffalokiwi.aerodrome.jet.reports;



import com.buffalokiwi.aerodrome.jet.BuildableObject;
import com.buffalokiwi.aerodrome.jet.IJetDate;
import com.buffalokiwi.aerodrome.jet.JetDate;
import com.buffalokiwi.aerodrome.jet.Utils;
import javax.json.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The bulk reporting feature allows the merchant to request a report which 
 * will return to the merchant a JSON file with the requested information. 
 * Jet.com will use this to pass data to the retailer
 * 
 * @param <R> Subclass Type
 * @param <B> Inner builder subclass type 
 * 
 * @author John Quinn
 */
public class ReportStatusRec<R extends ReportStatusRec, B extends ReportStatusRec.Builder> extends BuildableObject<R,B> 
{
  /**
   * Log instance 
   */
  private final static Log LOG = LogFactory.getLog( ReportStatusRec.class );


  //////////////////////////////////////////////////////////////////////////////
  // BEGIN BUILDER                                                            //
  //////////////////////////////////////////////////////////////////////////////
  
  
  /**
   * Builder 
   * @param <T> Subclass Type 
   * @param <R> Outer class Type 
   */
  public static class Builder<T extends Builder, R extends ReportStatusRec> extends BuildableObject.Builder<T,R> 
  {
    /**
     * The Jet defined merchant ID associated with the merchant account
     */
    private String merchantId = "";

    /**
     * The Jet defined report ID associated with the report requested
     */
    private String reportId = "";

    /**
     * The date the report was requested
     */
    private IJetDate requestedDate = null;

    /**
     * The current status of the report
     */
    private ReportStatus status = ReportStatus.NONE;

    /**
     * The type of report requested.
     */
    private ReportType type = ReportType.NONE;

    /**
     * The date-time the report started processing
     */
    private IJetDate processingStart = null;

    /**
     * The date-time the report ended processing
     */
    private IJetDate processingEnd = null;

    /**
     * The date the report will no longer be accessible for download. A new report 
     * will need to be requested
     */
    private IJetDate reportExpDate = null;

    /**
     * The URL where the report can be downloaded
     */
    private String reportUrl = "";


    /**
     * The Jet defined merchant ID associated with the merchant account
     * @param merchantId the merchantId to set
     */
    public T setMerchantId( String merchantId )
    {
      Utils.checkNullEmpty( merchantId, "merchantId" );
      this.merchantId = merchantId;
      return getReference();
    }


    /**
     * The Jet defined report ID associated with the report requested
     * @param reportId the reportId to set
     */
    public T setReportId( String reportId )
    {
      Utils.checkNull( reportId, "reportId" );
      this.reportId = reportId;
      return getReference();
    }
    

    /**
     * The date the report was requested
     * @param requestedDate the requestedDate to set
     */
    public T setRequestedDate( IJetDate requestedDate )
    {
      this.requestedDate = requestedDate;
      return getReference();
    }

    
    /**
     * The current status of the report
     * @param status the status to set
     */
    public T setStatus( ReportStatus status )
    {
      Utils.checkNull( status, "status" );
      this.status = status;
      return getReference();
    }

    
    /**
     * The type of report requested.
     * @param type the type to set
     */
    public T setType( ReportType type )
    {
      Utils.checkNull( type, "type" );
      this.type = type;
      return getReference();
    }

    
    /**
     * The date-time the report started processing
     * @param processingStart the processingStart to set
     */
    public T setProcessingStart( IJetDate processingStart )
    {
      this.processingStart = processingStart;
      return getReference();
    }


    /**
     * The date-time the report ended processing
     * @param processingEnd the processingEnd to set
     */
    public T setProcessingEnd( IJetDate processingEnd )
    {
      this.processingEnd = processingEnd;
      return getReference();
    }


    /**
     * The date the report will no longer be accessible for download. A new 
     * report will need to be requested
     * @param reportExpDate the reportExpDate to set
     */
    public T setReportExpDate( IJetDate reportExpDate )
    {
      this.reportExpDate = reportExpDate;
      return getReference();
    }


    /**
     * The URL where the report can be downloaded
     * @param reportUrl the reportUrl to set
     */
    public T setReportUrl( String reportUrl )
    {
      if ( reportUrl != null )
        this.reportUrl = reportUrl;
      
      return getReference();
    }
    

    /**
     * The type of report requested.
     * @return the type
     */
    public ReportType getType()
    {
      return type;
    }

    
    /**
     * The date the report was requested
     * @return the requestedDate
     */
    public IJetDate getRequestedDate()
    {
      return requestedDate;
    }


    /**
     * The Jet defined merchant ID associated with the merchant account
     * @return the merchantId
     */
    public String getMerchantId()
    {
      return merchantId;
    }


    /**
     * The Jet defined report ID associated with the report requested
     * @return the reportId
     */
    public String getReportId()
    {
      return reportId;
    }
    

    /**
     * The current status of the report
     * @return the status
     */
    public ReportStatus getStatus()
    {
      return status;
    }

    
    /**
     * The date-time the report started processing
     * @return the processingStart
     */
    public IJetDate getProcessingStart()
    {
      return processingStart;
    }


    /**
     * The date-time the report ended processing
     * @return the processingEnd
     */
    public IJetDate getProcessingEnd()
    {
      return processingEnd;
    }


    /**
     * The date the report will no longer be accessible for download. A 
     * new report will need to be requested
     * @return the reportExpDate
     */
    public IJetDate getReportExpDate()
    {
      return reportExpDate;
    }


    /**
     * The URL where the report can be downloaded
     * @return the reportUrl
     */
    public String getReportUrl()
    {
      return reportUrl;
    }      
    
    
    /**
     * Build the object 
     * @return Built object 
     */
    @Override
    public R build() 
    {
      return (R)(new ReportStatusRec( Builder.class, this ));
    }    
  }
  

  //////////////////////////////////////////////////////////////////////////////
  // END BUILDER                                                              //
  //////////////////////////////////////////////////////////////////////////////


  //////////////////////////////////////////////////////////////////////////////
  // ReportStatusRec Properties                                                         
  //////////////////////////////////////////////////////////////////////////////
  
  
  /**
   * The Jet defined merchant ID associated with the merchant account
   */
  private final String merchantId;
  
  /**
   * The Jet defined report ID associated with the report requested
   */
  private final String reportId;
  
  /**
   * The date the report was requested
   */
  private final IJetDate requestedDate;
  
  /**
   * The current status of the report
   */
  private final ReportStatus status;
  
  /**
   * The type of report requested.
   */
  private final ReportType type;
  
  /**
   * The date-time the report started processing
   */
  private final IJetDate processingStart;
  
  /**
   * The date-time the report ended processing
   */
  private final IJetDate processingEnd;
  
  /**
   * The date the report will no longer be accessible for download. A new report 
   * will need to be requested
   */
  private final IJetDate reportExpDate;
  
  /**
   * The URL where the report can be downloaded
   */
  private final String reportUrl;  
  

  /**
   * Turn jet json into an instance of this object
   * @param reportId The report id 
   * @param json json 
   * @return object 
   */
  public static ReportStatusRec fromJSON( final String reportId,
    final JsonObject json )     
  {
    Utils.checkNullEmpty( reportId, "reportId" );
    Utils.checkNull( json, "json" );

    return new Builder()
      .setMerchantId( json.getString( "merchant_id" ))
      .setProcessingEnd( JetDate.fromJetValueOrNull( json.getString( "processing_end" )))
      .setProcessingStart( JetDate.fromJetValueOrNull( json.getString( "processing_start" )))
      .setReportExpDate( JetDate.fromJetValueOrNull( json.getString( "report_expiration_date" )))
      .setReportId( reportId )
      .setReportUrl( json.getString( "report_url" ))
      .setRequestedDate( JetDate.fromJetValueOrNull( json.getString( "report_requested_date" )))
      .setStatus( ReportStatus.fromText( json.getString( "report_status" )))
      .setType( ReportType.fromText( json.getString( "report_type" )))
      .build();
  }
  
  
  /**
   * Constructor.
   * Creates an immutable object instance based on the builder properties.
   * @param builderClass Builder class type
   * @param b Builder instance 
   */
  protected ReportStatusRec( final Class<? extends ReportStatusRec.Builder> builderClass, final Builder b )
  {
    super( builderClass, b );
    
    //..Set the local properties from the builder here.
    merchantId = b.merchantId;
    reportId = b.reportId;
    requestedDate = b.requestedDate;
    status = b.status;
    type = b.type;
    processingStart = b.processingStart;
    processingEnd = b.processingEnd;
    reportExpDate = b.reportExpDate;
    reportUrl = b.reportUrl;
  }
  

  
  //////////////////////////////////////////////////////////////////////////////
  // ReportStatusRec Methods                                                            
  //////////////////////////////////////////////////////////////////////////////  
  
  
  /**
   * Convert the immutable instance into a mutable builder instance.
   * @return Builder
   */
  @Override
  public B toBuilder() 
  {
    return (B)super.toBuilder()
      .setMerchantId( merchantId )
      .setReportId( reportId )
      .setRequestedDate( requestedDate )
      .setStatus( status )
      .setType( type )
      .setProcessingStart( processingStart )
      .setProcessingEnd( processingEnd )
      .setReportExpDate( reportExpDate )
      .setReportUrl( reportUrl );
  }    
  
  
  /**
   * The type of report requested
   * @return the type
   */
  public ReportType getType()
  {
    return type;
  }

  /**
   * The date the report was requested
   * @return the requestedDate
   */
  public IJetDate getRequestedDate()
  {
    return requestedDate;
  }
  
  
  /**
   * The Jet defined merchant ID associated with the merchant account
   * @return the merchantId
   */
  public String getMerchantId()
  {
    return merchantId;
  }
  
  
  /**
   * The Jet defined report ID associated with the report requested
   * @return the reportId
   */
  public String getReportId()
  {
    return reportId;
  }

  /**
   * The current status of the report
   * @return the status
   */
  public ReportStatus getStatus()
  {
    return status;
  }

  /**
   * The date-time the report started processing
   * @return the processingStart
   */
  public IJetDate getProcessingStart()
  {
    return processingStart;
  }
  

  /**
   * The date-time the report ended processing
   * @return the processingEnd
   */
  public IJetDate getProcessingEnd()
  {
    return processingEnd;
  }
  
  
  /**
   * The date the report will no longer be accessible for download. A new 
   * report will need to be requested
   * @return the reportExpDate
   */
  public IJetDate getReportExpDate()
  {
    return reportExpDate;
  }
  
  
  /**
   * The URL where the report can be downloaded
   * @return the reportUrl
   */
  public String getReportUrl()
  {
    return reportUrl;
  }  
  
  
  /**
   * If the report is complete and ready for download or marked as failed.
   * @return is done
   */
  public boolean isDone()
  {
    return !getStatus().equals( ReportStatus.REQUESTED );
  }
}
