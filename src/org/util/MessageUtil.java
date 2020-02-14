package org.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

public class MessageUtil {

	// HTML标签的正则表达式
	private static final String regEx_html = "<[^>]+>";
	// 标点 空格 数字的正则表达式
	private static final String regEx_punch = "[\\p{Punct}\\p{Space}\\d]+";

	// 过滤html标签
	public static String removeHtmlLabels(String message) {
		// 过滤html标签
		Pattern pHtml = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher mHtml = pHtml.matcher(message);
		message = mHtml.replaceAll("");

		// 过滤
		return message.trim(); // 返回文本字符串
	}

	// 去除标点 空格 数字
	public static String removeSpaceAndPunct(String message) {
		return message.replaceAll(regEx_punch, "");
	}

	// 字符串集合的词频
	public static ArrayList<Entry<String, Integer>> getWordFrequency(List<String> messages) throws IOException {
		List<Term> lineList = null;
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		for (String message : messages) {
			lineList = HanLP.segment(message);
			for (Term term : lineList) {
				if (!counts.containsKey(term.toString())) {
					counts.put(term.toString(), 1);
				} else {
					counts.put(term.toString(), counts.get(term.toString()) + 1);
				}
			}
		}
		return sortMap(counts);
	}

	// 按照value排序Map,返回Entry
	private static ArrayList<Entry<String, Integer>> sortMap(Map map) {
		List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(map.entrySet());
		Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
				return obj2.getValue() - obj1.getValue();
			}
		});
		return (ArrayList<Entry<String, Integer>>) entries;
	}
}
