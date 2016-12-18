
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIHttpClient;
import com.sheepguru.jetimport.api.PostFile;
import com.sheepguru.jetimport.api.jet.JetAPI;
import com.sheepguru.jetimport.api.jet.JetAPIResponse;
import com.sheepguru.jetimport.api.jet.JetConfig;
import com.sheepguru.jetimport.api.jet.JetException;
import com.sheepguru.jetimport.api.jet.Utils;
import com.sun.net.httpserver.Headers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;


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
  public BulkUploadAuthRec getUploadToken()
    throws APIException, JetException
  {
    return BulkUploadAuthRec.fromJSON( sendGetUploadToken().getJsonObject());
  }
  
  
  /**
   * Once you receive the url to upload to from getUploadToken(), feed that 
   * into the url argument in this method along with the file to upload..
   * @param url Url from getUploadToken()
   * @return response
   * @throws APIException
   * @throws JetException 
   */
  public JetAPIResponse sendAuthorizedFile( final String url, final PostFile file )
    throws APIException, JetException
  {
    final Map<String,String> headers = new HashMap<>();
    headers.put( "x-ms-blob-type", "blockblob" );
    
    return put( url, file, headers );
  }
    
  
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
  public JetAPIResponse sendPostUploadedFiles( final String uploadUrl, 
    final PostFile file, 
    BulkUploadFileType uploadType ) throws APIException, JetException
  {
    Utils.checkNull( file, "file" );
    
    JsonObject o = Json.createObjectBuilder().add("url", uploadUrl)
      .add( "file_type", uploadType.getText())
      .add( "file_name", file.getFilename()).build();
    
    
    return JetAPIResponse.createFromAPIResponse( post(
      config.getPostBulkUploadedFilesUrl(), o.toString(), getJSONHeaderBuilder().build()
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
  public FileIdRec getJetFileId( final String fileId )
    throws APIException, JetException
  {
    return FileIdRec.fromJSON( sendGetJetFileId( fileId ).getJsonObject());
  }
  
    
  /**
   * Generate a bulk Product Sku file to upload 
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

        writeInputStreamToJson( reader, g, outputFile );

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
   * Generate a bulk product sku file to upload 
   * @param products
   * @param outputFile 
   * @throws JetException
   */
  public void generateBulkSkuUploadFile( final List<ProductRec> products, 
    final File outputFile ) throws JetException
  {
    try ( final JsonGenerator g = Json.createGenerator( new GZIPOutputStream( 
      new FileOutputStream( outputFile )))) {
      
      final Charset cs = Charset.forName( "UTF-8" );
      
      g.writeStartObject(); //.. adds a { 
      
      //..Write each product to the json stream 
      for( final ProductRec p : products )
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
  
  
  /**
   * Assuming that each line of a BufferedReader contains a complete json
   * object (ProductRec json), this will simply take each line and write it 
   * to some json generator (stream).
   * 
   * @param reader Buffered Reader with json 
   * @param g Json Generator output stream 
   * @param outputFile The output filename (just used for an exception message)
   * @throws JetException If there is a problem with the product json 
   * @throws IOException If there is a problem reading from the stream 
   */
  private void writeInputStreamToJson( final BufferedReader reader, 
    final JsonGenerator g, final File outputFile ) 
    throws JetException, IOException
  {
    //..Write each product to the json stream 
    String line;

    while (( line = reader.readLine()) != null )
    {
      //..Attempt to convertfrom json to product 
      try ( final JsonReader jsonReader = Json.createReader( 
        new StringReader( line ))) 
      {
        final ProductRec p = ProductRec.fromJSON( jsonReader.readObject());

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
  }  
}