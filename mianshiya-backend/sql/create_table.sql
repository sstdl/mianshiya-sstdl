-- 创建库
create database if not exists mianshiya;

-- 切换库
use mianshiya;


-- 创建用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(256)                           not null comment '密码',
    userName     varchar(256)                           null comment '昵称',
    userAvatar   varchar(1024)                          null comment '头像',
    userProfile  varchar(256)                           null comment '简介',
    userRole     varchar(256) default 'user'            not null comment '角色',
    editTime     datetime     default CURRENT_TIMESTAMP not null comment '编辑时间',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '用户表' collate = utf8mb4_unicode_ci;
-- 插入用户数据
INSERT INTO user (userAccount, userPassword, userName, userAvatar, userProfile, userRole, editTime, createTime, updateTime, isDelete) VALUES
                                                                                                                                          ('sstdl', '456a9cb552a21b7e2afe4e107d585585', 'SSTDL', 'cat', NULL, 'admin', '2025-01-14 11:44:58', '2025-01-14 11:44:58', '2025-01-14 20:20:45', 0),
                                                                                                                                          ('mianshiya', 'f1739fb5eab754c4caa30d7ed263e02b', 'mianshiya', 'cat', NULL, 'user', '2025-01-14 20:31:34', '2025-01-14 20:31:34', '2025-01-15 21:02:38', 0),
                                                                                                                                          ('wangwu', '34e7081875196274fb0a38c75cb00c93', 'wangwu', 'cat', NULL, 'user', '2025-01-14 20:56:37', '2025-01-14 20:56:37', '2025-01-14 20:56:37', 0),
                                                                                                                                          ('lisi', '34e7081875196274fb0a38c75cb00c93', 'lisi', 'cat', NULL, 'user', '2025-01-14 20:56:45', '2025-01-14 20:56:45', '2025-01-14 20:56:45', 0),
                                                                                                                                          ('zhangsan', '34e7081875196274fb0a38c75cb00c93', 'zhangsan', 'cat', NULL, 'user', '2025-01-14 20:56:52', '2025-01-14 20:56:52', '2025-01-14 20:56:52', 0);


-- 创建题目表
create table if not exists question
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(256)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    answer     text                               null comment '推荐答案',
    userId     bigint                             not null comment '创建用户 id',
    editTime   datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_title (title),
    index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;
