package com.lazydsr.util.net;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * UtilHttpRequestHelper
 * PROJECT_NAME: lazy-utils
 * PACKAGE_NAME: com.lazy.com.lazydsr.util.net
 * Created by Lazy on 2017/7/11 10:28
 * Version: 1.0
 * Info:发起http和https请求工具类
 */
public class UtilHttpRequestHelper {
    private static int READ_TIMEOUT = 30000;
    private static int CONNECT_TIMEOUT = 30000;
    private static String CHARSET_UTF_8 = "UTF-8";


    /**
     * 使用HttpURLConnection发送get请求
     *
     * @param requestUrl 请求地址
     * @return 返回结果
     */
    public static String sendHttpGet(String requestUrl) {
        return sendHttpGet(requestUrl, null);
    }

    /**
     * 使用HttpURLConnection发送get请求
     *
     * @param requestUrl    请求地址
     * @param requestParams 参数map String:参数名，Object:参数值
     * @return 返回结果
     */
    public static String sendHttpGet(String requestUrl, Map<String, Object> requestParams) {
        return sendHttpGet(requestUrl, requestParams, CHARSET_UTF_8);
    }

    /**
     * 使用HttpURLConnection发送get请求
     *
     * @param requestUrl    请求地址
     * @param requestParams 参数map String:参数名，Object:参数值
     * @param charset       字符集
     * @return 返回结果
     */
    public static String sendHttpGet(String requestUrl, Map<String, Object> requestParams, String charset) {
        StringBuffer resultBuffer = null;
        // 构建请求参数

        HttpURLConnection conn = null;
        BufferedReader br = null;
        try {
            URL url = new URL(requestUrl + "?" + getRequestString(requestParams));
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.connect();
            resultBuffer = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            String temp;
            while ((temp = br.readLine()) != null) {
                resultBuffer.append(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return resultBuffer.toString();
    }

    /**
     * 使用HttpURLConnection发送post请求
     * 默认使用UTF-8进行编码
     *
     * @param requestUrl 请求地址
     * @return 返回结果
     */
    public static String sendHttpPost(String requestUrl) {
        return sendHttpPost(requestUrl, null);
    }

    /**
     * 使用HttpURLConnection发送post请求
     * 默认使用UTF-8进行编码
     *
     * @param requestUrl    请求地址
     * @param requestParams 参数map String:参数名，Object:参数值
     * @return 返回结果
     */
    public static String sendHttpPost(String requestUrl, Map<String, Object> requestParams) {
        return sendHttpPost(requestUrl, requestParams, CHARSET_UTF_8);
    }

    /**
     * 使用HttpURLConnection发送post请求
     *
     * @param requestUrl    请求地址
     * @param requestParams 参数map String:参数名，Object:参数值
     * @param charset       字符集
     * @return 返回结果
     */
    public static String sendHttpPost(String requestUrl, Map<String, Object> requestParams, String charset) {
        StringBuffer resultBuffer = null;
        OutputStream outputStream = null;
        BufferedReader responseReader = null;
        HttpURLConnection conn = null;
        try {
            resultBuffer = new StringBuffer();
            // 建立连接
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            // //设置连接属性
            conn.setDoOutput(true);// 使用 URL 连接进行输出
            conn.setDoInput(true);// 使用 URL 连接进行输入
            conn.setUseCaches(false);// 忽略缓存
            conn.setRequestMethod("POST");// 设置URL请求方法
            // 设置请求属性
            // 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            byte[] requestParamsBytes = getRequestString(requestParams).getBytes(charset);
            conn.setRequestProperty("Content-length", "" + requestParamsBytes.length);
            //httpConn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setRequestProperty("Charset", charset);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            // 建立输出流，并写入数据
            outputStream = conn.getOutputStream();
            outputStream.write(requestParamsBytes);
            outputStream.close();
            // 获得响应状态
            int responseCode = conn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功
                String readLine;
                // 处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
                while ((readLine = responseReader.readLine()) != null) {
                    resultBuffer.append(readLine);
                }
                responseReader.close();
            } else {
                resultBuffer.append("请求错误：" + responseCode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (responseReader != null) {
                try {
                    responseReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return resultBuffer.toString();
    }

    public static String sendHttpPostUploadFile(String requestUrl, String[] uploadFilePaths, String charset) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        try {
            // 统一资源
            URL url = new URL(requestUrl);
            // 连接类的父类，抽象类
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设置是否从httpUrlConnection读入，默认情况下是true;
            conn.setDoInput(true);
            // 设置是否向httpUrlConnection输出
            conn.setDoOutput(true);
            // Post 请求不能使用缓存
            conn.setUseCaches(false);
            // 设定请求的方法，默认是GET
            conn.setRequestMethod("POST");
            // 设置字符编码连接参数
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置字符编码
            conn.setRequestProperty("Charset", charset);
            // 设置请求内容类型
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // 设置DataOutputStream
            ds = new DataOutputStream(conn.getOutputStream());
            for (int i = 0; i < uploadFilePaths.length; i++) {
                String uploadFile = uploadFilePaths[i];
                String filename = uploadFile.substring(uploadFile.lastIndexOf("//") + 1);
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; " + "name=\"file" + i + "\";filename=\"" + filename
                        + "\"" + end);
                ds.writeBytes(end);
                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                /* close streams */
                fStream.close();
            }
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* close streams */
            ds.flush();
            if (conn.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + conn.getResponseCode());
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);
                tempLine = null;
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }


    /**
     * 发送get请求保存下载文件
     *
     * @param requestUrl          请求地址
     * @param requestParams       参数map String:参数名，Object:参数值
     * @param fileSavePathAndName 文件保存地址+文件名  因为无法确定url或者是header中是否包含文件名，所以此处默认输入路径加文件名+文件后缀
     */
    public static void sendHttpGetAndSaveFile(String requestUrl, Map<String, Object> requestParams, String fileSavePathAndName) {
        HttpURLConnection conn = null;
        BufferedReader br = null;
        FileOutputStream os = null;
        try {
            URL url = new URL(requestUrl + "?" + getRequestString(requestParams));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            InputStream is = conn.getInputStream();
            String fileName = conn.getHeaderField("Content-Disposition");
            System.out.println(fileName);
            os = new FileOutputStream(fileSavePathAndName);
            byte buf[] = new byte[1024];
            int count = 0;
            while ((count = is.read(buf)) != -1) {
                os.write(buf, 0, count);
            }
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    os = null;
                    throw new RuntimeException(e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                        conn = null;
                    }
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                    throw new RuntimeException(e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                        conn = null;
                    }
                }
            }
        }
    }

    ///**
    // * @Description:使用HttpClient发送post请求
    // * @author:liuyc
    // * @time:2016年5月17日 下午3:28:23
    // */
//    public static String httpClientPost(String urlParam, Map<String, Object> params, String charset) {
//        StringBuffer resultBuffer = null;
//        HttpClient client = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(urlParam);
//        // 构建请求参数
//        List<NameValuePair> list = new ArrayList<NameValuePair>();
//        Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Entry<String, Object> elem = iterator.next();
//            list.add(new BasicNameValuePair(elem.getKey(), String.valueOf(elem.getValue())));
//        }
//        BufferedReader br = null;
//        try {
//            if (list.size() > 0) {
//                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
//                httpPost.setEntity(entity);
//            }
//            HttpResponse response = client.execute(httpPost);
//            // 读取服务器响应数据
//            resultBuffer = new StringBuffer();
//            br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            String temp;
//            while ((temp = br.readLine()) != null) {
//                resultBuffer.append(temp);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    br = null;
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        return resultBuffer.toString();
//    }
//
//    /**
//     * @Description:使用HttpClient发送get请求
//     * @author:liuyc
//     * @time:2016年5月17日 下午3:28:56
//     */
//    public static String httpClientGet(String urlParam, Map<String, Object> params, String charset) {
//        StringBuffer resultBuffer = null;
//        HttpClient client = new DefaultHttpClient();
//        BufferedReader br = null;
//        // 构建请求参数
//        StringBuffer sbParams = new StringBuffer();
//        if (params != null && params.size() > 0) {
//            for (Entry<String, Object> entry : params.entrySet()) {
//                sbParams.append(entry.getKey());
//                sbParams.append("=");
//                try {
//                    sbParams.append(URLEncoder.encode(String.valueOf(entry.getValue()), charset));
//                } catch (UnsupportedEncodingException e) {
//                    throw new RuntimeException(e);
//                }
//                sbParams.append("&");
//            }
//        }
//        if (sbParams != null && sbParams.length() > 0) {
//            urlParam = urlParam + "?" + sbParams.substring(0, sbParams.length() - 1);
//        }
//        HttpGet httpGet = new HttpGet(urlParam);
//        try {
//            HttpResponse response = client.execute(httpGet);
//            // 读取服务器响应数据
//            br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            String temp;
//            resultBuffer = new StringBuffer();
//            while ((temp = br.readLine()) != null) {
//                resultBuffer.append(temp);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    br = null;
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        return resultBuffer.toString();
//    }
//

    /**
     * @Description:使用socket发送post请求
     * @author:liuyc
     * @time:2016年5月18日 上午9:26:22
     */
    /**
     * 使用socket发送post请求
     *
     * @param urlParam 地址url
     * @param params   参数
     * @param charset  字符编码
     * @return 结果
     */
    public static String sendSocketPost(String urlParam, Map<String, Object> params, String charset) {
        String result = "";
        // 构建请求参数
        StringBuffer sbParams = new StringBuffer();
        if (params != null && params.size() > 0) {
            for (Entry<String, Object> entry : params.entrySet()) {
                sbParams.append(entry.getKey());
                sbParams.append("=");
                sbParams.append(entry.getValue());
                sbParams.append("&");
            }
        }
        Socket socket = null;
        OutputStreamWriter osw = null;
        InputStream is = null;
        try {
            URL url = new URL(urlParam);
            String host = url.getHost();
            int port = url.getPort();
            if (-1 == port) {
                port = 80;
            }
            String path = url.getPath();
            socket = new Socket(host, port);
            StringBuffer sb = new StringBuffer();
            sb.append("POST " + path + " HTTP/1.1\r\n");
            sb.append("Host: " + host + "\r\n");
            sb.append("Connection: Keep-Alive\r\n");
            sb.append("Content-Type: application/x-www-form-urlencoded; charset=utf-8 \r\n");
            sb.append("Content-Length: ").append(sb.toString().getBytes().length).append("\r\n");
            // 这里一个回车换行，表示消息头写完，不然服务器会继续等待
            sb.append("\r\n");
            if (sbParams != null && sbParams.length() > 0) {
                sb.append(sbParams.substring(0, sbParams.length() - 1));
            }
            osw = new OutputStreamWriter(socket.getOutputStream());
            osw.write(sb.toString());
            osw.flush();
            is = socket.getInputStream();
            String line = null;
            // 服务器响应体数据长度
            int contentLength = 0;
            // 读取http响应头部信息
            do {
                line = readLine(is, 0, charset);
                if (line.startsWith("Content-Length")) {
                    // 拿到响应体内容长度
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
                // 如果遇到了一个单独的回车换行，则表示请求头结束
            } while (!line.equals("\r\n"));
            // 读取出响应体数据（就是你要的数据）
            result = readLine(is, contentLength, charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    osw = null;
                    throw new RuntimeException(e);
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            socket = null;
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    throw new RuntimeException(e);
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            socket = null;
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 使用socket发送get请求
     *
     * @param urlParam 地址url
     * @param params   参数
     * @param charset  字符集
     * @return 返回结果
     */
    public static String sendSocketGet(String urlParam, Map<String, Object> params, String charset) {
        String result = "";
        // 构建请求参数
        StringBuffer sbParams = new StringBuffer();
        if (params != null && params.size() > 0) {
            for (Entry<String, Object> entry : params.entrySet()) {
                sbParams.append(entry.getKey());
                sbParams.append("=");
                sbParams.append(entry.getValue());
                sbParams.append("&");
            }
        }
        Socket socket = null;
        OutputStreamWriter osw = null;
        InputStream is = null;
        try {
            URL url = new URL(urlParam);
            String host = url.getHost();
            int port = url.getPort();
            if (-1 == port) {
                port = 80;
            }
            String path = url.getPath();
            socket = new Socket(host, port);
            StringBuffer sb = new StringBuffer();
            sb.append("GET " + path + " HTTP/1.1\r\n");
            sb.append("Host: " + host + "\r\n");
            sb.append("Connection: Keep-Alive\r\n");
            sb.append("Content-Type: application/x-www-form-urlencoded; charset=utf-8 \r\n");
            sb.append("Content-Length: ").append(sb.toString().getBytes().length).append("\r\n");
            // 这里一个回车换行，表示消息头写完，不然服务器会继续等待
            sb.append("\r\n");
            if (sbParams != null && sbParams.length() > 0) {
                sb.append(sbParams.substring(0, sbParams.length() - 1));
            }
            osw = new OutputStreamWriter(socket.getOutputStream());
            osw.write(sb.toString());
            osw.flush();
            is = socket.getInputStream();
            String line = null;
            // 服务器响应体数据长度
            int contentLength = 0;
            // 读取http响应头部信息
            do {
                line = readLine(is, 0, charset);
                if (line.startsWith("Content-Length")) {
                    // 拿到响应体内容长度
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
                // 如果遇到了一个单独的回车换行，则表示请求头结束
            } while (!line.equals("\r\n"));
            // 读取出响应体数据（就是你要的数据）
            result = readLine(is, contentLength, charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    osw = null;
                    throw new RuntimeException(e);
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            socket = null;
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    throw new RuntimeException(e);
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            socket = null;
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return result;
    }


    /**
     * 读取一行数据，contentLe内容长度为0时，读取响应头信息，不为0时读正文
     *
     * @param is            InputStream
     * @param contentLength contentLength
     * @param charset       charset
     * @return 内容
     * @throws IOException IOException
     */
    private static String readLine(InputStream is, int contentLength, String charset) throws IOException {
        List<Byte> lineByte = new ArrayList<Byte>();
        byte tempByte;
        int cumsum = 0;
        if (contentLength != 0) {
            do {
                tempByte = (byte) is.read();
                lineByte.add(Byte.valueOf(tempByte));
                cumsum++;
            } while (cumsum < contentLength);// cumsum等于contentLength表示已读完
        } else {
            do {
                tempByte = (byte) is.read();
                lineByte.add(Byte.valueOf(tempByte));
            } while (tempByte != 10);// 换行符的ascii码值为10
        }

        byte[] resutlBytes = new byte[lineByte.size()];
        for (int i = 0; i < lineByte.size(); i++) {
            resutlBytes[i] = (lineByte.get(i)).byteValue();
        }
        return new String(resutlBytes, charset);
    }

    /**
     * 构建请求参数
     *
     * @param requestParams 请求参数Map
     * @return 构建后的请求参数
     */
    private static String getRequestString(Map<String, Object> requestParams) {
        //请求buffer
        StringBuffer requestBuffer = null;
        if (requestParams != null && requestParams.size() > 0) {
            requestBuffer = new StringBuffer();
            for (Entry<String, Object> entry : requestParams.entrySet()) {
                requestBuffer.append(entry.getKey());
                requestBuffer.append("=");
                requestBuffer.append(entry.getValue());
                requestBuffer.append("&");
            }
            return requestBuffer.toString();
        }
        return "";

    }

}
