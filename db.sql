CREATE TABLE `task` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` varchar(255) DEFAULT NULL,
  `tname` varchar(255) DEFAULT NULL,
  `turl` varchar(255) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `runable` char(1) DEFAULT '0',
  `nextstart` datetime DEFAULT NULL,
  `sleeptime` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
 
INSERT INTO `task` VALUES (26,'a','a','http://www.lz13.cn/lizhi/jingdianyulu.html','2011-01-01','0','2011-01-01','60');
 
CREATE TABLE `log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `runinfo` varchar(255) DEFAULT NULL,
  `beginTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `pageLength` int(11) DEFAULT NULL,
  `urlSize` int(11) DEFAULT NULL,
  `convSize` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
CREATE TABLE `conversation` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `publishTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `mainLink` varchar(255) DEFAULT NULL,
  `selfLink` varchar(255) DEFAULT NULL,
  `content` text,
  `mainLinkMd5` varchar(255) DEFAULT NULL,
  `selfLinkMd5` varchar(255) DEFAULT NULL,
  `isTopic` bit(1) DEFAULT NULL,
  `savetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `tid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `selfLink` (`selfLink`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
