package org.bigmouth.ticket4j.report;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.bigmouth.framework.util.PathUtils;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.utils.BaseLifeCycleSupport;
import org.bigmouth.ticket4j.utils.HttpClientUtils;
import org.bigmouth.ticket4j.utils.Ticket4jOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

public class TicketReport extends BaseLifeCycleSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketReport.class);
    private static final Executor EXECUTOR = Executors.newFixedThreadPool(1);

    private final FileAlterationMonitor monitor = new FileAlterationMonitor(1000);
    private String reportAddress = Ticket4jDefaults.URL_REPORT;

    public void write(Report object) {
        File file = new File(PathUtils.appendEndFileSeparator(getDirectoryPath()) + new Date().getTime() + ".order");
        try {
            Ticket4jOutputStream.write(object, file);
        }
        catch (IOException e) {
            LOGGER.error("Written train ticket file failed!", e);
        }
    }

    private File getDirectory() {
        File file = new File(getDirectoryPath());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private String getDirectoryPath() {
        return Ticket4jDefaults.PATH_ORDER_DIRECTORY;
    }

    @Override
    protected void doInit() {
        initLegacy();
        initAlterationListener();
    }

    private void initLegacy() {
        File directory = getDirectory();
        File[] files = directory.listFiles();
        for (File file : files) {
            upload(file);
        }
    }

    private void initAlterationListener() {
        FileAlterationObserver observer = new FileAlterationObserver(getDirectory());
        observer.addListener(new TicketReportFileAlterationListener());
        monitor.addObserver(observer);
        try {
            monitor.start();
        }
        catch (Exception e) {
            LOGGER.error("init: ", e);
            System.exit(-1);
        }
    }

    @Override
    protected void doDestroy() {
        try {
            monitor.stop();
        }
        catch (Exception e) {
        }
    }

    private void upload(final File file) {
        // TODO 有可能多开程序。这样会存在多个进程同时处理同一个文件的问题，需要做多线程处理
        EXECUTOR.execute(new Runnable() {

            @Override
            public void run() {
                Report report = read(file);
                boolean flag = upload(report);
                if (flag) {
                    try {
                        FileUtils.forceDelete(file);
                    }
                    catch (IOException e) {
                    }
                }
            }

            private boolean upload(Report report) {
                if (null == report)
                    return false;

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost post = new HttpPost(reportAddress);

                String data = new Gson().toJson(report);

                try {
                    addPair(post, new NameValuePair[] { new BasicNameValuePair("order", data) });
                    HttpResponse httpResponse = httpClient.execute(post);
                    String responseBody = HttpClientUtils.getResponseBody(httpResponse);
                    return (StringUtils.equals(responseBody, "0"));
                }
                catch (Exception e) {
                    LOGGER.error("Train ticket order data upload failed!", e);
                }
                finally {
                    httpClient.getConnectionManager().shutdown();
                }
                return false;
            }

            private Report read(File file) {
                try {
                    return Ticket4jOutputStream.read(file, true);
                }
                catch (Exception e) {
                    LOGGER.error("Read train ticket file failed!", e);
                }
                return null;
            }
        });
    }

    protected void addPair(HttpEntityEnclosingRequestBase requestBase, NameValuePair... pairs)
            throws UnsupportedEncodingException {
        List<NameValuePair> list = Lists.newArrayList(pairs);
        requestBase.setEntity(new UrlEncodedFormEntity(list, Ticket4jDefaults.DEFAULT_CHARSET));
    }

    private class TicketReportFileAlterationListener implements FileAlterationListener {

        @Override
        public void onStart(FileAlterationObserver observer) {
        }

        @Override
        public void onDirectoryCreate(File directory) {
        }

        @Override
        public void onDirectoryChange(File directory) {
        }

        @Override
        public void onDirectoryDelete(File directory) {
        }

        @Override
        public void onFileCreate(File file) {
            upload(file);
        }

        @Override
        public void onFileChange(File file) {
        }

        @Override
        public void onFileDelete(File file) {
        }

        @Override
        public void onStop(FileAlterationObserver observer) {
        }
    }
    
    public void setReportAddress(String reportAddress) {
        this.reportAddress = reportAddress;
    }
}
