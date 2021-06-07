package main;

import java.io.File;
import s3_information.S3_Info;
import cloud_file.Cloud_File;
import delete.Delete;
import multipart_download.Multipart_Download;
import multipart_etag.Multipart_File_Etag;
import multipart_upload.Multipart_Upload;

public class Main extends Thread {

	private  static String atStart="Yes";
	public static void main(String[] args)  {
		while(true){
		S3_Info.generate_local_file_list(S3_Info.filePath);
		S3_Info.generate_local_file_md5(S3_Info.filePath);	
		S3_Info.cloud_files=Cloud_File.getBucketObjects(S3_Info.bucketName, S3_Info.cloudPath);
		for(int i=0;i<S3_Info.MAX;i++) {
			
		S3_Info.cloud_keyName=S3_Info.cloudPath+S3_Info.local_name_str[i];
		S3_Info.local_keyname=S3_Info.local_name_str[i]; //Set the address.
		
		if(S3_Info.cloud_files.containsKey(S3_Info.local_name_str[i])) {
			
			if(S3_Info.cloud_files.get(S3_Info.local_name_str[i]).equals(S3_Info.local_md5[i])) {
				System.out.println("The file "+S3_Info.local_name_str[i]+" is the Newest!");
			}
			else {
				System.out.println("The file "+S3_Info.local_name_str[i]+ " isn't the newest£¡");
				Multipart_Upload.main(S3_Info.local_name_str[i]);
			}
		}else {
			System.out.println("Can't find file "+S3_Info.local_name_str[i]+" in the cloud!,Now Uploading!");
			Multipart_Upload.main(S3_Info.local_name_str[i]);
		}
				
				
		}
		
		S3_Info.cloud_files=Cloud_File.getBucketObjects(S3_Info.bucketName, S3_Info.cloudPath);
		for(String s : S3_Info.cloud_files.keySet()) {
			int count=0;
			for (int i= 0;i<S3_Info.MAX;i++) {
				if(!s.equals(S3_Info.local_name_str[i])) {
					//System.out.println(s);
					//System.out.println(S3_Info.local_name_str[i]);
					count+=1;
					//System.out.println(count);
					//Do nothing
				}else {
					break;
				}
			}
			if(count==(S3_Info.MAX)){
				System.out.println("We can't find the same file  "+s+" in local");
				
				if (atStart.equals("Yes")) {
					System.out.println("Now Downloading from cloud!");
					Multipart_Download.main(s);
					
				}else {
					System.out.println("Now Deleting files!");
					Delete.deleteObject(s);
				}
				
			}
		}
		System.out.println("Now Your Files are all Synced!");
		try
        { 
            Thread.sleep(15000);    //ÑÓÊ±15s
        }
        catch(InterruptedException e)
        { 
            e.printStackTrace(); 
        }
		atStart="No";
	}
	}
}


