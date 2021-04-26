-- MySQL dump 10.13  Distrib 5.7.30, for Linux (x86_64)
--
-- Host: localhost    Database: records
-- ------------------------------------------------------
-- Server version	5.7.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `directory`
--

DROP TABLE IF EXISTS `directory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `directory` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `name` varchar(30) NOT NULL,
  `father_id` int(10) unsigned NOT NULL DEFAULT '1',
  `rank` int(10) unsigned NOT NULL,
  `description` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint(1) DEFAULT '1' COMMENT '1-normal,0-deleted',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `father_id` (`father_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `directory`
--

LOCK TABLES `directory` WRITE;
/*!40000 ALTER TABLE `directory` DISABLE KEYS */;
INSERT INTO `directory` VALUES (1,1,'技术',0,1,'这是技术目录','2020-08-28 15:16:16',1),(2,1,'研究生',0,2,'这是研究生目录','2020-08-28 15:16:32',1),(3,1,'生活',0,3,'这是生活','2020-08-28 15:16:40',1),(4,1,'Java',1,1,'这是','2020-08-28 15:16:49',1),(5,1,'数据库',1,2,'技术','2020-08-28 15:16:59',1),(6,1,'MySQL',5,1,'数据库','2020-08-28 15:17:08',1);
/*!40000 ALTER TABLE `directory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notes`
--

DROP TABLE IF EXISTS `notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `type` varchar(30) NOT NULL,
  `tags` varchar(30) NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(1) DEFAULT '1' COMMENT '1-normal,0-deleted',
  `thumbUp` int(10) unsigned NOT NULL DEFAULT '0',
  `star` int(10) unsigned NOT NULL DEFAULT '0',
  `cover` varchar(255) DEFAULT NULL,
  `directory` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notes`
--

LOCK TABLES `notes` WRITE;
/*!40000 ALTER TABLE `notes` DISABLE KEYS */;
INSERT INTO `notes` VALUES (1,1,'技术','Java','第一份笔记','## 第一份笔记\n\n### 第一份笔记\n#### 第一份笔记\n\n第一份笔记\n第一份笔记','2020-08-24 21:01:45','2020-08-27 19:56:49',0,14,34,'http://139.196.8.131/aboutme.jpg',0),(2,1,'技术','Java','note','<img src=\"http://139.196.8.131/1/20200824/D7E96B0FB22A4DB59662A8BA07F76D8E.jpg\" alt=\"image uploaded\" width=\"100%\"/> \n\n# note\nthis is my note','2020-08-24 21:16:14','2020-08-27 10:35:51',0,14,34,'http://139.196.8.131/aboutme.jpg',0),(3,1,'技术','MySQL;数据库','关于索引','### **MySQL中索引的优点和缺点和使用原则**\n\n\n**优点：**\n\n​	1、所有的MySql列类型(字段类型)都可以被索引，也就是可以给任意字段设置索引\n\n​	2、大大加快数据的查询速度\n\n**缺点：**\n\n1、创建索引和维护索引要**耗费时间**，并且随着数据量的增加所耗费的时间也会增加\n\n2、索引也需要**占空间**，我们知道数据表中的数据也会有最大上线设置的，如果我们有大量的索引，索引文件可能会比数据文件更快达到上线值\n\n3、当对表中的数据进行增加、删除、修改时，索引也需要动态的**维护**，降低了数据的维护速度。\n\n**使用原则：**\n\n1、对**经常更新的表**就避免对其进行过多的索引，对经常用于查询的**字段**应该创建索引\n\n2、**数据量小的表最好不要使用索引**。因为由于数据较少，可能查询全部数据花费的时间比遍历索引的时间还要短，索引就可能不会产生优化效果。\n\n3、**在一同值少的列上(字段上)不要建立索引**，相反的，在一个字段上不同值较多可以建立索引。','2020-08-25 00:21:29','2020-08-27 20:10:42',1,14,34,'http://139.196.8.131/1/20200825/0D896CAA38F940BEA7A16743F84113BC.jpg',0),(4,1,'技术','测试','测试','测试测试','2020-08-27 14:20:56','2020-08-27 19:56:45',0,14,34,'http://139.196.8.131/1/20200827/A596EBEFF8BC462DB5247DBC38B9CD55',0),(5,1,'技术','test','test','testtesttesttest','2020-08-27 14:39:12','2020-08-27 19:56:41',0,14,34,'http://139.196.8.131/1/20200827/15985103448083.jpg',0),(6,0,'技术','数据库;事务','事务','\n## 事务的概念\n\n事务是**用户定义**的一个**数据库的操作序列**，这些操作要么全做，要么全不做，是一个**不可分割**的工作单位\n\n### 事务的特性：ACID\n\n1) **原子性**（atomicity）：**对于其数据修改，要么全部执行，要么全部不执行**。事务是数据库的逻辑工作单位，而且是必须是原子工作单位。\n\n2) **一致性**（consistency）：**在事务执行前后，数据状态保持一致性**。在相关数据库中，所有规则都必须应用于事务的修改，以保持所有数据的完整性。（实例：转账，两个账户余额相加，值不变。）\n\n3) **隔离性**（isolation）：一个事务的执行不能被其他事务所影响。\n\n4) **持久性**（durability）：一个事务一旦提交，事物的操作便**永久**性的保存在DB中。即便是在数据库系统遇到故障的情况下也不会丢失提交事务的操作。\n\n## 并发事务带来的问题\n\n* **脏读（Dirty Read）:** 当一个事务正在访问数据并且对数据进行了修改，而这种修改还没有提交到数据库中，这时另外一个事务也访问了这个数据，然后使用了这个数据。因为这个数据是还没有提交的数据，那么另外一个事务读到的这个数据是“脏数据”，依据“脏数据”所做的操作可能是不正确的。\n\n* **丢失修改（Lost to Modify）:** 指在一个事务读取一个数据时，另外一个事务也访问了该数据，那么在第一个事务中修改了这个数据后，第二个事务也修改了这个数据。这样第一个事务内的修改结果就被丢失，因此称为丢失修改。    \n\n<img src=\"http://139.196.8.131/1/20200827/B6F2AA81A1FF497C8855EA037F77A7B9.png\" alt=\"image uploaded\" width=\"50%\"/> \n\n* **不可重复读（Unrepeatable Read）:** 指在一个事务内多次读同一数据。在这个事务还没有结束时，另一个事务也访问该数据。那么，在第一个事务中的两次读数据之间，由于第二个事务的修改导致第一个事务两次读取的数据可能不太一样。这就发生了在一个事务内两次读到的数据是不一样的情况，因此称为不可重复读。\n\n* **幻读（Phantom Read）:** 幻读与不可重复读类似。它发生在一个事务（T1）读取了几行数据，接着另一个并发事务（T2）插入了一些数据时。在随后的查询中，第一个事务（T1）就会发现多了一些原本不存在的记录，就好像发生了幻觉一样，所以称为幻读。\n\n**不可重复度和幻读区别：**\n\n​	不可重复读的重点是**修改**，幻读的重点在于**新增或者删除**。\n\n​	例1：事务1中的A先生读取自己的工资为 1000的操作还没完成，事务2中的B先生就修改了A的工资为2000，导 致A再读自己的工资时工资变为 2000；这就是不可重复读。\n\n​	例2：假某工资单表中工资大于3000的有4人，事务1读取了所有工资大于3000的人，共查到4条记录，这时事务2 又插入了一条工资大于3000的记录，事务1再次读取时查到的记录就变为了5条，这样就导致了幻读。\n\n## 事务隔离级别\n\n- **READ-UNCOMMITTED(读取未提交)：** 最低的隔离级别，允许读取尚未提交的数据变更，**可能会导致脏读、幻读或不可重复读**\n- **READ-COMMITTED(读取已提交):** 允许读取并发事务已经提交的数据，**可以阻止脏读，但是幻读或不可重复读仍有可能发生** **。** **(Oracle默认的事务隔离级别)**\n- **REPEATABLE-READ（可重读）:** 对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，**可以阻止脏读和不可重复读，但幻读仍有可能发生。** **(MySQL默认的事务隔离级别)**\n- **SERIALIZABLE(可串行化):** 最高的隔离级别，完全服从ACID的隔离级别。所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，**该级别可以防止脏读、不可重复读以及幻读**。\n\n<img src=\"http://139.196.8.131/1/20200827/3B305E9F3BB346F489A3F24D0ECA0C19.png\" alt=\"image uploaded\" style=\"zoom:30%;\"/> ','2020-08-27 20:05:22','2020-08-27 20:05:22',1,14,34,'',0),(7,0,'技术','','t1','t2','2020-08-27 20:06:04','2020-08-27 20:06:04',1,14,34,'',0),(8,0,'乱七八糟','123','123','123','2020-08-28 13:13:08','2020-08-28 13:13:08',1,14,34,'http://139.196.8.131/undefined/20200828/15985915610227.jpg',0),(9,1,'乱七八糟','测试','测试','测试\n测试测试\n测试\n测试\n','2020-08-28 13:32:10','2020-08-28 13:32:10',1,14,34,'http://139.196.8.131/1/20200828/15985927210616.jpg',0),(10,1,'技术','Java','Java JVM','## 1. 运行时数据区\n\nJava 虚拟机在执行 Java 程序的过程中会把它管理的内存划分成若干个不同的数据区域。\n\n### 主要知识点：\n\n* 运行时数据区的**组成**（JDK 1.6，JDK 1.7，JDK 1.8的区别）\n\n* 每个区域的**详解**\n\n  <!--more-->\n\n### Java 虚拟机运行时数据区组成\n\n<img src=\"1_1.jpg\" style=\"zoom:20%\"/>\n\n#### 具体介绍：\n\n#### 1. 虚拟机栈（JVM Stacks）\n\n* 线程私有\n* 每个方法被执行的时候，Java虚拟机都会同步创建一个**栈帧**（Stack Frame）用于存储**局部变量表、操作数栈、动态连接、方法出口**等信息。每一个方法被调用直至执行完毕的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。\n* 异常情况：\n  * 如果线程请求的栈深度大于虚拟机所允许的深度，将抛出**StackOverflowError**异常；\n  * 如果Java虚拟机栈容量可以动态扩展，当栈扩展时无法申请到足够的内存会抛出**OutOfMemoryError**异常。\n\n> **局部变量表**：基本数据类型（boolean、byte、char、short、int、 float、long、double）、对象引用（reference类型，它并不等同于对象本身，可能是一个指向对象起始 地址的引用指针，也可能是指向一个代表对象的句柄或者其他与此对象相关的位置）和returnAddress 类型（指向了一条字节码指令的地址））。当进入一个方法时，这个方法需要在栈帧中分配多大的局部变量空间是完全确定 的，在方法运行期间不会改变局部变量表的大小。\n\n#### 2. 本地方法栈（Native Method Stacks）\n\n* 线程私有\n* **本地方法栈则是为虚拟机使用到的本地（Native） 方法服务。**虚拟机栈为虚拟机执行Java方法（也就是字节码）服务\n* 异常情况：与虚拟机栈一样，本地方法栈也会在栈深度溢出或者栈扩展失败时分别抛出StackOverflowError和OutOfMemoryError异常\n\n#### 3. 程序计数器（Program Counter Register）\n\n* 线程私有\n* 存放：每个线程**下一步将执行的JVM指令**, 如果线程正在执行的是一个Java方法，这个计数器记录的是正在执行的虚拟机字节码指令的地址；如果正在执行的是本地（Native）方法，这个计数器值则应为空（Undefined）。\n* 字节码解释器工作时通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等功能都需要依赖这个计数器来完成。\n* 记录当前线程的位置便于线程切换与恢复；\n* 异常情况：**程序计数器是唯一一个不会出现OutOfMemoryError （OOM）的内存区域**，它的生命周期随着线程的创建而创建，随着线程的结束而死亡。\n\n#### 4. 堆（Heap）\n\n* 线程共享\n* 虚拟机启动时创建\n* 存放：**对象实例和数组**\n* Java堆可以处于物理上不连续的内存空间中，但在逻辑上它应该被视为连续的。Java虚拟机是可扩展的（通过参数-Xmx和-Xms设定）。\n* 异常情况：如果在Java堆中没有内存完成实例分配，并且堆也无法再扩展时，Java虚拟机将会抛出OutOfMemoryError异常。\n\n#### 5. 方法区（Method Area）\n\n* 线程共享\n\n* **存放：**存储已被虚拟机加载的类型信息、常量、静态变量、即时编译器编译后的代码缓存等数据。\n\n* 方法区只是一个概念，永久代或者元空间是它的实现\n\n  > HotSpot虚拟机设计团队把收集器的分代设计扩展至方法区(使用永久代来实现方法区)，但是对于其他虚拟机实现，譬如BEA JRockit、IBM J9等来说，是不存在永久代的概念的。\n  >\n  > 在JDK6的时候HotSpot逐步改为采用本地内存（Native Memory）来实现方法区，到JDK7的HotSpot，已经把原本放在永久代的字符串常量池、静态变量等移出，而到了 JDK 8，完全废弃了永久代的概念，改用与JRockit、J9一样在本地内存中实现的元空间（Metaspace）来代替，把JDK 7中永久代还剩余的内容（主要是类型信息）全部移到元空间中。\n\n#### 6. 运行时常量池（Runtime Constant Pool）\n\n- 是方法区的一部分\n- 存放：Class文件中除了有类的版本、字 段、方法、接口等描述信息外，还有一项信息是常量池表（Constant Pool Table），用于存放编译期生成的各种字面量与符号引用，这部分内容将在类加载后存放到方法区的运行时常量池中。运行期间也可以将新的常量放入池中。\n- 异常情况：既然运行时常量池是方法区的一部分，自然受到方法区内存的限制，当常量池无法再申请到内存时会抛出OutOfMemoryError异常。\n\n#### 7. 直接内存\n\n* 不属于虚拟机运行时数据区\n\n* 异常情况：动态扩展时会出现 OutOfMemoryError异常\n\n* **元空间(MetaSpace)**\n\n  > 存放：类的**元数据**。如方法、字段、类、包的描述信息，这些信息可以用于创建文档、跟踪代码中的依赖性、执行编译时检查\n  >\n  > 方法区里面的那些东西，大部分都被移到堆里面去了，还剩下一些元数据被保存在元空间里面\n  >\n  > 元空间有单独的元空间虚拟机执行内存分配与垃圾回收\n\n\n\n\n\n**为什么用元空间替换永久代？**\n\n1. **内存溢出的几率变小：**永久代有固定大小上限。元空间的大小是受本机可用内存的限制，内存溢出的概率会更小。\n2. **提升GC性能：**永久代的 GC 特别难搞，严重影响 Full GC 的性能。于是抛弃永久代，使用元空间，从而让     Full GC 不再关心方法区\n3. **增加可操作空间：**元空间里面存放的是类的元数据，这样加载多少类的元数据就不由 MaxPermSize 控制了, 而由系统的实际可用空间来控制，这样能加载的类就更多了。\n\n\n\n#### 参考文献：\n\n周志明. 深入理解Java虚拟机:JVM高级特性与最佳实践. 机械工业出版社, 2019.\n\n\n\n','2020-08-28 15:19:54','2020-08-28 15:19:54',1,0,0,'http://139.196.8.131/1/20200828/15985991900110.jpg',4);
/*!40000 ALTER TABLE `notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `photos`
--

DROP TABLE IF EXISTS `photos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `photos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `path` varchar(300) NOT NULL,
  `email` varchar(30) NOT NULL,
  `team_id` int(8) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `path` (`path`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `photos`
--

LOCK TABLES `photos` WRITE;
/*!40000 ALTER TABLE `photos` DISABLE KEYS */;
INSERT INTO `photos` VALUES (1,'/root/ProjectFiles/Records/photos/1/20200824/DA89DBC9E4DD43A18A4082917A89911B.jpg','20200824',1),(2,'/root/ProjectFiles/Records/photos/1/20200824/84ECD069D116416D9510CAED5195D31E.jpg','20200824',1),(3,'/root/ProjectFiles/Records/photos/1/20200824/D7E96B0FB22A4DB59662A8BA07F76D8E.jpg','20200824',1),(4,'/root/ProjectFiles/Records/photos/1/20200824/182083517ABA46F2A3762F3CE90948A8.png','20200824',1),(5,'/root/ProjectFiles/Records/photos/1/20200825/0D896CAA38F940BEA7A16743F84113BC.jpg','20200825',1),(6,'/root/ProjectFiles/Records/photos/1/20200827/7B934CF44622447FB8915A0F71E2619D.jpg','20200827',1),(7,'/root/ProjectFiles/Records/photos/1/20200827/A596EBEFF8BC462DB5247DBC38B9CD55','20200827',1),(8,'/root/ProjectFiles/Records/photos/1/20200827/5341F078DCE344F6A91C0D3F1B418ED2','20200827',1),(9,'/root/ProjectFiles/Records/photos/1/20200827/2063A8D3D48C4751BA587D0A3B532EF8','20200827',1),(10,'/root/ProjectFiles/Records/photos/1/20200827/B6F2AA81A1FF497C8855EA037F77A7B9.png','20200827',1),(11,'/root/ProjectFiles/Records/photos/1/20200827/3B305E9F3BB346F489A3F24D0ECA0C19.png','20200827',1);
/*!40000 ALTER TABLE `photos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(30) NOT NULL,
  `password` varchar(32) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1-normal,2-admin,3-superAdmin',
  `status` tinyint(1) DEFAULT '1' COMMENT '1-normal,0-deleted',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,'黄家晏','JiayanFighting@qq.com','123456','/1/avatars/15982805983005.jpg',1,1),(2,'Mila','2817465223@qq.com','1',NULL,1,1),(3,'1','1@163.com','1',NULL,1,1);
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-26 20:40:56
