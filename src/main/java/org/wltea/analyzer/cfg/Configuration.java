/**
 * 
 */
package org.wltea.analyzer.cfg;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.PathUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.plugin.analysis.ik.AnalysisIkPlugin;
import org.wltea.analyzer.dic.Dictionary;

import java.io.File;
import java.nio.file.Path;

public class Configuration {

	private Environment environment;
	private Settings settings;

	//是否启用智能分词
	private boolean useSmart;

	//是否启用远程词典加载
	private boolean enableRemoteDict=false;

	//是否启用小写处理
	private boolean enableLowercase=true;

	//是否只从数据库中加载词库
	private boolean enableDBDict=false;

	//是否记录日志到数据库中
	private boolean enableDBDictLog=false;

	//管理词库的数据库链接
	private String dictDBUrl=null;

	//检查数据库的时间间隔
	private int checkDBInterval=120;

	@Inject
	public Configuration(Environment env,Settings settings) {
		this.environment = env;
		this.settings=settings;

		this.useSmart = settings.get("use_smart", "false").equals("true");
		this.enableLowercase = settings.get("enable_lowercase", "true").equals("true");
		this.enableRemoteDict = settings.get("enable_remote_dict", "true").equals("true");
		this.enableDBDict = settings.get("enable_db_dict", "false").equals("true");
		this.enableDBDictLog = settings.get("enable_db_dict_log", "false").equals("true");
		this.dictDBUrl = settings.get("db_dict_url");
		this.checkDBInterval = settings.getAsInt("db_dict_check_interval", 120);

		if(this.enableDBDict && this.dictDBUrl == null){
			throw new RuntimeException("The parameter [db_dict_url] is requird!");
		}

		try {
			Dictionary.initial(this);
		} catch (Exception e) {
			throw new RuntimeException("Init dictionary occur exception!", e);
		}
	}

	public Path getConfigInPluginDir() {
		return PathUtils
				.get(new File(AnalysisIkPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath())
						.getParent(), "config")
				.toAbsolutePath();
	}

	public boolean isUseSmart() {
		return useSmart;
	}

	public Configuration setUseSmart(boolean useSmart) {
		this.useSmart = useSmart;
		return this;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public Settings getSettings() {
		return settings;
	}

	public boolean isEnableRemoteDict() {
		return enableRemoteDict;
	}

	public boolean isEnableLowercase() {
		return enableLowercase;
	}

	public boolean isEnableDBDict() {
		return enableDBDict;
	}

	public boolean isEnableDBDictLog() {
		return enableDBDictLog;
	}

	public String getDictDBUrl() {
		return dictDBUrl;
	}

	public int getCheckDBInterval() {
		return checkDBInterval;
	}
}
