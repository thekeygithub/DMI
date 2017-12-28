package com.ts.controller.mts.loadrule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


import com.ts.util.AESSecurityUtil;
import com.ts.util.FileUtil;

public class MtsDataTest {
	/*public static void traverseFolder(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolder(file2.getAbsolutePath());
                    } else {
                    	//读取xml文件
                        System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }*/
	
	public static List<File> getFileList(String strPath) throws Exception {
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        List<File> filelist = new ArrayList<>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
                } else if (fileName.endsWith("txt")) { // 判断文件名是否以.avi结尾
                    String strFileName = files[i].getAbsolutePath();
                    BufferedReader bf = new BufferedReader(new FileReader(strFileName));
                    byte[] bs = FileUtil.getContent(strFileName);
                    String str = new String(bs);
                    String[] strs =str.split("\\n");
                    for (int j = 0; j < 1; j++) {
						String readLine = strs[j];
						String[] lines = readLine.split("\\t\\t\\t");
						String[] line1s = lines[1].split("\\s\\t");
						for (int k = 0; k < line1s.length; k++) {
						      System.out.println(line1s[k].split("\\t")[3]);
						}
					}
                } else {
                    continue;
                }
            }

        }
        return filelist;
    }
	
	public static void main(String[] args) throws Exception {
		//getFileList("C:/Users/heroliyaoa/Desktop/实体识别工具/全部原始语料/yl2"); 
		//q/HdLnopGOrfx+KcpeAt2/GPvmKUUspl4kQq35KuoaY=
		System.out.println(AESSecurityUtil.encrypt("丹痧", "VxDksHQiTvQt9MMPtMVXdA=="));
		//System.out.println(AESSecurityUtil.encrypt("17011", "VxDksHQiTvQt9MMPtMVXdA=="));
		//410201#04#28#眼电图(EOG)检查
		//System.out.println(AESSecurityUtil.decrypt("hwFUa69Q/hnDb7mLGNCw/WqX5i5lB0jdogj3IPk8GvPZDMSH7V5Mp20V7WRWlSW3", "VxDksHQiTvQt9MMPtMVXdA=="));
		//System.out.println(AESSecurityUtil.decrypt("jW4BZJzqIObrEuODkSqqgXQXt8Ifz0RJjTveLMELfx4=", "VxDksHQiTvQt9MMPtMVXdA=="));
		//System.out.println(StringUtil.full2Half(AESSecurityUtil.decrypt("uqCQ4zLsdVLruml0x/zM6T5SlVyxS+BaOsJn3IxDmDk=", "VxDksHQiTvQt9MMPtMVXdA==")).toUpperCase());
		//System.out.println(AESSecurityUtil.decrypt("FyGLrDmTASnuoH0IcQ/urA==", "VxDksHQiTvQt9MMPtMVXdA=="));
		//5Zew6CUQ4i8wxmKCEEpz2Ym1XoISKaalAI8L4C18b6xNyWYuIZgh+y/4dP3+tTA/@#&HwsjsQyJu1DqRxr/mtTJUg==@#&RWn/2++3w3bFqNl1lQxzwA==
		/*String str = "肺结节|间质性肺炎||疾病";
		String[] strs  = str.split("\\|");
		for (int i = 0; i < strs.length; i++) {
			System.out.println(strs[i]);
		}*/
		
	}
	
}
