package com.dhcc.ts.onetomany;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年6月1日 下午2:06:27 
* @类说明：
*/
public class OneToMany {

	public static Map<String,Object> map=new HashMap<String,Object>();
	public static Map<String,Map<String,Object>> jsdBasicMap=new HashMap<String,Map<String,Object>>();//结算单
	static{
		Map<String,Object> jsdmap1=new HashMap<String,Object>();
		Map<String,Object> jsdmap2=new HashMap<String,Object>();
		Map<String,Object> jsdmap3=new HashMap<String,Object>();
		Map<String,Object> jsdmap4=new HashMap<String,Object>();
		jsdmap1.put("rq","2016年3月11日");
		jsdmap1.put("jgmc","河南大学淮河医院");
		jsdmap1.put("jgbm","000013");
		jsdmap1.put("cfh","1603017930");
		jsdmap1.put("xm","孙威威");
		jsdmap1.put("xb","男");
		jsdmap1.put("nl","20岁");
		jsdmap1.put("fb","金保");
		jsdmap1.put("lczd","泌尿系统疾病    N39.900");
		jsdmap1.put("ks","泌尿外科门诊");
		List<String> list1=new ArrayList<String>();
		list1.add("泌淋胶囊    0.3g/粒    胶囊    口服    中成药    泌淋胶囊 0.3g/粒×6盒  Sig. 0.9g tid 口服");
		jsdmap1.put("zdjg",list1);
		jsdmap1.put("ys","侯俊清");
		jsdmap1.put("ypje","22.00");
		jsdmap1.put("blh","1603020053" );
		
	    jsdmap2.put("rq","2016年3月11日");
	    jsdmap2.put("jgmc","开封市第二人民医院分院");
	    jsdmap2.put("jgbm","000011");
	    jsdmap2.put("cfh","1603017959");
	    jsdmap2.put("xm","王素敏");
	    jsdmap2.put("xb","女");
	    jsdmap2.put("nl","47岁");
	    jsdmap2.put("fb","全费");
	    jsdmap2.put("lczd","乳腺腺病    N60.201");
	    jsdmap2.put("ks","乳腺甲状腺门诊");
	    List<String> list2=new ArrayList<String>();
	    list2.add("0.9%氯化钠注射液（塑料瓶）        100ml/瓶×1瓶        Sig.    100ml    st    静滴");
	    list2.add("5%葡萄糖注射液 (直立软包)    100ml/瓶×1瓶        Sig.    30ml     st    膀胱灌注");
	    list2.add("头孢哌酮舒巴坦针                               2g/支×3支                Sig.    2g       qd    封管");
	    list2.add("炎琥宁针                                              0.2g/支×2支          Sig.    0.2g     qd    静滴");
	    list2.add("西米替丁针                                          0.2g×1支                 Sig.    0.1g     qd    静滴");
	    list2.add("维生素B6针                                        0.1g/支×1支           Sig.    0.1g     qd    穴位封闭");
	    jsdmap2.put("zdjg", list2);
	    jsdmap2.put("ys","王顺昌");
	    jsdmap2.put("ypje","217.98");
	    jsdmap2.put("blh","1603020131" );
	    
	    
	    List<String> list3=new ArrayList<String>();
		jsdmap3.put("blh","1604040131");
		jsdmap3.put("rq","2016年4月23日");
		jsdmap3.put("jgmc","开封市第二人民医院分院");
		jsdmap3.put("jgbm","000011");
		jsdmap3.put("cfh","1604036132");
		jsdmap3.put("xm","常怡飞");
		jsdmap3.put("xb","男");
		jsdmap3.put("nl","26岁");
		jsdmap3.put("fb","全费");
        jsdmap3.put("lczd","甲状腺功能亢进");
		jsdmap3.put("ks","内分泌门诊");
		list3.add("0.9%氯化钠注射液（塑料瓶）        100ml/瓶×1瓶        Sig.    100ml    st    静滴");
		list3.add("头孢呋辛钠                                          1.5g/支×4支          Sig.    3g       qd    静滴 60 滴/分");
		jsdmap3.put("zdjg",list3);
		jsdmap3.put("ys","薛磊");
		jsdmap3.put("ypje","33.00");
		jsdmap3.put("blh","1604040131" );
		  
		List<String> list4=new ArrayList<String>();
		jsdmap4.put("blh","1608030112");
		jsdmap4.put("rq","2016年8月16日");
		jsdmap4.put("jgmc","通许县中医院");
		jsdmap4.put("jgbm","000013");
		jsdmap4.put("cfh","1608025070");
		jsdmap4.put("xm","李随群");
		jsdmap4.put("xb","男");
		jsdmap4.put("nl","53岁");
		jsdmap4.put("fb","全费");
		jsdmap4.put("lczd","高血压病    I10.x00");
		jsdmap4.put("ks","神经内科门诊");
		list4.add("数字化摄影(DR)");
	    jsdmap4.put("zdjg",list4);
		jsdmap4.put("ys","刘伟");
		jsdmap4.put("ypje","60.00"); 
		jsdmap4.put("blh","1608030112" );
		
		jsdBasicMap.put("1603017930", jsdmap1);
		jsdBasicMap.put("1603017959", jsdmap2);
		jsdBasicMap.put("1604036132", jsdmap3);
		jsdBasicMap.put("1608025070", jsdmap4);
	}
}
