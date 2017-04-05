DROP TABLE IF EXISTS `es_word_log`;
CREATE TABLE `es_word_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自动递增的唯一主键',
  `log_content` varchar(256) DEFAULT NULL COMMENT '日志内容',
  `log_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '日志记录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

