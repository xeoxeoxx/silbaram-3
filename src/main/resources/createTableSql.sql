CREATE TABLE `board` (
  `bdid` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(50) DEFAULT NULL,
  `content` text NOT NULL,
  `memberId` bigint NOT NULL,
  `regDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `pageType` char(1) DEFAULT 'R',
  PRIMARY KEY (`bdid`)
) ENGINE=InnoDB AUTO_INCREMENT=5030 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `book` (
  `bkid` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `writer` varchar(50) NOT NULL,
  `publisher` varchar(50) NOT NULL,
  `price` int NOT NULL,
  `synopsis` text,
  `pages` int NOT NULL,
  `isbnNum` varchar(13) DEFAULT NULL,
  `cid` bigint NOT NULL,
  `fileSize` varchar(10) DEFAULT NULL,
  `lid` bigint NOT NULL,
  `voiceLength` varchar(150) DEFAULT NULL,
  `bookUrl` varchar(2083) DEFAULT NULL,
  `bookTextUrl` text,
  `bookImage` varchar(2083) DEFAULT NULL,
  PRIMARY KEY (`bkid`)
) ENGINE=InnoDB AUTO_INCREMENT=2150 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `cart` (
  `lkid` bigint NOT NULL AUTO_INCREMENT,
  `memberId` bigint NOT NULL,
  `bookId` bigint NOT NULL,
  PRIMARY KEY (`lkid`)
) ENGINE=InnoDB AUTO_INCREMENT=6008 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `language` (
  `lgid` bigint NOT NULL AUTO_INCREMENT,
  `language` varchar(15) NOT NULL,
  PRIMARY KEY (`lgid`),
  UNIQUE KEY `language` (`language`)
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `category` (
  `cid` bigint NOT NULL AUTO_INCREMENT,
  `cateName` varchar(15) NOT NULL,
  PRIMARY KEY (`cid`),
  UNIQUE KEY `cateName` (`cateName`)
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `member` (
  `mid` bigint NOT NULL AUTO_INCREMENT,
  `userId` varchar(15) NOT NULL,
  `password` varchar(20) NOT NULL,
  `nickName` varchar(10) NOT NULL,
  `name` varchar(15) NOT NULL,
  `birthDate` date NOT NULL,
  `gender` varchar(2) NOT NULL,
  `phoneNumber` varchar(11) NOT NULL,
  `email` varchar(30) NOT NULL,
  `zipcode` varchar(5) DEFAULT NULL,
  `address1` varchar(100) DEFAULT NULL,
  `socialLogin` varchar(20) DEFAULT '0',
  `isAdmin` tinyint(1) NOT NULL DEFAULT '0',
  `regDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `address2` varchar(100) DEFAULT NULL,
  `uuid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`mid`),
  UNIQUE KEY `userId` (`userId`),
  UNIQUE KEY `nickName` (`nickName`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `orderinfo` (
  `ogid` bigint NOT NULL AUTO_INCREMENT,
  `bookId` bigint NOT NULL,
  `bookName` varchar(50) NOT NULL,
  `bookPrice` int NOT NULL DEFAULT '0',
  `orderNum` varchar(20) NOT NULL,
  `oid` bigint NOT NULL,
  `memberId` bigint NOT NULL,
  `status` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`ogid`),
  KEY `bookId` (`bookId`),
  KEY `memberId` (`memberId`),
  KEY `oid` (`oid`),
  CONSTRAINT `orderinfo_ibfk_1` FOREIGN KEY (`bookId`) REFERENCES `book` (`bkid`) ON DELETE CASCADE,
  CONSTRAINT `orderinfo_ibfk_2` FOREIGN KEY (`memberId`) REFERENCES `member` (`mid`) ON DELETE CASCADE,
  CONSTRAINT `orderinfo_ibfk_3` FOREIGN KEY (`oid`) REFERENCES `orderlist` (`oid`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4001 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `orderlist` (
  `oid` bigint NOT NULL AUTO_INCREMENT,
  `orderDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `memberId` bigint NOT NULL,
  `totalPrice` int NOT NULL DEFAULT '0',
  `orderNum` varchar(20) NOT NULL,
  `payBy` varchar(10) NOT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `orderName` varchar(50) NOT NULL,
  PRIMARY KEY (`oid`)
) ENGINE=InnoDB AUTO_INCREMENT=5001 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `review` (
  `rid` bigint NOT NULL AUTO_INCREMENT,
  `mid` bigint NOT NULL,
  `title` varchar(50) NOT NULL,
  `reviewLike` tinyint(1) NOT NULL DEFAULT '1',
  `content` text,
  `regDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `bkid` bigint NOT NULL,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=3001 DEFAULT CHARSET=utf8mb3;

