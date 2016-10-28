package com.mkch.youshi.util;

import com.google.gson.Gson;
import com.mkch.youshi.config.CommonConstants;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Smith on 2016/10/26.
 */

public class XUtil {

        /**
         * 发送get请求
         * @param <T>
         */
        public static <T> Callback.Cancelable Get(String url, Map<String,String> map, Callback.CommonCallback<T> callback){
            RequestParams params=new RequestParams(url);
            if(null!=map){
                for(Map.Entry<String, String> entry : map.entrySet()){
                    params.addQueryStringParameter(entry.getKey(), entry.getValue());
                }
            }
            Callback.Cancelable cancelable = x.http().get(params, callback);
            return cancelable;
        }

        /**
         * 发送post请求
         * @param <T>
         */
        public static <T> Callback.Cancelable Post(String url, Map<String,Object> map, Callback.CommonCallback<T> callback){
            RequestParams params=new RequestParams(url);
            if(null!=map){
                for(Map.Entry<String, Object> entry : map.entrySet()){
                    params.addParameter(entry.getKey(), entry.getValue());
                }
            }
            Callback.Cancelable cancelable = x.http().post(params, callback);
            return cancelable;
        }


        /**
         * 上传文件
         * @param <T>
         */
        public static <T> Callback.Cancelable UpLoadFile(String url, File file, Callback.CommonCallback<T> callback){

            RequestParams params = new RequestParams(url);
            String loginCode = CommonUtil.getUserInfo(UIUtils.getContext()).getLoginCode();
            params.setMultipart(true);

            if (file != null){
                params.addBodyParameter("uploadFile",file);
                if (loginCode != null) {
                    params.addHeader("sVerifyCode", loginCode);//头信息
                    params.addHeader("FileName", file.getName());//头信息
                }
            }

            Callback.Cancelable cancelable = x.http().post(params, callback);
            return cancelable;
        }

        /**
         * 下载文件
         * @param <T>
         */
        public static <T> Callback.Cancelable DownLoadFile(String url, String filepath, Callback.CommonCallback<T> callback){
            RequestParams params=new RequestParams(url);
            //设置断点续传
//            String loginCode = CommonUtil.getUserInfo(UIUtils.getContext()).getLoginCode();
//            params.addHeader("sVerifyCode", loginCode);//头信息
            params.setAutoResume(true);
            params.setSaveFilePath(filepath);

            Callback.Cancelable cancelable = x.http().post(params, callback);
            return cancelable;
        }
    }


    class JsonResponseParser implements ResponseParser {
        //检查服务器返回的响应头信息
        @Override
        public void checkResponse(UriRequest request) throws Throwable {
        }

        /**
         * 转换result为resultType类型的对象
         *
         * @param resultType  返回值类型(可能带有泛型信息)
         * @param resultClass 返回值类型
         * @param result      字符串数据
         * @return
         * @throws Throwable
         */
        @Override
        public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
            return new Gson().fromJson(result, resultClass);
        }
    }


