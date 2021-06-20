package s3_writer;

import java.io.File;

import java.util.ArrayList;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class Upload {
    public final static String accessKey = "88939849D3327875CFFC";
    public final static String secretKey = "W0U1RkE4NzdFQkMzNDE3NjFBMDU1RjY3QzZEODM0";
    public final static String serviceEndpoint = "http://10.16.0.1:81";
    public static String signingRegion="";
    public static String filePath="/Volumes/HD/Onedrive/STUDY/大三下/实训/Exercise_3/stream_compute/splits/";
    public static String bucketName="edwards";
    private static long partSize=20<<20;

    public static void main(String keyName) {
        final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        final ClientConfiguration ccfg = new ClientConfiguration().withUseExpectContinue(true);

        final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint,
                signingRegion);

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withClientConfiguration(ccfg)
                .withEndpointConfiguration(endpoint).withPathStyleAccessEnabled(true).build(); // create Link to S3.


        // Create a list of UploadPartResponse objects. You get one of these
        // for each part upload.
        ArrayList<PartETag> partETags = new ArrayList<PartETag>();
        File file = new File(filePath + keyName);
        long contentLength = file.length();
        String uploadId = null;
        if (contentLength < (20 << 20)) {
            s3.putObject(bucketName,"upload/"+keyName,file);
            System.out.println( keyName+" has been Uploaded!");
        } else {
            try {
                // Step 1: Initialize.
                InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName,
                        keyName);
                uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
                System.out.format("Created upload ID was %s\n", uploadId);

                // Step 2: Upload parts.
                long filePosition = 0;
                for (int i = 1; filePosition < contentLength; i++) {
                    // Last part can be less than 20 MB. Adjust part size.
                    partSize = Math.min(partSize, contentLength - filePosition);

                    // Create request to upload a part.
                    UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(bucketName)
                            .withKey(keyName).withUploadId(uploadId).withPartNumber(i).withFileOffset(filePosition)
                            .withFile(file).withPartSize(partSize);

                    // Upload part and add response to our list.
                    System.out.format("Uploading part %d\n", i);
                    partETags.add(s3.uploadPart(uploadRequest).getPartETag());

                    filePosition += partSize;
                }

                // Step 3: Complete.
                System.out.println("Completing upload");
                CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName,
                        keyName, uploadId, partETags);

                s3.completeMultipartUpload(compRequest);
            } catch (Exception e) {
                System.err.println(e.toString());
                if (uploadId != null && !uploadId.isEmpty()) {
                    // Cancel when error occurred
                    System.out.println("Aborting upload");
                    s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, uploadId));
                }
                System.exit(1);
            }
            System.out.println("Done!");
        }

    }
}
