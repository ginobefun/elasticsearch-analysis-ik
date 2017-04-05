package org.wltea.analyzer.db;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ginozhang on 2017/4/5.
 * 数据库词库版本管理类。
 */
public class WordTypeVersionMgr {

    private final String dictDBUrl;

    private Map<WordType, Long> versionMap = new HashMap<>();

    public WordTypeVersionMgr(String dictDBUrl) throws Exception {
        this.dictDBUrl = dictDBUrl;
        this.versionMap = getVersionMapFromDB();
    }

    public Map<WordType, Long> checkModify() throws Exception {
        Map<WordType, Long> updateVersionMap = new HashMap<>();
        Map<WordType, Long> tempVersionMap = getVersionMapFromDB();
        for (Map.Entry<WordType, Long> entry : tempVersionMap.entrySet()) {
            if (!entry.getValue().equals(this.versionMap.getOrDefault(entry.getKey(), 0L))) {
                updateVersionMap.put(entry.getKey(), entry.getValue());
            }
        }

        return updateVersionMap;
    }

    public void reportAfterReload(Map<WordType, Long> updateVersionMap) {
        this.versionMap.putAll(updateVersionMap);
    }

    private Map<WordType, Long> getVersionMapFromDB() throws Exception {
        Map<WordType, Long> tempVersionMap = JDBCUtils.queryMaxVersion(dictDBUrl);
        for (WordType wordType : WordType.values()) {
            tempVersionMap.computeIfAbsent(wordType, key -> 0L);
        }

        return tempVersionMap;
    }

    public Map<WordType, Long> getVersionMap() {
        return Collections.unmodifiableMap(this.versionMap);
    }
}
