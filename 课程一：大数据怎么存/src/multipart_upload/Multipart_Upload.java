package multipart_upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import s3_information.S3_Info;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.mediastoredata.model.PutObjectRequest;
import com.amazonaws.services.mediastoredata.model.PutObjectResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class Multipart_Upload {
	public static void main(String keyName) {
		final BasicAWSCredentials credentials = new BasicAWSCredentials(S3_Info.accessKey, S3_Info.secretKey);
		final ClientConfiguration ccfg = new ClientConfiguration().withUseExpectContinue(true);

		final EndpointConfiguration endpoint = new EndpointConfiguration(S3_Info.serviceEndpoint,
				S3_Info.signingRegion);

		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withClientConfiguration(ccfg)
				.withEndpointConfiguration(endpoint).withPathStyleAccessEnabled(true).build(); // create Link to S3.


		// Create a list of UploadPartResponse objects. You get one of these
		// for each part upload.
		ArrayList<PartETag> partETags = new ArrayList<PartETag>();
		File file = new File(S3_Info.filePath + "\\" + keyName);
		long contentLength = file.length();
		String uploadId = null;
		if (contentLength < (20 << 20)) {
			s3.putObject(S3_Info.bucketName,keyName,file);
			System.out.println( keyName+" has been Uploaded!");
		} else {
			try {
				// Step 1: Initialize.
				InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(S3_Info.bucketName,
						keyName);
				uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
				System.out.format("Created upload ID was %s\n", uploadId);

				// Step 2: Upload parts.
				long filePosition = 0;
				for (int i = 1; filePosition < contentLength; i++) {
					// Last part can be less than 20 MB. Adjust part size.
					S3_Info.partSize = Math.min(S3_Info.partSize, contentLength - filePosition);

					// Create request to upload a part.
					UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(S3_Info.bucketName)
							.withKey(keyName).withUploadId(uploadId).withPartNumber(i).withFileOffset(filePosition)
							.withFile(file).withPartSize(S3_Info.partSize);

					// Upload part and add response to our list.
					System.out.format("Uploading part %d\n", i);
					partETags.add(s3.uploadPart(uploadRequest).getPartETag());

					filePosition += S3_Info.partSize;
				}

				// Step 3: Complete.
				System.out.println("Completing upload");
				CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(S3_Info.bucketName,
						keyName, uploadId, partETags);

				s3.completeMultipartUpload(compRequest);
			} catch (Exception e) {
				System.err.println(e.toString());
				if (uploadId != null && !uploadId.isEmpty()) {
					// Cancel when error occurred
					System.out.println("Aborting upload");
					s3.abortMultipartUpload(new AbortMultipartUploadRequest(S3_Info.bucketName, keyName, uploadId));
				}
				System.exit(1);
			}
			System.out.println("Done!");
		}

	}
}