-- 插入题目信息
INSERT INTO question (title, content, tags, answer, userId, editTime, createTime, updateTime, isDelete) VALUES
                                                                                                            ('什么是Java多线程？', 'Java多线程是指在单个Java程序中同时运行多个线程以提高程序效率。', '["Java", "多线程", "编程"]', 'Java多线程是一种并发编程方式，通过Thread类或实现Runnable接口来实现。', 1879011799970971650, '2025-01-14 21:01:40', '2025-01-14 21:01:40', '2025-01-15 20:52:18', 0),
                                                                                                            ('如何优化SQL查询性能？', '优化SQL性能可以从索引、查询结构、表设计等方面入手。', '["SQL", "性能优化", "数据库"]', '常用方法包括创建合适的索引、避免SELECT *、减少子查询等。', 1879011799970971650, '2025-01-14 21:01:40', '2025-01-14 21:01:40', '2025-01-15 20:52:18', 0),
                                                                                                            ('Python中的装饰器是什么？', '装饰器是Python中用来扩展函数功能的一种高级特性。', '["Python", "装饰器", "函数"]', '装饰器本质上是一个函数，它可以在不修改原函数的情况下增强其功能。', 1879144322885484545, '2025-01-14 21:01:40', '2025-01-14 21:01:40', '2025-01-15 20:52:53', 0),
                                                                                                            ('如何实现前端页面的响应式布局？', '响应式布局可以使用CSS媒体查询实现，适配不同设备尺寸。', '["前端", "CSS", "响应式设计"]', '常用方法是使用flex布局、grid布局以及媒体查询进行适配。', 1879144322885484545, '2025-01-14 21:01:40', '2025-01-14 21:01:40', '2025-01-15 20:52:53', 0),
                                                                                                            ('机器学习中的过拟合如何处理？', '过拟合是机器学习模型在训练数据上表现优异，但在测试数据上表现较差的现象。', '["机器学习", "过拟合", "模型"]', '常见解决方法包括正则化、交叉验证、增加数据集、使用更简单的模型等。', 1879150630485467138, '2025-01-14 21:01:40', '2025-01-14 21:01:40', '2025-01-15 20:53:08', 0),
                                                                                                            ('JavaScript的闭包是什么？', '闭包是指有权访问另一个函数作用域中的变量的函数。', '["JavaScript", "闭包", "函数"]', '闭包可以捕获函数外的变量并在需要时访问。', 1879150630485467138, '2025-01-14 21:01:40', '2025-01-14 21:01:40', '2025-01-15 20:53:08', 0),
                                                                                                            ('什么是微服务架构？', '微服务架构是一种将应用程序拆分成多个小服务的设计方法。', '["架构", "微服务", "分布式系统"]', '每个服务独立运行，通信通常通过API完成。', 1879150662924214273, '2025-01-14 21:01:40', '2025-01-14 21:01:40', '2025-01-15 20:53:40', 0),
                                                                                                            ('如何使用Git进行版本控制？', 'Git是一种分布式版本控制系统，用于管理代码的变更。', '["Git", "版本控制", "开发工具"]', '常见操作包括初始化仓库、提交变更、创建分支和合并分支。', 1879150662924214273, '2025-01-14 21:01:40', '2025-01-14 21:01:40', '2025-01-15 20:53:40', 0),
                                                                                                            ('区块链的基本原理是什么？', '区块链是一种分布式账本技术，通过链式结构存储数据。', '["区块链", "加密", "分布式"]', '核心是去中心化、不可篡改和共识机制。', 1879150690149441538, '2025-01-14 21:01:40', '2025-01-14 21:01:40', '2025-01-15 20:53:54', 0),
                                                                                                            ('大数据中的数据清洗方法有哪些？', '数据清洗是指对数据进行格式化、修复、删除重复等处理的过程。', '["大数据", "数据清洗", "数据处理"]', '常用方法包括去重、填补缺失值、标准化数据格式等。', 1879150690149441538, '2025-01-14 21:01:40', '2025-01-14 21:01:40', '2025-01-15 20:53:54', 0);


