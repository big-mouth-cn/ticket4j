package org.bigmouth.ticket4j.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.bigmouth.ticket4j.Ticket4jDefaults;


public final class HttpClientUtils {

    private HttpClientUtils() {
    }
    
    public static boolean isGZIPEncoding(HttpResponse response) {
        for (Header header : response.getAllHeaders()) {
            if (StringUtils.equals("Content-Encoding", header.getName())) {
                return StringUtils.equals("gzip", header.getValue());
            }
        }
        return false;
    }
    
    public static String getResponseBody(HttpResponse httpResponse) throws IllegalStateException, IOException {
        boolean isGzip = isGZIPEncoding(httpResponse);
        return isGzip ?
                getResponseBodyAsGZIP(httpResponse) : getResponseBodyAsString(httpResponse);
    }
    
    public static String getResponseBodyAsGZIP(HttpResponse httpResponse) throws IllegalStateException, IOException {
        if (null == httpResponse) {
            return null;
        }
        BufferedReader br = null;
        GZIPInputStream gzipis = null;
        InputStream is = null;
        try {
            is = httpResponse.getEntity().getContent();
            gzipis = new GZIPInputStream(is);
            br = new BufferedReader(new InputStreamReader(gzipis, Ticket4jDefaults.DEFAULT_CHARSET));
            StringBuilder content = new StringBuilder();
            String line = "";
            while ( (line = br.readLine()) != null ) {
                content.append(URLDecoder.decode(line, Ticket4jDefaults.DEFAULT_CHARSET));
            }
            return content.toString();
        }
        finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(is);
        }
    }
    
    public static String getResponseBodyAsString(HttpResponse httpResponse) throws IllegalStateException, IOException {
        if (null == httpResponse) {
            return null;
        }
        BufferedReader br = null;
        InputStream is = null;
        try {
            is = httpResponse.getEntity().getContent();
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder content = new StringBuilder();
            String line = "";
            while ( (line = br.readLine()) != null ) {
                content.append(URLDecoder.decode(line, Ticket4jDefaults.DEFAULT_CHARSET));
            }
            return content.toString();
        }
        finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(is);
        }
    }
    
    public static File getResponseBodyAsFile(HttpResponse httpResponse, String filePath) throws IllegalStateException, IOException {
        if (null == httpResponse) {
            return null;
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        File file = null;
        try {
            is = httpResponse.getEntity().getContent();
            bis = new BufferedInputStream(is);
            file = new File(filePath);
            bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bytes = new byte[Ticket4jDefaults.BUFFER];
            int len;
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            bos.flush();
        }
        finally {
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(is);
        }
        return file;
    }
}
