-- ============================================
-- 代码片段管理系统 MySQL 建表脚本
-- 数据库名称：code_snippet_db
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS code_snippet_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE code_snippet_db;

-- ============================================
-- 1. 用户表
-- ============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(32) NOT NULL COMMENT '登录账号',
  `password` varchar(100) NOT NULL COMMENT '加密密码（BCrypt）',
  `nick_name` varchar(32) DEFAULT '' COMMENT '昵称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 2. 代码分类表
-- ============================================
DROP TABLE IF EXISTS `code_category`;
CREATE TABLE `code_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '所属用户id',
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort` int DEFAULT 0 COMMENT '排序号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码分类表';

-- ============================================
-- 3. 代码标签表
-- ============================================
DROP TABLE IF EXISTS `code_tag`;
CREATE TABLE `code_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '所属用户id',
  `tag_name` varchar(30) NOT NULL COMMENT '标签名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid_tag` (`user_id`,`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码标签表';

-- ============================================
-- 4. 代码片段主表
-- ============================================
DROP TABLE IF EXISTS `code_snippet`;
CREATE TABLE `code_snippet` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '所属用户id',
  `category_id` bigint DEFAULT NULL COMMENT '分类id',
  `title` varchar(100) NOT NULL COMMENT '代码标题',
  `language` varchar(30) NOT NULL COMMENT '编程语言（如 Java / Python / JavaScript / Go / SQL）',
  `code_content` longtext NOT NULL COMMENT '完整代码内容',
  `remark` text COMMENT '备注说明',
  `is_collect` tinyint(1) DEFAULT 0 COMMENT '是否收藏：0-未收藏 1-已收藏',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category` (`category_id`),
  KEY `idx_language` (`language`),
  KEY `idx_is_collect` (`is_collect`),
  KEY `idx_title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码片段主表';

-- ============================================
-- 5. 代码-标签关联表（多对多）
-- ============================================
DROP TABLE IF EXISTS `snippet_tag_rel`;
CREATE TABLE `snippet_tag_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `snippet_id` bigint NOT NULL COMMENT '代码片段id',
  `tag_id` bigint NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_snip_tag` (`snippet_id`,`tag_id`),
  KEY `idx_snippet_id` (`snippet_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码-标签关联表';

-- ============================================
-- 6. 用户配置表（AI 设置等）
-- ============================================
DROP TABLE IF EXISTS `user_config`;
CREATE TABLE `user_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '所属用户id',
  `config_key` varchar(50) NOT NULL COMMENT '配置键',
  `config_value` varchar(500) DEFAULT '' COMMENT '配置值',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid_key` (`user_id`,`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户配置表';
