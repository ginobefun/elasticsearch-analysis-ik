DROP TABLE IF EXISTS `es_word_def`;
CREATE TABLE `es_word_def` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自动递增的唯一主键',
  `word_type` int(1) NOT NULL DEFAULT '1' COMMENT '词的类型 1为主词典的词 2为停用词 3为姓氏 4为量词 5为后缀词 6为介词',
  `word` varchar(32) NOT NULL COMMENT '词的内容',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '1为正常 0为废弃',
  `version` int(10) NOT NULL DEFAULT '0' COMMENT '全局版本号，需要保证每次修改后递增，如时间戳',
  PRIMARY KEY (`id`),
  KEY `IDX_ESWORD_VERSION` (`version`) USING BTREE,
  KEY `IDX_ESWORD_TYPE` (`word_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