-- 创建题库表
create table if not exists question_bank
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(256)                       null comment '标题',
    description text                               null comment '描述',
    picture     varchar(2048)                      null comment '图片',
    userId      bigint                             not null comment '创建用户 id',
    editTime    datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_title (title)
) comment '题库' collate = utf8mb4_unicode_ci;
-- 插入题库信息
INSERT INTO question_bank (title, description, picture, userId, editTime, createTime, updateTime, isDelete) VALUES
                                                                                                                ('Java基础题库', '包含Java多线程的基础问题与解答，适合初学者。涉及标签：[""Java"", ""多线程"", ""编程""]。', NULL, 1879011799970971650, '2025-01-14 21:09:14', '2025-01-14 21:09:14', '2025-01-15 20:52:36', 0),
                                                                                                                ('SQL优化题库', '涵盖SQL查询性能优化的相关问题与答案，适合数据库开发人员。涉及标签：[""SQL"", ""性能优化"", ""数据库""]。', NULL, 1879011799970971650, '2025-01-14 21:09:14', '2025-01-14 21:09:14', '2025-01-15 20:52:36', 0),
                                                                                                                ('Python装饰器题库', '包含关于Python装饰器的常见问题与解答，适合进阶开发者。涉及标签：[""Python"", ""装饰器"", ""函数""]。', NULL, 1879144322885484545, '2025-01-14 21:09:14', '2025-01-14 21:09:14', '2025-01-15 20:52:53', 0),
                                                                                                                ('前端响应式布局题库', '涵盖CSS媒体查询与响应式设计的相关问题，适合前端开发人员。涉及标签：[""前端"", ""CSS"", ""响应式设计""]。', NULL, 1879144322885484545, '2025-01-14 21:09:14', '2025-01-14 21:09:14', '2025-01-15 20:52:53', 0),
                                                                                                                ('机器学习基础题库', '包括机器学习中关于过拟合处理的相关问题与解答。涉及标签：[""机器学习"", ""过拟合"", ""模型""]。', NULL, 1879150630485467138, '2025-01-14 21:09:14', '2025-01-14 21:09:14', '2025-01-15 20:53:08', 0),
                                                                                                                ('JavaScript闭包题库', '包含JavaScript闭包的基础问题，适合入门学习者。涉及标签：[""JavaScript"", ""闭包"", ""函数""]。', NULL, 1879150630485467138, '2025-01-14 21:09:14', '2025-01-14 21:09:14', '2025-01-15 20:53:08', 0),
                                                                                                                ('微服务架构题库', '讲解微服务架构的基本概念和相关问题，适合后端开发人员。涉及标签：[""架构"", ""微服务"", ""分布式系统""]。', NULL, 1879150662924214273, '2025-01-14 21:09:14', '2025-01-14 21:09:14', '2025-01-15 20:53:40', 0),
                                                                                                                ('Git版本控制题库', '汇总关于Git版本控制的常见问题，适合团队协作开发人员。涉及标签：[""Git"", ""版本控制"", ""开发工具""]。', NULL, 1879150662924214273, '2025-01-14 21:09:14', '2025-01-14 21:09:14', '2025-01-15 20:53:40', 0),
                                                                                                                ('区块链基础题库', '介绍区块链的基本原理与问题解答，适合技术爱好者。涉及标签：[""区块链"", ""加密"", ""分布式""]。', NULL, 1879150690149441538, '2025-01-14 21:09:14', '2025-01-14 21:09:14', '2025-01-15 20:53:54', 0),
                                                                                                                ('大数据清洗题库', '关于大数据处理中数据清洗的相关问题与答案，适合数据分析师。涉及标签：[""大数据"", ""数据清洗"", ""数据处理""]。', NULL, 1879150690149441538, '2025-01-14 21:09:14', '2025-01-14 21:09:14', '2025-01-15 20:53:54', 0);


-- 创建题库题目关联表
create table if not exists question_bank_mapping
(
    id             bigint auto_increment comment 'id' primary key,
    questionBankId bigint                             not null comment '题库 id',
    questionId     bigint                             not null comment '题目 id',
    userId         bigint                             not null comment '创建用户 id',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    UNIQUE (questionBankId, questionId)
) comment '题库题目' collate = utf8mb4_unicode_ci;
-- 插入关联数据
INSERT INTO question_bank_mapping (questionBankId, questionId, userId, createTime, updateTime) VALUES
                                                                                                   (1889145699753852930, 1879145699753852930, 1879011799970971650, '2025-01-14 21:09:14', '2025-01-15 20:52:36'),
                                                                                                   (1889145699753852931, 1879145699753852931, 1879011799970971650, '2025-01-14 21:09:14', '2025-01-15 20:52:36'),
                                                                                                   (1889145699753852932, 1879145699753852932, 1879144322885484545, '2025-01-14 21:09:14', '2025-01-15 20:52:53'),
                                                                                                   (1889145699753852933, 1879145699753852933, 1879144322885484545, '2025-01-14 21:09:14', '2025-01-15 20:52:53'),
                                                                                                   (1889145699753852934, 1879145699753852934, 1879150630485467138, '2025-01-14 21:09:14', '2025-01-15 20:53:08'),
                                                                                                   (1889145699753852935, 1879145699753852935, 1879150630485467138, '2025-01-14 21:09:14', '2025-01-15 20:53:08'),
                                                                                                   (1889145699753852936, 1879145699753852936, 1879150662924214273, '2025-01-14 21:09:14', '2025-01-15 20:53:40'),
                                                                                                   (1889145699753852937, 1879145699753852937, 1879150662924214273, '2025-01-14 21:09:14', '2025-01-15 20:53:40'),
                                                                                                   (1889145699753852938, 1879145699753852938, 1879150690149441538, '2025-01-14 21:09:14', '2025-01-15 20:53:54'),
                                                                                                   (1889145699753852939, 1879145699753852939, 1879150690149441538, '2025-01-14 21:09:14', '2025-01-15 20:53:54');
