package cloud_file;

import s3_information.S3_Info;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.StringUtils;
import com.sun.javafx.collections.MappingChange.Map;

import org.springframework.beans.factory.annotation.Value;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Cloud_File {
	private static  BasicAWSCredentials credentials = 
			new BasicAWSCredentials(S3_Info.accessKey,S3_Info.secretKey);
    private static AmazonS3 conn;
    private static ClientConfiguration ccfg = new ClientConfiguration().
			withUseExpectContinue(true);
    private static EndpointConfiguration endpoint = 
    		new EndpointConfiguration(S3_Info.serviceEndpoint, S3_Info.signingRegion);


	public static HashMap getBucketObjects (String bucketName, String prefix) {
		HashMap <String,String>cloud_files= new HashMap<String,String>();
		if (credentials != null) {
            conn = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withClientConfiguration(ccfg)
                    .withEndpointConfiguration(endpoint)
                    .withPathStyleAccessEnabled(true)
                    .build();
            
        ListObjectsRequest listObjectsRequest  = new ListObjectsRequest().withBucketName(bucketName);
        ObjectListing objects = conn.listObjects(listObjectsRequest);
        do
        {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries())
            {
                
                //System.out.println(objectSummary.getKey()+"  " + "MD5: "+ objectSummary.getETag());
                cloud_files.put(objectSummary.getKey(), objectSummary.getETag());
       
            }
            objects = conn.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());

    }
	return cloud_files;
  }
}
