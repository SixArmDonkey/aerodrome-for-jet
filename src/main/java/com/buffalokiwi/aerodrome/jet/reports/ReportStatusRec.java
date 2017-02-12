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
package com.buffalokiwi.aerodrome.jet.reports;

import com.buffalokiwi.aerodrome.jet.JetDate;


/**
 * Represents a report status response 
 * @author John Quinn
 */
public class ReportStatusRec
{
  /**
   * The Jet defined merchant ID associated with the merchant account
   */
  private String merchantId;
  
  /**
   * The Jet defined report ID associated with the report requested
   */
  private String reportId;
  
  /**
   * The date the report was requested
   */
  private JetDate requestedDate;
  
  /**
   * The current status of the report
   */
  private ReportStatus status;
  
  /**
   * The type of report requested.
   */
  private ReportType type;
  
  /**
   * The date-time the report started processing
   */
  private JetDate processingStart;
  
  /**
   * The date-time the report ended processing
   */
  private JetDate processingEnd;
  
  /**
   * The date the report will no longer be accessible for download. A new report 
   * will need to be requested
   */
  private JetDate reportExpDate;
  
  /**
   * The URL where the report can be downloaded
   */
  private String reportUrl;
}
