package s3_information;

import java.io.File;
import java.util.HashMap;
import multipart_etag.Multipart_File_Etag;
import md5.MD5;
import com.amazonaws.services.s3.AmazonS3;


public class S3_Info {

	public final static String bucketName = "sync";
	public final static String filePath   = "D:\\OneDrive\\STUDY\\大三下\\实训\\Exercise 1\\Sync_Test";
	public final static String savePath   = "D:\\OneDrive\\STUDY\\大三下\\实训\\Exercise 1\\Sync_Test";
	public final static String cloudPath  = "/"; //bucket's folder
	public final static String accessKey = "88939849D3327875CFFC";
	public final static String secretKey = "W0U1RkE4NzdFQkMzNDE3NjFBMDU1RjY3QzZEODM0";
	public final static String serviceEndpoint = "http://10.16.0.1:81";
	public final static String signingRegion = "";
	public static long partSize = 5 << 20;
	public static String local_name_str[] = new String[1000]; //save local folder's file name
	public static int MAX=0;
	public static String local_md5[] = new String[1000];      //save local folder's md5
	public static HashMap<String,String> cloud_files= new HashMap<String,String>();
	public static String local_keyname="";
	public static String cloud_keyName="";
	
	public static void generate_local_file_list(String filePath) {
			File file = new File(filePath);
			File[] tempList = file.listFiles();
			MAX=tempList.length;
			for (int i = 0; i < tempList.length; i++) {
				if (tempList[i].isFile()) {
					local_name_str[i]=tempList[i].getName();
					System.out.println("Scanning Local File"+" "+i+" "+tempList[i].getName());
				}
			}
	}
	public static void generate_local_file_md5(String filePath) {
		File file = new File(filePath);
		File[] tempList = file.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			if(tempList[i].length()>(20<<20)) {
				local_md5[i]=Multipart_File_Etag.getMultipartFileEtag(filePath+"\\"+tempList[i].getName(), (int)S3_Info.partSize);
				//System.out.println("Caculating Big File Local MD5"+" "+i+" "+local_md5[i]);
			}else {
				local_md5[i]=md5.MD5.getMD5(tempList[i]);
				//System.out.println("Caculating Local MD5"+" "+i+" "+local_md5[i]);
			}
		}
		
	 }
}



