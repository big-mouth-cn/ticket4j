package org.bigmouth.ticket4j.cookie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.bigmouth.framework.util.PathUtils;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class DefaultCookieCache implements CookieCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCookieCache.class);

    @Override
    public void write(Header[] headers, String id) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            String path = getDirPath();
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(PathUtils.appendEndFileSeparator(path) + id + ".cookie");
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(Lists.newArrayList(headers));
            oos.flush();
        }
        catch (FileNotFoundException e) {
            LOGGER.warn("Cookie 缓存失败,错误原因：{}", e.getMessage());
        }
        catch (IOException e) {
            LOGGER.warn("Cookie 缓存失败,错误原因：{}", e.getMessage());
        }
        finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(fos);
        }
    }

    @Override
    public Header[] read(String id) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        
        File file = new File(PathUtils.appendEndFileSeparator(getDirPath()) + id + ".cookie");
        if (!file.exists())
            return null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            @SuppressWarnings("unchecked")
            List<Header> object = (List<Header>) ois.readObject();
            return object.toArray(new Header[0]);
        }
        catch (FileNotFoundException e) {
            LOGGER.warn("Cookie 从缓存读取失败,错误原因：{}", e.getMessage());
        }
        catch (IOException e) {
            LOGGER.warn("Cookie 从缓存读取失败,错误原因：{}", e.getMessage());
        }
        catch (ClassNotFoundException e) {
            LOGGER.warn("Cookie 从缓存读取失败,错误原因：{}", e.getMessage());
        }
        finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }
        return null;
    }

    private String getDirPath() {
        return Ticket4jDefaults.PATH_COOKIE_DIRECTORY;
    }
}
