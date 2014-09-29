package com.tarena.fashionmusic.util;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.File;
import java.nio.charset.Charset;


public class StrUtil {

	
	@SuppressWarnings("deprecation")
	public static String getcode(String filepath){
		/*------------------------------------------------------------------------ 
		  detector��̽����������̽�����񽻸������̽��ʵ�����ʵ����ɡ� 
		  cpDetector������һЩ���õ�̽��ʵ���࣬��Щ̽��ʵ�����ʵ������ͨ��add���� 
		  �ӽ�������ParsingDetector�� JChardetFacade��ASCIIDetector��UnicodeDetector��   
		  detector���ա�˭���ȷ��طǿյ�̽���������Ըý��Ϊ׼����ԭ�򷵻�̽�⵽�� 
		  �ַ������롣 
		--------------------------------------------------------------------------*/
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		/*------------------------------------------------------------------------- 
		  ParsingDetector�����ڼ��HTML��XML���ļ����ַ����ı���,���췽���еĲ������� 
		  ָʾ�Ƿ���ʾ̽����̵���ϸ��Ϣ��Ϊfalse����ʾ�� 
		---------------------------------------------------------------------------*/
		detector.add(new ParsingDetector(false));// �����ϣ���ж�xml��encoding������Ҫ�жϸ�xml�ļ��ı��룬�����ע�͵�
		/*-------------------------------------------------------------------------- 
		   JChardetFacade��װ����Mozilla��֯�ṩ��JChardet����������ɴ�����ļ��ı��� 
		   �ⶨ�����ԣ�һ���������̽�����Ϳ�����������Ŀ��Ҫ������㻹�����ģ�����  
		   �ٶ�Ӽ���̽���������������ASCIIDetector��UnicodeDetector�ȡ� 
		  ---------------------------------------------------------------------------*/
		detector.add(JChardetFacade.getInstance());
		// ASCIIDetector����ASCII����ⶨ
		detector.add(ASCIIDetector.getInstance());
		// UnicodeDetector����Unicode�������Ĳⶨ
		detector.add(UnicodeDetector.getInstance());
		Charset charset = null;
		File f = new File(filepath);
		try {
			charset = detector.detectCodepage(f.toURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			System.out.println(f.getName() + "�����ǣ�" + charset.name());
			   return charset.name();
		} else {
			System.out.println(f.getName() + "δ֪");
			return "BIG-5";
		}


	
	}
	
}
