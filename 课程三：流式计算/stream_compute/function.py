import json
import os
import boto3
import pandas as pd


endpoint_url = 'http://10.202.92.12:81'

s3_client = boto3.client('s3',
        aws_access_key_id='FFEF2034AE29FFEB64C9',
        aws_secret_access_key='WzQwOUZFRkYzMUU5NDZDM0RENTAzREFGQjBEREIxNUI0QzI2QjUzMjJd',
        endpoint_url=endpoint_url)


def download_file(bucket, key, download_path):
    key = key.encode('utf-8', errors='surrogateescape').decode('utf-8')
    print(f'download {endpoint_url}/{bucket}/{key}')
    response = s3_client.get_object(Bucket=bucket, Key=key)

    try:
        os.unlink(download_path)
    except:
        pass

    try:
        f = open(download_path, 'wb')
        f.write(response['Body'].read())
        f.close()
    except:
        raise ValueError(str(response))


def lambda_handler(event, context):
    evtdata = event['Records'][0]['s3']
    bucket = evtdata['bucket']['name']
    key = evtdata['object']['key']
    put_endpoint = endpoint_url

    print(f'slicing {put_endpoint}/{bucket}/{key}')

    file_path = 

    #download_file(bucket, key, file_path)

    date_col = 'ccsj'
    pk_col = ['sfzhm']
    column_names = 'sfzhm,rymc,bc,ccsj,dpsj,ccrq,cfd,mdd'

    col_set = set(column_names.split(","))

    # 读取文件
    df = pd.read_csv(file_path)

    # 对称差集
    d_set = col_set ^ set(df.columns)
    if len(d_set) != 0:
        return {
            'statusCode': 500,
            'body': 'data columns different!'
        }

    # 转换时间类型
    df[date_col] = pd.to_datetime(df[date_col], errors='ignore')

    # 缺失值处理
    df.dropna(subset=[date_col], inplace=True)

    # 时间索引
    df = df.set_index(date_col, drop=False).sort_index(axis=0)

    df_period = df.to_period('M')

    m_period = [str(x) for x in set(df_period.index.asfreq('M'))]

    for year in set(df_period.index.asfreq('A')):
        year = str(year)
        for m in range(1, 13):
            f = ''
            if m < 10:
                f = 0
            m_ind = "%s-%s%s" % (year, f, m)
            print("date:%s" % m_ind)
            if m_ind in m_period:
                df_slice = df.get(m_ind)
                print("length:%s" % len(df_slice))

                filename = '%s.csv' % m_ind
                new_path = '/tmp/' + filename
                slice_key = os.path.join("bingo_edw", "slice", f'dt={year}', filename)
                file_path = f'/tmp/{filename}'

                try:
                    print(f"update file {filename}")
                    download_file(bucket, slice_key, file_path)

                    # 读取文件
                    df_slice_old = pd.read_csv(file_path)

                    df_slice = pd.concat([df_slice_old, df_slice])

                    df_slice.drop_duplicates(pk_col, keep="last", inplace=True)
                except:
                    print(f"create new file:{filename}")
                    pass
                df_slice.to_csv(new_path, index=False)
                response = s3_client.put_object(
                    Bucket=bucket,
                    Key=slice_key,
                    Body=open(new_path, "rb"),
                    ContentType="text/csv"
                )
            else:
                print("length:0")

            print("-"*40)

    return {
        'statusCode': 200,
        'body': 'data slicing success!'
    }

if __name__=="__main__":
    lambda_handler({"Records":[{"eventName":"ObjectCreated:Put","eventSource":"aws:s3","eventTime":"2021-06-17T01:19:37.114Z","eventVersion":"2.0","s3":{"bucket":{"name":"edwards","ownerIdentity":{"principalId":"5be89721-77b9-472d-ba65-9717f6bddc7c"}},"configurationId":"Partition","object":{"eTag":"3d30280f51ec9d0bd7bf83409e186529","key":"upload/2014055311032","size":2597334,"versionId":"ERL.DAsGJoZNi4PlMXlaWcD0l7GYG7I"},"s3SchemaVersion":"1.0"},"userIdentity":{"principalId":"5be89721-77b9-472d-ba65-9717f6bddc7c"}}]},"")
