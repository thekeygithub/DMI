package com.dhcc.ts.gh;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class AddressParse {
	/**
	 * @描述：根据xy获取所在省市县
	 * @作者：SZ
	 * @时间：2017年4月17日 下午1:17:48
	 * @param xy
	 * @return
	 */
	public static Map<String, String> geodecode(String xy) {
		String sUrl = "http://api.map.baidu.com/geocoder/v2/?ak=yaDRGoon5YoRzAAwH781yUgn&location=" + xy
				+ "&output=json&pois=0";
		StringBuffer str = new StringBuffer();
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			URL url = new URL(sUrl);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				str.append(line);
			}
			in.close();
			if (str.equals("") || str == null) {
				System.err.println("百度服务无返回");
				return null;
			}
			System.out.println(str.toString());

			JSONObject jsonobj = null;
			try {
				jsonobj = JSONObject.fromObject(str.toString());
				JSONObject result = jsonobj.getJSONObject("result");// 获取对象
				JSONObject addressComponent = result.getJSONObject("addressComponent");
				String province = addressComponent.getString("province");
				resultMap.put("province", province);
				String city = addressComponent.getString("city");
				resultMap.put("city", city);
				String district = addressComponent.getString("district");
				resultMap.put("district", district);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("此坐标获取不到省份：" + xy);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * @描述：根据地址名称，匹配得到经纬度坐标
	 * @作者：SZ
	 * @时间：2017年4月17日 下午1:17:17
	 * @param addr
	 * @return
	 */
	public static Map<String,String> geocode(String addr) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			addr = URLEncoder.encode(addr, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringBuffer str = new StringBuffer();
		try {
			String sUrl = "http://api.map.baidu.com/geocoder/v2/?address=" + addr
					+ "&output=json&ak=yaDRGoon5YoRzAAwH781yUgn";
			java.net.URL url = new java.net.URL(sUrl);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while ((line = in.readLine()) != null) {
				str.append(line);
			}
			in.close();
			JSONObject dataJson = null;
			dataJson = JSONObject.fromObject(str.toString());
			JSONObject result = dataJson.getJSONObject("result");
			JSONObject location = result.getJSONObject("location");
			resultMap.put("lng", location.getDouble("lng") + "");//经度
			resultMap.put("lat", location.getDouble("lat") + "");//经度
		} catch (Exception e) {
		}
		return resultMap;
	}

	public static void main(String[] args) {
		Map<String, String> xy = AddressParse.geocode("开封市龙亭区国税局附近");
		System.out.println(xy);

		Map<String, String> address = AddressParse.geodecode("34.800525,114.355336");
		System.out.println("===" + address);
	}
}
