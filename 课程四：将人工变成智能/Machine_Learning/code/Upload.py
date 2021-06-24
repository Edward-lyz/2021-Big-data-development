import os.path
from boto3 import Session

endpoint_url = "http://10.16.0.1:81"
access_key = "88939849D3327875CFFC"
secret_key = "W0U1RkE4NzdFQkMzNDE3NjFBMDU1RjY3QzZEODM0"
bucket = "edwards"


def upload():
    session = Session(access_key, secret_key)
    s3_client = session.client('s3', endpoint_url=endpoint_url)
    for root, dirs, files in os.walk(os.path.abspath('..') + '/result'):
        for file in files:
            if not file.startswith('.'):
                path = os.path.join(root, file)
                print("正在上传 ", file)
                resp = s3_client.put_object(Bucket=bucket, Key="result/" + file, Body=open(path, 'r').read())
