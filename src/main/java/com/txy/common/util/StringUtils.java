package com.txy.common.util;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import com.txy.common.exception.ServiceException;

/*
 * @desc字符串处理工具类
 */
public class StringUtils
{
	
	/**
	 * 身份证校验位
	 */
	public static String[] CHECK_DIGIT = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
	
	/**
	 * 身份证加权因子
	 */
	public static int[] gene = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
	
	public static final int pass1 = 10;
	
	public static final int pass2 = 1;
	
	/*
	 * 去掉字符前后的空格
	 */
	
	public static String trim(String str)
	{
		return isNull(str) ? "" : str.trim();
	}
	
	/*
	 * @desc判断字符串是否为空 return true 为空 return false 不为空
	 */
	public static boolean isNull(String str)
	{
		return str == null ? true : false;
	}
	
	/*
	 * @desc:检查字符串是否为空字符
	 */
	public static boolean isEmpty(String str)
	{
		return isNull(str) || "".equals(str) ? true : false;
	}
	
	/*
	 * 检查空字符串，如果为null返回空字符
	 */
	public static String nullToString(String str)
	{
		return isNull(str) ? "" : str;
	}
	
	public static String formartDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(date);
	}
	
	/**
	 * 随机生成6位包括数字和字母的字符
	 * 
	 * @return
	 */
	public static String randomMath()
	{
		char[] character = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
				'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
				'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		
		StringBuffer randomStr = new StringBuffer();
		
		for (int n = 0; n < 6; n++)
		{
			randomStr.append(character[Math.abs(new Random().nextInt(character.length)) % 62]);
		}
		
		return randomStr.toString();
	}
	
	/**
	 * 判断是否为email
	 * 
	 * @param email
	 * @return 是email返回true; 不是email返回false
	 */
	public static boolean isEmail(String email)
	{
		if (email == null || email.equals(""))
			return false;
		Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		Matcher isMail = pattern.matcher(email);
		return isMail.matches();
		
	}
	
	/**
	 * 判断字符串是否为数字
	 * 
	 * @param email
	 * @return 是数字返回true; 不是数字返回false
	 */
	public static boolean isNum(String num)
	{
		if (num == null || num.equals(""))
			return false;
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(num);
		return isNum.matches();
	}
	
	/**
	 * 判断是否为手机号
	 */
	public static boolean isMobile(String mo)
	{
		if (mo == null || mo.equals(""))
			return false;
		Pattern pattern = Pattern.compile("^1[358]\\d{9}$");
		Matcher isMobile = pattern.matcher(mo);
		return isMobile.matches();
	}
	
	/**
	 * 判断是否为身份证号^(\d{15}|(\d{17}[xX\d]))$
	 */
	public static boolean isIdentityCard(String card)
	{
		if (card == null || card.equals(""))
			return false;
		if (card.length() != 15 && card.length() != 18)
			return false;
		Pattern pattern = Pattern.compile("^(\\d{15}|(\\d{17}[xX\\d]))$");
		Matcher isIdentityCard = pattern.matcher(card);
		if (!isIdentityCard.matches())
			return false;
		if (card.length() == 18)
		{
			int yearPrefix = Integer.parseInt(card.substring(6, 8));
			if (yearPrefix < 19 || yearPrefix > 21)
				return false;// 出生日期必须大于1900年小于2100年
			int month = Integer.parseInt(card.substring(10, 12));
			if (month > 12 || month == 0)
				return false; // 验证月
			int day = Integer.parseInt(card.substring(12, 14));
			if (day > 31 || day == 0)
				return false; // 验证日
			String checkDigit = getCheckDigitFor18(card);
			if (checkDigit.equals("-1"))
				return false;
			if (checkDigit.equals(card.substring(17, 18).toUpperCase()))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (card.length() == 15)
		{
			int month = Integer.parseInt(card.substring(8, 10));
			if (month > 12 || month == 0)
				return false; // 验证月
			int day = Integer.parseInt(card.substring(10, 12));
			if (day > 31 || day == 0)
				return false;
			return true;
		}
		return false;
	}
	
	private static String getCheckDigitFor18(String card)
	{
		if (card == null || card.equals(""))
			return "-1";
		int sum = 0;
		for (int i = 0; i < 17; i++)
		{
			sum += Integer.parseInt(card.substring(i, i + 1)) * gene[i];
		}
		return CHECK_DIGIT[sum % 11];
	}
	
	/**
	 * 随机字符串生成
	 * 
	 * @return
	 */
	public static String getRandmStr(int length)
	{
		char[] tempCs = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f',
				'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm' };
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			int temp = random.nextInt();
			if (0 != temp)
			{
				sb.append(tempCs[Math.abs(temp) % tempCs.length]);
			}
			else
			{
				sb.append(tempCs[Integer.MIN_VALUE % tempCs.length]);
			}
		}
		return sb.toString();
	}
	
	// @desc:将字符型数据转换成int型数据
	public static int toNum(String str)
	{
		return Integer.parseInt(str);
	}
	
	public static int[] toNum(String[] str)
	{
		int[] i = new int[str.length];
		for (int j = 0; j < str.length; j++)
		{
			i[j] = Integer.parseInt(str[j]);
		}
		return i;
	}
	
	/*
	 * @desc:替换字符串
	 * 
	 * @param:str:要替换的字符串
	 * 
	 * @param:tag:替换字符串
	 * 
	 * @param:reStr:替换后的字符串
	 * 
	 * @return:String
	 */
	public static String repace(String str, String tag, String reStr)
	{
		if (StringUtils.isEmpty(str))
		{
			return "";
		}
		else
		{
			return str.replaceAll(tag, reStr);
		}
	}
	
	public static long toLong(String str)
	{
		return Long.parseLong(str);
	}
	
	public static Date toDate(String str)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try
		{
			return dateFormat.parse(str);
		}
		catch (ParseException e)
		{
			throw new ServiceException("[" + str + "]不能转换成日期格式形如(yyyy-MM-dd HH:mm:ss)");
		}
	}
	
	/**
	 * 验证字符串是否为日期格式
	 * 
	 * @param args
	 * @return true 是 false 否
	 */
	public static boolean isDateType(String... args)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false);
		boolean isTag = true;
		try
		{
			for (int i = 0; i < args.length; i++)
			{
				df.format(df.parse(args[i]));
			}
		}
		catch (Exception e)
		{
			isTag = false;
		}
		return isTag;
	}
	
	/**
	 * 验证字符串是否为float，doub type：true。需要进行非空验证 false不验证非空字符
	 * 
	 * @param srgs
	 * @param type
	 * @return
	 */
	public static boolean isFloat(boolean type, String... args)
	{
		boolean isTag = true;
		try
		{
			if (type)
			{
				for (int i = 0; i < args.length; i++)
				{
					Double.parseDouble(args[i]);
				}
			}
			else
			{
				for (int i = 0; i < args.length; i++)
				{
					if (isNull(args[i]))
					{
						continue;
					}
					Double.parseDouble(args[i]);
				}
			}
		}
		catch (Exception e)
		{
			isTag = false;
		}
		
		return isTag;
	}
	
	public static double toDouble(String arg)
	{
		double dou = Double.parseDouble(arg);
		return dou;
	}
	
	public static float toFloat(String arg)
	{
		float flo = Float.parseFloat(arg);
		return flo;
	}
	
	public static boolean isEmptyObj(Object obj)
	{
		if (obj == null)
		{
			return true;
		}
		if (StringUtils.isEmpty(obj.toString()))
		{
			return true;
		}
		return false;
	}
	
	public static String objToString(Object obj)
	{
		if (!isEmptyObj(obj))
		{
			return obj.toString();
		}
		return "";
	}
	
	public static String getPrimarykeyId()
	{
		String s = UUID.randomUUID().toString();
		return (System.currentTimeMillis() + s.substring(0, 7)).toUpperCase();
	}
	
	public static boolean validateSign(Map<String, Object> argsMap, String sign, String privateStringKey)
	{
		StringBuffer params = new StringBuffer();
		for (Entry<String, Object> entry : argsMap.entrySet())
		{
			params.append(entry.getValue());
		}
		return false;
	}
	
	/**
	 * 256方式加密
	 * 
	 * @param str
	 * @return
	 */
	public static String SHA256(String str)
	{
		String resultStr = "";
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(str.getBytes("GBK"));
			resultStr = new HexBinaryAdapter().marshal(md.digest());
			resultStr = resultStr.substring(0, 16);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return resultStr;
	}
	
	/**
	 * 
	 * 格式化时间
	 * 
	 * @param date
	 * @param formart
	 * @return
	 */
	public static String formartDate(Date date, String formart)
	{
		SimpleDateFormat format = new SimpleDateFormat(formart);
		return format.format(date);
	}
	
	/**
	 * 对字符窜进行加密
	 * 
	 * @param str
	 * @return
	 */
	public static String DoEncrypt(String str)
	{
		StringBuffer enStrBuff = new StringBuffer();
		for (int i = 0; i < str.length(); i++)
		{
			int tmpch = str.charAt(i);
			tmpch ^= 1;
			tmpch ^= 0xa;
			enStrBuff.append(Integer.toHexString(tmpch));
		}
		
		return enStrBuff.toString().toUpperCase();
	}
	
	/**
	 * @Title: DoDecrypt
	 * @Description: 对密码进行解密的方法
	 * @param @param str
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String DoDecrypt(String str)
	{
		String deStr = str.toLowerCase();
		StringBuffer deStrBuff = new StringBuffer();
		for (int i = 0; i < deStr.length(); i += 2)
		{
			String subStr = deStr.substring(i, i + 2);
			int tmpch = Integer.parseInt(subStr, 16);
			tmpch ^= 1;
			tmpch ^= 0xa;
			deStrBuff.append((char) tmpch);
		}
		
		return deStrBuff.toString();
	}
	
	public static String getRandom()
	{
		char[] character = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
				'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		Random random = new Random();
		int n = random.nextInt(character.length);
		StringBuilder sb = new StringBuilder(2);
		sb.append("_" + n);
		return sb.toString();
	}
	
	public static String getRandomAsTable()
	{
		char[] character = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
				'Y', 'Z' };
		Random random = new Random();
		int n = random.nextInt(character.length);
		StringBuilder sb = new StringBuilder(2);
		sb.append(character[n]);
		return sb.toString();
	}
	
	/**
	 * 替换字符串中的第INDEX位为newStr
	 * 
	 * @param oldStr 目标字符串
	 * @param newStr 需要替换为
	 * @param index 替换的位置
	 * @return
	 */
	public static String replaceByIndex(String oldStr, String newStr, int index)
	{
		return oldStr.substring(0, index - 1) + newStr + oldStr.substring(index);
	}
	
	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNullOrEmpty(Object obj)
	{
		if (obj == null)
			return true;
		
		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;
		
		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();
		
		if (obj instanceof Map)
			return ((Map) obj).isEmpty();
		
		if (obj instanceof Object[])
		{
			Object[] object = (Object[]) obj;
			if (object.length == 0)
			{
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++)
			{
				if (!isNullOrEmpty(object[i]))
				{
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}
	
	/**
	 * 字符串转为16进制
	 * 
	 * @param s
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String convertStringTo16(String s)
	{
		byte[] b = s.getBytes();
		String str = "";
		try
		{
			for (int i = 0; i < b.length; i++)
			{
				Integer I = new Integer(b[i]);
				String strTmp = I.toHexString(b[i]);
				if (strTmp.length() > 2)
					strTmp = strTmp.substring(strTmp.length() - 2);
				str = str + strTmp;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return str.toUpperCase();
	}
	
	/**
	 * 16进制转为字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String toStringHex(String s)
	{
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++)
		{
			try
			{
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			s = new String(baKeyword, "utf-8");
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 将日期转为时间戳
	 */
	public static long dateStrToTimes(String time)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try
		{
			date = format.parse(time);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date.getTime();
	}
	
	public static String dutyPersonPres(String str)
	{
	    if (str == null || "".equals(str))
	    {
		return "";
	    }
	    String[] arrs = str.split(":");
	    if (arrs != null && arrs.length > 1)
	    {
		String nameStr = arrs[1];
		if (!"".equals(nameStr))
		{
		    return nameStr.replace("_", "");
		}
	    }
	    return "";
	}
}
