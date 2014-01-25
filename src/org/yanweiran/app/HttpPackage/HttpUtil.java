package org.yanweiran.app.HttpPackage;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by lenov on 14-1-17.
 */
public class HttpUtil {
    public static HttpClient httpClient= new DefaultHttpClient();
    public static  final  String BASE_URL = "http://115.28.46.167:83/app_feed/";

    /**
    * 发送请求的 url
     * 服务器响应字符串
     *
    */
    public static String getRequest(final String url)
        throws Exception
    {
        FutureTask<String> task = new FutureTask<String>(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        HttpGet get = new HttpGet(url);
//                        List<NameValuePair> params = new ArrayList<NameValuePair>();
////                        for(String key : rawParas.keySet())
////                        {
////                            params.add(new BasicNameValuePair(key,rawParas.get(key)));
////                        }
                        HttpResponse httpResponse = httpClient.execute(get);
                        if(httpResponse.getStatusLine().getStatusCode() == 200)
                        {
                            String result = EntityUtils.toString(httpResponse.getEntity());
                            return result;
                        }
                        return  null;
                    }
                }
        );
        new Thread(task).start();
        return task.get();
    }


    /**
     * 发送请求的url
     * 请求参数
     * 服务器响应字符串
     *
     * */

    public static String postRequest(final String url,final Map<String,String> rawParams)
        throws  Exception
    {
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                HttpPost post = new HttpPost(url);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for(String key : rawParams.keySet())
                {
                    params.add(new BasicNameValuePair(key, rawParams.get(key)));
                }
                    post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
                    HttpResponse httpResponse = httpClient.execute(post);
                if (httpResponse.getStatusLine().getStatusCode()==200)
                {
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    return result;
                }
                return null;
            }
        });
        new Thread(task).start();
        return task.get();
    }

}
