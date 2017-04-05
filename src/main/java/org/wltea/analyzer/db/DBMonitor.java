package org.wltea.analyzer.db;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.dic.Monitor;

import java.util.Map;

/**
 * 数据库词库监视器。
 * Created by ginozhang on 2017/4/5.
 */
public class DBMonitor implements Runnable {

    private static final Logger logger = ESLoggerFactory.getLogger(Monitor.class.getName());

    private WordTypeVersionMgr wordTypeVersionMgr;

    public DBMonitor(WordTypeVersionMgr wordTypeVersionMgr) {
        this.wordTypeVersionMgr = wordTypeVersionMgr;
    }

    @Override
    public void run() {
        try {
            Map<WordType, Long> updateVersionMap = wordTypeVersionMgr.checkModify();
            if (updateVersionMap != null && !updateVersionMap.isEmpty() && Dictionary.getSingleton().reloadDBDict(updateVersionMap)) {
                this.wordTypeVersionMgr.reportAfterReload(updateVersionMap);
            }
        } catch (Exception e) {
            logger.error("Failed to reload db dict!", e);
        }
    }
}