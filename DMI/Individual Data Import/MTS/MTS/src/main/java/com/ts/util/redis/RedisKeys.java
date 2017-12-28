package com.ts.util.redis;

 /** 
 * @类名称: RedisKey
 * @类描述:Redis缓存服务器key(统一管理)
 * @作者:慕金剑
 * @创建时间:2016-2-16 上午10:34:34
 */
 
public class RedisKeys {
	/**
	 * MTS系统
	 */
	public static String SYSMTS="mts";
	 /**
	 * 系统缓存标示-OA系统
	 */
	public static String SYSOA="oa";
	/**
	 * 系统缓存标示-HIS系统
	 */
	public static String SYSHIS="his";
	/**
	 * 系统缓存标示-PACS系统
	 */
	public static String SYSPACS="pacs";
	/**
	 * 系统缓存标示-LIS系统
	 */
	public static String SYSLIS="lis";
	/**
	 * 系统缓存标示-手术麻醉系统
	 */
	public static String SYSSM="sm";
	/**
	 * 系统缓存标示-药品进销存系统
	 */
	public static String SYSYPJXC="ypjxc";
	/**
	 * 系统缓存标示-左侧菜单前缀（+人员id）
	 * 用户继承时清除
	 * 员工选择角色时清除（添加与删除）
	 * 角色选择人员时清除（添加与删除）
	 * 配置角色权限时清除
	 * 员工配置权限时清除
	 */
	public static String LEFTMENUSPrefix="menus_";
	
	/**
	 * 系统缓存标示-员工权限前缀（+模块id+人员id）
	 * 
	 */
	public static String UserPerPrefix="UserPer_";
	
	public static String getSysKey(String sysid){
		//模块类别(0:HIS  1:PACS 2: LIS 3:手术麻醉  4:OA
		//5:药品进销存)
		if("0".equals(sysid)){
			return RedisKeys.SYSHIS;
		}else if("1".equals(sysid)){
			return RedisKeys.SYSPACS;
		}else if("2".equals(sysid)){
			return RedisKeys.SYSLIS;
		}else if("3".equals(sysid)){
			return RedisKeys.SYSSM;
		}else if("4".equals(sysid)){
			return RedisKeys.SYSOA;
		}else if("5".equals(sysid)){
			return RedisKeys.SYSYPJXC;
		}
		return "";
	}
}

