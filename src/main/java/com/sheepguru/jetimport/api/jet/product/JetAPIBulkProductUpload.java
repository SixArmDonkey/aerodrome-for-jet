
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIHttpClient;
import com.sheepguru.jetimport.api.PostFile;
import com.sheepguru.jetimport.api.jet.JetAPI;
import com.sheepguru.jetimport.api.jet.JetAPIResponse;
import com.sheepguru.jetimport.api.jet.JetConfig;
import com.sheepguru.jetimport.api.jet.JetException;
import com.sheepguru.jetimport.api.jet.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;


/**
 * This should handle bulk product uploads and stuff.....
 * @author john Quinn
 */
public class JetAPIBulkProductUpload extends JetAPI
{
  public JetAPIBulkProductUpload( final APIHttpClient client, 
    final JetConfig config )
  {
    super( client, config );
  }
  
  
  /**
   * Retrieve a token for uploading some file.
   * @return api response
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendGetUploadToken()
    throws APIException, JetException
  {
    return get(
      config.getGetBulkUploadTokenUrl(),
      getJSONHeaderBuilder().build()
    );
  }
  
  
  /**
   * Retrieve an upload token for uploading a bulk feed of some sort.
   * @return auth token
   * @throws APIException
   * @throws JetException 
   */
  public BulkUploadAuth getUploadToken()
    throws APIException, JetException
  {
    return BulkUploadAuth.fromJSON( sendGetUploadToken().getJsonObject());
  }
  
  
  /**
   * I have no idea.... The docs are blank besides wanting this header and "put"
   * @param url
   * @return
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendUploadedFile( final String url )
    throws APIException, JetException
  {
    final Map<String,String> headers = new HashMap<>();
    headers.put( "x-ms-blob-type", "blockblob" );
    
    return put( url, "", headers );
  }
    
  
  /**
   * Not quite sure yet...
   * @param file
   * @param uploadType
   * @return
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendPostUploadedFiles( final PostFile file, 
    BulkUploadFileType uploadType ) throws APIException, JetException
  {
    Utils.checkNull( file, "file" );
    
    final Map<String,PostFile> files = new HashMap<>();
    files.put( file.getFilename(), file );
    
    return JetAPIResponse.createFromAPIResponse( post(
      config.getPostBulkUploadedFilesUrl(),
      null,
      files,
      getJSONHeaderBuilder().build()
    ));
  }
  
  
  /**
   * Query the status of an uploaded file..
   * @param fileId Jet File id 
   * @return The status 
   * @throws APIException
   * @throws JetException
   */
  public JetAPIResponse sendGetJetFileId( final String fileId )
    throws APIException, JetException 
  {
    return get(
      config.getGetBulkJetFileIdUrl( fileId ),
      getJSONHeaderBuilder().build()
    );
  }
  
  
  /**
   * Query the status of an uploaded file..
   * @param fileId Jet File id 
   * @return The status 
   * @throws APIException
   * @throws JetException
   */
  public JetFileId getJetFileId( final String fileId )
    throws APIException, JetException
  {
    return JetFileId.fromJSON( sendGetJetFileId( fileId ).getJsonObject());
  }
  
  
  /**
   * 
   * @param input
   * @param outputFile 
   * @throws JetException 
   */
  public void generateBulkSkuUploadFile( final InputStream input, 
    final File outputFile ) throws JetException
  {
    try ( final BufferedReader reader = new BufferedReader(
      new InputStreamReader( input )))
    {
      try ( final JsonGenerator g = getGzipJsonOutputStream( outputFile ))
      {
        g.writeStartObject(); //.. adds a { 

        //..Write each product to the json stream 
        String line;
        
        while (( line = reader.readLine()) != null )
        {
          //..Attempt to convertfrom json to product 
          try ( final JsonReader jsonReader = Json.createReader( 
            new StringReader( line ))) 
          {
            final JetProduct p = JetProduct.fromJSON( jsonReader.readObject());
            
            //..Writes a name/value pair to the json stream
            // sku : { product object } 
            g.write( p.getMerchantSku(), p.toJSON());
          } catch( JsonException e ) {
            //..Failed to parse the json
            throw new JetException( "Failed to parse JSON: " + line 
              + " to file " + outputFile, e );
          }
          
          g.write( line );
        }

        //..adds a } 
        g.writeEnd();        
      } catch( IOException e ) {
        //..Failed to open the output stream 
        throw new JetException( "Failed to open the output stream for " 
          + outputFile, e );
      }
    } catch( IOException e ) {
      //..Failed to open the input stream reader 
      throw new JetException( "Failed to open the reader for the supplied "
        + "input stream", e );
    }
  }
    
  
  /**
   * 
   * @param products
   * @param outputFile 
   * @throws JetException
   */
  public void generateBulkSkuUploadFile( final List<JetProduct> products, 
    final File outputFile ) throws JetException
  {
    try ( final JsonGenerator g = Json.createGenerator( new GZIPOutputStream( 
      new FileOutputStream( outputFile )))) {
      
      g.writeStartObject(); //.. adds a { 
      
      //..Write each product to the json stream 
      for( final JetProduct p : products )
      {
        //..Writes a name/value pair to the json stream
        // sku : { product object } 
        g.write( p.getMerchantSku(), p.toJSON());
      }
      
      //..adds a } 
      g.writeEnd();
    } catch( IOException e ) {
      throw new JetException( "Failed to open output stream: " 
        + outputFile, e );
    }
  }
  
  
  /**
   * Creates a gzipped output stream to some file on disk and provides a 
   * JsonGenerator to write some json to that file.   
   * @param outputFile Where to write 
   * @return Resource 
   * @throws IOException if it cant open the file 
   */
  private JsonGenerator getGzipJsonOutputStream( final File outputFile ) 
    throws IOException
  {
    return Json.createGenerator( 
      new GZIPOutputStream( new FileOutputStream( outputFile ))
    );
  }
}