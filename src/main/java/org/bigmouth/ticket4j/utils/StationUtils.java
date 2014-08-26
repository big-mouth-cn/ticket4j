/*
 * 文件名称: StationUtils.java
 * 版权信息: Copyright 2005-2014 Allen.Hu Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2014-1-5
 * 修改内容: 
 */
package org.bigmouth.ticket4j.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class StationUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(StationUtils.class);
    private static final String STATION_ITEM_SPLIT = "|";
    private static final int VALID_ITEM_LENGTH = 2;
    private static final String PATH = "data/stations.cfg";
    private static final Map<String, String> STATIONS = Maps.newHashMap();
    private static final AtomicBoolean initialize = new AtomicBoolean(false);

    static {
        initialize();
    }

    private static void initialize() {
        BufferedReader reader = null;
        try {
            Resource resource = new ClassPathResource(PATH);
            Preconditions.checkNotNull(resource, PATH + " Cannot found.");
            File file = resource.getFile();
            if (!file.exists())
                throw new FileNotFoundException("File " + file + " Does not exist!");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Ticket4jDefaults.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(line)) {
                    String[] items = StringUtils.split(line, STATION_ITEM_SPLIT);
                    if (isValidItems(items)) {
                        String name = items[1];
                        String code = items[2];
                        STATIONS.put(name, code);
                    }
                }
            }
            initialize.set(true);
        }
        catch (Exception e) {
            LOGGER.error("initialize: {}", e.getMessage());
            System.exit(0);
        }
        finally {
            IOUtils.closeQuietly(reader);
        }
    }
    
    public static String find(String name) {
        if (!initialize.get()) {
            initialize();
        }
        return STATIONS.get(name);
    }

    private static boolean isValidItems(String[] items) {
        return null != items && items.length > VALID_ITEM_LENGTH;
    }
}
