package multipart_download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import s3_information.S3_Info;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class Multipart_Download {
	public static void main(String keyName) {
		final BasicAWSCredentials credentials = new BasicAWSCredentials(S3_Info.accessKey,S3_Info.secretKey);
		final ClientConfiguration ccfg = new ClientConfiguration().
				withUseExpectContinue(true);

		final EndpointConfiguration endpoint = 
new EndpointConfiguration(S3_Info.serviceEndpoint, S3_Info.signingRegion);

		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(ccfg)
                .withEndpointConfiguration(endpoint)
                .withPathStyleAccessEnabled(true)
                .build();
		
		final String filePath = Paths.get(S3_Info.savePath, keyName).toString();
		
		File file = new File(filePath);
		
		S3Object o = null;
		
		S3ObjectInputStream s3is = null;
		FileOutputStream fos = null;
		try {
			// Step 1: Initialize.
			ObjectMetadata oMetaData = s3.getObjectMetadata(S3_Info.bucketName, keyName);
			final long contentLength = oMetaData.getContentLength();
			if(contentLength<(20<<20)) {
				S3Object s3object = s3.getObject(S3_Info.bucketName, keyName);
				S3ObjectInputStream inputStream = s3object.getObjectContent();
				FileUtils.copyInputStreamToFile(inputStream, new File(filePath + keyName));
				System.out.format("save %s to %s\n", keyName, filePath);
			}
			else {
				final GetObjectRequest downloadRequest = new GetObjectRequest(S3_Info.bucketName, keyName);
				fos = new FileOutputStream(file);

				// Step 2: Download parts.
				long filePosition = 0;
				for (int i = 1; filePosition < contentLength; i++) {
					// Last part can be less than 5 MB. Adjust part size.
					S3_Info.partSize = Math.min(S3_Info.partSize, contentLength - filePosition);

					// Create request to download a part.
					downloadRequest.setRange(filePosition, filePosition + S3_Info.partSize);
					o = s3.getObject(downloadRequest);

					// download part and save to local file.
					System.out.format("Downloading part %d\n", i);

					filePosition += S3_Info.partSize+1;
					s3is = o.getObjectContent();
					byte[] read_buf = new byte[64 * 1024];
					int read_len = 0;
					while ((read_len = s3is.read(read_buf)) > 0) {
						fos.write(read_buf, 0, read_len);
					}
				}

				// Step 3: Complete.
				System.out.println("Completing download");

				System.out.format("save %s to %s\n", keyName, filePath);}
			} catch (Exception e) {
				System.err.println(e.toString());
				
				System.exit(1);
			} finally {
				if (s3is != null) try { s3is.close(); } catch (IOException e) { }
				if (fos != null) try { fos.close(); } catch (IOException e) { }
			}
			System.out.println("Done!");
			}
			
	}



