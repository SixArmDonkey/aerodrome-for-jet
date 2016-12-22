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

package com.sheepguru.aerodrome.api.jet.products;

import com.sheepguru.aerodrome.api.APIException;
import com.sheepguru.aerodrome.api.PostFile;
import com.sheepguru.aerodrome.api.jet.IJetAPI;
import com.sheepguru.aerodrome.api.jet.IJetAPIResponse;
import com.sheepguru.aerodrome.api.jet.JetException;
import java.io.File;
import java.io.InputStream;
import java.util.List;


/**
 * 
 * @author John Quinn
 */
public interface IJetAPIBulkProductUpload extends IJetAPI
{

  /**
   * Generate a bulk Product Sku file to upload
   * @param input
   * @param outputFile
   * @throws JetException
   */
  public void generateBulkSkuUploadFile(final InputStream input, final File outputFile) throws JetException;

  /**
   * Generate a bulk product sku file to upload
   * @param products
   * @param outputFile
   * @throws JetException
   */
  public void generateBulkSkuUploadFile(final List<ProductRec> products, final File outputFile) throws JetException;

  /**
   * Query the status of an uploaded file..
   * @param fileId Jet File id
   * @return The status
   * @throws APIException
   * @throws JetException
   */
  public FileIdRec getJetFileId(final String fileId) throws APIException, JetException;

  /**
   * Retrieve an upload token for uploading a bulk feed of some sort.
   * @return auth token
   * @throws APIException
   * @throws JetException
   */
  public BulkUploadAuthRec getUploadToken() throws APIException, JetException;

  /**
   * Once you receive the url to upload to from getUploadToken(), feed that
   * into the url argument in this method along with the file to upload..
   * @param url Url from getUploadToken()
   * @return response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendAuthorizedFile(final String url, final PostFile file) throws APIException, JetException;

  /**
   * Query the status of an uploaded file..
   * @param fileId Jet File id
   * @return The status
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetJetFileId(final String fileId) throws APIException, JetException;

  /**
   * Retrieve a token for uploading some file.
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetUploadToken() throws APIException, JetException;

  /**
   * Get authorization to add an additional file to an existing uploadToken,
   * AND/OR I'm pretty sure this is required to tell Jet what type of file
   * was uploaded, and to start the batch import on Jet itself.
   * The documentation on Jet is lacking, well documentation.
   *
   * @param file File to send
   * @param uploadType File type
   * @return
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPostUploadedFiles(final String uploadUrl, final PostFile file, BulkUploadFileType uploadType) throws APIException, JetException;
  
}
