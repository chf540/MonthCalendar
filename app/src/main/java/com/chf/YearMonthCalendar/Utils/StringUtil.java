package com.chf.YearMonthCalendar.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * 字符串的工具类
 * 
 * @author chf
 * 
 */
public class StringUtil {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (null == str || str.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否为空或为"null"字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyOrNull(String str) {
		if (null == str || str.equals("") || "null".equals(str.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * null==str时返回"",否则返回自身
	 * 
	 * @param str
	 *            待处理字符串
	 * @return
	 */
	public static String toEmpytIfNull(String str) {
		return null == str ? "" : str;
	}

	/**
	 * 获取非空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getNotNullString(String str) {
		if (null == str || "".equals(str) || "null".equals(str)) {
			return "";
		}
		return str;
	}

	/**
	 * 去除字符串中所有空格 前后 以及中间，包括全角空格、半角空格以及 &nbsp; 和 &nbsp;代表的空格 -- 160
	 * 
	 * @param input
	 * @return
	 */
	public static String trim(String input) {
		if (input == null)
			return "";
		return input.replaceAll("&nbsp;", "").replace(" ", "").replace("　", "")
				.trim();
	}

	/**
	 * 去除前后空格 ，包括全角空格、半角空格以及 &nbsp; 和 &nbsp;代表的空格 -- 160
	 * 
	 * @param input
	 * @return
	 */
	public static String trimAllSpace(String input) {
		{
			if (input == null)
				return "";

			input = input.trim();

			char[] charArray = input.toCharArray();
			if (charArray.length == 0) {
				return "";
			}
			int st = 0;
			int end = charArray.length - 1;
			while (st <= end && isSpaceChar(charArray[st])) {
				st++;
			}
			while (end >= st && isSpaceChar(charArray[end])) {
				end--;
			}

			return input.substring(st, end + 1);
		}
	}

	/**
	 * 字符是否为空白
	 * 
	 * @param ch
	 * @return
	 */
	private static boolean isSpaceChar(char ch) {
		return ch == 32 || ch == 12288 || ch == 160 || ch == 9;
	}

	/**
	 * 获取字符串长度（一个汉字占2个字符）
	 * 
	 * @param s
	 * @return
	 */
	public static int getStringLength(String s) {
		char[] chs = s.toCharArray();
		int count = 0;
		for (int i = 0; i < chs.length; i++) {
			if ((chs[i] + "").getBytes().length >= 2) {
				count += 2;
			} else {
				count++;
			}
		}
		return count;
	}

	/**
	 * 分割返回结果
	 * 
	 * @param response
	 *            返回字符串
	 * @return
	 */
	public static Map<String, String> splitResponse(String response)
			throws Exception {
		// 保存返回结果
		Map<String, String> map = new HashMap<String, String>();
		// 判断是否为空
		if (!isEmpty(response)) {
			// 以“&”进行分割
			String[] array = response.split("&");
			if (array.length > 2) {
				String tokenStr = array[0]; // oauth_token=xxxxx
				String secretStr = array[1];// oauth_token_secret=xxxxxxx
				String[] token = tokenStr.split("=");
				if (token.length == 2) {
					map.put("oauth_token", token[1]);
				}
				String[] secret = secretStr.split("=");
				if (secret.length == 2) {
					map.put("oauth_token_secret", secret[1]);
				}
			} else {
				throw new Exception("分割字符串不符合要求。");
			}
		} else {
			throw new Exception("分割字符串为空");
		}
		return map;
	}

	/**
	 * 参数反编码
	 * 
	 * @param s
	 * @return
	 */
	public static String decode(String s) {
		if (s == null) {
			return "";
		}
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 过滤HTML标签
	 * 
	 * @param value
	 * @param regex
	 * @return
	 */
	public static String filterString(String value, String regex) {

		try {
			Pattern p_html = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher m_html = p_html.matcher(value);
			value = m_html.replaceAll("");
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		return value;
	}

	/**
	 * 判断用户输入的字符是否正确
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isMorezifuOrFeifazifu(String value) {
		if (value == null) {
			return false;
		}
		try {
			// 字符的长度
			int length = 0;
			Pattern p = Pattern.compile("[a-zA-Z0-9]");
			for (int i = 0; i < value.length(); ++i) {
				String str = String.valueOf(value.charAt(i));
				Matcher m = p.matcher(str);
				if (m.find()) {
					++length;
				} else {
					int len = str.getBytes("gbk").length;
					if (len == 2) {
						Character.UnicodeBlock ub = Character.UnicodeBlock
								.of(value.charAt(i));
						if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
								|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
								|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
							return false;
						}
						length += 2;
					} else
						return false;
				}
			}
			if (length > 40 || length <= 0)
				return false;
			else
				return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 传入字符串是否包含中文
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isContainChinese(String string) {
		boolean isContain = false;
		char[] charArray = string.toCharArray();
		for (char c : charArray) {
			isContain = isContain || isChinese(c);
		}
		return isContain;
	}

	/**
	 * 判断字符是否是中文字符
	 * 
	 * @param ch
	 * @return
	 */
	public static boolean isChinese(char ch) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 获取字符串的长度(中、英文)
	 * 
	 * @param str
	 * @return
	 */
	public static int getStringSize(String str) {
		int value = 0;
		int tmp = 0;
		int result = 0;
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			boolean b = isChinese(ch);
			if (b) {
				++value;
			} else {
				++tmp;
			}
		}
		if ((value + tmp / 2 == 30) && (value + (tmp + 1) / 2 > 30)) {
			result = 31;
		} else {
			result = value + tmp / 2;
		}
		return result;
	}

	/**
	 * 获取字符串的长度(中、英文)
	 * 
	 * @param str
	 * @return
	 */
	public static int getStringSize300(String str) {
		int value = 0;
		int tmp = 0;
		int result = 0;
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			boolean b = isChinese(ch);
			if (b) {
				++value;
			} else {
				++tmp;
			}
		}
		if ((value + tmp / 2 == 300) && (value + (tmp + 1) / 2 > 300)) {
			result = 301;
		} else {
			result = value + tmp / 2;
		}
		return result;
	}

	/**
	 * 发博者昵称过长时 获取截取过的昵称
	 * 
	 * @param str
	 * @return
	 */
	public static String getScreenName(String str) {
		if (str == null) {
			return "";
		}
		int value = 0;
		String screenName = null;
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			boolean b = isChinese(ch);
			if (b) {
				value += 2;
			} else {
				value++;
			}
			if (value > 16) {
				screenName = str.substring(0, i) + "...";
				break;
			}
		}
		if (screenName == null) {
			return str;
		}
		return screenName;
	}

	/**
	 * 获取非空字符串
	 * 
	 * @param inputstring
	 * @return "" 如果inputstring为空;否则返回本身
	 * 
	 */
	public static String makeSureStringNotNull(String inputstring) {
		if (TextUtils.isEmpty(inputstring)
				|| "null".equalsIgnoreCase(inputstring)) {
			return "";
		}

		return inputstring;
	}

	/**
	 * 按空格截取List<String>, 多空格合并为单一空格。若输入参数param为空(null或""或全为空格)，返回长度为0的list
	 * 
	 * @author hcp
	 * @param param
	 *            待截取字符串
	 * @return 截取后的list
	 */
	public static List<String> getStringList(String param) {
		if (null == param || "".equals(param)) {
			return new ArrayList<String>();
		}
		List<String> stringList = new ArrayList<String>();

		String newParma = param.replaceAll("\\s{1,}", " ");
		String[] array = newParma.split(" ");

		for (int i = 0; i < array.length; i++) {
			stringList.add(array[i]);
		}

		return stringList;
	}

	/**
	 * 常用用途页使用 获取字符串的长度(中、英文)
	 * 
	 * @param str
	 * @return
	 */
	public static int getStringSize200(String str) {
		int value = 0;
		int tmp = 0;
		int result = 0;
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			boolean b = isChinese(ch);
			if (b) {
				++value;
			} else {
				++tmp;
			}
		}
		if ((value * 2 + tmp == 201) && (value * 2 + (tmp + 1) > 201)) {
			result = 201;
		} else {
			result = value * 2 + tmp;
		}
		return result;
	}

	/**
	 * 截取指定长度字符串，一个中文算两个字符，一个英文算一个字符。如果截取长度将一个中文截取一半，则将这个中文全部截取。例如“我在boc工作”
	 * 截取10个字符则是截取半个“作”，次方法截取9个结果为“我在boc工”
	 * 
	 * @param str
	 *            要截取的字符串
	 * @param len
	 *            要截取的长度
	 * @return 返回""表示不需要截取，否则返回截取后的字符串
	 */
	public static String getAppointLengthString(String str, int len) {
		if (getStringLength(str) <= len) {
			return "";
		}
		byte[] arr = null;// 定义字节数组用于存放字符串转化的字节
		try {
			arr = str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
		int count = 0;// 定义计数器用于记录从截取位置开始练习的负数个数
		for (int x = len - 1; x >= 0; x--) {
			if (arr[x] < 0)// 如果字节是负数则自增
				count++;
			else
				// 如果不是跳出循环
				break;
		}
		if (count % 2 == 0)
			try {
				return new String(arr, 0, len, "GBK");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
		else
			try {
				return new String(arr, 0, len - 1, "GBK");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
	}

	public static ArrayList<String> StringsToList(String[] strs) {
		int size = strs.length;
		ArrayList<String> lists = new ArrayList<String>(size);
		for (int i = 0; i < size; i++) {
			lists.add(i, strs[i]);
		}
		return lists;
	}

	/***
	 * 给输入汉字的text加粗
	 */
	public static void blodChineseText(TextView textView) {
		TextPaint tp = textView.getPaint();
		tp.setFakeBoldText(true);
	}

	/***
	 * 加下划线
	 */
	public static void underlineText(TextView textView) {
		TextPaint tp = textView.getPaint();
		tp.setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
	}
}
