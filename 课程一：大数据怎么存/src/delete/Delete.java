package delete;

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

public class Delete {

	private static  BasicAWSCredentials credentials = 
			new BasicAWSCredentials(S3_Info.accessKey,S3_Info.secretKey);
    private static AmazonS3 conn;
    private static ClientConfiguration ccfg = new ClientConfiguration().
			withUseExpectContinue(true);
    private static EndpointConfiguration endpoint = 
    		new EndpointConfiguration(S3_Info.serviceEndpoint, S3_Info.signingRegion);
    
	public static void deleteObject(String keyName){
		if (credentials != null) {
            conn = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withClientConfiguration(ccfg)
                    .withEndpointConfiguration(endpoint)
                    .withPathStyleAccessEnabled(true)
                    .build();
		DeleteObjectRequest request = new DeleteObjectRequest(S3_Info.bucketName, keyName
		);
		conn.deleteObject(request);
		}
}
}
